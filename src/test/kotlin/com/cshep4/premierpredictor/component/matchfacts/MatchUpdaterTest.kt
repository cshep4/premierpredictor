package com.cshep4.premierpredictor.component.matchfacts

import com.cshep4.premierpredictor.component.api.ApiRequester
import com.cshep4.premierpredictor.component.time.Time
import com.cshep4.premierpredictor.data.api.live.commentary.Commentary
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.entity.MatchFactsEntity
import com.cshep4.premierpredictor.repository.dynamodb.MatchFactsRepository
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDateTime

@RunWith(MockitoJUnitRunner::class)
internal class MatchUpdaterTest {
    @Mock
    private lateinit var fixtureApiRequester: ApiRequester

    @Mock
    private lateinit var matchFactsRepository: MatchFactsRepository

    @Mock
    private lateinit var time: Time

    @InjectMocks
    private lateinit var matchUpdater: MatchUpdater

    @Test
    fun updateUpcomingMatchesWithLatestScores() {
    }

    @Test
    fun `'updateMatch' will return match from db if there is a problem with the API call`() {
        val currentlyStoredMatch = MatchFacts()

        whenever(fixtureApiRequester.retrieveMatch("1")).thenReturn(null)

        val result = matchUpdater.updateMatch(1, currentlyStoredMatch)

        assertThat(result, `is`(currentlyStoredMatch))
        verify(fixtureApiRequester).retrieveMatch("1")
        verify(time, times(0)).localDateTimeNow()
        verify(matchFactsRepository, times(0)).save(ArgumentMatchers.any(MatchFactsEntity::class.java))
    }

    @Test
    fun `'updateMatch' will retrieve the match from the api and update the db`() {
        val commentary = Commentary()
        val now = LocalDateTime.now().plusDays(1)
        val currentlyStoredMatch = MatchFacts(lastUpdated = LocalDateTime.now().minusSeconds(21), commentary = commentary)

        val matchFromApi = MatchFacts(lastUpdated = LocalDateTime.now())
        val expectedResult = MatchFacts(lastUpdated = now, commentary = commentary)

        whenever(fixtureApiRequester.retrieveMatch("1")).thenReturn(matchFromApi)
        whenever(time.localDateTimeNow()).thenReturn(now)

        val result = matchUpdater.updateMatch(1, currentlyStoredMatch)

        assertThat(result, `is`(expectedResult))
        verify(fixtureApiRequester).retrieveMatch("1")
        verify(time).localDateTimeNow()
        verify(matchFactsRepository).save(MatchFactsEntity.fromDto(expectedResult))
    }

}