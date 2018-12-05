package com.cshep4.premierpredictor.service.livematch

import com.cshep4.premierpredictor.component.matchfacts.CommentaryUpdater
import com.cshep4.premierpredictor.component.matchfacts.MatchUpdater
import com.cshep4.premierpredictor.constant.MatchConstants.LIVE_MATCH_SUBSCRIPTION
import com.cshep4.premierpredictor.data.MatchSummary
import com.cshep4.premierpredictor.data.api.live.commentary.Commentary
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.entity.MatchFactsEntity
import com.cshep4.premierpredictor.extension.isInNeedOfUpdate
import com.cshep4.premierpredictor.repository.dynamodb.MatchFactsRepository
import com.cshep4.premierpredictor.service.prediction.MatchPredictionSummaryService
import com.cshep4.premierpredictor.service.prediction.PredictionsService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class LiveMatchService {
    @Autowired
    private lateinit var matchFactsRepository: MatchFactsRepository

    @Autowired
    private lateinit var matchUpdater: MatchUpdater

    @Autowired
    private lateinit var commentaryUpdater: CommentaryUpdater

    @Autowired
    private lateinit var matchPredictionSummaryService: MatchPredictionSummaryService

    @Autowired
    private lateinit var predictionsService: PredictionsService

    @Autowired
    private lateinit var template: SimpMessagingTemplate

//    @Autowired
//    private lateinit var teamService: TeamService

    fun retrieveLiveMatchFacts(id: String): MatchFacts? {
        val storedMatch = matchFactsRepository
                .findById(id)
                .map { it.toDto() }
                .orElse(null)

        GlobalScope.launch {
            updateMatchFacts(storedMatch, id)
        }

        return storedMatch
    }

    private fun updateMatchFacts(storedMatch: MatchFacts?, id: String) {
        var updatedMatch: MatchFacts? = null
        var updatedCommentary: Commentary? = null

        runBlocking {
            val matchFactsCoRoutine = async {
                if (doesMatchFactsNeedUpdating(storedMatch)) {
                    updatedMatch = matchUpdater.retrieveMatchFromApi(id)
                }
            }

            val commentaryCoRoutine = async {
                if (doesCommentaryNeedUpdating(storedMatch)) {
                    updatedCommentary = commentaryUpdater.retrieveCommentaryFromApi(id)
                }
            }

            matchFactsCoRoutine.await()
            commentaryCoRoutine.await()

            val match = getRelevantMatchFacts(storedMatch, updatedMatch, updatedCommentary) ?: return@runBlocking
            template.convertAndSend(LIVE_MATCH_SUBSCRIPTION + match.id, match)
        }
    }

    private fun doesMatchFactsNeedUpdating(matchFacts: MatchFacts?) =
            (matchFacts == null || matchFacts.lastUpdated!!.isInNeedOfUpdate()) && matchFacts?.status != "FT"

    private fun doesCommentaryNeedUpdating(matchFacts: MatchFacts?) =
            (matchFacts?.commentary?.lastUpdated == null || matchFacts.commentary!!.lastUpdated!!.isInNeedOfUpdate()) && matchFacts?.status != "FT"

    private fun getRelevantMatchFacts(storedMatch: MatchFacts?, updatedMatch: MatchFacts?, updatedCommentary: Commentary?): MatchFacts? {
        val matchFacts = updatedMatch ?: storedMatch ?: return null

        matchFacts.commentary = when (updatedCommentary) {
            null -> storedMatch?.commentary
            else -> updatedCommentary
        }

        matchFactsRepository.save(MatchFactsEntity.fromDto(matchFacts))

        return matchFacts
    }

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