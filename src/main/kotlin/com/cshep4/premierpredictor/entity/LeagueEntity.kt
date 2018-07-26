package com.cshep4.premierpredictor.entity

import com.cshep4.premierpredictor.data.League
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "League")
data class LeagueEntity (
        @Id
        val id: Long = 0,
        val name: String = ""
){
    fun toDto(): League = League(
            id = this.id,
            name = this.name)

    companion object {
        fun fromDto(dto: League) = LeagueEntity(
                id = dto.id,
                name = dto.name)
    }
}
