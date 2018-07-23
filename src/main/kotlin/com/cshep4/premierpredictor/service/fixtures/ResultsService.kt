package com.cshep4.premierpredictor.service.fixtures

import com.cshep4.premierpredictor.component.fixtures.FixturesApi
import com.cshep4.premierpredictor.component.fixtures.OverrideMatchScore
import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.service.OverrideMatchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ResultsService {
    @Autowired
    private lateinit var overrideMatchService: OverrideMatchService

    @Autowired
    private lateinit var overrideMatchScore: OverrideMatchScore

    @Autowired
    private lateinit var fixturesApi: FixturesApi

    @Autowired
    private lateinit var updateFixturesService: UpdateFixturesService

    fun update(): List<Match> {
        val matches = fixturesApi.retrieveMatches()

        if (matches.isEmpty()) {
            return emptyList()
        }

        val overrides = overrideMatchService.retrieveAllOverriddenMatches()

        val finalScores = overrideMatchScore.update(matches, overrides)

        return updateFixturesService.update(finalScores)
    }
}