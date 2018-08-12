package com.cshep4.premierpredictor.service.fixtures

import com.cshep4.premierpredictor.component.fixtures.FixturesByDate
import com.cshep4.premierpredictor.component.fixtures.PredictionMerger
import com.cshep4.premierpredictor.component.matchfacts.MatchUpdater
import com.cshep4.premierpredictor.constant.MatchConstants.UPCOMING_SUBSCRIPTION
import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.PredictedMatch
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.entity.MatchEntity
import com.cshep4.premierpredictor.extension.isInNeedOfUpdate
import com.cshep4.premierpredictor.extension.isToday
import com.cshep4.premierpredictor.extension.isUpcoming
import com.cshep4.premierpredictor.repository.dynamodb.MatchFactsRepository
import com.cshep4.premierpredictor.repository.sql.FixturesRepository
import com.cshep4.premierpredictor.service.prediction.PredictionsService
import kotlinx.coroutines.experimental.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class FixturesService {
    @Autowired
    private lateinit var fixturesRepository: FixturesRepository

    @Autowired
    private lateinit var predictionMerger: PredictionMerger

    @Autowired
    private lateinit var predictionsService: PredictionsService

    @Autowired
    private lateinit var matchFactsRepository: MatchFactsRepository

    @Autowired
    private lateinit var fixturesByDate: FixturesByDate

    @Autowired
    private lateinit var matchUpdater: MatchUpdater

    @Autowired
    private lateinit var template: SimpMessagingTemplate

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

    fun retrieveAllUpcomingFixtures() : Map<LocalDate, List<MatchFacts>> {
        val upcomingMatches = matchFactsRepository.findAll()
                .filter { it.getDateTime()!!.isToday() || it.getDateTime()!!.isUpcoming() }
                .sortedBy { it.getDateTime() }
                .take(20)
                .map { it.toDto() }

        if (upcomingMatches.isEmpty()) {
            return emptyMap()
        }

        launch {
            if (upcomingMatches.any { it.lastUpdated!!.isInNeedOfUpdate() }) {
                val updatedMatches = matchUpdater.updateUpcomingMatchesWithLatestScores(upcomingMatches)

                template.convertAndSend(UPCOMING_SUBSCRIPTION, fixturesByDate.format(updatedMatches))
            }
        }

        return fixturesByDate.format(upcomingMatches)
    }

    fun retrieveLiveScoreForMatch(id: String) : MatchFacts? {
        val match = matchFactsRepository.findById(id)
                .map { it.toDto() }
                .orElse(null)

        if (match == null || match.lastUpdated!!.isInNeedOfUpdate()) {
            return matchUpdater.updateMatch(id, match)
        }

        return match
    }

    fun saveMatches(matches: List<Match>): List<Match> {
        val matchEntities = matches.map { MatchEntity.fromDto(it) }

        return fixturesRepository.saveAll(matchEntities)
                .map { it.toDto() }
    }
}