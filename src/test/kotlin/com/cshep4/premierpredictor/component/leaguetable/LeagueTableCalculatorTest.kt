package com.cshep4.premierpredictor.component.leaguetable

import com.cshep4.premierpredictor.data.LeagueTable
import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.TableTeam
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

internal class LeagueTableCalculatorTest {
    private val team1 = "Team 1"
    private val team2 = "Team 2"
    private val team3 = "Team 3"

    private val leagueTableCalculator = LeagueTableCalculator()

    @Test
    fun `'calculate' all data is calculated correctly for each team`() {
        val leagueTable = LeagueTable(table = mutableListOf(
                TableTeam(teamName = team1),
                TableTeam(teamName = team2)
        ))
        val matches = listOf(Match(hTeam = team1, aTeam = team2, hGoals = 3, aGoals = 2, played = 1))

        val result = leagueTableCalculator.calculate(matches, leagueTable)
        val table = result.table

        assertThat(table[0].rank, `is`(1))
        assertThat(table[0].teamName, `is`(team1))
        assertThat(table[0].played, `is`(1))
        assertThat(table[0].points, `is`(3))
        assertThat(table[0].wins, `is`(1))
        assertThat(table[0].draws, `is`(0))
        assertThat(table[0].losses, `is`(0))
        assertThat(table[0].goalsFor, `is`(3))
        assertThat(table[0].goalsAgainst, `is`(2))
        assertThat(table[0].goalDifference, `is`(1))
        assertThat(table[1].rank, `is`(2))
        assertThat(table[1].teamName, `is`(team2))
        assertThat(table[1].played, `is`(1))
        assertThat(table[1].points, `is`(0))
        assertThat(table[1].wins, `is`(0))
        assertThat(table[1].draws, `is`(0))
        assertThat(table[1].losses, `is`(1))
        assertThat(table[1].goalsFor, `is`(2))
        assertThat(table[1].goalsAgainst, `is`(3))
        assertThat(table[1].goalDifference, `is`(-1))
    }

    @Test
    fun `'calculate' puts teams in order of points`() {
        val leagueTable = LeagueTable(table = mutableListOf(
                TableTeam(teamName = team1),
                TableTeam(teamName = team2),
                TableTeam(teamName = team3)
        ))

        val matches = listOf(
                Match(hTeam = team1, aTeam = team2, hGoals = 3, aGoals = 2, played = 1),
                Match(hTeam = team2, aTeam = team3, hGoals = 1, aGoals = 0, played = 1),
                Match(hTeam = team1, aTeam = team3, hGoals = 3, aGoals = 0, played = 1)
        )

        val result = leagueTableCalculator.calculate(matches, leagueTable)
        val table = result.table

        assertThat(table[0].rank, `is`(1))
        assertThat(table[0].teamName, `is`(team1))
        assertThat(table[1].rank, `is`(2))
        assertThat(table[1].teamName, `is`(team2))
        assertThat(table[2].rank, `is`(3))
        assertThat(table[2].teamName, `is`(team3))
    }

    @Test
    fun `'calculate' if points are level then teams will be in order of goal difference`() {
        val leagueTable = LeagueTable(table = mutableListOf(
                TableTeam(teamName = team1),
                TableTeam(teamName = team2),
                TableTeam(teamName = team3)
        ))

        val matches = listOf(
                Match(hTeam = team1, aTeam = team3, hGoals = 3, aGoals = 2, played = 1),
                Match(hTeam = team2, aTeam = team3, hGoals = 4, aGoals = 0, played = 1)
        )

        val result = leagueTableCalculator.calculate(matches, leagueTable)
        val table = result.table

        assertThat(table[0].rank, `is`(1))
        assertThat(table[0].teamName, `is`(team2))
        assertThat(table[1].rank, `is`(2))
        assertThat(table[1].teamName, `is`(team1))
        assertThat(table[0].points, `is`(table[1].points))
    }

    @Test
    fun `'calculate' if goal differences are level then teams will be in order of goals scored`() {
        val leagueTable = LeagueTable(table = mutableListOf(
                TableTeam(teamName = team1),
                TableTeam(teamName = team2),
                TableTeam(teamName = team3)
        ))

        val matches = listOf(
                Match(hTeam = team1, aTeam = team3, hGoals = 3, aGoals = 2, played = 1),
                Match(hTeam = team2, aTeam = team3, hGoals = 2, aGoals = 1, played = 1)
        )

        val result = leagueTableCalculator.calculate(matches, leagueTable)
        val table = result.table

        assertThat(table[0].rank, `is`(1))
        assertThat(table[0].teamName, `is`(team1))
        assertThat(table[1].rank, `is`(2))
        assertThat(table[1].teamName, `is`(team2))
        assertThat(table[0].points, `is`(table[1].points))
        assertThat(table[0].goalDifference, `is`(table[1].goalDifference))
    }

    @Test
    fun `'calculate' only matches that have been played are counted`() {
        val leagueTable = LeagueTable(table = mutableListOf(
                TableTeam(teamName = team1),
                TableTeam(teamName = team2)
        ))

        val matches = listOf(
                Match(hTeam = team1, aTeam = team2, hGoals = 3, aGoals = 2, played = 1),
                Match(hTeam = team1, aTeam = team2, hGoals = null, aGoals = null, played = 0),
                Match(hTeam = team1, aTeam = team2, hGoals = null, aGoals = null, played = 0)
        )

        val result = leagueTableCalculator.calculate(matches, leagueTable)
        val table = result.table

        assertThat(table[0].played, `is`(1))
        assertThat(table[1].played, `is`(1))
    }
}