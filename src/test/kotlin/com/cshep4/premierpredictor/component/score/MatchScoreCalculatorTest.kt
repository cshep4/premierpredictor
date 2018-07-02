package com.cshep4.premierpredictor.component.score

import com.cshep4.premierpredictor.data.MatchPredictionResult
import com.cshep4.premierpredictor.data.User
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

internal class MatchScoreCalculatorTest {
    companion object {
        const val CORRECT_GOAL = 1
        const val CORRECT_RESULT = 3
        const val CORRECT_SCORELINE = 3
    }
    private lateinit var users: List<User>
    private val matchScoreCalculator = MatchScoreCalculator()

    @Before
    fun init() {
        users = listOf(
                User(id = 1, score = 0),
                User(id = 2, score = 0),
                User(id = 3, score = 0)
        )
    }

    @Test
    fun `'calculate' loops through all predictions adds 1 point if user predicts correct goals for home team`() {
        val predictedMatches = listOf(
                MatchPredictionResult(userId = 1, hGoals = 1, aGoals = 3, hPredictedGoals = 1, aPredictedGoals = 0),
                MatchPredictionResult(userId = 2, hGoals = 1, aGoals = 3, hPredictedGoals = 2, aPredictedGoals = 0),
                MatchPredictionResult(userId = 3, hGoals = 1, aGoals = 3, hPredictedGoals = 2, aPredictedGoals = 2)
        )

        val result = matchScoreCalculator.calculate(users, predictedMatches)

        assertThat(result[0].score, `is`(CORRECT_GOAL))
        assertThat(result[1].score, `is`(0))
        assertThat(result[2].score, `is`(0))

    }

    @Test
    fun `'calculate' loops through all predictions adds 1 point if user predicts correct goals for away team`() {
        val predictedMatches = listOf(
                MatchPredictionResult(userId = 1, hGoals = 3, aGoals = 1, hPredictedGoals = 0, aPredictedGoals = 1),
                MatchPredictionResult(userId = 2, hGoals = 3, aGoals = 1, hPredictedGoals = 0, aPredictedGoals = 2),
                MatchPredictionResult(userId = 3, hGoals = 3, aGoals = 1, hPredictedGoals = 2, aPredictedGoals = 2)
        )

        val result = matchScoreCalculator.calculate(users, predictedMatches)

        assertThat(result[0].score, `is`(CORRECT_GOAL))
        assertThat(result[1].score, `is`(0))
        assertThat(result[2].score, `is`(0))
    }

    @Test
    fun `'calculate' loops through all predictions adds 3 points if user predicts the correct result, without goals`() {
        val predictedMatches = listOf(
                MatchPredictionResult(userId = 1, hGoals = 5, aGoals = 0, hPredictedGoals = 2, aPredictedGoals = 1),
                MatchPredictionResult(userId = 2, hGoals = 3, aGoals = 1, hPredictedGoals = 0, aPredictedGoals = 2),
                MatchPredictionResult(userId = 3, hGoals = 3, aGoals = 1, hPredictedGoals = 2, aPredictedGoals = 2)
        )

        val result = matchScoreCalculator.calculate(users, predictedMatches)

        assertThat(result[0].score, `is`(CORRECT_RESULT))
        assertThat(result[1].score, `is`(0))
        assertThat(result[2].score, `is`(0))
    }

    @Test
    fun `'calculate' loops through all predictions adds 3 points if user predicts the correct result, and correct goal bonus (see above)`() {
        val predictedMatches = listOf(
                MatchPredictionResult(userId = 1, hGoals = 5, aGoals = 0, hPredictedGoals = 5, aPredictedGoals = 1),
                MatchPredictionResult(userId = 1, hGoals = 4, aGoals = 1, hPredictedGoals = 5, aPredictedGoals = 1),
                MatchPredictionResult(userId = 2, hGoals = 3, aGoals = 1, hPredictedGoals = 0, aPredictedGoals = 2),
                MatchPredictionResult(userId = 3, hGoals = 3, aGoals = 1, hPredictedGoals = 2, aPredictedGoals = 2)
        )

        val result = matchScoreCalculator.calculate(users, predictedMatches)

        val expectedScore = CORRECT_GOAL + CORRECT_RESULT + CORRECT_GOAL + CORRECT_RESULT
        assertThat(result[0].score, `is`(expectedScore))
        assertThat(result[1].score, `is`(0))
        assertThat(result[2].score, `is`(0))
    }

    @Test
    fun `'calculate' loops through all predictions adds 3 points if user predicts the correct scoreline, plus 3 points for correct result and 2 points for correct goals to each team`() {
        val predictedMatches = listOf(
                MatchPredictionResult(userId = 1, hGoals = 1, aGoals = 0, hPredictedGoals = 1, aPredictedGoals = 0),
                MatchPredictionResult(userId = 2, hGoals = 1, aGoals = 1, hPredictedGoals = 1, aPredictedGoals = 1),
                MatchPredictionResult(userId = 3, hGoals = 3, aGoals = 1, hPredictedGoals = 2, aPredictedGoals = 2)
        )

        val result = matchScoreCalculator.calculate(users, predictedMatches)

        val expectedScore = CORRECT_GOAL + CORRECT_GOAL + CORRECT_RESULT + CORRECT_SCORELINE
        assertThat(result[0].score, `is`(expectedScore))
        assertThat(result[1].score, `is`(expectedScore))
        assertThat(result[2].score, `is`(0))
    }
}