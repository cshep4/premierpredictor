package com.cshep4.premierpredictor.controller

import com.cshep4.premierpredictor.data.MatchSummary
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.service.livematch.LiveMatchService
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK

@RunWith(MockitoJUnitRunner::class)
internal class LiveMatchControllerTest {
    @Mock
    private lateinit var liveMatchService: LiveMatchService

    @InjectMocks
    private lateinit var liveMatchController: LiveMatchController

    @Test
    fun `'getLiveMatchFacts' will get current match facts and return OK`() {
        val matchFacts = MatchFacts()

        whenever(liveMatchService.retrieveLiveMatchFacts("1")).thenReturn(matchFacts)

        val result = liveMatchController.getLiveMatchFacts(1)

        assertThat(result.statusCode, `is`(OK))
        assertThat(result.body, `is`(matchFacts))
    }

    @Test
    fun `'getLiveMatchFacts' will return NOT FOUND if match cannot be retrieved`() {
        whenever(liveMatchService.retrieveLiveMatchFacts("1")).thenReturn(null)

        val result = liveMatchController.getLiveMatchFacts(1)

        assertThat(result.statusCode, `is`(NOT_FOUND))
        assertThat(result.body, `is`(nullValue()))
    }

    @Test
    fun `'getMatchSummary' will return OK with a MatchSummary`() {
        val match = MatchSummary()

        whenever(liveMatchService.retrieveMatchSummary("1", "1")).thenReturn(match)

        val result = liveMatchController.getMatchSummary(1, 1)

        assertThat(result.statusCode, `is`(OK))
        assertThat(result.body, `is`(match))
    }

    @Test
    fun `'getMatchSummary' will return NOT FOUND if match cannot be retrieved`() {
        whenever(liveMatchService.retrieveMatchSummary("1", "1")).thenReturn(null)

        val result = liveMatchController.getMatchSummary(1, 1)

        assertThat(result.statusCode, `is`(NOT_FOUND))
        assertThat(result.body, `is`(nullValue()))
    }
}