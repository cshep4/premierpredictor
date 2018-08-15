package com.cshep4.premierpredictor.service.prediction

import com.cshep4.premierpredictor.component.prediction.PredictionCleaner
import com.cshep4.premierpredictor.data.DuplicateSummary
import com.cshep4.premierpredictor.enum.DuplicateSearch
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class PredictionCleanerServiceTest {
    @Mock
    private lateinit var predictionCleaner: PredictionCleaner

    @InjectMocks
    private lateinit var predictionCleanerService: PredictionCleanerService

    @Test
    fun `'removeDuplicatesIfAnyExist' will delete duplicates if they exist`() {
        val duplicates = listOf(Any())
        whenever(predictionCleaner.findDuplicates(DuplicateSearch.QUICK)).thenReturn(duplicates)

        val result = predictionCleanerService.removeDuplicatesIfAnyExist(DuplicateSearch.QUICK)

        val expectedResult = DuplicateSummary(exists = true, numDuplicates = duplicates.size, duplicates = duplicates)

        assertThat(result, `is`(expectedResult))
        verify(predictionCleaner, times(1)).deduplicate()
    }

    @Test
    fun `'removeDuplicatesIfAnyExist' will not delete duplicates if none exist`() {
        whenever(predictionCleaner.findDuplicates(DuplicateSearch.QUICK)).thenReturn(null)

        val result = predictionCleanerService.removeDuplicatesIfAnyExist(DuplicateSearch.QUICK)

        val expectedResult = DuplicateSummary(exists = false, numDuplicates = 0)

        assertThat(result, `is`(expectedResult))
        verify(predictionCleaner, times(0)).deduplicate()
    }

    @Test
    fun `'removeDuplicatesIfAnyExist' will not delete duplicates if none exist when empty list returned`() {
        whenever(predictionCleaner.findDuplicates(DuplicateSearch.QUICK)).thenReturn(listOf())

        val result = predictionCleanerService.removeDuplicatesIfAnyExist(DuplicateSearch.QUICK)

        val expectedResult = DuplicateSummary(exists = false, numDuplicates = 0, duplicates = emptyList<Any>())

        assertThat(result, `is`(expectedResult))
        verify(predictionCleaner, times(0)).deduplicate()
    }
}