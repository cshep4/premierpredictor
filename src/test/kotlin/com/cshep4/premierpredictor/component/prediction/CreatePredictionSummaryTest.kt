package com.cshep4.premierpredictor.component.prediction

import com.cshep4.premierpredictor.data.PredictedMatch
import com.cshep4.premierpredictor.data.Prediction
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class CreatePredictionSummaryTest {
    @Mock
    private lateinit var predictionMerger: PredictionMerger

    @InjectMocks
    private lateinit var createPredictionSummary: CreatePredictionSummary

    @Test
    fun `'format' collects both sets of matches and returns in the right format`() {
        val predictions = listOf(Prediction())
        val predictedMatches = listOf(PredictedMatch())
        val matches = predictedMatches.map { it.toMatch() }

        whenever(predictionMerger.merge(matches, predictions)).thenReturn(predictedMatches)

        val result = createPredictionSummary.format(matches, predictions)

        assertThat(result.matches, `is`(predictedMatches))
    }

}