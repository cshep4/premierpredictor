package com.cshep4.premierpredictor.service.fixtures

import com.cshep4.premierpredictor.component.fixtures.FixturesByDate
import com.cshep4.premierpredictor.component.prediction.PredictionMerger
import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.Prediction
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.entity.MatchEntity
import com.cshep4.premierpredictor.entity.MatchFactsEntity
import com.cshep4.premierpredictor.repository.dynamodb.MatchFactsRepository
import com.cshep4.premierpredictor.repository.sql.FixturesRepository
import com.cshep4.premierpredictor.service.prediction.PredictionsService
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import org.hamcrest.CoreMatchers.`is` as Is


@RunWith(MockitoJUnitRunner::class)
internal class FixturesServiceTest {
    @Mock
    private lateinit var fixturesRepository: FixturesRepository

    @Mock
    private lateinit var predictionsService: PredictionsService

    @Mock
    private lateinit var predictionMerger: PredictionMerger

    @Mock
    private lateinit var fixturesByDate: FixturesByDate

    @Mock
    private lateinit var matchFactsRepository: MatchFactsRepository

    @InjectMocks
    private lateinit var fixturesService: FixturesService

    @Test
    fun `'retrieveAllMatches' should retrieve all matches`() {
        val matchEntity = MatchEntity()
        val matches = listOf(matchEntity)
        whenever(fixturesRepository.findAll()).thenReturn(matches)

        val result = fixturesService.retrieveAllMatches()

        assertThat(result.isEmpty(), Is(false))
        assertThat(result[0], Is(matchEntity.toDto()))
    }

    @Test
    fun `'retrieveAllMatches' should return empty list if no matches exist`() {
        whenever(fixturesRepository.findAll()).thenReturn(emptyList())

        val result = fixturesService.retrieveAllMatches()

        assertThat(result.isEmpty(), Is(true))
    }

    @Test
    fun `'retrieveAllPredictedMatchesByUserId' should retrieve all predicted matches by id`() {
        val matchEntity = MatchEntity()
        val matches = listOf(matchEntity)
        whenever(fixturesRepository.findPredictedMatchesByUserId(1)).thenReturn(matches)

        val result = fixturesService.retrieveAllPredictedMatchesByUserId(1)

        assertThat(result.isEmpty(), Is(false))
        assertThat(result[0], Is(matchEntity.toDto()))
    }

    @Test
    fun `'retrieveAllPredictedMatchesByUserId' should return empty list if no matches exist`() {
        whenever(fixturesRepository.findPredictedMatchesByUserId(1)).thenReturn(emptyList())

        val result = fixturesService.retrieveAllPredictedMatchesByUserId(1)

        assertThat(result.isEmpty(), Is(true))
    }

    @Test
    fun `'retrieveAllMatchesWithPredictions' should retrieve all matches with predicted scorelines by user id`() {
        val matchEntities = listOf(MatchEntity(id = 1),
                MatchEntity(id = 2))

        val matches = matchEntities.map { it.toDto() }
        val predictedMatches = matches.map { it.toPredictedMatch() }

        val predictions = listOf(Prediction(matchId = 1, hGoals = 2, aGoals = 3),
                Prediction(matchId = 2, hGoals = 1, aGoals = 0))

        whenever(fixturesRepository.findAll()).thenReturn(matchEntities)
        whenever(predictionsService.retrievePredictionsByUserId(1)).thenReturn(predictions)
        whenever(predictionMerger.merge(matches, predictions)).thenReturn(predictedMatches)

        val result = fixturesService.retrieveAllMatchesWithPredictions(1)

        assertThat(result.isEmpty(), Is(false))
        assertThat(result[0].id, Is(1L))
        assertThat(result[1].id, Is(2L))

    }

    @Test
    fun `'retrieveAllUpcomingFixtures' will return map of matches by date if there are some upcoming`() {
        val matches = listOf(
                MatchFactsEntity(lastUpdated = LocalDateTime.now()),
                MatchFactsEntity(lastUpdated = LocalDateTime.now())
        )
        matches[0].setDateTime(LocalDateTime.now().plusDays(1))
        matches[1].setDateTime(LocalDateTime.now().minusDays(1))

        val upcomingMatches = matches.filter { it.getDateTime()!!.isAfter(LocalDateTime.now()) }.map { it.toDto() }

        val expectedResult = mapOf(Pair(LocalDate.now(), listOf(MatchFacts())))

        whenever(matchFactsRepository.findAll()).thenReturn(matches)
        whenever(fixturesByDate.format(upcomingMatches)).thenReturn(expectedResult)

        val result = fixturesService.retrieveAllUpcomingFixtures()

        assertThat(result, Is(expectedResult))

    }

    @Test
    fun `'retrieveAllUpcomingFixtures' will return empty map if there are none upcoming`() {
        val matches = listOf(
                MatchFactsEntity()
        )
        matches[0].setDateTime(LocalDateTime.now().minusDays(1))

        whenever(matchFactsRepository.findAll()).thenReturn(matches)

        val result = fixturesService.retrieveAllUpcomingFixtures()

        verify(fixturesByDate, times(0)).format(any())
        assertThat(result, Is(emptyMap()))
    }

    @Test
    fun `'retrieveLiveScoreForMatch' will retrieve the match from the db`() {
        val currentlyStoredMatch = MatchFactsEntity()

        whenever(matchFactsRepository.findById("1")).thenReturn(Optional.of(currentlyStoredMatch))

        val result = fixturesService.retrieveLiveScoreForMatch("1")

        assertThat(result, Is(currentlyStoredMatch.toDto()))
    }

    @Test
    fun `'retrieveLiveScoreForMatch' will return null if match is not found in db`() {
        whenever(matchFactsRepository.findById("1")).thenReturn(Optional.empty())

        val result = fixturesService.retrieveLiveScoreForMatch("1")

        assertThat(result, Is(nullValue()))
    }

    @Test
    fun `'saveMatches' saves matches to db`() {
        val matches = listOf(Match())
        val matchEntities = matches.map { MatchEntity.fromDto(it) }

        whenever(fixturesRepository.saveAll(matchEntities)).thenReturn(matchEntities)

        val result = fixturesService.saveMatches(matches)

        assertThat(result, Is(matches))
        verify(fixturesRepository).saveAll(matchEntities)
    }

    @Test
    fun `'retrieveAllMatches' should retrieve only past matches that have been played`() {
        val pastMatch = MatchEntity(hGoals = 1, aGoals = 1, dateTime = LocalDateTime.now().minusSeconds(1))
        val pastMatchNotPlayed = MatchEntity(dateTime = LocalDateTime.now().minusDays(1))

        val matchesFromDb = listOf(pastMatch, pastMatchNotPlayed)
        whenever(fixturesRepository.findAll()).thenReturn(matchesFromDb)

        val result = fixturesService.retrieveAllPastMatches()

        val expectedResult = listOf(pastMatch).map { it.toDto() }

        assertThat(result.isEmpty(), Is(false))
        assertThat(result, Is(expectedResult))
    }
}