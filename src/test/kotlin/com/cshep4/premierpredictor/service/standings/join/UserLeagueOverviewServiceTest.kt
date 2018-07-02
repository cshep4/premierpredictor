package com.cshep4.premierpredictor.service.standings.join

import com.cshep4.premierpredictor.data.UserLeagueOverview
import com.cshep4.premierpredictor.repository.sql.UserLeagueOverviewRepository
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class UserLeagueOverviewServiceTest {
    private companion object {
        const val LEAGUE_PIN: Long = 1234567890
        const val USER_ID: Long = 1
        const val LEAGUE_NAME = "League"
        const val RANK: Long = 1
    }

    @Mock
    private lateinit var userLeagueOverviewRepository: UserLeagueOverviewRepository

    @InjectMocks
    private lateinit var userLeagueOverviewService: UserLeagueOverviewService

    @Test
    fun `'retrieveUserLeagueOverview' retrieves user's league overview for added league and returns it`() {
        val rawData = listOf(arrayOf(LEAGUE_NAME, LEAGUE_PIN.toBigInteger(), RANK.toBigInteger()))
        val userLeagueOverview = UserLeagueOverview(LEAGUE_NAME, LEAGUE_PIN, RANK)

        whenever(userLeagueOverviewRepository.getUserLeagueOverview(LEAGUE_PIN, USER_ID)).thenReturn(rawData)

        val result = userLeagueOverviewService.retrieveUserLeagueOverview(LEAGUE_PIN, USER_ID)

        assertThat(result, `is`(userLeagueOverview))
    }

}