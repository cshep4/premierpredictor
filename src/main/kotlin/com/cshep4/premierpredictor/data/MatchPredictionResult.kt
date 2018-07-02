package com.cshep4.premierpredictor.data

data class MatchPredictionResult (
        val id: Long = 0,
        val userId: Long = 0,
        var hTeam: String = "",
        var aTeam: String = "",
        var hGoals: Int? = null,
        var aGoals: Int? = null,
        var hPredictedGoals: Int? = null,
        var aPredictedGoals: Int? = null,
        var matchday: Int = 0,
        var matchId: Long = 0
) {
    fun toPredictedMatch(): Match = Match(
            id = this.matchId,
            hTeam = this.hTeam,
            aTeam = this.aTeam,
            hGoals = this.hPredictedGoals,
            aGoals = this.aPredictedGoals,
            matchday = this.matchday)
}