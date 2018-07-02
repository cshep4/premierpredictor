package com.cshep4.premierpredictor.data

data class TableTeam(
        var rank: Int = 0,
        val teamName: String = "",
        var played: Int = 0,
        var points: Int = 0,
        var wins: Int = 0,
        var draws: Int = 0,
        var losses: Int = 0,
        var goalsFor: Int = 0,
        var goalsAgainst: Int = 0,
        var goalDifference: Int = 0
)

data class LeagueTable(
        var table: MutableList<TableTeam> = mutableListOf()
){
    companion object {
        fun emptyTable() : LeagueTable {
            return LeagueTable(
                    table = mutableListOf(
                            TableTeam(teamName = "AFC Bournemouth"),
                            TableTeam(teamName = "Arsenal"),
                            TableTeam(teamName = "Brighton & Hove Albion"),
                            TableTeam(teamName = "Burnley"),
                            TableTeam(teamName = "Cardiff City"),
                            TableTeam(teamName = "Chelsea"),
                            TableTeam(teamName = "Crystal Palace"),
                            TableTeam(teamName = "Everton"),
                            TableTeam(teamName = "Fulham"),
                            TableTeam(teamName = "Huddersfield Town"),
                            TableTeam(teamName = "Leicester City"),
                            TableTeam(teamName = "Liverpool"),
                            TableTeam(teamName = "Manchester City"),
                            TableTeam(teamName = "Manchester United"),
                            TableTeam(teamName = "Newcastle United"),
                            TableTeam(teamName = "Southampton"),
                            TableTeam(teamName = "Tottenham Hotspur"),
                            TableTeam(teamName = "Watford"),
                            TableTeam(teamName = "West Ham United"),
                            TableTeam(teamName = "Wolverhampton Wanderers")
                    )
            )
        }
    }
}