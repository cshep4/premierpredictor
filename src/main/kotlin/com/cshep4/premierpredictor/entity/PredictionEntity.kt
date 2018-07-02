package com.cshep4.premierpredictor.entity

import com.cshep4.premierpredictor.data.Prediction
import javax.persistence.*

@Entity
@Table(name = "Prediction")
data class PredictionEntity (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,
        var hGoals: Int? = null,
        var aGoals: Int? = null,
        var userId: Int? = null,
        var matchId: Long? = null
){
    fun toDto(): Prediction = Prediction(
            id = this.id,
            hGoals = this.hGoals,
            aGoals = this.aGoals,
            userId = this.userId,
            matchId = this.matchId)

    companion object {
        fun fromDto(dto: Prediction) = PredictionEntity(
                id = dto.id,
                hGoals = dto.hGoals,
                aGoals = dto.aGoals,
                userId = dto.userId,
                matchId = dto.matchId)
    }
}