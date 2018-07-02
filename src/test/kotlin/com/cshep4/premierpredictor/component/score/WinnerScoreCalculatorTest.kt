package com.cshep4.premierpredictor.component.score

import com.cshep4.premierpredictor.component.leaguetable.LeagueTableCollector
import com.cshep4.premierpredictor.data.LeagueTable
import com.cshep4.premierpredictor.data.TableTeam
import com.cshep4.premierpredictor.data.User
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class WinnerScoreCalculatorTest {
    companion object {
        const val TEAM_ONE = "Team 1"
        const val TEAM_TWO = "Team 2"
        const val WINNER_ADDITION = 20
    }

    @Mock
    private lateinit var leagueTableCollector: LeagueTableCollector

    @InjectMocks
    private lateinit var winnerScoreCalculator: WinnerScoreCalculator

    private val users = listOf(
            User(id = 1, score = 0, predictedWinner = TEAM_ONE),
            User(id = 2, score = 0, predictedWinner = TEAM_TWO),
            User(id = 3, score = 0, predictedWinner = TEAM_ONE)
    )

    @Test
    fun `'calculate' checks if final has been played and does nothing if not`() {
        val leagueTable = LeagueTable(table = mutableListOf(
                TableTeam(rank = 1, teamName = TEAM_ONE, played = 37),
                TableTeam(rank = 2, teamName = TEAM_TWO, played = 37))
        )

        whenever(leagueTableCollector.getCurrentLeagueTable()).thenReturn(leagueTable)

        val result = winnerScoreCalculator.calculate(users)

        assertThat(result[0].score, `is`(0))
        assertThat(result[1].score, `is`(0))
        assertThat(result[2].score, `is`(0))
    }

    @Test
    fun `'calculate' loops through users and adds 20 to their score if the final winner matches their prediction`() {
        val leagueTable = LeagueTable(table = mutableListOf(
                TableTeam(rank = 1, teamName = TEAM_ONE, played = 38),
                TableTeam(rank = 2, teamName = TEAM_TWO, played = 38))
        )

        whenever(leagueTableCollector.getCurrentLeagueTable()).thenReturn(leagueTable)

        val result = winnerScoreCalculator.calculate(users)

        assertThat(result[0].score, `is`(WINNER_ADDITION))
        assertThat(result[1].score, `is`(0))
        assertThat(result[2].score, `is`(WINNER_ADDITION))
    }

}