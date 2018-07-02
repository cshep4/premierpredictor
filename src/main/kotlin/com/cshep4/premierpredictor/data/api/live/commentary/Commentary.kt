package com.cshep4.premierpredictor.data.api.live.commentary

import com.fasterxml.jackson.annotation.JsonProperty

data class Commentary(
		@JsonProperty("match_id")
		val matchId: String? = null,

		@JsonProperty("match_info")
		val matchInfo: List<MatchInfo>? = null,

		@JsonProperty("lineup")
		val lineup: Lineup? = null,

		@JsonProperty("subs")
		val subs: Lineup? = null,

		@JsonProperty("substitutions")
		val substitutions: Substitutions? = null,

		@JsonProperty("comments")
		val comments: List<Comment>? = null,

		@JsonProperty("match_stats")
		val matchStats: MatchStats? = null,

		@JsonProperty("player_stats")
		val playerStats: PlayerStats? = null
)
