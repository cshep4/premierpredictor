package com.cshep4.premierpredictor.service.team

import com.cshep4.premierpredictor.component.fixtures.FixtureFormatter
import com.cshep4.premierpredictor.component.team.FormFormatter
import com.cshep4.premierpredictor.data.TeamForm
import com.cshep4.premierpredictor.service.fixtures.FixturesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TeamService {
    @Autowired
    private lateinit var fixturesService: FixturesService

    @Autowired
    private lateinit var fixtureFormatter: FixtureFormatter

    @Autowired
    private lateinit var formFormatter: FormFormatter

    fun retrieveRecentForms(): Map<String, TeamForm> {
        val matches = fixturesService.retrieveAllPastMatches()

        val teamMatches = fixtureFormatter.groupIntoTeams(matches)

        return formFormatter.formatLastFiveGames(teamMatches)
    }
}