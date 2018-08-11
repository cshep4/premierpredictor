package com.cshep4.premierpredictor.component.matchfacts

import com.cshep4.premierpredictor.component.api.ApiRequester
import com.cshep4.premierpredictor.component.time.Time
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.entity.MatchFactsEntity
import com.cshep4.premierpredictor.extension.isInNeedOfUpdate
import com.cshep4.premierpredictor.repository.dynamodb.MatchFactsRepository
import kotlinx.coroutines.experimental.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MatchUpdater {
    @Autowired
    private lateinit var fixtureApiRequester: ApiRequester

    @Autowired
    private lateinit var matchFactsRepository: MatchFactsRepository

    @Autowired
    private lateinit var time: Time

    fun updateUpcomingMatchesWithLatestScores(upcomingMatches: List<MatchFacts>): List<MatchFacts> {
        val apiResult = fixtureApiRequester.retrieveFixtures()

        val updated = upcomingMatches
                .filter { it.lastUpdated!!.isInNeedOfUpdate() }
                .map { m -> mergeWithLatestVersion(m, apiResult.firstOrNull { it.id == m.id }) }

        val notUpdated = upcomingMatches.filter { !it.lastUpdated!!.isInNeedOfUpdate() }

        val updatedMatchEntities = updated.map { MatchFactsEntity.fromDto(it) }

        launch {
            matchFactsRepository.saveAll(updatedMatchEntities)
        }

        return listOf(notUpdated, updated).flatten()
    }

    private fun mergeWithLatestVersion(match: MatchFacts, apiMatch: MatchFacts?): MatchFacts {
        apiMatch ?: return match

        apiMatch.commentary = match.commentary
        apiMatch.lastUpdated = time.localDateTimeNow()

        return apiMatch
    }

    fun updateMatch(id: String, match: MatchFacts?): MatchFacts? {
        val apiResult = retrieveMatchFromApi(id) ?: return match
        apiResult.commentary = match?.commentary

        launch {
            matchFactsRepository.save(MatchFactsEntity.fromDto(apiResult))
        }

        return apiResult
    }

    fun retrieveMatchFromApi(id: String): MatchFacts? {
        val apiResult = fixtureApiRequester.retrieveMatch(id) ?: return null
        apiResult.lastUpdated = time.localDateTimeNow()

        return apiResult
    }
}