package com.cshep4.wcpredictor.repository

import com.cshep4.wcpredictor.constant.Queries.QUERY_GET_PREDICTED_MATCHES_BY_USER_ID
import com.cshep4.wcpredictor.entity.MatchEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FixturesRepository : JpaRepository<MatchEntity, Long> {
    @Query(value = QUERY_GET_PREDICTED_MATCHES_BY_USER_ID, nativeQuery = true)
    fun findPredictedMatchesByUserId(id: Long): List<MatchEntity>
}