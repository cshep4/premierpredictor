package com.cshep4.premierpredictor.service.livematch

import com.cshep4.premierpredictor.component.matchfacts.CommentaryUpdater
import com.cshep4.premierpredictor.component.matchfacts.MatchUpdater
import com.cshep4.premierpredictor.constant.MatchConstants.REFRESH_RATE
import com.cshep4.premierpredictor.data.MatchPredictionSummary
import com.cshep4.premierpredictor.data.Prediction
import com.cshep4.premierpredictor.data.api.live.commentary.Commentary
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.entity.MatchFactsEntity
import com.cshep4.premierpredictor.repository.dynamodb.MatchFactsRepository
import com.cshep4.premierpredictor.service.prediction.MatchPredictionSummaryService
import com.cshep4.premierpredictor.service.prediction.PredictionsService
import com.nhaarman.mockito_kotlin.any
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
import java.time.LocalDateTime
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
    private lateinit var matchUpdater: MatchUpdater

    @Mock
    private lateinit var commentaryUpdater: CommentaryUpdater

    @Mock
    private lateinit var matchPredictionSummaryService: MatchPredictionSummaryService

    @Mock
    private lateinit var predictionsService: PredictionsService

    @InjectMocks
    private lateinit var liveMatchService: LiveMatchService

    @Test
    fun `'retrieveLiveMatchFacts' will retrieve currently stored match facts for specified id and return if no need for update`() {
        val commentary = Commentary(lastUpdated = LocalDateTime.now().minusSeconds(REFRESH_RATE - 5))
        val matchFacts = MatchFactsEntity(lastUpdated = LocalDateTime.now().minusSeconds(REFRESH_RATE - 5), commentary = commentary)

        whenever(matchFactsRepository.findById(ID)).thenReturn(Optional.of(matchFacts))

        val result = liveMatchService.retrieveLiveMatchFacts(ID)

        assertThat(result, `is`(matchFacts.toDto()))
        verify(matchUpdater, times(0)).retrieveMatchFromApi(any())
        verify(commentaryUpdater, times(0)).retrieveCommentaryFromApi(any())
        verify(matchFactsRepository).save(MatchFactsEntity.fromDto(result!!))
    }

    @Test
    fun `'retrieveLiveMatchFacts' will update the match facts if they need updating`() {
        val commentary = Commentary(lastUpdated = LocalDateTime.now().minusSeconds(REFRESH_RATE - 5))
        val matchFacts = MatchFactsEntity(lastUpdated = LocalDateTime.now().minusSeconds(REFRESH_RATE + 5), commentary = commentary)
        val updatedMatch = MatchFacts()

        whenever(matchFactsRepository.findById(ID)).thenReturn(Optional.of(matchFacts))
        whenever(matchUpdater.retrieveMatchFromApi(ID)).thenReturn(updatedMatch)

        val result = liveMatchService.retrieveLiveMatchFacts(ID)

        assertThat(result, `is`(updatedMatch))
        verify(matchUpdater, times(1)).retrieveMatchFromApi(any())
        verify(commentaryUpdater, times(0)).retrieveCommentaryFromApi(any())
        verify(matchFactsRepository).save(MatchFactsEntity.fromDto(result!!))
    }

    @Test
    fun `'retrieveLiveMatchFacts' will update the commentary if it needs updating`() {
        val commentary = Commentary(lastUpdated = LocalDateTime.now().minusSeconds(REFRESH_RATE + 5))
        val matchFacts = MatchFactsEntity(lastUpdated = LocalDateTime.now().minusSeconds(REFRESH_RATE - 5), commentary = commentary)
        val updatedCommentary = Commentary()

        val expectedResult = matchFacts.toDto()
        expectedResult.commentary = updatedCommentary

        whenever(matchFactsRepository.findById(ID)).thenReturn(Optional.of(matchFacts))
        whenever(commentaryUpdater.retrieveCommentaryFromApi(ID)).thenReturn(updatedCommentary)

        val result = liveMatchService.retrieveLiveMatchFacts(ID)

        assertThat(result, `is`(expectedResult))
        verify(matchUpdater, times(0)).retrieveMatchFromApi(any())
        verify(commentaryUpdater, times(1)).retrieveCommentaryFromApi(any())
        verify(matchFactsRepository).save(MatchFactsEntity.fromDto(result!!))
    }

    @Test
    fun `'retrieveLiveMatchFacts' will update both match facts and commentary if they need updating`() {
        val commentary = Commentary(lastUpdated = LocalDateTime.now().minusSeconds(REFRESH_RATE + 5))
        val matchFacts = MatchFactsEntity(lastUpdated = LocalDateTime.now().minusSeconds(REFRESH_RATE + 5), commentary = commentary)
        val updatedMatch = MatchFacts()
        val updatedCommentary = Commentary()

        whenever(matchFactsRepository.findById(ID)).thenReturn(Optional.of(matchFacts))
        whenever(matchUpdater.retrieveMatchFromApi(ID)).thenReturn(updatedMatch)
        whenever(commentaryUpdater.retrieveCommentaryFromApi(ID)).thenReturn(updatedCommentary)

        val result = liveMatchService.retrieveLiveMatchFacts(ID)

        assertThat(result, `is`(updatedMatch))
        assertThat(result!!.commentary, `is`(updatedCommentary))
        verify(matchUpdater, times(1)).retrieveMatchFromApi(any())
        verify(commentaryUpdater, times(1)).retrieveCommentaryFromApi(any())
        verify(matchFactsRepository).save(MatchFactsEntity.fromDto(result))
    }

    @Test
    fun `'retrieveLiveMatchFacts' will return current match facts if nothing returned from api even if they need updating`() {
        val commentary = Commentary(lastUpdated = LocalDateTime.now().minusSeconds(REFRESH_RATE - 5))
        val matchFacts = MatchFactsEntity(lastUpdated = LocalDateTime.now().minusSeconds(REFRESH_RATE + 5), commentary = commentary)

        whenever(matchFactsRepository.findById(ID)).thenReturn(Optional.of(matchFacts))
        whenever(matchUpdater.retrieveMatchFromApi(ID)).thenReturn(null)

        val result = liveMatchService.retrieveLiveMatchFacts(ID)

        assertThat(result, `is`(matchFacts.toDto()))
        verify(matchUpdater, times(1)).retrieveMatchFromApi(any())
        verify(commentaryUpdater, times(0)).retrieveCommentaryFromApi(any())
        verify(matchFactsRepository).save(MatchFactsEntity.fromDto(result!!))
    }

    @Test
    fun `'retrieveLiveMatchFacts' will return current commentary if nothing returned from api even if it needs updating`() {
        val commentary = Commentary(lastUpdated = LocalDateTime.now().minusSeconds(REFRESH_RATE + 5))
        val matchFacts = MatchFactsEntity(lastUpdated = LocalDateTime.now().minusSeconds(REFRESH_RATE - 5), commentary = commentary)

        whenever(matchFactsRepository.findById(ID)).thenReturn(Optional.of(matchFacts))
        whenever(commentaryUpdater.retrieveCommentaryFromApi(ID)).thenReturn(null)

        val result = liveMatchService.retrieveLiveMatchFacts(ID)

        assertThat(result, `is`(matchFacts.toDto()))
        verify(matchUpdater, times(0)).retrieveMatchFromApi(any())
        verify(commentaryUpdater, times(1)).retrieveCommentaryFromApi(any())
        verify(matchFactsRepository).save(MatchFactsEntity.fromDto(result!!))
    }

    @Test
    fun `'retrieveLiveMatchFacts' will return current match facts and commentary if nothing returned from api even if they need updating`() {
        val commentary = Commentary(lastUpdated = LocalDateTime.now().minusSeconds(REFRESH_RATE + 5))
        val matchFacts = MatchFactsEntity(lastUpdated = LocalDateTime.now().minusSeconds(REFRESH_RATE + 5), commentary = commentary)

        whenever(matchFactsRepository.findById(ID)).thenReturn(Optional.of(matchFacts))
        whenever(matchUpdater.retrieveMatchFromApi(ID)).thenReturn(null)
        whenever(commentaryUpdater.retrieveCommentaryFromApi(ID)).thenReturn(null)

        val result = liveMatchService.retrieveLiveMatchFacts(ID)

        assertThat(result, `is`(matchFacts.toDto()))
        assertThat(result!!.commentary, `is`(commentary))
        verify(matchUpdater, times(1)).retrieveMatchFromApi(any())
        verify(commentaryUpdater, times(1)).retrieveCommentaryFromApi(any())
        verify(matchFactsRepository).save(MatchFactsEntity.fromDto(result))
    }

    @Test
    fun `'retrieveLiveMatchFacts' will update the match facts if none are currently stored`() {
        val updatedMatch = MatchFacts()
        val commentary = Commentary()

        whenever(matchFactsRepository.findById(ID)).thenReturn(Optional.empty())
        whenever(matchUpdater.retrieveMatchFromApi(ID)).thenReturn(updatedMatch)
        whenever(commentaryUpdater.retrieveCommentaryFromApi(ID)).thenReturn(commentary)

        val result = liveMatchService.retrieveLiveMatchFacts(ID)

        assertThat(result, `is`(updatedMatch))
        assertThat(result!!.commentary, `is`(commentary))
        verify(matchUpdater, times(1)).retrieveMatchFromApi(any())
        verify(commentaryUpdater, times(1)).retrieveCommentaryFromApi(any())
        verify(matchFactsRepository).save(MatchFactsEntity.fromDto(result))
    }

    @Test
    fun `'retrieveLiveMatchFacts' will return current commentary if none are currently stored`() {
        val matchFacts = MatchFactsEntity(lastUpdated = LocalDateTime.now().minusSeconds(REFRESH_RATE - 5), commentary = null)
        val commentary = Commentary()

        val expectedResult = matchFacts.toDto()
        expectedResult.commentary = commentary

        whenever(matchFactsRepository.findById(ID)).thenReturn(Optional.of(matchFacts))
        whenever(commentaryUpdater.retrieveCommentaryFromApi(ID)).thenReturn(commentary)

        val result = liveMatchService.retrieveLiveMatchFacts(ID)

        assertThat(result, `is`(expectedResult))
        assertThat(result!!.commentary, `is`(commentary))
        verify(matchUpdater, times(0)).retrieveMatchFromApi(any())
        verify(commentaryUpdater, times(1)).retrieveCommentaryFromApi(any())
        verify(matchFactsRepository).save(MatchFactsEntity.fromDto(result))
    }

    @Test
    fun `'retrieveMatchSummary' will retrieve matchFacts, predictionSummary and match prediction and return`() {
        val commentary = Commentary(lastUpdated = LocalDateTime.now().minusSeconds(REFRESH_RATE - 5))
        val matchFacts = MatchFactsEntity(lastUpdated = LocalDateTime.now().minusSeconds(REFRESH_RATE - 5), commentary = commentary)

        whenever(matchFactsRepository.findById(ID)).thenReturn(Optional.of(matchFacts))

        val prediction = Prediction()
        whenever(predictionsService.retrievePredictionByUserIdForMatch(LONG_ID, LONG_ID)).thenReturn(prediction)

        val matchPredictionSummary = MatchPredictionSummary()
        whenever(matchPredictionSummaryService.retrieveMatchPredictionSummary(ID)).thenReturn(matchPredictionSummary)

        val result = liveMatchService.retrieveMatchSummary(ID, ID)

        assertThat(result!!.match, `is`(matchFacts.toDto()))
        assertThat(result.prediction, `is`(prediction))
        assertThat(result.predictionSummary, `is`(matchPredictionSummary))
    }
}