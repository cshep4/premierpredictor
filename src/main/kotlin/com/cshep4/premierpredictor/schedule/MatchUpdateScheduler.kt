package com.cshep4.premierpredictor.schedule

import com.cshep4.premierpredictor.constant.MatchConstants.LIVE_MATCH_SUBSCRIPTION
import com.cshep4.premierpredictor.constant.MatchConstants.UPCOMING_SUBSCRIPTION
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.extension.isPlaying
import com.cshep4.premierpredictor.service.fixtures.FixturesService
import com.cshep4.premierpredictor.service.livematch.LiveMatchService
import kotlinx.coroutines.experimental.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class MatchUpdateScheduler {
    val liveMatchIds = HashSet<String>()

    @Autowired
    private lateinit var fixturesService: FixturesService

    @Autowired
    private lateinit var liveMatchService: LiveMatchService

    @Autowired
    private lateinit var template: SimpMessagingTemplate

    @Scheduled(cron = "0 1,31 * * * *")
    fun getMatchesCurrentlyPlaying() {
        val matches = fixturesService.retrieveAllUpcomingFixtures()
                .values
                .flatten()
                .filter { it.isPlaying() }
                .map { it.id!! }

        addLiveMatch(matches)
    }

    @Scheduled(fixedDelay = 5000)
    fun updateLiveScores() {
        val liveMatches = liveMatchIds
                .mapNotNull { liveMatchService.retrieveLiveMatchFacts(it) }

        liveMatchIds.removeIf { id -> !liveMatches.find { it.id == id }.isPlaying() }

        val finishedMatches = liveMatches
                .filter { it.status == "FT" }
                .map { it.toMatch() }

        if (!finishedMatches.isEmpty()) {
            fixturesService.saveMatches(finishedMatches)
        }

        sendUpdates(liveMatches)
    }

    private fun sendUpdates(liveMatches: List<MatchFacts>) {
        launch {
            liveMatches.forEach { template.convertAndSend(LIVE_MATCH_SUBSCRIPTION + it.id, it) }
        }

        launch {
            template.convertAndSend(UPCOMING_SUBSCRIPTION, liveMatches)
        }
    }

    fun addLiveMatch(ids: List<String>) {
        liveMatchIds.addAll(ids)
    }
}