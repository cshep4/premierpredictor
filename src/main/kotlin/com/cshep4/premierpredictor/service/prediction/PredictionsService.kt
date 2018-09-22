package com.cshep4.premierpredictor.service.prediction

import com.cshep4.premierpredictor.component.prediction.CreatePredictionSummary
import com.cshep4.premierpredictor.data.*
import com.cshep4.premierpredictor.entity.PredictionEntity
import com.cshep4.premierpredictor.repository.sql.PredictionsRepository
import com.cshep4.premierpredictor.service.fixtures.FixturesService
import com.cshep4.premierpredictor.service.team.TeamService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Service
class PredictionsService {
    @Autowired
    private lateinit var predictionsRepository: PredictionsRepository

    @Autowired
    private lateinit var fixturesService: FixturesService

    @Autowired
    private lateinit var createPredictionSummary: CreatePredictionSummary

    @Autowired
    private lateinit var teamService: TeamService

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    private lateinit var matches: List<Match>

    fun savePredictions(predictions: List<Prediction>) : List<Prediction> {
        matches = fixturesService.retrieveAllMatches()

        val predictionEntities = predictions
                .filter { matchYetToPlay(it.matchId!!) }
                .map { PredictionEntity.fromDto(it) }

        val p = predictionsRepository.saveAll(predictionEntities).map { it.toDto() }

        entityManager.clear()

        return p
    }

    private fun matchYetToPlay(id: Long) : Boolean {
        return matches.first { it.id == id }
                .dateTime!!
                .isAfter(LocalDateTime.now(Clock.systemUTC()))
    }

    fun retrievePredictionsByUserId(id: Long) : List<Prediction> = predictionsRepository.findByUserId(id).map { it.toDto() }

    fun retrievePredictionByUserIdForMatch(id: Long, matchId: Long) : Prediction? = retrievePredictionsByUserId(id).firstOrNull { it.matchId == matchId }

    fun retrievePredictionsSummaryByUserId(id: Long) : PredictionSummary {
        val matches = fixturesService.retrieveAllMatches()
                .filter { it.dateTime!!.isBefore(LocalDateTime.now(Clock.systemUTC())) }

        val predictions = retrievePredictionsByUserId(id)

        return createPredictionSummary.format(matches, predictions)
    }

    fun retrievePredictorData(id: Long): PredictorData {
//        return runBlocking {
            var predictions: List<PredictedMatch> = emptyList()
            var forms: Map<String, TeamForm> = emptyMap()

//            val predictionsCoRoutine = async {
                predictions = fixturesService.retrieveAllMatchesWithPredictions(id)
//            }
//
//            val formCoRoutine = async {
                forms = teamService.retrieveRecentForms()
//            }

//            predictionsCoRoutine.await()
//            formCoRoutine.await()

            return PredictorData(predictions = predictions, forms = forms)
//        }
    }
}