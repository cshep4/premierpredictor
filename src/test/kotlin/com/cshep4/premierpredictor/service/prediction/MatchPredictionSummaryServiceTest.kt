package com.cshep4.premierpredictor.service.prediction

import com.cshep4.premierpredictor.entity.MatchPredictionSummaryEntity
import com.cshep4.premierpredictor.repository.sql.MatchPredictionSummaryRepository
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class MatchPredictionSummaryServiceTest {
    @Mock
    private lateinit var matchPredictionSummaryRepository: MatchPredictionSummaryRepository

    @InjectMocks
    private lateinit var matchPredictionSummaryService: MatchPredictionSummaryService

    @Test
    fun `'retrieveMatchPredictionSummary' retrieves prediction summary`() {
        val summaryEntity = MatchPredictionSummaryEntity()
        val summary = summaryEntity.toDto()

        whenever(matchPredictionSummaryRepository.getPredictionSummary(1)).thenReturn(summaryEntity)

        val result = matchPredictionSummaryService.retrieveMatchPredictionSummary("1")

        assertThat(result, `is`(summary))
    }

}