package com.cshep4.premierpredictor.component.leaguetable

import com.cshep4.premierpredictor.data.LeagueTable
import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.TableTeam
import org.springframework.stereotype.Component

@Component
class LeagueTableCalculator {
    fun calculate(matches: List<Match>,leagueTable: LeagueTable) : LeagueTable {
        matches.filter { it.hGoals != null && it.aGoals != null }
                .forEach { updateTableForMatch(it, leagueTable) }

        sortTables(leagueTable)

        updateRank(leagueTable)

        return leagueTable
    }

    private fun updateTableForMatch(match: Match, leagueTable: LeagueTable) {
        leagueTable.table
                .filter { it.teamName == match.hTeam || it.teamName == match.aTeam }
                .forEach { updateTableTeam(it, match) }
    }

    private fun updateTableTeam(tableTeam: TableTeam, match: Match) {
        if (tableTeam.teamName == match.hTeam) {
            updateTeamStats(tableTeam, match.hGoals!!, match.aGoals!!)
        } else if (tableTeam.teamName == match.aTeam) {
            updateTeamStats(tableTeam, match.aGoals!!, match.hGoals!!)
        }
    }

    private fun updateTeamStats(tableTeam: TableTeam, goalsFor: Int, goalsAgainst: Int) {
        when {
            goalsFor > goalsAgainst -> {
                tableTeam.wins += 1
                tableTeam.points += 3
            }
            goalsFor == goalsAgainst -> {
                tableTeam.draws += 1
                tableTeam.points += 1
            }
            else -> tableTeam.losses += 1
        }
        tableTeam.played += 1
        tableTeam.goalsFor += goalsFor
        tableTeam.goalsAgainst += goalsAgainst
        tableTeam.goalDifference = tableTeam.goalsFor - tableTeam.goalsAgainst
    }

    private fun sortTables(leagueTable: LeagueTable) {
        leagueTable.table = ArrayList(leagueTable.table)
                .sortedWith(
                        compareByDescending<TableTeam>{ it.points }
                        .thenByDescending { it.goalDifference }
                        .thenByDescending { it.goalsFor }
                )
                .toMutableList()
    }

    private fun updateRank(leagueTable: LeagueTable) {
        var i = 1
        leagueTable.table.forEach { it.rank = i++ }
    }
}