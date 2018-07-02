package com.cshep4.premierpredictor.controller

import com.cshep4.premierpredictor.data.Prediction
import com.cshep4.premierpredictor.data.PredictionSummary
import com.cshep4.premierpredictor.service.PredictionsService
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus.*

@RunWith(MockitoJUnitRunner::class)
internal class PredictionsControllerTest {
    @Mock
    private lateinit var predictionsService: PredictionsService

    @InjectMocks
    private lateinit var predictionsController: PredictionsController

    @Test
    fun `'updatePredictions' returns UPDATED with the predictions in the request body when the predictions are successfully updated`() {
        val matches = listOf(Prediction())
        whenever(predictionsService.savePredictions(matches)).thenReturn(matches)

        val result = predictionsController.updatePredictions(matches)

        assertThat(result.statusCode, `is`(OK))
        assertThat(result.body, `is`(matches))
    }

    @Test
    fun `'updatePredictions' returns BAD_REQUEST predictions are not updated`() {
        val matches = listOf(Prediction())
        whenever(predictionsService.savePredictions(matches)).thenReturn(emptyList())

        val result = predictionsController.updatePredictions(matches)

        assertThat(result.statusCode, `is`(BAD_REQUEST))
        assertThat(result.body, `is`(nullValue()))
    }

    @Test
    fun `'getPredictionsByUserId' returns list of users predictions in the request body when predictions are found`() {
        val predictions = listOf(Prediction())

        whenever(predictionsService.retrievePredictionsByUserId(1)).thenReturn(predictions)

        val result = predictionsController.getPredictionsByUserId(1)

        assertThat(result.statusCode, `is`(OK))
        assertThat(result.body, `is`(predictions))
    }

    @Test
    fun `'getPredictionsByUserId' returns NOT_FOUND when no predictions are found`() {
        whenever(predictionsService.retrievePredictionsByUserId(1)).thenReturn(emptyList())

        val result = predictionsController.getPredictionsByUserId(1)

        assertThat(result.statusCode, `is`(NOT_FOUND))
        assertThat(result.body, `is`(nullValue()))
    }

    @Test
    fun `'getPredictionsSummaryByUserId' returns list of users predictions in the request body when predictions are found`() {
        val predictions = PredictionSummary()

        whenever(predictionsService.retrievePredictionsSummaryByUserId(1)).thenReturn(predictions)

        val result = predictionsController.getPredictionsSummaryByUserId(1)

        assertThat(result.statusCode, `is`(OK))
        assertThat(result.body, `is`(predictions))
    }
}