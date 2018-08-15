package com.cshep4.premierpredictor.component.prediction

import com.cshep4.premierpredictor.constant.Queries.QUERY_REMOVE_DUPLICATE_PREDICTIONS
import com.cshep4.premierpredictor.enum.DuplicateSearch
import com.cshep4.premierpredictor.repository.sql.PredictionsRepository
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.EntityTransaction
import javax.persistence.Query

@RunWith(MockitoJUnitRunner::class)
internal class PredictionCleanerTest {
    @Mock
    private lateinit var entityManagerFactory: EntityManagerFactory

    @Mock
    private lateinit var predictionsRepository: PredictionsRepository

    @Mock
    private lateinit var entityManager: EntityManager

    @Mock
    private lateinit var query: Query

    @Mock
    private lateinit var transaction: EntityTransaction

    @InjectMocks
    private lateinit var predictionCleaner: PredictionCleaner

    @Before
    fun init() {
        whenever(entityManagerFactory.createEntityManager()).thenReturn(entityManager)
        whenever(entityManager.createNativeQuery(QUERY_REMOVE_DUPLICATE_PREDICTIONS)).thenReturn(query)
        whenever(entityManager.transaction).thenReturn(transaction)
    }

    @Test
    fun `'deduplicate' creates executes db query to remove duplicate predictions`() {
        whenever(query.executeUpdate()).thenReturn(0)

        predictionCleaner.deduplicate()

        verify(transaction).begin()
        verify(entityManager).createNativeQuery(QUERY_REMOVE_DUPLICATE_PREDICTIONS)
        verify(query).executeUpdate()
        verify(transaction).commit()
        verify(entityManager).close()
    }

    @Test
    fun `'deduplicate' returns true if some duplicates are removed`() {
        whenever(query.executeUpdate()).thenReturn(1)

        val result = predictionCleaner.deduplicate()

        assertThat(result, `is`(true))
    }

    @Test
    fun `'deduplicate' returns false if none are removed`() {
        whenever(query.executeUpdate()).thenReturn(0)

        val result = predictionCleaner.deduplicate()

        assertThat(result, `is`(false))
    }

    @Test
    fun `'findDuplicates' will search for duplicates using correct query - QUICK`() {
        val queryResult = listOf(Any())
        whenever(predictionsRepository.findDuplicatesQuick()).thenReturn(queryResult)

        val result = predictionCleaner.findDuplicates(DuplicateSearch.QUICK)

        assertThat(result, `is`(queryResult))
        verify(predictionsRepository).findDuplicatesQuick()
        verify(predictionsRepository, times(0)).findDuplicatesThorough()
        verify(predictionsRepository, times(0)).findDuplicatesMismatchedScores()
    }

    @Test
    fun `'findDuplicates' will search for duplicates using correct query - THOROUGH`() {
        val queryResult = listOf(Any())
        whenever(predictionsRepository.findDuplicatesThorough()).thenReturn(queryResult)

        val result = predictionCleaner.findDuplicates(DuplicateSearch.THOROUGH)

        assertThat(result, `is`(queryResult))
        verify(predictionsRepository, times(0)).findDuplicatesQuick()
        verify(predictionsRepository).findDuplicatesThorough()
        verify(predictionsRepository, times(0)).findDuplicatesMismatchedScores()
    }

    @Test
    fun `'findDuplicates' will search for duplicates using correct query - MISMATCHED`() {
        val queryResult = listOf(Any())
        whenever(predictionsRepository.findDuplicatesMismatchedScores()).thenReturn(queryResult)

        val result = predictionCleaner.findDuplicates(DuplicateSearch.MISMATCHED)

        assertThat(result, `is`(queryResult))
        verify(predictionsRepository, times(0)).findDuplicatesQuick()
        verify(predictionsRepository, times(0)).findDuplicatesThorough()
        verify(predictionsRepository).findDuplicatesMismatchedScores()
    }
}