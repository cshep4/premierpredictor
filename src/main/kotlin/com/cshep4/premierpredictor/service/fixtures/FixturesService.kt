package com.cshep4.premierpredictor.service.fixtures

import com.cshep4.premierpredictor.component.fixtures.FixturesByDate
import com.cshep4.premierpredictor.component.prediction.PredictionMerger
import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.PredictedMatch
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.entity.MatchEntity
import com.cshep4.premierpredictor.extension.hasPlayed
import com.cshep4.premierpredictor.extension.isToday
import com.cshep4.premierpredictor.extension.isUpcoming
import com.cshep4.premierpredictor.repository.dynamodb.MatchFactsRepository
import com.cshep4.premierpredictor.repository.sql.FixturesRepository
import com.cshep4.premierpredictor.service.prediction.PredictionsService
import org.springframework.beans.factory.annotation.Autowired
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

    fun retrieveAllMatches(): List<Match> = fixturesRepository.findAll().map { it.toDto() }

    fun retrieveAllPredictedMatchesByUserId(id: Long): List<Match> = fixturesRepository.findPredictedMatchesByUserId(id).map { it.toDto() }

    fun retrieveAllMatchesWithPredictions(id: Long): List<PredictedMatch> {
        val matches = retrieveAllMatches()

        if (matches.isEmpty()) {
            return emptyList()
        }

        val predictions = predictionsService.retrievePredictionsByUserId(id)

        return predictionMerger.merge(matches, predictions)
    }

//    fun retrieveAllUpcomingFixtures(): Map<LocalDate, List<MatchFacts>> {
//        val upcomingMatches = matchFactsRepository.findAll()
//                .filter { it.getDateTime()!!.isToday() || it.getDateTime()!!.isUpcoming() }
//                .sortedBy { it.getDateTime() }
//                .take(20)
//                .map { it.toDto() }
//
//        if (upcomingMatches.isEmpty()) {
//            return emptyMap()
//        }
//
//        return fixturesByDate.format(upcomingMatches)
//    }

    fun retrieveAllUpcomingFixtures(): Map<LocalDate, List<MatchFacts>> {
        val ids = fixturesRepository.findUpcomingFixtureIds().map { it.toString() }

        val upcomingMatches = matchFactsRepository.findAllById(ids)
//                .filter { it.getDateTime()!!.isToday() || it.getDateTime()!!.isUpcoming() }
                .sortedBy { it.getDateTime() }
//                .take(20)
                .map { it.toDto() }

        if (upcomingMatches.isEmpty()) {
            return emptyMap()
        }

        return fixturesByDate.format(upcomingMatches)
    }

    fun retrieveLiveScoreForMatch(id: String): MatchFacts? = matchFactsRepository.findById(id)
            .map { it.toDto() }
            .orElse(null)

    fun saveMatches(matches: List<Match>): List<Match> {
        val matchEntities = matches.map { MatchEntity.fromDto(it) }

        return fixturesRepository.saveAll(matchEntities)
                .map { it.toDto() }
    }

    fun retrieveAllPastMatches(): List<Match> = retrieveAllMatches().filter { it.hasPlayed() }
}