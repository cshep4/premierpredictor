package com.cshep4.premierpredictor.extension

import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime

fun LocalDateTime.isToday(): Boolean =
        this.toLocalDate() == LocalDate.now(Clock.systemUTC())

fun LocalDateTime.isUpcoming(): Boolean =
        this.isAfter(LocalDateTime.now(Clock.systemUTC()))

fun LocalDateTime.isInPast(): Boolean =
        this.isBefore(LocalDateTime.now())