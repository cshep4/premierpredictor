package com.cshep4.premierpredictor.service

import com.cshep4.premierpredictor.component.api.ApiRequester
import com.cshep4.premierpredictor.component.fixtures.FixtureFormatter
import com.cshep4.premierpredictor.component.fixtures.FixturesByDate
import com.cshep4.premierpredictor.component.fixtures.OverrideMatchScore
import com.cshep4.premierpredictor.component.fixtures.PredictionMerger
import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.OverrideMatch
import com.cshep4.premierpredictor.data.Prediction
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.entity.MatchEntity
import com.cshep4.premierpredictor.repository.sql.FixturesRepository
import com.cshep4.premierpredictor.service.fixtures.UpdateFixturesService
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate
import java.time.LocalDateTime
import org.hamcrest.CoreMatchers.`is` as Is

@RunWith(MockitoJUnitRunner::class)
internal class FixturesServiceTest {
    @Mock
    private lateinit var fixtureApiRequester: ApiRequester

    @Mock
    private lateinit var fixtureFormatter: FixtureFormatter

    @Mock
    private lateinit var updateFixturesService: UpdateFixturesService

    @Mock
    private lateinit var fixturesRepository: FixturesRepository

    @Mock
    private lateinit var predictionsService: PredictionsService

    @Mock
    private lateinit var predictionMerger: PredictionMerger

    @Mock
    private lateinit var overrideMatchService: OverrideMatchService

    @Mock
    private lateinit var overrideMatchScore: OverrideMatchScore

    @Mock
    private lateinit var fixturesByDate: FixturesByDate

    @InjectMocks
    private lateinit var fixturesService: FixturesService

    @Test
    fun `'update' returns list of matches when successfully updated to db`() {
        val fixturesApiResult = listOf(MatchFacts())
        val matches = listOf(Match())
        val overrides = listOf(OverrideMatch())

        whenever(fixtureApiRequester.retrieveFixtures()).thenReturn(fixturesApiResult)
        whenever(fixtureFormatter.format(fixturesApiResult)).thenReturn(matches)
        whenever(updateFixturesService.update(matches)).thenReturn(matches)
        whenever(overrideMatchService.retrieveAllOverriddenMatches()).thenReturn(overrides)
        whenever(overrideMatchScore.update(matches, overrides)).thenReturn(matches)

        val result = fixturesService.update()

        verify(overrideMatchScore).update(matches, overrides)

        assertThat(result, Is(matches))
    }

    @Test
    fun `'update' returns empty list when no result from API`() {
        whenever(fixtureApiRequester.retrieveFixtures()).thenReturn(null)

        val result = fixturesService.update()

        assertThat(result, Is(emptyList()))
    }

    @Test
    fun `'update' returns empty list when fixtures are not formatted`() {
        val fixturesApiResult = listOf(MatchFacts())

        whenever(fixtureApiRequester.retrieveFixtures()).thenReturn(fixturesApiResult)
        whenever(fixtureFormatter.format(fixturesApiResult)).thenReturn(emptyList())

        val result = fixturesService.update()

        assertThat(result, Is(emptyList()))
    }

    @Test
    fun `'update' returns empty list when not successfully stored to db`() {
        val fixturesApiResult = listOf(MatchFacts())
        val matches = listOf(Match())

        whenever(fixtureApiRequester.retrieveFixtures()).thenReturn(fixturesApiResult)
        whenever(fixtureFormatter.format(fixturesApiResult)).thenReturn(matches)

        val result = fixturesService.update()

        assertThat(result, Is(emptyList()))
    }

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
    fun `'retrieveAllUpcomingFixtures' will return list of matches by date if there are some upcoming`() {
        val matches = listOf(
                MatchEntity(dateTime = LocalDateTime.now().plusDays(1)),
                MatchEntity(dateTime = LocalDateTime.now().minusDays(1))
        )

        val upcomingMatches = matches.filter { it.dateTime!!.isAfter(LocalDateTime.now()) }.map { it.toDto() }

        val expectedResult = mapOf(Pair(LocalDate.now(), listOf(Match())))

        whenever(fixturesRepository.findAll()).thenReturn(matches)
        whenever(fixturesByDate.format(upcomingMatches)).thenReturn(expectedResult)

        val result = fixturesService.retrieveAllUpcomingFixtures()

        assertThat(result, Is(expectedResult))

    }

    @Test
    fun `'retrieveAllUpcomingFixtures' will return empty list of matches by date if there are none upcoming`() {
        val matches = listOf(MatchEntity(dateTime = LocalDateTime.now().minusDays(1)))

        whenever(fixturesRepository.findAll()).thenReturn(matches)

        val result = fixturesService.retrieveAllUpcomingFixtures()

        verify(fixturesByDate, times(0)).format(any())
        assertThat(result, Is(emptyMap()))
    }
}