package com.cshep4.premierpredictor.service

import com.cshep4.premierpredictor.data.*
import com.cshep4.premierpredictor.entity.LeagueEntity
import com.cshep4.premierpredictor.entity.LeagueTableUserEntity
import com.cshep4.premierpredictor.repository.sql.LeagueRepository
import com.cshep4.premierpredictor.repository.sql.LeagueTableRepository
import com.cshep4.premierpredictor.repository.sql.StandingsRepository
import com.cshep4.premierpredictor.repository.sql.UserLeagueRepository
import com.cshep4.premierpredictor.service.standings.add.AddLeagueService
import com.cshep4.premierpredictor.service.standings.join.ExistingLeagueCheckerService
import com.cshep4.premierpredictor.service.standings.join.JoinLeagueService
import com.cshep4.premierpredictor.service.standings.join.UserLeagueOverviewService
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.math.BigInteger.valueOf

@RunWith(MockitoJUnitRunner::class)
internal class StandingsServiceTest {
    private companion object {
        const val LEAGUE_PIN: Long = 1234567890
        const val USER_ID: Long = 1
        const val LEAGUE_NAME = "League"
    }

    @Mock
    private lateinit var standingsRepository: StandingsRepository

    @Mock
    private lateinit var existingLeagueCheckerService: ExistingLeagueCheckerService

    @Mock
    private lateinit var joinLeagueService: JoinLeagueService

    @Mock
    private lateinit var userLeagueOverviewService: UserLeagueOverviewService

    @Mock
    private lateinit var addLeagueService: AddLeagueService

    @Mock
    private lateinit var userLeagueRepository: UserLeagueRepository

    @Mock
    private lateinit var leagueTableRepository: LeagueTableRepository

    @Mock
    private lateinit var leagueRepository: LeagueRepository

    @InjectMocks
    private lateinit var standingsService: StandingsService

    @Test
    fun `'retrieveStandingsOverview' gets standings overview from db and returns them`() {
        val userLeagues = listOf(arrayOf("test", valueOf(12345), valueOf(2)))
        val overallLeagueOverview = listOf(arrayOf(valueOf(1), valueOf(2), 3, valueOf(4)))

        whenever(standingsRepository.getUsersLeagueList(1)).thenReturn(userLeagues)
        whenever(standingsRepository.getOverallLeagueOverview(1)).thenReturn(overallLeagueOverview)

        val expectedUserLeagues = listOf(UserLeagueOverview("test", 12345, 2))
        val expectedOverallLeague = OverallLeagueOverview(4, 2)
        val expectedResult = StandingsOverview(expectedOverallLeague, expectedUserLeagues)

        val result = standingsService.retrieveStandingsOverview(1)

        assertThat(result, `is`(expectedResult))
    }

    @Test
    fun `'joinLeague' returns null if league doesn't exist`() {
        val userLeague = UserLeague(leagueId = LEAGUE_PIN, userId = USER_ID)

        whenever(existingLeagueCheckerService.doesLeagueExist(LEAGUE_PIN)).thenReturn(false)

        val result = standingsService.joinLeague(userLeague)

        verify(joinLeagueService, times(0)).joinLeague(userLeague)
        verify(userLeagueOverviewService, times(0)).retrieveUserLeagueOverview(LEAGUE_PIN, USER_ID)
        assertThat(result, `is`(nullValue()))
    }

    @Test
    fun `'joinLeague' adds user to league and returns the league's overview`() {
        val userLeague = UserLeague(leagueId = LEAGUE_PIN, userId = USER_ID)
        val userLeagueOverview = UserLeagueOverview()

        whenever(existingLeagueCheckerService.doesLeagueExist(LEAGUE_PIN)).thenReturn(true)
        whenever(joinLeagueService.joinLeague(userLeague)).thenReturn(userLeague)
        whenever(userLeagueOverviewService.retrieveUserLeagueOverview(LEAGUE_PIN, USER_ID)).thenReturn(userLeagueOverview)

        val result = standingsService.joinLeague(userLeague)

        verify(joinLeagueService).joinLeague(userLeague)
        verify(userLeagueOverviewService).retrieveUserLeagueOverview(LEAGUE_PIN, USER_ID)
        assertThat(result, `is`(userLeagueOverview))
    }

    @Test
    fun `'addLeague' adds league, adds user to league then returns the league object`() {
        val userLeague = UserLeague(leagueId = LEAGUE_PIN, userId = USER_ID)
        val league = League(id = LEAGUE_PIN, name = LEAGUE_NAME)

        whenever(addLeagueService.addLeagueToDb(USER_ID, LEAGUE_NAME)).thenReturn(league)
        whenever(joinLeagueService.joinLeague(userLeague)).thenReturn(userLeague)

        val result = standingsService.addLeague(LEAGUE_NAME, USER_ID)

        verify(addLeagueService).addLeagueToDb(USER_ID, LEAGUE_NAME)
        verify(joinLeagueService).joinLeague(userLeague)
        assertThat(result, `is`(league))
    }

    @Test
    fun `'leaveLeague' removes user from league in db`() {
        val userLeague = UserLeague(leagueId = LEAGUE_PIN, userId = USER_ID)

        standingsService.leaveLeague(userLeague)

        verify(userLeagueRepository).delete(any())
    }

    @Test
    fun `'retrieveLeagueTable' get league table from db, sorts it descending by points and returns it`() {
        val tableEntity = listOf(LeagueTableUserEntity(score = 44), LeagueTableUserEntity(score = 80))

        whenever(leagueTableRepository.getLeagueTable(1)).thenReturn(tableEntity)

        val expectedResult = tableEntity.sortedByDescending { it.score }.map { it.toDto() }
        val result = standingsService.retrieveLeagueTable(1)

        assertThat(result, `is`(expectedResult))
    }

    @Test
    fun `'retrieveOverallLeagueTable' get league table from db, sorts it descending by points and returns it`() {
        val tableEntity = listOf(LeagueTableUserEntity(score = 44), LeagueTableUserEntity(score = 80))

        whenever(leagueTableRepository.getOverallLeagueTable()).thenReturn(tableEntity)

        val expectedResult = tableEntity.sortedByDescending { it.score }.map { it.toDto() }
        val result = standingsService.retrieveOverallLeagueTable()

        assertThat(result, `is`(expectedResult))
    }

    @Test
    fun `'renameLeague' saves the new league name and returns new object`() {
        val league = League()
        val leagueEntity = LeagueEntity.fromDto(league)

        whenever(leagueRepository.save(leagueEntity)).thenReturn(leagueEntity)

        val result = standingsService.renameLeague(league)

        assertThat(result, `is`(league))
        verify(leagueRepository).save(leagueEntity)
    }
}