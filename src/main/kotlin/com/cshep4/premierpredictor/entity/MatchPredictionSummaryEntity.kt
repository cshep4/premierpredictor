package com.cshep4.premierpredictor.entity

import com.cshep4.premierpredictor.data.MatchPredictionSummary
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class MatchPredictionSummaryEntity(@Id val homeWin: Int = 0, val draw: Int = 0, val awayWin: Int = 0) {
    fun toDto(): MatchPredictionSummary = MatchPredictionSummary(
            homeWin = this.homeWin,
            draw = this.draw,
            awayWin = this.awayWin)

    companion object {
        fun fromDto(dto: MatchPredictionSummary) = MatchPredictionSummaryEntity(
                homeWin = dto.homeWin,
                draw = dto.draw,
                awayWin = dto.awayWin)
    }
}