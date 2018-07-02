package com.cshep4.premierpredictor.data.api.live.commentary

import com.fasterxml.jackson.annotation.JsonProperty

data class Substitutions(
        @JsonProperty("localteam")
        val localTeam: List<Substitution>? = null,

        @JsonProperty("visitorteam")
        val visitorTeam: List<Substitution>? = null
)

data class Substitution(
        @JsonProperty("off_name")
        val offName: String? = null,

        @JsonProperty("on_name")
        val onName: String? = null,

        @JsonProperty("off_id")
        val offId: String? = null,

        @JsonProperty("on_id")
        val onId: String? = null,

        @JsonProperty("minute")
        val minute: String? = null,

        @JsonProperty("table_id")
        val tableId: String? = null
)