package com.cshep4.premierpredictor.service

import com.cshep4.premierpredictor.component.prediction.CreatePredictionSummary
import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.Prediction
import com.cshep4.premierpredictor.data.PredictionSummary
import com.cshep4.premierpredictor.entity.PredictionEntity
import com.cshep4.premierpredictor.repository.sql.PredictionsRepository
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDateTime

@RunWith(MockitoJUnitRunner::class)
internal class PredictionsServiceTest {
    @Mock
    private lateinit var predictionsRepository: PredictionsRepository

    @Mock
    private lateinit var fixturesService: FixturesService

    @Mock
    private lateinit var createPredictionSummary: CreatePredictionSummary

    @InjectMocks
    private lateinit var predictionsService: PredictionsService

    @Test
    fun `'update' returns list of predictions when successfully updated to db`() {
        val dateTime = LocalDateTime.now().plusDays(1)
        val dateTime2 = LocalDateTime.now().plusDays(1)
        val predictions = listOf(Prediction(aGoals = 0, hGoals = 0, matchId = 1), Prediction(aGoals = 0, hGoals = 0, matchId = 2))
        val predictionEntities = predictions.map { PredictionEntity.fromDto(it) }
        val matches = listOf(Match(id = 1, dateTime = dateTime), Match(id = 2, dateTime = dateTime2))

        whenever(fixturesService.retrieveAllMatches()).thenReturn(matches)
        whenever(predictionsRepository.saveAll(predictionEntities)).thenReturn(predictionEntities)

        val result = predictionsService.savePredictions(predictions)

        assertThat(result, `is`(predictions))
    }

    @Test
    fun `'update' only saves predictions for matches that haven't played`() {
        val dateTime = LocalDateTime.now().minusDays(1)
        val dateTime2 = LocalDateTime.now().plusDays(1)
        val predictions = listOf(Prediction(aGoals = 0, hGoals = 0, matchId = 1), Prediction(aGoals = 0, hGoals = 0, matchId = 2))
        val predictionEntities = listOf(PredictionEntity.fromDto(predictions[1]))
        val matches = listOf(Match(id = 1, dateTime = dateTime), Match(id = 2, dateTime = dateTime2))

        whenever(fixturesService.retrieveAllMatches()).thenReturn(matches)
        whenever(predictionsRepository.saveAll(predictionEntities)).thenReturn(predictionEntities)

        val result = predictionsService.savePredictions(predictions)

        assertThat(result.size, `is`(1))
        assertThat(result[0], `is`(predictions[1]))
    }

    @Test
    fun `'update' returns empty list when not successfully stored to db`() {
        val dateTime = LocalDateTime.now().plusDays(1)
        val dateTime2 = LocalDateTime.now().plusDays(1)
        val predictions = listOf(Prediction(matchId = 1))
        val predictionEntities = predictions.map { PredictionEntity.fromDto(it) }
        val matches = listOf(Match(id = 1, dateTime = dateTime), Match(id = 2, dateTime = dateTime2))

        whenever(fixturesService.retrieveAllMatches()).thenReturn(matches)
        whenever(predictionsRepository.saveAll(predictionEntities)).thenReturn(emptyList())

        val result = predictionsService.savePredictions(predictions)

        assertThat(result, `is`(emptyList()))
    }

    @Test
    fun `'retrievePredictionsByUserId' should retrieve all predictions for that user`() {
        val predictionEntity = PredictionEntity()
        val predictions = listOf(predictionEntity)
        whenever(predictionsRepository.findByUserId(1)).thenReturn(predictions)

        val result = predictionsService.retrievePredictionsByUserId(1)

        assertThat(result.isEmpty(), `is`(false))
        assertThat(result[0], `is`(predictionEntity.toDto()))
    }

    @Test
    fun `'retrievePredictionsByUserId' should return empty list if no predictions exist for that user id`() {
        whenever(predictionsRepository.findByUserId(1)).thenReturn(emptyList())

        val result = predictionsService.retrievePredictionsByUserId(1)

        assertThat(result.isEmpty(), `is`(true))
    }

    @Test
    fun `'retrievePredictionsSummaryByUserId' should retrieve all predictions for that user and return it in the correct format`() {
        val predictionEntity = PredictionEntity()
        val predictionEntities = listOf(predictionEntity)
        val predictions = listOf(predictionEntity.toDto())
        val dateTime = LocalDateTime.now().minusDays(1)
        val matches = listOf(Match(id = 1, dateTime = dateTime), Match(id = 2, dateTime = dateTime), Match(id = 2, matchday = 4, dateTime = dateTime))
        val predictionSummary = PredictionSummary()

        whenever(fixturesService.retrieveAllMatches()).thenReturn(matches)
        whenever(predictionsRepository.findByUserId(1)).thenReturn(predictionEntities)
        whenever(createPredictionSummary.format(matches, predictions)).thenReturn(predictionSummary)

        val result = predictionsService.retrievePredictionsSummaryByUserId(1)

        assertThat(result, `is`(predictionSummary))
    }
}