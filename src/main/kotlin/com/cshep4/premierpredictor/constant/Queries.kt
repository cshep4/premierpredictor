package com.cshep4.premierpredictor.constant

object Queries {
    const val QUERY_GET_USER_BY_EMAIL = "SELECT * " +
            "FROM Users " +
            "WHERE email = ?1"

    const val QUERY_SAVE_USER = "INSERT INTO Users (email, password) " +
            "VALUES (?1, ?2)"

    const val QUERY_GET_PREDICTIONS_BY_USER_ID = "SELECT * " +
            "FROM Prediction " +
            "WHERE userId = ?1"

    const val QUERY_GET_PREDICTED_MATCHES_BY_USER_ID = "SELECT Match.id, " +
            "Match.hTeam, " +
            "Match.aTeam, " +
            "Prediction.hGoals, " +
            "Prediction.aGoals, " +
            "Match.played, " +
            "Match.dateTime, " +
            "Match.matchday " +
            "FROM Match " +
            "INNER JOIN Prediction " +
            "ON Match.id = Prediction.matchId " +
            "WHERE Prediction.userId = ?1"

    const val QUERY_GET_SCORE_AND_RANK = "SELECT u.id, " +
            "RANK() OVER (ORDER BY u.score DESC), " +
            "u.score " +
            "FROM Users AS u"

    const val QUERY_GET_USERS_LEAGUE_LIST = "SELECT League.name as leagueName," +
            "  League.id as pin," +
            "  (SELECT rank" +
            "    FROM (" +
            "          SELECT RANK() OVER (ORDER BY score DESC) as rank," +
            "            id as uId" +
            "            FROM Users" +
            "            INNER JOIN UserLeague u" +
            "            ON Users.id = u.userId" +
            "            WHERE u.leagueId = League.id) as t" +
            "    WHERE uId = ?1) as rank" +
            " FROM UserLeague" +
            " INNER JOIN League" +
            " ON League.id = UserLeague.leagueId" +
            " WHERE userId = ?1" +
            " ORDER BY League.id"

    const val QUERY_GET_USERS_LEAGUE_OVERVIEW = "SELECT League.name as leagueName," +
            "  League.id as pin," +
            "  (SELECT rank" +
            "    FROM (" +
            "          SELECT RANK() OVER (ORDER BY score DESC) as rank," +
            "            id as uId" +
            "            FROM Users" +
            "            INNER JOIN UserLeague u" +
            "            ON Users.id = u.userId" +
            "            WHERE u.leagueId = League.id) as t" +
            "    WHERE uId = ?2) as rank" +
            " FROM UserLeague" +
            " INNER JOIN League" +
            " ON League.id = UserLeague.leagueId" +
            " WHERE userId = ?2" +
            " AND leagueId = ?1"

    const val QUERY_GET_OVERALL_LEAGUE_OVERVIEW = "SELECT id, count, score, rank" +
            " FROM (" +
            "  SELECT" +
            "    id," +
            "    COUNT(*)" +
            "    OVER ()                 AS count," +
            "    score," +
            "    RANK()" +
            "    OVER (" +
            "      ORDER BY score DESC ) AS rank" +
            "  FROM users" +
            " ) as u" +
            " WHERE id = ?1"

    const val QUERY_GET_LEAGUE_DETAILS = "SELECT users.id," +
            "  users.firstname," +
            "  users.surname," +
            "  users.predictedwinner," +
            "  users.score" +
            " FROM users" +
            " LEFT JOIN userleague u" +
            " ON users.id = u.userid" +
            " WHERE u.leagueid = ?1"

    const val QUERY_GET_OVERALL_LEAGUE_DETAILS = "SELECT id," +
            "  firstname," +
            "  surname," +
            "  predictedwinner," +
            "  score" +
            " FROM users"

    const val QUERY_GET_ALL_MATCHES_WITH_PREDICTIONS = "SELECT prediction.id, prediction.userid," +
            "  match.hteam, match.ateam, match.hgoals, match.agoals," +
            "  prediction.hgoals as hPredictedGoals, prediction.agoals as aPredictedGoals," +
            "  match.matchday, match.id as matchId" +
            " FROM match" +
            " INNER JOIN prediction" +
            " ON match.id = prediction.matchid" +
            " ORDER BY prediction.userid"

    const val QUERY_SET_USER_SIGNATURE = "UPDATE users" +
            " SET signature = ?1 " +
            " WHERE email = ?2"

    const val QUERY_RESET_USER_PASSWORD = "UPDATE users" +
            " SET password = ?1" +
            " WHERE email = ?2" +
            " AND signature = ?3"

    const val QUERY_GET_PREDICTION_SUMMARY = "select count(case when hgoals > agoals then 1 else null end) as homeWin,\n" +
            "  count(case when hgoals = agoals then 1 else null end) as draw,\n" +
            "  count(case when hgoals < agoals then 1 else null end) as awayWin\n" +
            " from prediction\n" +
            " WHERE matchid = ?1"
}