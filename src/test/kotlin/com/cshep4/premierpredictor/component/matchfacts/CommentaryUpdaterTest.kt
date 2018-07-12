package com.cshep4.premierpredictor.component.matchfacts

import com.cshep4.premierpredictor.component.api.ApiRequester
import com.cshep4.premierpredictor.component.time.Time
import com.cshep4.premierpredictor.data.api.live.commentary.Commentary
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDateTime

@RunWith(MockitoJUnitRunner::class)
internal class CommentaryUpdaterTest {
    @Mock
    private lateinit var fixtureApiRequester: ApiRequester

    @Mock
    private lateinit var time: Time

    @InjectMocks
    private lateinit var commentaryUpdater: CommentaryUpdater

    @Test
    fun `'retrieveCommentaryFromApi' will retrieve the commentary from the api`() {
        val now = LocalDateTime.now().plusDays(1)

        val apiResult = Commentary()
        val expectedResult = Commentary(lastUpdated = now)

        whenever(fixtureApiRequester.retrieveCommentary("1")).thenReturn(apiResult)
        whenever(time.localDateTimeNow()).thenReturn(now)

        val result = commentaryUpdater.retrieveCommentaryFromApi("1")

        assertThat(result, `is`(expectedResult))
        verify(fixtureApiRequester).retrieveCommentary("1")
        verify(time).localDateTimeNow()
    }

    @Test
    fun `'retrieveCommentaryFromApi' will return null if nothing is retrieved from the api`() {
        whenever(fixtureApiRequester.retrieveCommentary("1")).thenReturn(null)

        val result = commentaryUpdater.retrieveCommentaryFromApi("1")

        assertThat(result, `is`(nullValue()))
        verify(fixtureApiRequester).retrieveCommentary("1")
        verify(time, times(0)).localDateTimeNow()
    }

}