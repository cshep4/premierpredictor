package com.cshep4.premierpredictor.data.api.live.commentary

import com.fasterxml.jackson.annotation.JsonProperty

data class MatchStats(
        @JsonProperty("localteam")
        val localTeam: List<TeamStats>? = null,

        @JsonProperty("visitorteam")
        val visitorTeam: List<TeamStats>? = null
)

data class TeamStats(
        @JsonProperty("shots_total")
        val shotsTotal: String? = null,

        @JsonProperty("shots_ongoal")
        val shotsOnGoal: String? = null,

        @JsonProperty("fouls")
        val fouls: String? = null,

        @JsonProperty("corners")
        val corners: String? = null,

        @JsonProperty("offsides")
        val offsides: String? = null,

        @JsonProperty("possesiontime")
        val possessionTime: String? = null,

        @JsonProperty("yellowcards")
        val yellowCards: String? = null,

        @JsonProperty("redcards")
        val redCards: String? = null,

        @JsonProperty("saves")
        val saves: String? = null,

        @JsonProperty("table_id")
        val tableId: String? = null
)