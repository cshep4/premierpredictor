package com.cshep4.premierpredictor.controller

import com.cshep4.premierpredictor.data.*
import com.cshep4.premierpredictor.service.StandingsService
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus.*

@RunWith(MockitoJUnitRunner::class)
internal class StandingsControllerTest {
    @Mock
    lateinit var standingsService: StandingsService

    @InjectMocks
    lateinit var standingsController: StandingsController

    @Test
    fun `'getUsersLeagueList' returns OK and standings overview`() {
        val standingsOverview = StandingsOverview()

        whenever(standingsService.retrieveStandingsOverview(1)).thenReturn(standingsOverview)

        val result = standingsController.getUsersLeagueList(1)

        assertThat(result.statusCode, `is`(OK))
        assertThat(result.body, `is`(standingsOverview))
    }

    @Test
    fun `'joinUserLeague' returns OK and returns league overview when user joins league`() {
        val userLeague = UserLeague()
        val userLeagueOverview = UserLeagueOverview()

        whenever(standingsService.joinLeague(userLeague)).thenReturn(userLeagueOverview)

        val result = standingsController.joinUserLeague(userLeague)

        assertThat(result.statusCode, `is`(OK))
        assertThat(result.body, `is`(userLeagueOverview))
    }

    @Test
    fun `'joinUserLeague' returns NOT_FOUND when league doesn't exist`() {
        val userLeague = UserLeague()

        whenever(standingsService.joinLeague(userLeague)).thenReturn(null)

        val result = standingsController.joinUserLeague(userLeague)

        assertThat(result.statusCode, `is`(NOT_FOUND))
        assertThat(result.body, `is`(nullValue()))
    }

    @Test
    fun `'addUserLeague' returns CREATED and returns league user adds league`() {
        val name = "League"
        val id = 1L
        val league = League(name = name)
        val addLeague = AddLeague(name, id)

        whenever(standingsService.addLeague(name, id)).thenReturn(league)

        val result = standingsController.addUserLeague(addLeague)

        assertThat(result.statusCode, `is`(CREATED))
        assertThat(result.body, `is`(league))
    }

    @Test
    fun `'leaveUserLeague' returns OK when user leaves league`() {
        val userLeague = UserLeague()

        val result = standingsController.leaveUserLeague(userLeague)

        assertThat(result.statusCode, `is`(OK))
        assertThat(result.body, `is`(nullValue()))
    }

    @Test
    fun `'getLeagueTable' returns OK  with league table`() {
        val table = listOf(LeagueTableUser())

        whenever(standingsService.retrieveLeagueTable(1)).thenReturn(table)

        val result = standingsController.getLeagueTable(1)

        assertThat(result.statusCode, `is`(OK))
        assertThat(result.body, `is`(table))
    }

    @Test
    fun `'getOverallLeagueTable' returns OK  with league table`() {
        val table = listOf(LeagueTableUser())

        whenever(standingsService.retrieveOverallLeagueTable()).thenReturn(table)

        val result = standingsController.getOverallLeagueTable()

        assertThat(result.statusCode, `is`(OK))
        assertThat(result.body, `is`(table))
    }

    @Test
    fun `'renameLeague' returns OK  when league is renamed`() {
        val league = League()

        whenever(standingsService.renameLeague(league)).thenReturn(league)

        val result = standingsController.renameLeague(league)

        assertThat(result.statusCode, `is`(OK))
        assertThat(result.body, `is`(league))
    }
}