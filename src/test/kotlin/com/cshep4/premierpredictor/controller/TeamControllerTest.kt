package com.cshep4.premierpredictor.controller

import com.cshep4.premierpredictor.data.TeamForm
import com.cshep4.premierpredictor.service.team.TeamService
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
internal class TeamControllerTest {
    @Mock
    private lateinit var teamService: TeamService

    @InjectMocks
    private lateinit var teamController: TeamController

    @Test
    fun `'getRecentForms' will call service and return team forms with OK`() {
        val forms = mapOf(Pair("Team 1", TeamForm()))

        whenever(teamService.retrieveRecentForms()).thenReturn(forms)

        val result = teamController.getRecentForms()

        assertThat(result.statusCode, `is`(OK))
        assertThat(result.body, `is`(forms))
    }
}