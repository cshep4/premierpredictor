package com.cshep4.premierpredictor.controller

import com.cshep4.premierpredictor.data.*
import com.cshep4.premierpredictor.enum.DuplicateSearch
import com.cshep4.premierpredictor.service.prediction.PredictionCleanerService
import com.cshep4.premierpredictor.service.prediction.PredictionsService
import com.nhaarman.mockito_kotlin.verify
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

    @Mock
    private lateinit var predictionCleanerService: PredictionCleanerService

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

    @Test
    fun `'removeDuplicatesIfAnyExist' returns Duplicate summary`() {
        val duplicates = DuplicateSummary()

        whenever(predictionCleanerService.removeDuplicatesIfAnyExist(DuplicateSearch.THOROUGH)).thenReturn(duplicates)

        val result = predictionsController.removeDuplicatesIfAnyExist(DuplicateSearch.THOROUGH)

        verify(predictionCleanerService).removeDuplicatesIfAnyExist(DuplicateSearch.THOROUGH)
        assertThat(result.statusCode, `is`(OK))
        assertThat(result.body, `is`(duplicates))
    }

    @Test
    fun `'removeDuplicatesIfAnyExist' will do a quick duplicate search if mode is not specified`() {
        predictionsController.removeDuplicatesIfAnyExist()

        verify(predictionCleanerService).removeDuplicatesIfAnyExist(DuplicateSearch.QUICK)
    }

    @Test
    fun `'getAllPredictedMatchesWithForm' should return a list of matches with users predictions and status OK if some are found`() {
        val matches = listOf(PredictedMatch())
        val data = PredictorData(predictions = matches)

        whenever(predictionsService.retrievePredictorData(1)).thenReturn(data)

        val result = predictionsController.getAllPredictedMatchesWithForm(1)

        assertThat(result.statusCode, `is`(OK))
        assertThat(result.body, `is`(data))
    }

    @Test
    fun `'getAllPredictedMatchesWithForm' should return a NOT FOUND if no matches are found`() {
        whenever(predictionsService.retrievePredictorData(1)).thenReturn(PredictorData(predictions = emptyList()))

        val result = predictionsController.getAllPredictedMatchesWithForm(1)

        assertThat(result.statusCode, `is`(NOT_FOUND))
        assertThat(result.body, `is`(nullValue()))
    }
}