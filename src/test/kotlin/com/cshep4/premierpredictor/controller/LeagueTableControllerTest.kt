package com.cshep4.premierpredictor.controller

import com.cshep4.premierpredictor.component.leaguetable.LeagueTableCollector
import com.cshep4.premierpredictor.data.LeagueTable
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus

@RunWith(MockitoJUnitRunner::class)
internal class LeagueTableControllerTest {
    @Mock
    private lateinit var leagueTableCollector: LeagueTableCollector

    @InjectMocks
    private lateinit var leagueTableController: LeagueTableController

    @Test
    fun `'getCurrentStandings' should return the current standings in the response body and status OK`() {
        val standings = LeagueTable()

        whenever(leagueTableCollector.getCurrentLeagueTable()).thenReturn(standings)

        val result = leagueTableController.getCurrentLeagueTable()

        assertThat(result.statusCode, `is`(HttpStatus.OK))
        assertThat(result.body, `is`(standings))
    }

    @Test
    fun `'getPredictedStandings' should return the predicted standings in the response body and status OK`() {
        val standings = LeagueTable()

        whenever(leagueTableCollector.getPredictedLeagueTable(1)).thenReturn(standings)

        val result = leagueTableController.getPredictedLeagueTable(1)

        assertThat(result.statusCode, `is`(HttpStatus.OK))
        assertThat(result.body, `is`(standings))
    }
}