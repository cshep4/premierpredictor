package com.cshep4.premierpredictor.component.prediction

import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.Prediction
import com.cshep4.premierpredictor.data.PredictionSummary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CreatePredictionSummary {
    @Autowired
    private lateinit var predictionMerger: PredictionMerger

    fun format(matches: List<Match>, predictions: List<Prediction>) : PredictionSummary {
        val matchesWithPredictions = predictionMerger.merge(matches, predictions)

        return PredictionSummary(matches = matchesWithPredictions)
    }
}