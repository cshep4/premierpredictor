package com.cshep4.wcpredictor.data

data class Prediction(val id: Long = 0,
        var hGoals: Int? = null,
        var aGoals: Int? = null,
        var userId: Int? = null,
        var matchId: Long? = null
)