package com.cshep4.premierpredictor.component.prediction

import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.Prediction
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

internal class PredictionMergerTest {
    private val predictionMerger = PredictionMerger()

    @Test
    fun `'merge' should loop through predictions and add scores to the matches`() {
        val matches = listOf(Match(id = 1),
                Match(id = 2))

        val predictions = listOf(Prediction(matchId = 1, hGoals = 2, aGoals = 3),
                Prediction(matchId = 2, hGoals = 1, aGoals = 0))

        val result = predictionMerger.merge(matches, predictions)

        MatcherAssert.assertThat(result[0].id, CoreMatchers.`is`(1L))
        MatcherAssert.assertThat(result[0].hGoals, CoreMatchers.`is`(2))
        MatcherAssert.assertThat(result[0].aGoals, CoreMatchers.`is`(3))
        MatcherAssert.assertThat(result[1].id, CoreMatchers.`is`(2L))
        MatcherAssert.assertThat(result[1].hGoals, CoreMatchers.`is`(1))
        MatcherAssert.assertThat(result[1].aGoals, CoreMatchers.`is`(0))
    }

}