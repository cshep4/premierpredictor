package com.cshep4.premierpredictor.service.team

import com.cshep4.premierpredictor.component.fixtures.FixtureFormatter
import com.cshep4.premierpredictor.component.team.FormFormatter
import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.TeamForm
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
internal class TeamServiceTest {
    @Mock
    private lateinit var fixturesService: FixturesService

    @Mock
    private lateinit var fixtureFormatter: FixtureFormatter

    @Mock
    private lateinit var formFormatter: FormFormatter

    @InjectMocks
    private lateinit var teamService: TeamService

    @Test
    fun `'retrieveRecentForms' will get past results from db, split matches into team's then format into correct response`() {
        val matches = listOf(Match())
        whenever(fixturesService.retrieveAllPastMatches()).thenReturn(matches)

        val groupedMatches = mapOf(
                Pair("Team 1", listOf(Match()))
        )
        whenever(fixtureFormatter.groupIntoTeams(matches)).thenReturn(groupedMatches)

        val formResult = mapOf(
                Pair("Team 1", TeamForm())
        )
        whenever(formFormatter.formatLastFiveGames(groupedMatches)).thenReturn(formResult)

        val result = teamService.retrieveRecentForms()

        assertThat(result, `is`(formResult))
    }

}