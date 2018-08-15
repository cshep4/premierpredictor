package com.cshep4.premierpredictor.service.prediction

import com.cshep4.premierpredictor.component.prediction.PredictionCleaner
import com.cshep4.premierpredictor.data.DuplicateSummary
import com.cshep4.premierpredictor.enum.DuplicateSearch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PredictionCleanerService {
    @Autowired
    private lateinit var predictionCleaner: PredictionCleaner

    fun removeDuplicatesIfAnyExist(mode: DuplicateSearch) : DuplicateSummary {
        val duplicates = predictionCleaner.findDuplicates(mode) ?: return DuplicateSummary(exists = false, numDuplicates = 0)

        if (!duplicates.isEmpty()) {
            predictionCleaner.deduplicate()
        }

        return DuplicateSummary(exists = !duplicates.isEmpty(), numDuplicates = duplicates.size, duplicates = duplicates)
    }
}