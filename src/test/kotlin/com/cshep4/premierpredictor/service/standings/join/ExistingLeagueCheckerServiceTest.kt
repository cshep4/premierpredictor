package com.cshep4.premierpredictor.service.standings.join

import com.cshep4.premierpredictor.entity.LeagueEntity
import com.cshep4.premierpredictor.repository.sql.LeagueRepository
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
internal class ExistingLeagueCheckerServiceTest {
    private companion object {
        const val LEAGUE_PIN: Long = 1234567890
    }

    @Mock
    private lateinit var leagueRepository: LeagueRepository

    @InjectMocks
    private lateinit var existingLeagueCheckerService: ExistingLeagueCheckerService

    @Test
    fun `'doesLeagueExist' returns true when league exists`() {
        whenever(leagueRepository.findById(LEAGUE_PIN)).thenReturn(Optional.of(LeagueEntity()))

        val result = existingLeagueCheckerService.doesLeagueExist(LEAGUE_PIN)

        assertThat(result, `is`(true))
    }

    @Test
    fun `'doesLeagueExist' returns false when league doesn't exist`() {
        whenever(leagueRepository.findById(LEAGUE_PIN)).thenReturn(Optional.empty())

        val result = existingLeagueCheckerService.doesLeagueExist(LEAGUE_PIN)

        assertThat(result, `is`(false))
    }
}