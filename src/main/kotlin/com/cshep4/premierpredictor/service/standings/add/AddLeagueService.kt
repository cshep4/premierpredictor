package com.cshep4.premierpredictor.service.standings.add

import com.cshep4.premierpredictor.component.time.Time
import com.cshep4.premierpredictor.data.League
import com.cshep4.premierpredictor.entity.LeagueEntity
import com.cshep4.premierpredictor.repository.sql.LeagueRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AddLeagueService {
    companion object{
        const val SUBTRACTOR = 1500000000000
    }

    @Autowired
    private lateinit var leagueRepository: LeagueRepository

    @Autowired
    private lateinit var time: Time

    fun addLeagueToDb(id: Long, name: String): League {
        val pin = createLeaguePin(id).toLong()
        val leagueEntity = LeagueEntity(id = pin, name = name)

        return leagueRepository.save(leagueEntity).toDto()
    }

    private fun createLeaguePin(id: Long): String {
        val timePart: String = (time.currentTimeMillis() - SUBTRACTOR).toString()

        return id.toString() + timePart
    }
}