package com.cshep4.premierpredictor.service.livematch

import com.cshep4.premierpredictor.data.MatchSummary
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.repository.dynamodb.MatchFactsRepository
import com.cshep4.premierpredictor.service.prediction.MatchPredictionSummaryService
import com.cshep4.premierpredictor.service.prediction.PredictionsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LiveMatchService {
    @Autowired
    private lateinit var matchFactsRepository: MatchFactsRepository

    @Autowired
    private lateinit var matchPredictionSummaryService: MatchPredictionSummaryService

    @Autowired
    private lateinit var predictionsService: PredictionsService

//    @Autowired
//    private lateinit var teamService: TeamService

    fun retrieveLiveMatchFacts(id: String): MatchFacts? = matchFactsRepository
                .findById(id)
                .map { it.toDto() }
                .orElse(null)

//    fun retrieveMatchSummary(matchId: String, id: String): MatchSummary? {
//        return runBlocking {
//            val matchFacts = retrieveLiveMatchFacts(matchId) ?: return@runBlocking null
//
//            var prediction: Prediction? = null
//            var predictionSummary = MatchPredictionSummary()
//            var forms: Map<String, TeamForm> = emptyMap()
//
//            val predictionCoRoutine = async {
//                prediction = predictionsService.retrievePredictionByUserIdForMatch(id.toLong(), matchId.toLong())
//            }
//
//            val predictionSummaryCoRoutine = async {
//                predictionSummary = matchPredictionSummaryService.retrieveMatchPredictionSummary(matchId)
//            }
//
//            val formsCoRoutine = async {
//                forms = teamService.retrieveRecentForms()
//            }
//
//            predictionCoRoutine.await()
//            predictionSummaryCoRoutine.await()
//            formsCoRoutine.await()
//
//            MatchSummary(match = matchFacts, prediction = prediction, predictionSummary = predictionSummary, forms = forms)
//        }
//    }

    fun retrieveMatchSummary(matchId: String, id: String): MatchSummary? {
        val matchFacts = retrieveLiveMatchFacts(matchId) ?: return null

        val prediction = predictionsService.retrievePredictionByUserIdForMatch(id.toLong(), matchId.toLong())

        val predictionSummary = matchPredictionSummaryService.retrieveMatchPredictionSummary(matchId)

        return MatchSummary(match = matchFacts, prediction = prediction, predictionSummary = predictionSummary)
    }
}