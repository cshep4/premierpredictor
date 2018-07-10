package com.cshep4.premierpredictor.component.leaguetable

import com.cshep4.premierpredictor.data.LeagueTable
import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.PredictedMatch
import com.cshep4.premierpredictor.service.fixtures.FixturesService
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class LeagueTableCollectorTest {
    @Mock
    private lateinit var fixturesService: FixturesService

    @Mock
    private lateinit var leagueTableCalculator: LeagueTableCalculator

    @InjectMocks
    private lateinit var leagueTableCollector: LeagueTableCollector

    @Test
    fun `'getCurrentLeagueTable' retrieves matches, creates a league table and returns standings`() {
        val matches = listOf(Match(matchday = 1), Match(matchday = 5))
        val leagueTable = LeagueTable()

        whenever(fixturesService.retrieveAllMatches()).thenReturn(matches)
        whenever(leagueTableCalculator.calculate(matches, LeagueTable.emptyTable())).thenReturn(leagueTable)

        val result = leagueTableCollector.getCurrentLeagueTable()

        assertThat(result, `is`(leagueTable))
    }

    @Test
    fun `'getPredictedLeagueTable' retrieves match predictions, creates a league table and returns standings`() {
        val predictedMatches = listOf(PredictedMatch(matchday = 1), PredictedMatch(matchday = 5))
        val matches = predictedMatches.map { it.toMatch() }
        val leagueTable = LeagueTable()

        whenever(fixturesService.retrieveAllMatchesWithPredictions(1)).thenReturn(predictedMatches)
        whenever(leagueTableCalculator.calculate(matches, LeagueTable.emptyTable())).thenReturn(leagueTable)

        val result = leagueTableCollector.getPredictedLeagueTable(1)

        assertThat(result, `is`(leagueTable))
    }
}