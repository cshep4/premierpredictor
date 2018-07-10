package com.cshep4.premierpredictor.service.user

import com.cshep4.premierpredictor.repository.sql.UserRepository
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.math.BigInteger.valueOf

@RunWith(MockitoJUnitRunner::class)
internal class ScoreServiceTest {
    @Mock
    lateinit var userRepository: UserRepository

    @InjectMocks
    lateinit var scoreService: ScoreService

    @Test
    fun `'retrieveScoreAndRank' should get user scores and ranks from db and return current users`() {
        val userRanks = listOf(arrayOf(valueOf(1), valueOf(1L), 4), arrayOf(valueOf(2L), valueOf(2L), 3), arrayOf(valueOf(3L), valueOf(3L), 1))

        whenever(userRepository.getUserRankAndScore()).thenReturn(userRanks)

        val result = scoreService.retrieveScoreAndRank(1)

        assertThat(result.id, `is`(1L))
        assertThat(result.rank, `is`(1L))
        assertThat(result.score, `is`(4))
    }

}