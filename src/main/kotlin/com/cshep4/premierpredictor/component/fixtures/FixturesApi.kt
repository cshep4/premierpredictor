package com.cshep4.premierpredictor.component.fixtures

import com.cshep4.premierpredictor.component.api.ApiRequester
import com.cshep4.premierpredictor.data.Match
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class FixturesApi {
    @Autowired
    private lateinit var fixtureApiRequester: ApiRequester

    @Autowired
    private lateinit var fixtureFormatter: FixtureFormatter

    fun retrieveMatches() : List<Match> {
        val apiResult = fixtureApiRequester.retrieveFixtures()

        return fixtureFormatter.format(apiResult)
    }
}