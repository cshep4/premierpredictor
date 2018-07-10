package com.cshep4.premierpredictor.component.leaguetable

import com.cshep4.premierpredictor.data.LeagueTable
import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.service.fixtures.FixturesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class LeagueTableCollector {
    @Autowired
    private lateinit var fixturesService: FixturesService

    @Autowired
    private lateinit var leagueTableCalculator: LeagueTableCalculator

    fun getCurrentLeagueTable() : LeagueTable {
        val matches = fixturesService.retrieveAllMatches()

        return createLeagueTableFromMatches(matches)
    }

    fun getPredictedLeagueTable(id: Long): LeagueTable {
        val matches = fixturesService.retrieveAllMatchesWithPredictions(id)
                .map { it.toMatch() }

        return createLeagueTableFromMatches(matches)
    }

    fun createLeagueTableFromMatches(matches: List<Match>) : LeagueTable {
        val leagueTable = LeagueTable.emptyTable()

        return leagueTableCalculator.calculate(matches, leagueTable)
    }
}