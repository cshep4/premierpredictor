package com.cshep4.premierpredictor.component.score

import com.cshep4.premierpredictor.component.leaguetable.LeagueTableCollector
import com.cshep4.premierpredictor.data.LeagueTable
import com.cshep4.premierpredictor.data.MatchPredictionResult
import com.cshep4.premierpredictor.data.TableTeam
import com.cshep4.premierpredictor.data.User
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class LeagueTableScoreCalculatorTest {
    companion object {
        const val NUM_TEAMS = 3
        const val SCORE_ADDITION = 5
    }

    @Mock
    private lateinit var leagueTableCollector: LeagueTableCollector

    @InjectMocks
    private lateinit var leagueTableScoreCalculator: LeagueTableScoreCalculator

    @Test
    fun `'calculate' gets a league table of the current scores`() {
        val users = listOf(User(id = 1))
        val predictedMatches = listOf(MatchPredictionResult(userId = 1, matchday = 4, hGoals = 1, aGoals = 1))
        val leagueTable = LeagueTable()

        whenever(leagueTableCollector.getCurrentLeagueTable()).thenReturn(leagueTable)

        leagueTableScoreCalculator.calculate(users, predictedMatches)

        verify(leagueTableCollector).getCurrentLeagueTable()

    }

    @Test
    fun `'calculate' loops through each user and creates a league table based on their predicted scores`() {
        val users = listOf(
                User(id = 1),
                User(id = 2),
                User(id = 3)
        )

        val predictedMatches = listOf(
                MatchPredictionResult(userId = 1, matchday = 4, hGoals = 1, aGoals = 1),
                MatchPredictionResult(userId = 2, matchday = 4, hGoals = 1, aGoals = 1),
                MatchPredictionResult(userId = 3, matchday = 4, hGoals = 1, aGoals = 1)
        )

        val leagueTable = LeagueTable()

        whenever(leagueTableCollector.getCurrentLeagueTable()).thenReturn(leagueTable)
        whenever(leagueTableCollector.createLeagueTableFromMatches(any())).thenReturn(leagueTable)

        leagueTableScoreCalculator.calculate(users, predictedMatches)

        verify(leagueTableCollector, times(3)).createLeagueTableFromMatches(any())
    }

    @Test
    fun `'calculate' loops through each user and compares their league table to the real one and awards 5 points for matching league positions, then returns the list of users`() {
        val users = listOf(
                User(id = 1, score = 0),
                User(id = 2, score = 0),
                User(id = 3, score = 0)
        )

        val predictedMatches = listOf(
                MatchPredictionResult(userId = 1, matchday = 4, hGoals = 1, aGoals = 1),
                MatchPredictionResult(userId = 2, matchday = 4, hGoals = 1, aGoals = 1),
                MatchPredictionResult(userId = 3, matchday = 4, hGoals = 1, aGoals = 1)
        )

        val leagueTable1 = LeagueTable(table = mutableListOf(
                TableTeam(teamName = "Team 1", played = 3),
                TableTeam(teamName = "Team 2", played = 3),
                TableTeam(teamName = "Team 3", played = 3))
        )

        val leagueTable2 = LeagueTable(table = mutableListOf(
                TableTeam(teamName = "Team 3", played = 3),
                TableTeam(teamName = "Team 2", played = 3),
                TableTeam(teamName = "Team 1", played = 3))
        )

        val leagueTable3 = LeagueTable(table = mutableListOf(
                    TableTeam(teamName = "Team 2", played = 3),
                    TableTeam(teamName = "Team 3", played = 3),
                    TableTeam(teamName = "Team 1", played = 3))
        )

        whenever(leagueTableCollector.getCurrentLeagueTable()).thenReturn(leagueTable1)

        whenever(leagueTableCollector.createLeagueTableFromMatches(any()))
                .thenReturn(leagueTable1)
                .thenReturn(leagueTable2)
                .thenReturn(leagueTable3)

        val result = leagueTableScoreCalculator.calculate(users, predictedMatches)

        val allRight = NUM_TEAMS * SCORE_ADDITION
        val oneRight = SCORE_ADDITION
        val noneRight = 0

        assertThat(result[0].score, `is`(allRight))
        assertThat(result[1].score, `is`(oneRight))
        assertThat(result[2].score, `is`(noneRight))
    }

    @Test
    fun `'calculate' does not update if the league hasn't finished`() {
        val users = listOf(User(id = 1, score = 0), User(id = 2, score = 0), User(id = 3, score = 0))
        val predictedMatches = listOf(
                MatchPredictionResult(userId = 1, matchday = 1, hGoals = 1, aGoals = 2),
                MatchPredictionResult(userId = 2, matchday = 2, hGoals = 3, aGoals = 0),
                MatchPredictionResult(userId = 3, matchday = 3, hGoals = null, aGoals = null)
        )

        leagueTableScoreCalculator.calculate(users, predictedMatches)

        verify(leagueTableCollector, times(0)).getCurrentLeagueTable()
        verify(leagueTableCollector, times(0)).createLeagueTableFromMatches(any())
    }
}