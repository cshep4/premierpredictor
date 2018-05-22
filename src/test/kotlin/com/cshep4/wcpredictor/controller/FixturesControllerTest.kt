package com.cshep4.wcpredictor.controller

import com.cshep4.wcpredictor.data.Match
import com.cshep4.wcpredictor.data.PredictedMatch
import com.cshep4.wcpredictor.service.FixturesService
import com.cshep4.wcpredictor.service.UserScoreService
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus.*
import org.hamcrest.CoreMatchers.`is` as Is

@RunWith(MockitoJUnitRunner::class)
internal class FixturesControllerTest {
    @Mock
    private lateinit var fixturesService: FixturesService

    @Mock
    lateinit var userScoreService: UserScoreService

    @InjectMocks
    private lateinit var fixturesController: FixturesController

    @Test
    fun `'updateFixtures' returns UPDATED with the matches in the request body when the fixtures are successfully updated`() {
        val matches = listOf(Match())
        whenever(fixturesService.update()).thenReturn(matches)

        val result = fixturesController.updateFixtures(true)

        assertThat(result.statusCode, Is(OK))
        assertThat(result.body, Is(matches))
        verify(userScoreService).updateScores()
    }

    @Test
    fun `'updateFixtures' returns BAD_REQUEST fixtures are not updated`() {
        whenever(fixturesService.update()).thenReturn(emptyList())

        val result = fixturesController.updateFixtures(true)

        assertThat(result.statusCode, Is(BAD_REQUEST))
        assertThat(result.body, Is(CoreMatchers.nullValue()))
        verify(userScoreService, times(0)).updateScores()
    }

    @Test
    fun `'getAllMatches' should return a list of matches and status OK if some are found`() {
        val matches = listOf(Match())

        whenever(fixturesService.retrieveAllMatches()).thenReturn(matches)

        val result = fixturesController.getAllMatches()

        assertThat(result.statusCode, Is(OK))
        assertThat(result.body, Is(matches))
    }

    @Test
    fun `'getAllMatches' should return a NOT FOUND if no matches are found`() {
        whenever(fixturesService.retrieveAllMatches()).thenReturn(emptyList())

        val result = fixturesController.getAllMatches()

        assertThat(result.statusCode, Is(NOT_FOUND))
        assertThat(result.body, Is(nullValue()))
    }

    @Test
    fun `'getAllPredictedMatches' should return a list of matches with users predictions and status OK if some are found`() {
        val matches = listOf(PredictedMatch())

        whenever(fixturesService.retrieveAllMatchesWithPredictions(1)).thenReturn(matches)

        val result = fixturesController.getAllPredictedMatches(1)

        assertThat(result.statusCode, Is(OK))
        assertThat(result.body, Is(matches))
    }

    @Test
    fun `'getAllPredictedMatches' should return a NOT FOUND if no matches are found`() {
        whenever(fixturesService.retrieveAllMatchesWithPredictions(1)).thenReturn(emptyList())

        val result = fixturesController.getAllPredictedMatches(1)

        assertThat(result.statusCode, Is(NOT_FOUND))
        assertThat(result.body, Is(nullValue()))
    }


}