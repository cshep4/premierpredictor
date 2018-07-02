package com.cshep4.premierpredictor.component.override

import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.MatchWithOverride
import com.cshep4.premierpredictor.data.OverrideMatch
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.time.LocalDateTime

internal class MatchOverrideMergerTest {
    private companion object {
        const val TEAM1 = "Team 1"
        const val TEAM2 = "Team 2"
        const val TEAM3 = "Team 3"
        const val TEAM4 = "Team 4"
        const val ONE = 1
        const val TWO = 2
        const val THREE = 3
        val NOW = LocalDateTime.now()!!
    }

    private val matchOverrideMerger = MatchOverrideMerger()

    @Test
    fun `'merge' takes a list of matches and overrides and merges them`() {
        val matches = listOf(
                Match(id = 1, hTeam = TEAM1, aTeam = TEAM2, hGoals = ONE, aGoals = TWO, played = ONE, dateTime = NOW, matchday = ONE),
                Match(id = 2, hTeam = TEAM3, aTeam = TEAM4, hGoals = TWO, aGoals = ONE, played = ONE, dateTime = NOW, matchday = ONE),
                Match(id = 3, hTeam = TEAM2, aTeam = TEAM1, hGoals = ONE, aGoals = TWO, played = ONE, dateTime = NOW, matchday = TWO),
                Match(id = 4, hTeam = TEAM3, aTeam = TEAM1, hGoals = ONE, aGoals = TWO, played = ONE, dateTime = NOW, matchday = THREE)
        )

        val overrides = listOf(
                OverrideMatch(id = 1, hGoals = TWO, aGoals = ONE),
                OverrideMatch(id = 3, hGoals = THREE, aGoals = ONE),
                OverrideMatch(id = 2, hGoals = ONE, aGoals = ONE)
        )

        val expectedMergedMatches = listOf(
                MatchWithOverride(id = 1, hTeam = TEAM1, aTeam = TEAM2, hGoals = ONE, aGoals = TWO, hOverride = TWO, aOverride = ONE, played = ONE, dateTime = NOW, matchday = ONE),
                MatchWithOverride(id = 2, hTeam = TEAM3, aTeam = TEAM4, hGoals = TWO, aGoals = ONE, hOverride = ONE, aOverride = ONE, played = ONE, dateTime = NOW, matchday = ONE),
                MatchWithOverride(id = 3, hTeam = TEAM2, aTeam = TEAM1, hGoals = ONE, aGoals = TWO, hOverride = THREE, aOverride = ONE, played = ONE, dateTime = NOW, matchday = TWO),
                MatchWithOverride(id = 4, hTeam = TEAM3, aTeam = TEAM1, hGoals = ONE, aGoals = TWO, hOverride = null, aOverride = null, played = ONE, dateTime = NOW, matchday = THREE)
        )

        val mergedMatches = matchOverrideMerger.merge(matches, overrides)

        assertThat(mergedMatches, `is`(expectedMergedMatches))
    }

}