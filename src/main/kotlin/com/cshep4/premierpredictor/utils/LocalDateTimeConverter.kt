package com.cshep4.premierpredictor.utils

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter
import java.time.LocalDateTime


class LocalDateTimeConverter : DynamoDBTypeConverter<String, LocalDateTime> {

    override fun convert(time: LocalDateTime): String {

        return time.toString()
    }

    override fun unconvert(stringValue: String): LocalDateTime {

        return LocalDateTime.parse(stringValue)
    }
}