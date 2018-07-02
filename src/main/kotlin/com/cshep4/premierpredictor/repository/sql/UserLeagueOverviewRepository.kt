package com.cshep4.premierpredictor.repository.sql

import com.cshep4.premierpredictor.constant.Queries.QUERY_GET_USERS_LEAGUE_OVERVIEW
import com.cshep4.premierpredictor.entity.UserLeagueEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserLeagueOverviewRepository : JpaRepository<UserLeagueEntity, Long> {
    @Query(value = QUERY_GET_USERS_LEAGUE_OVERVIEW, nativeQuery = true)
    fun getUserLeagueOverview(leagueId: Long, userId: Long): List<Array<Any>>
}