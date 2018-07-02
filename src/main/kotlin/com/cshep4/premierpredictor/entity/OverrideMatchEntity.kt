package com.cshep4.premierpredictor.entity

import com.cshep4.premierpredictor.data.OverrideMatch
import javax.persistence.*

@Entity
@Table(name = "OverrideMatch")
data class OverrideMatchEntity (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,
        var hGoals: Int? = null,
        var aGoals: Int? = null
){
    fun toDto(): OverrideMatch = OverrideMatch(
            id = this.id,
            hGoals = this.hGoals,
            aGoals = this.aGoals)

    companion object {
        fun fromDto(dto: OverrideMatch) = OverrideMatchEntity(
                id = dto.id,
                hGoals = dto.hGoals,
                aGoals = dto.aGoals)
    }
}