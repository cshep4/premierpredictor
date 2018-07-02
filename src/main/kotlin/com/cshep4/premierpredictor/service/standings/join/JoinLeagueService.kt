package com.cshep4.premierpredictor.service.standings.join

import com.cshep4.premierpredictor.data.UserLeague
import com.cshep4.premierpredictor.entity.UserLeagueEntity
import com.cshep4.premierpredictor.repository.sql.UserLeagueRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class JoinLeagueService {
    @Autowired
    private lateinit var userLeagueRepository: UserLeagueRepository

    fun joinLeague(userLeague: UserLeague) : UserLeague {
        val userLeagueEntity = UserLeagueEntity.fromDto(userLeague)

        return userLeagueRepository.save(userLeagueEntity).toDto()
    }
}