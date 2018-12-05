package com.cshep4.premierpredictor.data.api.live.match

import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.api.live.commentary.Commentary
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

data class MatchFacts(
		@JsonProperty("id")
		val id: String? = null,

		@JsonProperty("comp_id")
		val compId: String? = null,

		@JsonProperty("formatted_date")
		var formattedDate: String? = null,

		@JsonProperty("season")
		val season: String? = null,

		@JsonProperty("week")
		val week: String? = null,

		@JsonProperty("venue")
		val venue: String? = null,

		@JsonProperty("venue_id")
		val venueId: String? = null,

		@JsonProperty("venue_city")
		val venueCity: String? = null,

		@JsonProperty("status")
		var status: String? = null,

		@JsonProperty("timer")
		var timer: String? = null,

		@JsonProperty("time")
		var time: String? = null,

		@JsonProperty("localteam_id")
		val localTeamId: String? = null,

		@JsonProperty("localteam_name")
		var localTeamName: String? = null,

		@JsonProperty("localteam_score")
		var localTeamScore: String? = null,

		@JsonProperty("visitorteam_id")
		val visitorTeamId: String? = null,

		@JsonProperty("visitorteam_name")
		var visitorTeamName: String? = null,

		@JsonProperty("visitorteam_score")
		var visitorTeamScore: String? = null,

		@JsonProperty("ht_score")
		val htScore: String? = null,

		@JsonProperty("ft_score")
		val ftScore: String? = null,

		@JsonProperty("et_score")
		val etScore: String? = null,

		@JsonProperty("penalty_local")
		val penaltyLocal: String? = null,

		@JsonProperty("penalty_visitor")
		val penaltyVisitor: String? = null,

		@JsonProperty("events")
		val events: List<Event>? = null,

		@JsonIgnore
		@JsonProperty("commentary")
		var commentary: Commentary? = null,

		@JsonIgnore
		@JsonProperty("lastUpdated")
		@JsonSerialize(using = LocalDateTimeSerializer::class)
		@JsonDeserialize(using = LocalDateTimeDeserializer::class)
		var lastUpdated: LocalDateTime? = LocalDateTime.now()
) {
	fun toMatch(): Match = Match(
			id = this.id!!.toLong(),
			hTeam = getFullTeamName(this.localTeamName)!!,
			aTeam = getFullTeamName(this.visitorTeamName)!!,
			hGoals = this.localTeamScore?.toIntOrNull(),
			aGoals = this.visitorTeamScore?.toIntOrNull(),
			played = getPlayed(),
			dateTime = getDateTime(),
			matchday = this.week!!.toInt())

	fun toSantisedMatchFacts() = MatchFacts(
			penaltyVisitor = this.penaltyVisitor,
			venue = this.venue,
			week = this.week,
			visitorTeamName = getFullTeamName(this.visitorTeamName),
			penaltyLocal = this.penaltyLocal,
			localTeamScore = makeScoreNumeric(this.localTeamScore),
			ftScore = this.ftScore,
			etScore = this.etScore,
			compId = this.compId,
			venueCity = this.venueCity,
			visitorTeamId = this.visitorTeamId,
			timer = this.timer,
			htScore = this.htScore,
			localTeamId = this.localTeamId,
			season = this.season,
			localTeamName = getFullTeamName(this.localTeamName),
			id = this.id,
			time = this.time,
			visitorTeamScore = makeScoreNumeric(this.visitorTeamScore),
			formattedDate = this.formattedDate,
			venueId = this.venueId,
			events = this.events,
			status = correctStatus(this.status),
			commentary = this.commentary,
			lastUpdated = this.lastUpdated)

	private fun makeScoreNumeric(score: String?): String? {
		if (score == null || score == "?") {
			return ""
		}

		return score
	}

	private fun correctStatus(status: String?): String? {
		if (status == null || ":" in status) {
			return null
		}

		return status
	}

	@JsonIgnore
	fun getDateTime(): LocalDateTime? {
		val time = LocalTime.parse(this.time)
		val date = LocalDate.parse(this.formattedDate, DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH))

		return LocalDateTime.of(date, time)
	}

	@JsonIgnore
	fun setDateTime(localDateTime: LocalDateTime) {
		val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
		this.time = localDateTime.format(timeFormatter)

		val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH)
		this.formattedDate = localDateTime.format(dateFormatter)
	}

	private fun getPlayed(): Int {
		return if (this.localTeamScore != null && this.localTeamScore != "" && this.visitorTeamScore != null && this.visitorTeamScore != "") {
			1
		} else {
			0
		}
	}

	private fun getFullTeamName(name: String?): String? {
		return when (name) {
			"Bournemouth" -> "AFC Bournemouth"
			"Brighton" -> "Brighton & Hove Albion"
			"Cardiff" -> "Cardiff City"
			"Huddersfield" -> "Huddersfield Town"
			"Leicester" -> "Leicester City"
			"Newcastle" -> "Newcastle United"
			"Tottenham" -> "Tottenham Hotspur"
			"West Ham" -> "West Ham United"
			"Wolves" -> "Wolverhampton Wanderers"
			"Manchester Utd" -> "Manchester United"
			else -> name
		}
	}
}
