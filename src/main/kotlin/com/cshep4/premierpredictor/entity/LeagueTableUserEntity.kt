package com.cshep4.premierpredictor.entity

import com.cshep4.premierpredictor.data.LeagueTableUser
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class LeagueTableUserEntity(@Id val id: Long = 0,
                                 val firstName: String = "",
                                 val surname: String = "",
                                 val predictedWinner: String = "",
                                 val score: Int = 0){
    fun toDto(): LeagueTableUser = LeagueTableUser(
            id = this.id,
            firstName = this.firstName,
            surname = this.surname,
            predictedWinner = this.predictedWinner,
            score = this.score)

    companion object {
        fun fromDto(dto: LeagueTableUser) = LeagueTableUserEntity(
                id = dto.id,
                firstName = dto.firstName,
                surname = dto.surname,
                predictedWinner = dto.predictedWinner,
                score = dto.score)
    }
}
