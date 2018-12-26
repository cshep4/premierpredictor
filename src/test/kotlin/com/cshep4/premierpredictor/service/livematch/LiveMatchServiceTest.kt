package com.cshep4.premierpredictor.service.livematch

import com.cshep4.premierpredictor.data.MatchPredictionSummary
import com.cshep4.premierpredictor.data.Prediction
import com.cshep4.premierpredictor.data.api.live.commentary.Commentary
import com.cshep4.premierpredictor.entity.MatchFactsEntity
import com.cshep4.premierpredictor.repository.dynamodb.MatchFactsRepository
import com.cshep4.premierpredictor.service.prediction.MatchPredictionSummaryService
import com.cshep4.premierpredictor.service.prediction.PredictionsService
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
internal class LiveMatchServiceTest {
    companion object {
        const val ID = "1"
        const val LONG_ID = 1L
    }

    @Mock
    private lateinit var matchFactsRepository: MatchFactsRepository

    @Mock
    private lateinit var matchPredictionSummaryService: MatchPredictionSummaryService

    @Mock
    private lateinit var predictionsService: PredictionsService

//    @Mock
//    private lateinit var teamService: TeamService

    @InjectMocks
    private lateinit var liveMatchService: LiveMatchService

    @Test
    fun `'retrieveLiveMatchFacts' will retrieve currently stored match facts for specified id`() {
        val matchFacts = MatchFactsEntity(commentary = Commentary())

        whenever(matchFactsRepository.findById(ID)).thenReturn(Optional.of(matchFacts))

        val result = liveMatchService.retrieveLiveMatchFacts(ID)

        assertThat(result, `is`(matchFacts.toDto()))
    }

    @Test
    fun `'retrieveMatchSummary' will retrieve matchFacts, predictionSummary, match prediction and forms and return`() {
        val matchFacts = MatchFactsEntity(commentary = Commentary())

        whenever(matchFactsRepository.findById(ID)).thenReturn(Optional.of(matchFacts))

        val prediction = Prediction()
        whenever(predictionsService.retrievePredictionByUserIdForMatch(LONG_ID, LONG_ID)).thenReturn(prediction)

        val matchPredictionSummary = MatchPredictionSummary()
        whenever(matchPredictionSummaryService.retrieveMatchPredictionSummary(ID)).thenReturn(matchPredictionSummary)

//        val forms = mapOf(Pair("Team 1", TeamForm()))
//        whenever(teamService.retrieveRecentForms()).thenReturn(forms)

        val result = liveMatchService.retrieveMatchSummary(ID, ID)

        assertThat(result!!.match, `is`(matchFacts.toDto()))
        assertThat(result.prediction, `is`(prediction))
        assertThat(result.predictionSummary, `is`(matchPredictionSummary))
//        assertThat(result.forms, `is`(forms))
    }
}