package com.cshep4.premierpredictor.data

import com.cshep4.premierpredictor.data.api.live.match.MatchFacts

data class MatchSummary(val match: MatchFacts = MatchFacts(),
                        val predictionSummary: MatchPredictionSummary = MatchPredictionSummary(),
                        val prediction: Prediction? = null)
//                        val forms: Map<String, TeamForm> = emptyMap())