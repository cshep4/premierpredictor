package com.cshep4.premierpredictor.service.standings.add

import com.cshep4.premierpredictor.data.League
import com.cshep4.premierpredictor.entity.LeagueEntity
import com.cshep4.premierpredictor.repository.sql.LeagueRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AddLeagueService {
    @Autowired
    private lateinit var leagueRepository: LeagueRepository

    fun addLeagueToDb(name: String): League {
        val leagueEntity = LeagueEntity(name = name)

        return leagueRepository.save(leagueEntity).toDto()
    }
}