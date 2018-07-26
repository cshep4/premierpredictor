package com.cshep4.premierpredictor.service.livematch

import com.cshep4.premierpredictor.component.matchfacts.CommentaryUpdater
import com.cshep4.premierpredictor.component.matchfacts.MatchUpdater
import com.cshep4.premierpredictor.data.MatchSummary
import com.cshep4.premierpredictor.data.api.live.commentary.Commentary
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.entity.MatchFactsEntity
import com.cshep4.premierpredictor.extension.isInNeedOfUpdate
import com.cshep4.premierpredictor.repository.dynamodb.MatchFactsRepository
import com.cshep4.premierpredictor.service.prediction.MatchPredictionSummaryService
import com.cshep4.premierpredictor.service.prediction.PredictionsService
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.springframework.beans.factory.annotation.Autowired
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

    fun retrieveLiveMatchFacts(id: String): MatchFacts? {
        return runBlocking {
            val storedMatch = matchFactsRepository
                    .findById(id)
                    .map { it.toDto() }
                    .orElse(null)

            var updatedMatch: MatchFacts? = null
            var updatedCommentary: Commentary? = null


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


            getRelevantMatchFacts(storedMatch, updatedMatch, updatedCommentary)
        }
    }

    private fun doesMatchFactsNeedUpdating(matchFacts: MatchFacts?) =
            matchFacts == null || matchFacts.lastUpdated!!.isInNeedOfUpdate()

    private fun doesCommentaryNeedUpdating(matchFacts: MatchFacts?) =
            matchFacts?.commentary?.lastUpdated == null || matchFacts.commentary!!.lastUpdated!!.isInNeedOfUpdate()

    private fun getRelevantMatchFacts(storedMatch: MatchFacts?, updatedMatch: MatchFacts?, updatedCommentary: Commentary?): MatchFacts? {
        val matchFacts = updatedMatch ?: storedMatch ?: return null

        matchFacts.commentary = when (updatedCommentary) {
            null -> storedMatch?.commentary
            else -> updatedCommentary
        }

        launch {
            matchFactsRepository.save(MatchFactsEntity.fromDto(matchFacts))
        }

        return matchFacts
    }

    fun retrieveMatchSummary(matchId: String, id: String): MatchSummary? {
        val matchFacts = retrieveLiveMatchFacts(matchId) ?: return null

        val prediction = predictionsService.retrievePredictionByUserIdForMatch(id.toLong(), matchId.toLong())

        val predictionSummary = matchPredictionSummaryService.retrieveMatchPredictionSummary(matchId)

        return MatchSummary(match = matchFacts, prediction = prediction, predictionSummary = predictionSummary)
    }
}