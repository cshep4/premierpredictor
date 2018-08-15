package com.cshep4.premierpredictor.component.prediction

import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.PredictedMatch
import com.cshep4.premierpredictor.data.Prediction
import org.springframework.stereotype.Component

@Component
class PredictionMerger {
    fun merge(matches: List<Match>, predictions: List<Prediction>): List<PredictedMatch> {
        val predictedMatches = matches.map { it.toPredictedMatch() }

        predictedMatches.forEach {
            val id = it.id
            val prediction = predictions.firstOrNull{ it.matchId == id } ?: Prediction(matchId = id, hGoals = null, aGoals = null)

            it.updatePrediction(prediction.id, prediction.hGoals, prediction.aGoals)
        }

        return predictedMatches
    }

}