package com.cshep4.premierpredictor.component.fixtures

import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.time.LocalDateTime
import org.hamcrest.CoreMatchers.`is` as Is

internal class FixtureFormatterTest {
    companion object {
        const val TEAM_1 = "Liverpool"
        const val TEAM_2 = "Chelsea"
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
}