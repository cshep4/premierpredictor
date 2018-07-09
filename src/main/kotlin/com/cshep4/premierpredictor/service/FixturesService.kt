package com.cshep4.premierpredictor.service

import com.cshep4.premierpredictor.component.api.ApiRequester
import com.cshep4.premierpredictor.component.fixtures.FixtureFormatter
import com.cshep4.premierpredictor.component.fixtures.FixturesByDate
import com.cshep4.premierpredictor.component.fixtures.OverrideMatchScore
import com.cshep4.premierpredictor.component.fixtures.PredictionMerger
import com.cshep4.premierpredictor.component.time.Time
import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.PredictedMatch
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.entity.MatchFactsEntity
import com.cshep4.premierpredictor.extension.isToday
import com.cshep4.premierpredictor.extension.isUpcoming
import com.cshep4.premierpredictor.repository.dynamodb.MatchFactsRepository
import com.cshep4.premierpredictor.repository.sql.FixturesRepository
import com.cshep4.premierpredictor.service.fixtures.UpdateFixturesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class FixturesService {
    @Autowired
    private lateinit var fixtureApiRequester: ApiRequester

    @Autowired
    private lateinit var fixtureFormatter: FixtureFormatter

    @Autowired
    private lateinit var updateFixturesService: UpdateFixturesService

    @Autowired
    private lateinit var fixturesRepository: FixturesRepository

    @Autowired
    private lateinit var predictionMerger: PredictionMerger

    @Autowired
    private lateinit var predictionsService: PredictionsService

    @Autowired
    private lateinit var overrideMatchService: OverrideMatchService

    @Autowired
    private lateinit var overrideMatchScore: OverrideMatchScore

    @Autowired
    private lateinit var matchFactsRepository: MatchFactsRepository

    @Autowired
    private lateinit var fixturesByDate: FixturesByDate

    @Autowired
    private lateinit var time: Time

    fun update(): List<Match> {
        val matches = retrieveMatchesFromApi()

        if (matches.isEmpty()) {
            return emptyList()
        }

        val overrides = overrideMatchService.retrieveAllOverriddenMatches()

        val finalScores = overrideMatchScore.update(matches, overrides)

        return updateFixturesService.update(finalScores)
    }

    fun retrieveMatchesFromApi() : List<Match> {
        val apiResult = fixtureApiRequester.retrieveFixtures()

        return fixtureFormatter.format(apiResult)
    }

    fun retrieveAllMatches() : List<Match> = fixturesRepository.findAll().map { it.toDto() }

    fun retrieveAllPredictedMatchesByUserId(id: Long) : List<Match> = fixturesRepository.findPredictedMatchesByUserId(id).map { it.toDto() }

    fun retrieveAllMatchesWithPredictions(id: Long) : List<PredictedMatch> {
        val matches = retrieveAllMatches()

        if (matches.isEmpty()) {
            return emptyList()
        }

        val predictions = predictionsService.retrievePredictionsByUserId(id)

        return predictionMerger.merge(matches, predictions)
    }

    //TODO - Refactor
    fun retrieveAllUpcomingFixtures() : Map<LocalDate, List<MatchFacts>> {
        var upcomingMatches = matchFactsRepository.findAll()
                .filter { it.getDateTime()!!.isToday() || it.getDateTime()!!.isUpcoming() }
                .sortedBy { it.getDateTime() }
                .take(20)
                .map { it.toDto() }

        if (upcomingMatches.any { it.isInNeedOfUpdate() }) {
            val apiResult = fixtureApiRequester.retrieveFixtures()

            val updated = upcomingMatches.filter { it.isInNeedOfUpdate() }
                    .map { m -> mergeWithLatestVersion(m, apiResult.firstOrNull { it.id == m.id }) }

            val notUpdated = upcomingMatches.filter { !it.isInNeedOfUpdate() }

            upcomingMatches = listOf(notUpdated, updated).flatten()

            val upcomingMatchEntities = upcomingMatches.map { MatchFactsEntity.fromDto(it) }

            matchFactsRepository.saveAll(upcomingMatchEntities)
        }

        return if (upcomingMatches.isEmpty()) {
            emptyMap()
        } else {
            fixturesByDate.format(upcomingMatches)
        }
    }
    // -----

    private fun mergeWithLatestVersion(match: MatchFacts, apiMatch: MatchFacts?): MatchFacts {
        if (!match.isInNeedOfUpdate() || apiMatch == null) {
            return match
        }

        apiMatch.commentary = match.commentary
        apiMatch.lastUpdated = time.localDateTimeNow()

        return apiMatch
    }

    fun retrieveLiveScoreForMatch(id: Long) : MatchFacts? {
        val match = matchFactsRepository.findById(id.toString())
                .map { it.toDto() }
                .orElse(null)

        if (match == null || match.isInNeedOfUpdate()) {
            val apiResult = fixtureApiRequester.retrieveMatch(id.toString()) ?: return match
            apiResult.lastUpdated = time.localDateTimeNow()
            apiResult.commentary = match?.commentary

            matchFactsRepository.save(MatchFactsEntity.fromDto(apiResult))

            return apiResult
        }

        return match
    }
}