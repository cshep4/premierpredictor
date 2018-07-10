package com.cshep4.premierpredictor.component.fixtures

import com.cshep4.premierpredictor.component.api.ApiRequester
import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class FixturesApiTest {
    @Mock
    private lateinit var fixtureApiRequester: ApiRequester

    @Mock
    private lateinit var fixtureFormatter: FixtureFormatter

    @InjectMocks
    private lateinit var fixturesApi: FixturesApi

    @Test
    fun `'retrieveMatches' will call api and format result`() {
        val apiResult = listOf(MatchFacts())
        val formattedMatches = listOf(Match())

        whenever(fixtureApiRequester.retrieveFixtures()).thenReturn(apiResult)
        whenever(fixtureFormatter.format(apiResult)).thenReturn(formattedMatches)

        val result = fixturesApi.retrieveMatches()

        assertThat(result, `is`(formattedMatches))
        verify(fixtureApiRequester).retrieveFixtures()
        verify(fixtureFormatter).format(apiResult)
    }

    @Test
    fun `'retrieveMatches' returns empty list if there is no result from api`() {
        whenever(fixtureApiRequester.retrieveFixtures()).thenReturn(null)

        val result = fixturesApi.retrieveMatches()

        assertThat(result, `is`(emptyList()))
        verify(fixtureApiRequester).retrieveFixtures()
        verify(fixtureFormatter, times(0)).format(any())
    }

}