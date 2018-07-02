package com.cshep4.premierpredictor.repository.sql

import com.cshep4.premierpredictor.constant.Queries.QUERY_GET_ALL_MATCHES_WITH_PREDICTIONS
import com.cshep4.premierpredictor.entity.MatchPredictionResultEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PredictedMatchRepository : JpaRepository<MatchPredictionResultEntity, Long> {
    @Query(value = QUERY_GET_ALL_MATCHES_WITH_PREDICTIONS, nativeQuery = true)
    fun getAllMatchesWithPredictions(): List<MatchPredictionResultEntity>
}