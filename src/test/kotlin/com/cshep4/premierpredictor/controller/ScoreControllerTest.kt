package com.cshep4.premierpredictor.controller

import com.cshep4.premierpredictor.data.UserRank
import com.cshep4.premierpredictor.service.ScoreService
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
internal class ScoreControllerTest {
    @Mock
    lateinit var scoreService: ScoreService

    @InjectMocks
    lateinit var scoreController: ScoreController

    @Test
    fun `'getScoreAndRank' should return OK with users score and rank in the body`() {
        val userRank = UserRank()

        whenever(scoreService.retrieveScoreAndRank(1)).thenReturn(userRank)

        val result = scoreController.getScoreAndRank(1)

        assertThat(result.statusCode, `is`(OK))
        assertThat(result.body, `is`(userRank))
    }

}