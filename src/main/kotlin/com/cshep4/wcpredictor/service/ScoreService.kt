package com.cshep4.wcpredictor.service

import com.cshep4.wcpredictor.data.UserRank
import com.cshep4.wcpredictor.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigInteger

@Service
class ScoreService {
    @Autowired
    private lateinit var userRepository: UserRepository

    fun retrieveScoreAndRank(id: Long) : UserRank {
        val rawData = userRepository.getUserRankAndScore()

        val userRank = rawData.first{ it[0] == id.toBigInteger() }

        val uId = (userRank[0] as BigInteger).toLong()
        val rank = (userRank[1] as BigInteger).toLong()
        val score = userRank[2] as Int

        return UserRank(id = uId, rank = rank, score = score)
    }

    fun updateUserScores() {

    }
}