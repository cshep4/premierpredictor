package com.cshep4.premierpredictor.component.fixtures

import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.time.LocalDateTime
import org.hamcrest.CoreMatchers.`is` as Is

internal class FixtureFormatterTest {
    companion object {
        const val TEAM_1 = "Liverpool"
        const val TEAM_2 = "Chelsea"
        const val TEAM_3 = "Man City"
    }
    private val fixtureFormatter = FixtureFormatter()

    @Test
    fun `'format' returns list of matches`() {
        val apiResult = listOf(MatchFacts(
                id = "1",
                localTeamName = TEAM_1,
                visitorTeamName = TEAM_2,
                localTeamScore = "2",
                visitorTeamScore = "1",
                formattedDate = "02.05.1993",
                time = "12:00",
                week = "38"
        ))

        val result = fixtureFormatter.format(apiResult)

        val match = result[0]

        val expectedDateTime = LocalDateTime.of(1993, 5, 2, 12, 0)

        assertThat(match.id, Is(1L))
        assertThat(match.hTeam, Is(TEAM_1))
        assertThat(match.aTeam, Is(TEAM_2))
        assertThat(match.hGoals, Is(2))
        assertThat(match.aGoals, Is(1))
        assertThat(match.played, Is(1))
        assertThat(match.dateTime, Is(expectedDateTime))
        assertThat(match.matchday, Is(38))
    }

    @Test
    fun `'format' returns empty list if no fixtures`() {
        val result = fixtureFormatter.format(emptyList())

        assertThat(result.isEmpty(), Is(true))
    }

    @Test
    fun `'groupIntoTeams' takes a list of matches and groups them into teams and orders matches by date`(){
        val m1 = Match(id = 1, hTeam = TEAM_1, aTeam = TEAM_2, dateTime = LocalDateTime.now().minusDays(1))
        val m2 = Match(id = 2, hTeam = TEAM_1, aTeam = TEAM_3, dateTime = LocalDateTime.now().minusDays(2))
        val m3 = Match(id = 3, hTeam = TEAM_2, aTeam = TEAM_1, dateTime = LocalDateTime.now().minusDays(3))

        val matches = listOf(m1, m2, m3)

        val result = fixtureFormatter.groupIntoTeams(matches)

        val expectedResult = mapOf(
                Pair(TEAM_1, listOf(m3, m2, m1)),
                Pair(TEAM_2, listOf(m3, m1)),
                Pair(TEAM_3, listOf(m2))
        )

        assertThat(result, Is(expectedResult))
    }
}