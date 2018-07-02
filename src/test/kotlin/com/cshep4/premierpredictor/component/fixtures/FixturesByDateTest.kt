package com.cshep4.premierpredictor.component.fixtures

import com.cshep4.premierpredictor.data.Match
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

internal class FixturesByDateTest {
    private val fixturesByDate = FixturesByDate()

    @Test
    fun `'format' puts matches in order of time and groups them by date in the correct format (may fail if time is passed 8pm)`() {
        val match1 = Match(dateTime = LocalDateTime.now().plusDays(1).plusHours(1))
        val match2 = Match(dateTime = LocalDateTime.now().plusDays(1).plusHours(3))
        val match3 = Match(dateTime = LocalDateTime.now().plusDays(2))
        val match4 = Match(dateTime = LocalDateTime.now().plusDays(3).plusHours(4))
        val match5 = Match(dateTime = LocalDateTime.now().plusDays(3).plusHours(1))
        val match6 = Match(dateTime = LocalDateTime.now().plusDays(1).plusHours(2))

        val matches = listOf(match1, match2, match3, match4, match5, match6)

        val expectedResult = mapOf(
                Pair(LocalDate.now().plusDays(1), listOf(match1, match6, match2)),
                Pair(LocalDate.now().plusDays(2), listOf(match3)),
                Pair(LocalDate.now().plusDays(3), listOf(match5, match4))
        )

        val result = fixturesByDate.format(matches)

        assertThat(result, `is`(expectedResult))
    }

}