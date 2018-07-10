package com.cshep4.premierpredictor.component.fixtures

import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import org.springframework.stereotype.Component

@Component
class FixtureFormatter {
    fun format(fixturesApiResult: List<MatchFacts>): List<Match> {
        return fixturesApiResult.map { it.toMatch() }
    }
}