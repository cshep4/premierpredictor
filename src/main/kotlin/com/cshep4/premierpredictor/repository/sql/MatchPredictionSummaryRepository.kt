package com.cshep4.premierpredictor.repository.sql

import com.cshep4.premierpredictor.constant.Queries.QUERY_GET_PREDICTION_SUMMARY
import com.cshep4.premierpredictor.entity.MatchPredictionSummaryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MatchPredictionSummaryRepository : JpaRepository<MatchPredictionSummaryEntity, Long> {
    @Query(value = QUERY_GET_PREDICTION_SUMMARY, nativeQuery = true)
    fun getPredictionSummary(id: Long): MatchPredictionSummaryEntity
}