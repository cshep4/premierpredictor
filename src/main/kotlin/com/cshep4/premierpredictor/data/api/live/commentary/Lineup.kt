package com.cshep4.premierpredictor.data.api.live.commentary

import com.fasterxml.jackson.annotation.JsonProperty

data class Lineup(
        @JsonProperty("localteam")
        val localTeam: List<Position>? = null,

        @JsonProperty("visitorteam")
        val visitorTeam: List<Position>? = null
)

data class Position(
        @JsonProperty("id")
        val id: String? = null,

        @JsonProperty("number")
        val number: String? = null,

        @JsonProperty("name")
        val name: String? = null,

        @JsonProperty("pos")
        val pos: String? = null
)