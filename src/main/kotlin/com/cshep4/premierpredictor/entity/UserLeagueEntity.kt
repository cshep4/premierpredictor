package com.cshep4.premierpredictor.entity

import com.cshep4.premierpredictor.data.UserLeague
import com.cshep4.premierpredictor.key.UserLeagueId
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "UserLeague")
data class UserLeagueEntity (
        @EmbeddedId
        var userLeagueId: UserLeagueId = UserLeagueId()
){
    fun toDto(): UserLeague = UserLeague(
            leagueId = this.userLeagueId.leagueId,
            userId = this.userLeagueId.userId)

    companion object {
        fun fromDto(dto: UserLeague) = UserLeagueEntity(userLeagueId = UserLeagueId(dto.leagueId, dto.userId))
    }
}