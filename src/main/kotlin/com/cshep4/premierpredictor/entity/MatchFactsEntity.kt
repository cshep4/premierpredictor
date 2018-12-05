package com.cshep4.premierpredictor.entity

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted
import com.cshep4.premierpredictor.data.api.live.commentary.Commentary
import com.cshep4.premierpredictor.data.api.live.match.Event
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.utils.CommentaryConverter
import com.cshep4.premierpredictor.utils.LocalDateTimeConverter
import com.cshep4.premierpredictor.utils.MatchEventsConverter
import org.springframework.data.annotation.Id
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@DynamoDBTable(tableName = "Match")
data class MatchFactsEntity(
        @Id
        @DynamoDBHashKey
        var id: String? = null,

        var penaltyVisitor: String? = null,

        var venue: String? = null,

        var week: String? = null,

        var visitorTeamName: String? = null,

        var penaltyLocal: String? = null,

        var localTeamScore: String? = null,

        var ftScore: String? = null,

        var etScore: String? = null,

        var compId: String? = null,

        var venueCity: String? = null,

        var visitorTeamId: String? = null,

        var timer: String? = null,

        var htScore: String? = null,

        var localTeamId: String? = null,

        var season: String? = null,

        var localTeamName: String? = null,

        var time: String? = null,

        var visitorTeamScore: String? = null,

        var formattedDate: String? = null,

        var venueId: String? = null,

        @DynamoDBTypeConverted( converter = MatchEventsConverter::class )
        var events: List<Event>? = null,

        var status: String? = null,

        @DynamoDBTypeConverted( converter = CommentaryConverter::class )
        var commentary: Commentary? = null,

        @DynamoDBTypeConverted( converter = LocalDateTimeConverter::class )
        var lastUpdated: LocalDateTime? = LocalDateTime.now()
){
    fun toDto(): MatchFacts = MatchFacts(
            penaltyVisitor = this.penaltyVisitor,
            venue = this.venue,
            week = this.week,
            visitorTeamName = getFullTeamName(this.visitorTeamName),
            penaltyLocal = this.penaltyLocal,
            localTeamScore = getNumericScore(this.localTeamScore),
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
            visitorTeamScore = getNumericScore(this.visitorTeamScore),
            formattedDate = this.formattedDate,
            venueId = this.venueId,
            events = this.events,
            status = correctStatus(this.status),
            commentary = this.commentary,
            lastUpdated = this.lastUpdated)

    @DynamoDBIgnore
    fun getDateTime(): LocalDateTime? {
        val time = LocalTime.parse(this.time)
        val date = LocalDate.parse(this.formattedDate, DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH))

        return LocalDateTime.of(date, time)
    }

    fun setDateTime(localDateTime: LocalDateTime) {
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        this.time = localDateTime.format(timeFormatter)

        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH)
        this.formattedDate = localDateTime.format(dateFormatter)
    }

    companion object {
        fun fromDto(dto: MatchFacts) = MatchFactsEntity(
                penaltyVisitor = dto.penaltyVisitor,
                venue = dto.venue,
                week = dto.week,
                visitorTeamName = dto.visitorTeamName,
                penaltyLocal = dto.penaltyLocal,
                localTeamScore = getNumericScore(dto.localTeamScore),
                ftScore = dto.ftScore,
                etScore = dto.etScore,
                compId = dto.compId,
                venueCity = dto.venueCity,
                visitorTeamId = dto.visitorTeamId,
                timer = dto.timer,
                htScore = dto.htScore,
                localTeamId = dto.localTeamId,
                season = dto.season,
                localTeamName = dto.localTeamName,
                id = dto.id,
                time = dto.time,
                visitorTeamScore = getNumericScore(dto.visitorTeamScore),
                formattedDate = dto.formattedDate,
                venueId = dto.venueId,
                events = dto.events,
                status = correctStatus(dto.status),
                commentary = dto.commentary,
                lastUpdated = dto.lastUpdated)

        private fun getNumericScore(score: String?): String? {
            if (score == null || score == "?") {
                return ""
            }

            return score
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

        private fun correctStatus(status: String?): String? {
            if (status == null || ":" in status) {
                return null
            }

            return status
        }
    }
}