package com.cshep4.premierpredictor.data

import java.time.LocalDateTime

data class MatchWithOverride(
        val id: Long = 0,
        var hTeam: String = "",
        var aTeam: String = "",
        var hGoals: Int? = null,
        var aGoals: Int? = null,
        val hOverride: Int? = null,
        val aOverride: Int? = null,
        var played: Int = 0,
        var dateTime: LocalDateTime? = null,
        var matchday: Int = 0
)