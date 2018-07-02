package com.cshep4.premierpredictor.component.score

import com.cshep4.premierpredictor.component.leaguetable.LeagueTableCollector
import com.cshep4.premierpredictor.data.LeagueTable
import com.cshep4.premierpredictor.data.MatchPredictionResult
import com.cshep4.premierpredictor.data.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class LeagueTableScoreCalculator {
    companion object {
        const val SCORE_ADDITION = 5
    }

    @Autowired
    private lateinit var leagueTableCollector: LeagueTableCollector

    fun calculate(users: List<User>, predictedMatches: List<MatchPredictionResult>): List<User> {
        if (hasLeagueFinished(predictedMatches)) {
            val leagueTable = leagueTableCollector.getCurrentLeagueTable()

            users.forEach { updateUserScore(it, leagueTable, predictedMatches) }
        }

        return users
    }

    private fun hasLeagueFinished(predictedMatches: List<MatchPredictionResult>): Boolean {
        return predictedMatches.none{ it.hGoals == null && it.aGoals == null }
    }

    private fun updateUserScore(user: User, leagueTable: LeagueTable, predictedMatches: List<MatchPredictionResult>) {
        val usersPredictedMatches = predictedMatches
                .filter { it.userId == user.id }
                .map { it.toPredictedMatch() }

        val predictedLeagueTable = leagueTableCollector.createLeagueTableFromMatches(usersPredictedMatches)

        var numMatches = 0
        for(i in 0 until leagueTable.table.size) {
            if (isCorrectPosition(i, leagueTable, predictedLeagueTable)) {
                numMatches++
            }
        }

        user.score += numMatches * SCORE_ADDITION
    }

    private fun isCorrectPosition(i: Int, leagueTable: LeagueTable, predictedLeagueTable: LeagueTable) =
            leagueTable.table[i].teamName === predictedLeagueTable.table[i].teamName
}