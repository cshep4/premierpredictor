package com.cshep4.premierpredictor.schedule

import com.cshep4.premierpredictor.constant.MatchConstants.UPCOMING_SUBSCRIPTION
import com.cshep4.premierpredictor.extension.isPlaying
import com.cshep4.premierpredictor.service.fixtures.FixturesService
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
    private lateinit var template: SimpMessagingTemplate

    @Scheduled(cron = "0 1 * ? * *")
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
                .mapNotNull { fixturesService.retrieveLiveScoreForMatch(it) }

        liveMatchIds.removeIf { id -> !liveMatches.find { it.id == id }.isPlaying() }

        template.convertAndSend(UPCOMING_SUBSCRIPTION, liveMatches)
    }

    fun addLiveMatch(ids: List<String>) {
        liveMatchIds.addAll(ids)
    }
}