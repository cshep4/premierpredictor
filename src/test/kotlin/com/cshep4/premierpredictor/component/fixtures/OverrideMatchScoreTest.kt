package com.cshep4.premierpredictor.component.fixtures

import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.OverrideMatch
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

internal class OverrideMatchScoreTest {
    private var overrideMatchScore = OverrideMatchScore()

    @Test
    fun `'update' takes a list of matches and overridden scores and merges them to create a list of matches`() {
        val matches = listOf(
                Match(id = 1, hGoals = 1, aGoals = 0),
                Match(id = 2, hGoals = 4, aGoals = 1),
                Match(id = 3, hGoals = null, aGoals = null),
                Match(id = 4, hGoals = null, aGoals = null)
        )

        val overrides = listOf(
                OverrideMatch(id = 1, hGoals = 3, aGoals = 3),
                OverrideMatch(id = 3, hGoals = 2, aGoals = 0)
        )

        val result = overrideMatchScore.update(matches, overrides)

        assertThat(result[0].id, `is`(1L))
        assertThat(result[0].hGoals, `is`(3))
        assertThat(result[0].aGoals, `is`(3))

        assertThat(result[1].id, `is`(2L))
        assertThat(result[1].hGoals, `is`(4))
        assertThat(result[1].aGoals, `is`(1))

        assertThat(result[2].id, `is`(3L))
        assertThat(result[2].hGoals, `is`(2))
        assertThat(result[2].aGoals, `is`(0))

        assertThat(result[3].id, `is`(4L))
        assertThat(result[3].hGoals, `is`(nullValue()))
        assertThat(result[3].aGoals, `is`(nullValue()))
    }
}