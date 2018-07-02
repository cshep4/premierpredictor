package com.cshep4.premierpredictor.component.score

import com.cshep4.premierpredictor.data.MatchPredictionResult
import com.cshep4.premierpredictor.data.User
import org.springframework.stereotype.Component

@Component
class MatchScoreCalculator {
    companion object {
        const val CORRECT_GOAL = 1
        const val CORRECT_RESULT = 3
        const val CORRECT_SCORELINE = 3
    }

    fun calculate(users: List<User>, predictedMatches: List<MatchPredictionResult>): List<User> {
        predictedMatches.filter { it.hGoals != null && it.aGoals != null &&
                it.hPredictedGoals != null && it.aPredictedGoals != null}
                .forEach {
            var matchScore = 0

            if (homeGoalsMatch(it)) {
                matchScore += CORRECT_GOAL
            }

            if (awayGoalsMatch(it)) {
                matchScore += CORRECT_GOAL
            }

            if (isCorrectResult(it)) {
                matchScore += CORRECT_RESULT
            }

            if (homeGoalsMatch(it) && awayGoalsMatch(it)) {
                matchScore += CORRECT_SCORELINE
            }

            val userId = it.userId
            users.find { it.id == userId }!!.score += matchScore
        }

        return users
    }

    private fun homeGoalsMatch(predictionResult: MatchPredictionResult) = predictionResult.hGoals == predictionResult.hPredictedGoals

    private fun awayGoalsMatch(predictionResult: MatchPredictionResult) = predictionResult.aGoals == predictionResult.aPredictedGoals

    private fun isCorrectResult(predictionResult: MatchPredictionResult): Boolean {
        return (predictionResult.hGoals!! > predictionResult.aGoals!! && predictionResult.hPredictedGoals!! > predictionResult.aPredictedGoals!!) ||
                (predictionResult.hGoals!! == predictionResult.aGoals!! && predictionResult.hPredictedGoals!! == predictionResult.aPredictedGoals!!) ||
                (predictionResult.hGoals!! < predictionResult.aGoals!! && predictionResult.hPredictedGoals!! < predictionResult.aPredictedGoals!!)
    }
}