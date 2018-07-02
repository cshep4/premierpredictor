package com.cshep4.premierpredictor.service.standings.join

import com.cshep4.premierpredictor.data.UserLeagueOverview
import com.cshep4.premierpredictor.repository.sql.UserLeagueOverviewRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigInteger

@Service
class UserLeagueOverviewService {
    private companion object {
        const val LEAGUE_NAME_INDEX = 0
        const val PIN_INDEX = 1
        const val LEAGUE_RANK_INDEX = 2
    }

    @Autowired
    lateinit var userLeagueOverviewRepository: UserLeagueOverviewRepository

    fun retrieveUserLeagueOverview(leagueId: Long, userId: Long): UserLeagueOverview {
        val rawData =  userLeagueOverviewRepository.getUserLeagueOverview(leagueId, userId)[0]

        return UserLeagueOverview(rawData[LEAGUE_NAME_INDEX] as String, (rawData[PIN_INDEX] as BigInteger).toLong(), (rawData[LEAGUE_RANK_INDEX] as BigInteger).toLong())
    }

}