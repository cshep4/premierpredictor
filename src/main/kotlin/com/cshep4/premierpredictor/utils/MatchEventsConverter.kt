package com.cshep4.premierpredictor.utils

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter
import com.cshep4.premierpredictor.data.api.live.match.Event
import com.fasterxml.jackson.databind.ObjectMapper


class MatchEventsConverter : DynamoDBTypeConverter<String, List<Event>> {

    override fun convert(events: List<Event>): String {

        return ObjectMapper().writeValueAsString(events)
    }

    override fun unconvert(stringValue: String): List<Event>? {

        return ObjectMapper().readValue(stringValue, Array<Event>::class.java).toList()
    }
}