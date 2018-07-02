package com.cshep4.premierpredictor.service.standings.join

import com.cshep4.premierpredictor.data.UserLeague
import com.cshep4.premierpredictor.entity.UserLeagueEntity
import com.cshep4.premierpredictor.repository.sql.UserLeagueRepository
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class JoinLeagueServiceTest {
    @Mock
    private lateinit var userLeagueRepository: UserLeagueRepository

    @InjectMocks
    private lateinit var joinLeagueService: JoinLeagueService

    @Test
    fun `'joinLeague' adds userLeague record to db`() {
        val userLeague = UserLeague()
        val userLeagueEntity = UserLeagueEntity.fromDto(userLeague)

        whenever(userLeagueRepository.save(any(UserLeagueEntity::class.java))).thenReturn(userLeagueEntity)

        val result = joinLeagueService.joinLeague(userLeague)

        verify(userLeagueRepository).save(any(UserLeagueEntity::class.java))
        assertThat(result, `is`(userLeague))
    }
}