package com.cshep4.premierpredictor.schedule

import com.cshep4.premierpredictor.constant.MatchConstants.LIVE_MATCH_SUBSCRIPTION
import com.cshep4.premierpredictor.constant.MatchConstants.UPCOMING_SUBSCRIPTION
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.service.fixtures.FixturesService
import com.cshep4.premierpredictor.service.fixtures.ResultsService
import com.cshep4.premierpredictor.service.livematch.LiveMatchService
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.messaging.simp.SimpMessagingTemplate
import java.time.LocalDate

@RunWith(MockitoJUnitRunner::class)
@Ignore
internal class MatchUpdateSchedulerTest {
    @Mock
    private lateinit var fixturesService: FixturesService

    @Mock
    private lateinit var liveMatchService: LiveMatchService

    @Mock
    private lateinit var template: SimpMessagingTemplate

    @Mock
    private lateinit var resultsService: ResultsService

    @InjectMocks
    private lateinit var matchUpdateScheduler: MatchUpdateScheduler

    @Test
    fun `'getMatchesCurrentlyPlaying' should get all upcoming fixtures and adds the currently playing match ids to the set`() {
        val matches = mapOf(
                Pair(
                        LocalDate.now(),
                        listOf(
                                MatchFacts(id = "1", status = ""),
                                MatchFacts(id = "2", status = "66"),
                                MatchFacts(id = "3", status = "FT")
                        )
                ),
                Pair(
                        LocalDate.now().plusDays(1),
                        listOf(
                                MatchFacts(id = "4", status = "HT"),
                                MatchFacts(id = "5", status = "80"),
                                MatchFacts(id = "6", status = "FT")
                        )
                )
        )

        whenever(fixturesService.retrieveAllUpcomingFixtures()).thenReturn(matches)

        matchUpdateScheduler.getMatchesCurrentlyPlaying()

        val currentlyPlaying = matchUpdateScheduler.liveMatchIds

        assertThat(currentlyPlaying.contains("1"), `is`(false))
        assertThat(currentlyPlaying.contains("2"), `is`(true))
        assertThat(currentlyPlaying.contains("3"), `is`(false))
        assertThat(currentlyPlaying.contains("4"), `is`(true))
        assertThat(currentlyPlaying.contains("5"), `is`(true))
        assertThat(currentlyPlaying.contains("6"), `is`(false))
    }

    @Test
    fun `'updateLiveScores' retrieves match facts for each match id in liveMatch set and sends to the 'upcoming' subscription and removes matches that are no longer playing from future updates`() {
        matchUpdateScheduler.addLiveMatch(listOf("1", "2", "3", "4", "5", "6"))
        val matches = listOf(
                MatchFacts(id = "1", status = "66", localTeamName = "Team 1", visitorTeamName = "Team 2", week = "1", time = "00:00", formattedDate = "01.01.2000"),
                MatchFacts(id = "2", status = "FT", localTeamName = "Team 1", visitorTeamName = "Team 2", week = "1", time = "00:00", formattedDate = "01.01.2000"),
                MatchFacts(id = "3", status = "", localTeamName = "Team 1", visitorTeamName = "Team 2", week = "1", time = "00:00", formattedDate = "01.01.2000"),
                MatchFacts(id = "4", status = "66", localTeamName = "Team 1", visitorTeamName = "Team 2", week = "1", time = "00:00", formattedDate = "01.01.2000"),
                MatchFacts(id = "5", status = "66", localTeamName = "Team 1", visitorTeamName = "Team 2", week = "1", time = "00:00", formattedDate = "01.01.2000")
        )

        whenever(liveMatchService.retrieveLiveMatchFacts(any())).thenReturn(matches[0])
                .thenReturn(matches[1])
                .thenReturn(matches[2])
                .thenReturn(matches[3])
                .thenReturn(matches[4])
                .thenReturn(null)

        matchUpdateScheduler.updateLiveScores()

        val currentlyPlaying = matchUpdateScheduler.liveMatchIds

        assertThat(currentlyPlaying.contains("1"), `is`(true))
        assertThat(currentlyPlaying.contains("2"), `is`(false))
        assertThat(currentlyPlaying.contains("3"), `is`(false))
        assertThat(currentlyPlaying.contains("4"), `is`(true))
        assertThat(currentlyPlaying.contains("5"), `is`(true))
        assertThat(currentlyPlaying.contains("6"), `is`(false))

        verify(liveMatchService, times(6)).retrieveLiveMatchFacts(any())
        verify(template).convertAndSend(UPCOMING_SUBSCRIPTION, matches)
    }

    @Test
    @Ignore(value = "This is handled elsewhere at the moment")
    fun `'updateLiveScores' will send each match to the corresponding subscription`() {
        matchUpdateScheduler.addLiveMatch(listOf("1", "2", "3", "4", "5", "6"))
        val matches = listOf(
                MatchFacts(id = "1", status = "66", localTeamName = "Team 1", visitorTeamName = "Team 2", week = "1", time = "00:00", formattedDate = "01.01.2000"),
                MatchFacts(id = "2", status = "FT", localTeamName = "Team 1", visitorTeamName = "Team 2", week = "1", time = "00:00", formattedDate = "01.01.2000"),
                MatchFacts(id = "3", status = "", localTeamName = "Team 1", visitorTeamName = "Team 2", week = "1", time = "00:00", formattedDate = "01.01.2000"),
                MatchFacts(id = "4", status = "66", localTeamName = "Team 1", visitorTeamName = "Team 2", week = "1", time = "00:00", formattedDate = "01.01.2000"),
                MatchFacts(id = "5", status = "66", localTeamName = "Team 1", visitorTeamName = "Team 2", week = "1", time = "00:00", formattedDate = "01.01.2000")
        )

        whenever(liveMatchService.retrieveLiveMatchFacts(any())).thenReturn(matches[0])
                .thenReturn(matches[1])
                .thenReturn(matches[2])
                .thenReturn(matches[3])
                .thenReturn(matches[4])
                .thenReturn(null)

        matchUpdateScheduler.updateLiveScores()

        verify(template).convertAndSend(LIVE_MATCH_SUBSCRIPTION + "1", matches[0])
        verify(template).convertAndSend(LIVE_MATCH_SUBSCRIPTION + "2", matches[1])
        verify(template).convertAndSend(LIVE_MATCH_SUBSCRIPTION + "3", matches[2])
        verify(template).convertAndSend(LIVE_MATCH_SUBSCRIPTION + "4", matches[3])
        verify(template).convertAndSend(LIVE_MATCH_SUBSCRIPTION + "5", matches[4])
    }

    @Test
    fun `'updateLiveScores' send result to postgres DB if match has finished`() {
        matchUpdateScheduler.addLiveMatch(listOf("1", "2", "3", "4", "5", "6"))
        val matches = listOf(
                MatchFacts(id = "1", status = "90", localTeamName = "Team 1", visitorTeamName = "Team 2", week = "1", time = "00:00", formattedDate = "01.01.2000"),
                MatchFacts(id = "2", status = "FT", localTeamName = "Team 1", visitorTeamName = "Team 2", week = "1", time = "00:00", formattedDate = "01.01.2000"),
                MatchFacts(id = "3", status = "FT", localTeamName = "Team 1", visitorTeamName = "Team 2", week = "1", time = "00:00", formattedDate = "01.01.2000"),
                MatchFacts(id = "4", status = "90", localTeamName = "Team 1", visitorTeamName = "Team 2", week = "1", time = "00:00", formattedDate = "01.01.2000"),
                MatchFacts(id = "5", status = "FT", localTeamName = "Team 1", visitorTeamName = "Team 2", week = "1", time = "00:00", formattedDate = "01.01.2000")
        )

        whenever(liveMatchService.retrieveLiveMatchFacts(any())).thenReturn(matches[0])
                .thenReturn(matches[1])
                .thenReturn(matches[2])
                .thenReturn(matches[3])
                .thenReturn(matches[4])
                .thenReturn(null)

        matchUpdateScheduler.updateLiveScores()

        val expectedUpdatedMatches = matches
                .filter { it.status == "FT" }
                .map { it.toMatch() }

        verify(fixturesService).saveMatches(expectedUpdatedMatches)
    }

    @Test
    fun `'updateMatchesAt3amEachDay' will run the fixture updater once a day at 3am UK time`() {
        matchUpdateScheduler.updateMatchesAt3amEachDay()

        verify(resultsService).update()
    }
}