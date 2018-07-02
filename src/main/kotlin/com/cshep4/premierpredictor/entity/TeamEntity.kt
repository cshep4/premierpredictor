package com.cshep4.premierpredictor.entity

import com.cshep4.premierpredictor.data.Team
import javax.persistence.*

@Entity
@Table(name = "Team")
data class TeamEntity (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,
        var name: String = ""
){
    fun toDto(): Team = Team(
            id = this.id,
            name = this.name)

    companion object {
        fun fromDto(dto: Team) = TeamEntity(
                id = dto.id,
                name = dto.name)
    }
}