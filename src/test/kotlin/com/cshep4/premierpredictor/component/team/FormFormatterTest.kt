package com.cshep4.premierpredictor.component.team
import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.TeamForm
import com.cshep4.premierpredictor.data.TeamMatchResult
import com.cshep4.premierpredictor.enum.Location.AWAY
import com.cshep4.premierpredictor.enum.Location.HOME
import com.cshep4.premierpredictor.enum.Result.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.time.LocalDateTime

internal class FormFormatterTest {
    companion object {
        const val TEAM_1 = "Liverpool"
        const val TEAM_2 = "Chelsea"
        const val TEAM_3 = "Man City"
    }

    private val formFormatter = FormFormatter()

    @Test
    fun `'formatLastFiveGames' will take only the last 5 matches for each team and return in correct format`() {
        val m1 = Match(id = 1, hTeam = TEAM_1, aTeam = TEAM_2, hGoals = 1, aGoals = 0, dateTime = LocalDateTime.now().minusDays(1))
        val m2 = Match(id = 2, hTeam = TEAM_1, aTeam = TEAM_2, hGoals = 2, aGoals = 1, dateTime = LocalDateTime.now().minusDays(2))
        val m3 = Match(id = 3, hTeam = TEAM_1, aTeam = TEAM_2, hGoals = 0, aGoals = 4, dateTime = LocalDateTime.now().minusDays(3))
        val m4 = Match(id = 4, hTeam = TEAM_1, aTeam = TEAM_2, hGoals = 3, aGoals = 3, dateTime = LocalDateTime.now().minusDays(4))
        val m5 = Match(id = 5, hTeam = TEAM_1, aTeam = TEAM_2, hGoals = 1, aGoals = 2, dateTime = LocalDateTime.now().minusDays(5))
        val m6 = Match(id = 6, hTeam = TEAM_1, aTeam = TEAM_2, hGoals = 3, aGoals = 1, dateTime = LocalDateTime.now().minusDays(6))
        val m7 = Match(id = 7, hTeam = TEAM_1, aTeam = TEAM_3, hGoals = 2, aGoals = 0, dateTime = LocalDateTime.now().minusDays(7))
        val m8 = Match(id = 8, hTeam = TEAM_1, aTeam = TEAM_3, hGoals = 1, aGoals = 1, dateTime = LocalDateTime.now().minusDays(8))
        val m9 = Match(id = 9, hTeam = TEAM_1, aTeam = TEAM_3, hGoals = 0, aGoals = 1, dateTime = LocalDateTime.now().minusDays(9))
        val m10 = Match(id = 10, hTeam = TEAM_1, aTeam = TEAM_3, hGoals = 0, aGoals = 1, dateTime = LocalDateTime.now().minusDays(10))
        val m11 = Match(id = 11, hTeam = TEAM_1, aTeam = TEAM_3, hGoals = 2, aGoals = 2, dateTime = LocalDateTime.now().minusDays(11))
        val m12 = Match(id = 12, hTeam = TEAM_1, aTeam = TEAM_3, hGoals = 1, aGoals = 3, dateTime = LocalDateTime.now().minusDays(12))

        val input = mapOf(
                Pair(TEAM_1, listOf(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12)),
                Pair(TEAM_2, listOf(m1, m2, m3, m4, m5, m6)),
                Pair(TEAM_3, listOf(m7, m8, m9, m10, m11, m12))
        )

        val expectedOutput = mapOf(
                Pair(TEAM_1, TeamForm(form = listOf(
                        TeamMatchResult(result = DRAW, score = "1-1", opponent = TEAM_3, location = HOME),
                        TeamMatchResult(result = LOSS, score = "0-1", opponent = TEAM_3, location = HOME),
                        TeamMatchResult(result = LOSS, score = "0-1", opponent = TEAM_3, location = HOME),
                        TeamMatchResult(result = DRAW, score = "2-2", opponent = TEAM_3, location = HOME),
                        TeamMatchResult(result = LOSS, score = "1-3", opponent = TEAM_3, location = HOME)
                        ))
                ),
                Pair(TEAM_2, TeamForm(form = listOf(
                        TeamMatchResult(result = LOSS, score = "2-1", opponent = TEAM_1, location = AWAY),
                        TeamMatchResult(result = WIN, score = "0-4", opponent = TEAM_1, location = AWAY),
                        TeamMatchResult(result = DRAW, score = "3-3", opponent = TEAM_1, location = AWAY),
                        TeamMatchResult(result = WIN, score = "1-2", opponent = TEAM_1, location = AWAY),
                        TeamMatchResult(result = LOSS, score = "3-1", opponent = TEAM_1, location = AWAY)
                        ))
                ),
                Pair(TEAM_3, TeamForm(form = listOf(
                        TeamMatchResult(result = DRAW, score = "1-1", opponent = TEAM_1, location = AWAY),
                        TeamMatchResult(result = WIN, score = "0-1", opponent = TEAM_1, location = AWAY),
                        TeamMatchResult(result = WIN, score = "0-1", opponent = TEAM_1, location = AWAY),
                        TeamMatchResult(result = DRAW, score = "2-2", opponent = TEAM_1, location = AWAY),
                        TeamMatchResult(result = WIN, score = "1-3", opponent = TEAM_1, location = AWAY)
                        ))
                )
        )

        val result = formFormatter.formatLastFiveGames(input)

        assertThat(result, `is`(expectedOutput))
    }

}