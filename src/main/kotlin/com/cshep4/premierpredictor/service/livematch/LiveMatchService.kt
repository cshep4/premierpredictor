package com.cshep4.premierpredictor.service.livematch

import com.cshep4.premierpredictor.component.matchfacts.CommentaryUpdater
import com.cshep4.premierpredictor.component.matchfacts.MatchUpdater
import com.cshep4.premierpredictor.data.api.live.commentary.Commentary
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.extension.isInNeedOfUpdate
import com.cshep4.premierpredictor.repository.dynamodb.MatchFactsRepository
import kotlinx.coroutines.experimental.async
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

    fun retrieveLiveMatchFacts(id: Long): MatchFacts? {
        return runBlocking {
            val storedMatch = matchFactsRepository
                    .findById(id.toString())
                    .map { it.toDto() }
                    .orElse(null)

            var updatedMatch: MatchFacts? = null
            var updatedCommentary: Commentary? = null


            val matchFactsCoRoutine = async {
                if (doesMatchFactsNeedUpdating(storedMatch)) {
                    updatedMatch = matchUpdater.retrieveMatchFromApi(id.toString())
                }
            }

            val commentaryCoRoutine = async {
                if (doesCommentaryNeedUpdating(storedMatch)) {
                    updatedCommentary = commentaryUpdater.retrieveCommentaryFromApi(id.toString())
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
            matchFacts?.commentary == null || matchFacts.commentary!!.lastUpdated!!.isInNeedOfUpdate()

    private fun getRelevantMatchFacts(storedMatch: MatchFacts?, updatedMatch: MatchFacts?, updatedCommentary: Commentary?): MatchFacts? {
        val matchFacts = updatedMatch ?: storedMatch ?: return null

        matchFacts.commentary = when (updatedCommentary) {
            null -> storedMatch?.commentary
            else -> updatedCommentary
        }

        return matchFacts
    }
}