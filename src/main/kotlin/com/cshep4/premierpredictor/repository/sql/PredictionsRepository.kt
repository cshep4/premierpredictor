package com.cshep4.premierpredictor.repository.sql

import com.cshep4.premierpredictor.constant.Queries.QUERY_GET_PREDICTIONS_BY_USER_ID
import com.cshep4.premierpredictor.entity.PredictionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PredictionsRepository : JpaRepository<PredictionEntity, Long> {
    @Query(value = QUERY_GET_PREDICTIONS_BY_USER_ID, nativeQuery = true)
    fun findByUserId(id: Long): List<PredictionEntity>
}