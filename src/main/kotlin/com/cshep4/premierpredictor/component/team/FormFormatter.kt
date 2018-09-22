package com.cshep4.premierpredictor.component.team

import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.TeamForm
import com.cshep4.premierpredictor.data.TeamMatchResult
import com.cshep4.premierpredictor.enum.Location
import com.cshep4.premierpredictor.enum.Location.AWAY
import com.cshep4.premierpredictor.enum.Location.HOME
import com.cshep4.premierpredictor.enum.Result
import com.cshep4.premierpredictor.enum.Result.*
import org.springframework.stereotype.Component

@Component
class FormFormatter {
    fun formatLastFiveGames(teamMatches: Map<String, List<Match>>): Map<String, TeamForm> {
        return teamMatches.map { Pair(it.key, createTeamForm(it.key, it.value.takeLast(5))) }.toMap()
    }

    private fun createTeamForm(teamName: String, matches: List<Match>): TeamForm {
        val matchResults = matches.map { convertMatchToResultSummary(teamName, it) }

        return TeamForm(form = matchResults)
    }

    private fun convertMatchToResultSummary(teamName: String, match: Match): TeamMatchResult {
        return TeamMatchResult(result = getResult(teamName, match), score = getScore(match), opponent = getOpponent(teamName, match), location = getLocation(teamName, match))
    }

    private fun getResult(teamName: String, match: Match): Result {
        if (match.hGoals == match.aGoals) {
            return DRAW
        }

        if (getLocation(teamName, match) == HOME) {
            if (match.hGoals!! > match.aGoals!!) {
                return WIN
            }
        } else {
            if (match.aGoals!! > match.hGoals!!) {
                return WIN
            }
        }

        return LOSS
    }

    private fun getScore(match: Match): String = "${match.hGoals}-${match.aGoals}"

    private fun getOpponent(teamName: String, match: Match): String {
        return when (teamName) {
            match.hTeam -> match.aTeam
            else -> match.hTeam
        }
    }

    private fun getLocation(teamName: String, match: Match): Location {
        return when (teamName) {
            match.hTeam -> HOME
            else -> AWAY
        }
    }
}