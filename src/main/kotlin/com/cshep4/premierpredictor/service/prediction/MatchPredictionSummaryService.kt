package com.cshep4.premierpredictor.service.prediction

import com.cshep4.premierpredictor.data.MatchPredictionSummary
import com.cshep4.premierpredictor.repository.sql.MatchPredictionSummaryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MatchPredictionSummaryService {
    @Autowired
    private lateinit var matchPredictionSummaryRepository: MatchPredictionSummaryRepository

    fun retrieveMatchPredictionSummary(id: String): MatchPredictionSummary =
            matchPredictionSummaryRepository
            .getPredictionSummary(id.toLong())
            .toDto()
}