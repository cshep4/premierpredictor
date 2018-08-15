package com.cshep4.premierpredictor.service.prediction

import com.cshep4.premierpredictor.component.prediction.PredictionCleaner
import com.cshep4.premierpredictor.enum.DuplicateSearch
import com.nhaarman.mockito_kotlin.verify
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class PredictionCleanerServiceParametrisedTest {
    @Mock
    private lateinit var predictionCleaner: PredictionCleaner

    @InjectMocks
    private lateinit var predictionCleanerService: PredictionCleanerService

    @ParameterizedTest
    @EnumSource(DuplicateSearch::class)
    fun `'removeDuplicatesIfAnyExist' will retrieve duplicates using the specified search mode`(mode: DuplicateSearch) {
        MockitoAnnotations.initMocks(this)
        predictionCleanerService.removeDuplicatesIfAnyExist(mode)

        verify(predictionCleaner).findDuplicates(mode)
    }
}