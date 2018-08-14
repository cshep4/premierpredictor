package com.cshep4.premierpredictor.data

import java.time.LocalDateTime

data class PredictedMatch (
        val id: Long = 0,
        var predictionId: Long? = null,
        var hTeam: String = "",
        var aTeam: String = "",
        var hGoals: Int? = null,
        var aGoals: Int? = null,
        var hResult: Int? = null,
        var aResult: Int? = null,
        var played: Int = 0,
        var dateTime: LocalDateTime? = null,
        var matchday: Int = 0
) {
    fun updatePrediction(id: Long?, hGoals: Int?, aGoals: Int?){
        this.predictionId = id
        this.hGoals = hGoals
        this.aGoals = aGoals
    }

    fun toMatch(): Match = Match(
            id = this.id,
            hTeam = this.hTeam,
            aTeam = this.aTeam,
            hGoals = this.hGoals,
            aGoals = this.aGoals,
            played = this.played,
            dateTime = this.dateTime,
            matchday = this.matchday)
}