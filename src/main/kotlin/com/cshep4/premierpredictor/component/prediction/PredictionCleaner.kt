package com.cshep4.premierpredictor.component.prediction

import com.cshep4.premierpredictor.constant.Queries.QUERY_REMOVE_DUPLICATE_PREDICTIONS
import com.cshep4.premierpredictor.enum.DuplicateSearch
import com.cshep4.premierpredictor.enum.DuplicateSearch.*
import com.cshep4.premierpredictor.repository.sql.PredictionsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.EntityManagerFactory


@Component
class PredictionCleaner {
    @Autowired
    private lateinit var entityManagerFactory: EntityManagerFactory

    @Autowired
    private lateinit var predictionsRepository: PredictionsRepository

    fun deduplicate(): Boolean {
        val entityManager = entityManagerFactory.createEntityManager()

        entityManager.transaction.begin()

        val query = entityManager.createNativeQuery(QUERY_REMOVE_DUPLICATE_PREDICTIONS)
        val deletedRows = query.executeUpdate()

        entityManager.transaction.commit()
        entityManager.close()

        return deletedRows > 0
    }

    fun findDuplicates(mode: DuplicateSearch): List<Any>? {
        return when (mode) {
            QUICK -> predictionsRepository.findDuplicatesQuick()
            THOROUGH -> predictionsRepository.findDuplicatesThorough()
            MISMATCHED -> predictionsRepository.findDuplicatesMismatchedScores()
        }
    }
}