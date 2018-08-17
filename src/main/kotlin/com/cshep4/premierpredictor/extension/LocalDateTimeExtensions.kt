package com.cshep4.premierpredictor.extension

import com.cshep4.premierpredictor.constant.MatchConstants.REFRESH_RATE
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime

fun LocalDateTime.isToday(): Boolean =
        this.toLocalDate() == LocalDate.now(Clock.systemUTC())

fun LocalDateTime.isUpcoming(): Boolean =
        this.isAfter(LocalDateTime.now(Clock.systemUTC()))

fun LocalDateTime.isInPast(): Boolean =
        this.isBefore(LocalDateTime.now())

fun LocalDateTime.isInNeedOfUpdate(): Boolean =
        this.isBefore(LocalDateTime.now().minusSeconds(REFRESH_RATE))