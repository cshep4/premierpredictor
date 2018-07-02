package com.cshep4.premierpredictor.service.standings.add

import com.cshep4.premierpredictor.entity.LeagueEntity
import com.cshep4.premierpredictor.repository.sql.LeagueRepository
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
internal class AddLeagueServiceTest {
    private companion object {
        const val LEAGUE_NAME = "League"
    }

    @Mock
    private lateinit var leagueRepository: LeagueRepository

    @InjectMocks
    private lateinit var addLeagueService: AddLeagueService

    @Test
    fun `'addLeagueToDb' adds league to db and returns result`() {
        val leagueEntity = LeagueEntity(name = LEAGUE_NAME)
        val league = leagueEntity.toDto()

        whenever(leagueRepository.save(leagueEntity)).thenReturn(leagueEntity)

        val result = addLeagueService.addLeagueToDb(LEAGUE_NAME)

        verify(leagueRepository).save(leagueEntity)
        assertThat(result, `is`(league))
    }
}