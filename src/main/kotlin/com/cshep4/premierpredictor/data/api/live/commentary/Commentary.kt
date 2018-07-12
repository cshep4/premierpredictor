package com.cshep4.premierpredictor.data.api.live.commentary

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import java.time.LocalDateTime

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
		val playerStats: PlayerStats? = null,

		@JsonIgnore
		@JsonProperty("lastUpdated")
		@JsonSerialize(using = LocalDateTimeSerializer::class)
		@JsonDeserialize(using = LocalDateTimeDeserializer::class)
		var lastUpdated: LocalDateTime? = LocalDateTime.now()
)
