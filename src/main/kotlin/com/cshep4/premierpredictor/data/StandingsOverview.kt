package com.cshep4.premierpredictor.data

data class StandingsOverview(val overallLeagueOverview: OverallLeagueOverview = OverallLeagueOverview(),
                             val userLeagues: List<UserLeagueOverview> = emptyList())