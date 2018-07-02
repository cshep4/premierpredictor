package com.cshep4.premierpredictor.data.api.live.commentary

import com.fasterxml.jackson.annotation.JsonProperty

data class PlayerStats(
        @JsonProperty("localteam")
        val localTeam: Players? = null,

        @JsonProperty("visitorteam")
        val visitorTeam: Players? = null
)

data class Players(
        @JsonProperty("player")
        val player: List<Player>? = null
)

data class Player(
        @JsonProperty("id")
        val id: String? = null,

        @JsonProperty("num")
        val num: String? = null,

        @JsonProperty("name")
        val name: String? = null,

        @JsonProperty("pos")
        val pos: String? = null,

        @JsonProperty("posx")
        val posX: String? = null,

        @JsonProperty("posy")
        val posY: String? = null,

        @JsonProperty("shots_total")
        val shotsTotal: String? = null,

        @JsonProperty("shots_on_goal")
        val shotsOnGoal: String? = null,

        @JsonProperty("goals")
        val goals: String? = null,

        @JsonProperty("assists")
        val assists: String? = null,

        @JsonProperty("offsides")
        val offsides: String? = null,

        @JsonProperty("fouls_drawn")
        val foulsDrawn: String? = null,

        @JsonProperty("fouls_committed")
        val foulsCommitted: String? = null,

        @JsonProperty("saves")
        val saves: String? = null,

        @JsonProperty("yellowcards")
        val yellowCards: String? = null,

        @JsonProperty("redcards")
        val redCards: String? = null,

        @JsonProperty("pen_score")
        val penScore: String? = null,

        @JsonProperty("pen_miss")
        val penMiss: String? = null
)