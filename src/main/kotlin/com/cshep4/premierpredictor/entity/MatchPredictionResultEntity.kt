package com.cshep4.premierpredictor.entity

import com.cshep4.premierpredictor.data.MatchPredictionResult
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class MatchPredictionResultEntity (
        @Id
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
    fun toDto(): MatchPredictionResult = MatchPredictionResult(
            id = this.id,
            userId = this.userId,
            hTeam = this.hTeam,
            aTeam = this.aTeam,
            hGoals = this.hGoals,
            aGoals = this.aGoals,
            hPredictedGoals = this.hPredictedGoals,
            aPredictedGoals = this.aPredictedGoals,
            matchday = this.matchday,
            matchId = this.matchId)

    companion object {
        fun fromDto(dto: MatchPredictionResult) = MatchPredictionResultEntity(
                id = dto.id,
                userId = dto.userId,
                hTeam = dto.hTeam,
                aTeam = dto.aTeam,
                hGoals = dto.hGoals,
                aGoals = dto.aGoals,
                hPredictedGoals = dto.hPredictedGoals,
                aPredictedGoals = dto.aPredictedGoals,
                matchday = dto.matchday,
                matchId = dto.matchId)
    }
}