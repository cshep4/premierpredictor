package com.cshep4.premierpredictor.repository.sql

import com.cshep4.premierpredictor.constant.Queries.QUERY_GET_OVERALL_LEAGUE_OVERVIEW
import com.cshep4.premierpredictor.constant.Queries.QUERY_GET_USERS_LEAGUE_LIST
import com.cshep4.premierpredictor.entity.UserLeagueEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface StandingsRepository : JpaRepository<UserLeagueEntity, Long> {
    @Query(value = QUERY_GET_USERS_LEAGUE_LIST, nativeQuery = true)
    fun getUsersLeagueList(id: Long): List<Array<Any>>

    @Query(value = QUERY_GET_OVERALL_LEAGUE_OVERVIEW, nativeQuery = true)
    fun getOverallLeagueOverview(id: Long): List<Array<Any>>
}