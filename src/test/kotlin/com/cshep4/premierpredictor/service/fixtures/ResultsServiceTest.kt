package com.cshep4.premierpredictor.service.fixtures

import com.cshep4.premierpredictor.component.fixtures.FixturesApi
import com.cshep4.premierpredictor.component.fixtures.OverrideMatchScore
import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.OverrideMatch
import com.cshep4.premierpredictor.service.OverrideMatchService
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
internal class ResultsServiceTest {
    @Mock
    private lateinit var fixturesApi: FixturesApi

    @Mock
    private lateinit var updateFixturesService: UpdateFixturesService

    @Mock
    private lateinit var overrideMatchService: OverrideMatchService

    @Mock
    private lateinit var overrideMatchScore: OverrideMatchScore

    @InjectMocks
    private lateinit var resultsService: ResultsService

    @Test
    fun `'update' returns list of matches when successfully updated to db`() {
        val matches = listOf(Match())
        val overrides = listOf(OverrideMatch())

        whenever(fixturesApi.retrieveMatches()).thenReturn(matches)
        whenever(updateFixturesService.update(matches)).thenReturn(matches)
        whenever(overrideMatchService.retrieveAllOverriddenMatches()).thenReturn(overrides)
        whenever(overrideMatchScore.update(matches, overrides)).thenReturn(matches)

        val result = resultsService.update()

        verify(overrideMatchScore).update(matches, overrides)

        assertThat(result, `is`(matches))
    }

    @Test
    fun `'update' returns empty list when fixtures are not formatted or no result from api`() {
        whenever(fixturesApi.retrieveMatches()).thenReturn(emptyList())

        val result = resultsService.update()

        assertThat(result, `is`(emptyList()))
    }

    @Test
    fun `'update' returns empty list when not successfully stored to db`() {
        val matches = listOf(Match())

        whenever(fixturesApi.retrieveMatches()).thenReturn(matches)

        val result = resultsService.update()

        assertThat(result, `is`(emptyList()))
    }
}