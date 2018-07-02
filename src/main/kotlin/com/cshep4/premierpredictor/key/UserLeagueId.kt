package com.cshep4.premierpredictor.key

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class UserLeagueId(
        @Column(name = "leagueId")
        val leagueId: Long = -1,
        @Column(name = "userId")
        val userId: Long = -1
): Serializable