package com.cshep4.premierpredictor.component.time

import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class Time {
    fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }

    fun makeDate(): Date {
        return Date()
    }

    fun localDateTimeNow(): LocalDateTime {
        return LocalDateTime.now()
    }
}