package com.cshep4.premierpredictor.data.api.live.match

import com.fasterxml.jackson.annotation.JsonProperty

data class Event(
		@JsonProperty("id")
		val id: String? = null,

		@JsonProperty("type")
		val type: String? = null,

		@JsonProperty("result")
		val result: String? = null,

		@JsonProperty("minute")
		val minute: String? = null,

		@JsonProperty("extra_min")
		val extraMin: String? = null,

		@JsonProperty("team")
		val team: String? = null,

		@JsonProperty("player")
		val player: String? = null,

		@JsonProperty("player_id")
		val playerId: String? = null,

		@JsonProperty("assist")
		val assist: String? = null,

		@JsonProperty("assist_id")
		val assistId: String? = null
)
