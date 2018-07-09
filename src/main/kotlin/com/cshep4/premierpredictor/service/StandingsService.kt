package com.cshep4.premierpredictor.service

import com.cshep4.premierpredictor.data.*
import com.cshep4.premierpredictor.entity.LeagueEntity
import com.cshep4.premierpredictor.entity.UserLeagueEntity
import com.cshep4.premierpredictor.repository.sql.LeagueRepository
import com.cshep4.premierpredictor.repository.sql.LeagueTableRepository
import com.cshep4.premierpredictor.repository.sql.StandingsRepository
import com.cshep4.premierpredictor.repository.sql.UserLeagueRepository
import com.cshep4.premierpredictor.service.standings.add.AddLeagueService
import com.cshep4.premierpredictor.service.standings.join.ExistingLeagueCheckerService
import com.cshep4.premierpredictor.service.standings.join.JoinLeagueService
import com.cshep4.premierpredictor.service.standings.join.UserLeagueOverviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigInteger

@Service
class StandingsService {
    private companion object {
        const val LEAGUE_NUMBER_INDEX = 0
        const val PIN_INDEX = 1
        const val LEAGUE_RANK_INDEX = 2
        const val COUNT_INDEX = 1
        const val OVERALL_RANK_INDEX = 3
    }

    @Autowired
    private lateinit var standingsRepository: StandingsRepository

    @Autowired
    private lateinit var existingLeagueCheckerService: ExistingLeagueCheckerService

    @Autowired
    private lateinit var joinLeagueService: JoinLeagueService

    @Autowired
    private lateinit var userLeagueOverviewService: UserLeagueOverviewService

    @Autowired
    private lateinit var addLeagueService: AddLeagueService

    @Autowired
    private lateinit var userLeagueRepository: UserLeagueRepository

    @Autowired
    private lateinit var leagueTableRepository: LeagueTableRepository

    @Autowired
    private lateinit var leagueRepository: LeagueRepository

    fun retrieveStandingsOverview(id: Long) : StandingsOverview {
        val userLeagues = getUsersLeagueList(id)

        val overallLeagueOverview = getOverallLeagueOverview(id)

        return StandingsOverview(overallLeagueOverview, userLeagues)
    }

    private fun getUsersLeagueList(id: Long) : List<UserLeagueOverview> {
        val rawData = standingsRepository.getUsersLeagueList(id)

        return rawData.map { UserLeagueOverview(it[LEAGUE_NUMBER_INDEX] as String, (it[PIN_INDEX] as BigInteger).toLong(), (it[LEAGUE_RANK_INDEX] as BigInteger).toLong()) }
    }

    private fun getOverallLeagueOverview(id: Long) : OverallLeagueOverview {
        val rawData = standingsRepository.getOverallLeagueOverview(id)[0]

        return OverallLeagueOverview((rawData[OVERALL_RANK_INDEX] as BigInteger).toLong(), (rawData[COUNT_INDEX] as BigInteger).toLong())
    }

    fun joinLeague(userLeague: UserLeague) : UserLeagueOverview? {
        val leagueExists = existingLeagueCheckerService.doesLeagueExist(userLeague.leagueId)

        if (!leagueExists) {
            return null
        }

        val addedUserLeagueRecord = joinLeagueService.joinLeague(userLeague)

        return userLeagueOverviewService.retrieveUserLeagueOverview(addedUserLeagueRecord.leagueId, addedUserLeagueRecord.userId)
    }

    fun addLeague(name: String, id: Long) : League {
        val league = addLeagueService.addLeagueToDb(id, name)

        val userLeague = UserLeague(leagueId = league.id, userId = id)
        joinLeagueService.joinLeague(userLeague)

        return league
    }

    fun leaveLeague(userLeague: UserLeague) {
        val userLeagueEntity = UserLeagueEntity.fromDto(userLeague)
        userLeagueRepository.delete(userLeagueEntity)
    }

    fun retrieveLeagueTable(pin: Long) : List<LeagueTableUser> = leagueTableRepository.getLeagueTable(pin)
            .sortedByDescending { it.score }
            .map { it.toDto() }

    fun retrieveOverallLeagueTable() : List<LeagueTableUser> = leagueTableRepository.getOverallLeagueTable()
            .sortedByDescending { it.score }
            .map { it.toDto() }

    fun renameLeague(league: League) : League = leagueRepository.save(LeagueEntity.fromDto(league)).toDto()
}