package com.cshep4.premierpredictor.controller

import com.cshep4.premierpredictor.data.MatchWithOverride
import com.cshep4.premierpredictor.data.OverrideMatch
import com.cshep4.premierpredictor.service.OverrideMatchService
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus.OK

@RunWith(MockitoJUnitRunner::class)
internal class OverrideMatchControllerTest {
    @Mock
    lateinit var overrideMatchService: OverrideMatchService

    @InjectMocks
    lateinit var overrideMatchController: OverrideMatchController

    @Test
    fun `'updateOverrides' sends list of objects to be updated and returns the objects added to the db`() {
        val overrides = listOf(OverrideMatch())

        whenever(overrideMatchService.updateOverrides(overrides)).thenReturn(overrides)

        val result = overrideMatchController.updateOverrides(overrides)

        assertThat(result.statusCode, `is`(OK))
        assertThat(result.body, `is`(overrides))
    }

    @Test
    fun `'getAllOverriddenMatches' gets all matches with score from the API and there overriden values`() {
        val matches = listOf(MatchWithOverride())

        whenever(overrideMatchService.retrieveAllMatchesWithOverrideScores()).thenReturn(matches)

        val result = overrideMatchController.getAllOverriddenMatches()

        assertThat(result.statusCode, `is`(OK))
        assertThat(result.body, `is`(matches))
    }

}