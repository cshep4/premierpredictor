package com.cshep4.premierpredictor.component.fixtures

import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class FixturesByDate {
    fun format(matches: List<MatchFacts>): Map<LocalDate, List<MatchFacts>> = matches
            .sortedBy { it.getDateTime() }
            .groupBy { it.getDateTime()!!.toLocalDate() }
}