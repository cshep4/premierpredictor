package com.cshep4.premierpredictor.service.standings.join

import com.cshep4.premierpredictor.repository.sql.LeagueRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ExistingLeagueCheckerService {
    @Autowired
    private lateinit var leagueRepository: LeagueRepository

    fun doesLeagueExist(pin: Long) : Boolean = leagueRepository.findById(pin).isPresent
}