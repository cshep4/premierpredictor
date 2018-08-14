package com.cshep4.premierpredictor.data

import java.time.LocalDateTime

data class Match (
        val id: Long = 0,
        var hTeam: String = "",
        var aTeam: String = "",
        var hGoals: Int? = null,
        var aGoals: Int? = null,
        var played: Int = 0,
        var dateTime: LocalDateTime? = null,
        var matchday: Int = 0
) {
    fun toPredictedMatch(): PredictedMatch = PredictedMatch(
            id = this.id,
            hTeam = this.hTeam,
            aTeam = this.aTeam,
            hResult = this.hGoals,
            aResult = this.aGoals,
            played = this.played,
            dateTime = this.dateTime,
            matchday = this.matchday)

    fun toMatchWithOverride(override: OverrideMatch): MatchWithOverride = MatchWithOverride(
            id = this.id,
            hTeam = this.hTeam,
            aTeam = this.aTeam,
            hGoals = this.hGoals,
            aGoals = this.aGoals,
            hOverride = override.hGoals,
            aOverride = override.aGoals,
            played = this.played,
            dateTime = this.dateTime,
            matchday = this.matchday
    )
}