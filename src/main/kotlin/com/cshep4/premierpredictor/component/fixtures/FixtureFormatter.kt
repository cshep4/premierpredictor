package com.cshep4.premierpredictor.component.fixtures

import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import org.springframework.stereotype.Component


@Component
class FixtureFormatter {
    private val remappingFunction = { v1: List<Match>, v2: List<Match> ->
        val list = v1.toMutableList()
        list.addAll(v2)
        list
    }

    fun format(fixturesApiResult: List<MatchFacts>): List<Match> {
        return fixturesApiResult.map { it.toMatch() }
    }

    fun groupIntoTeams(matches: List<Match>): Map<String, List<Match>> {
        val homeTeamMatches = matches.groupBy { it.hTeam }
        val awayTeamMatches = matches.groupBy { it.aTeam }

        val allMatches = homeTeamMatches.toMutableMap()

        awayTeamMatches.forEach { k, v -> allMatches.merge(k, v, remappingFunction) }

        return allMatches.map { Pair(it.key, it.value.sortedBy { v -> v.dateTime }) }.toMap()
    }
}