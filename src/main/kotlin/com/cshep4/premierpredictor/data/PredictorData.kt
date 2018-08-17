package com.cshep4.premierpredictor.data

data class PredictorData(val predictions: List<PredictedMatch> = emptyList(), val forms: Map<String, TeamForm> = emptyMap())