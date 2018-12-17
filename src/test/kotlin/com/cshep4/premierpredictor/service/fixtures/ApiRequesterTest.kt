package com.cshep4.premierpredictor.service.fixtures

import com.cshep4.premierpredictor.component.api.ApiRequester
import com.cshep4.premierpredictor.constant.APIConstants.SENDER_EMAIL
import com.cshep4.premierpredictor.data.api.live.commentary.Commentary
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.email.Email
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.core.Client
import com.github.kittinunf.fuel.core.FuelManager
import com.nhaarman.mockito_kotlin.verify
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.hamcrest.CoreMatchers.`is` as Is

@RunWith(MockitoJUnitRunner::class)
internal class ApiRequesterTest {
    companion object {
        const val MATCHES_SUBJECT = "API call being made => MATCHES"
        const val MATCH_SUBJECT = "API call being made => MATCH - Id: 1"
        const val COMMENTARY_SUBJECT = "API call being made => COMMENTARY - Id: 1"
    }

    @Mock
    private lateinit var email: Email

    @InjectMocks
    private lateinit var apiRequester: ApiRequester

    private lateinit var client: Client

    @Before
    fun init() {
        client = mockk()
        FuelManager.instance.client = client
    }

    @Test
    fun `'retrieveFixtures' parses API result and returns object, sends email notifying that an API call is being made`() {
        val matches = listOf(MatchFacts(localTeamScore = "", visitorTeamScore = ""), MatchFacts(localTeamScore = "", visitorTeamScore = ""))
        val jsonResponse = ObjectMapper().writeValueAsString(matches)

        every { client.executeRequest(any()).httpStatusCode } returns 200
        every { client.executeRequest(any()).httpResponseMessage } returns "OK"
        every { client.executeRequest(any()).data } returns jsonResponse.toByteArray()

        val fixturesApiResult = apiRequester.retrieveFixtures()

        verify(email).send(SENDER_EMAIL, MATCHES_SUBJECT, MATCHES_SUBJECT)
        assertThat(fixturesApiResult, Is(matches))
    }

    @Test
    fun `'retrieveFixtures' returns empty list if API does not return OK, sends email notifying that an API call is being made`() {
        every { client.executeRequest(any()).httpStatusCode } returns 500
        every { client.executeRequest(any()).httpResponseMessage } returns "Internal Server Error"
        every { client.executeRequest(any()).data } returns "".toByteArray()

        val fixturesApiResult = apiRequester.retrieveFixtures()

        verify(email).send(SENDER_EMAIL, MATCHES_SUBJECT, MATCHES_SUBJECT)
        assertThat(fixturesApiResult, Is(emptyList()))
    }

    @Test
    fun `'retrieveCommentary' retrieves commentary and returns in object form, sends email notifying that an API call is being made`() {
        val commentary = Commentary()
        val jsonResponse = ObjectMapper().writeValueAsString(commentary)

        every { client.executeRequest(any()).httpStatusCode } returns 200
        every { client.executeRequest(any()).httpResponseMessage } returns "OK"
        every { client.executeRequest(any()).data } returns jsonResponse.toByteArray()

        val fixturesApiResult = apiRequester.retrieveCommentary("1")

        verify(email).send(SENDER_EMAIL, COMMENTARY_SUBJECT, COMMENTARY_SUBJECT)
        assertThat(fixturesApiResult, Is(commentary))
    }

    @Test
    fun `'retrieveCommentary' returns null if commentary is not found, sends email notifying that an API call is being made`() {
        val json = "{ \"status\": \"error\",\"message\": \"We did not find commentaries for the provided match\",\"code\": 404 }"

        every { client.executeRequest(any()).httpStatusCode } returns 404
        every { client.executeRequest(any()).httpResponseMessage } returns "Not Found"
        every { client.executeRequest(any()).data } returns json.toByteArray()

        val fixturesApiResult = apiRequester.retrieveCommentary("1")

        verify(email).send(SENDER_EMAIL, COMMENTARY_SUBJECT, COMMENTARY_SUBJECT)
        assertThat(fixturesApiResult, Is(nullValue()))
    }

    @Test
    fun `'retrieveCommentary' returns null if there is an error, sends email notifying that an API call is being made`() {
        every { client.executeRequest(any()).httpStatusCode } returns 500
        every { client.executeRequest(any()).httpResponseMessage } returns "Internal Server Error"
        every { client.executeRequest(any()).data } returns "".toByteArray()

        val fixturesApiResult = apiRequester.retrieveCommentary("1")

        verify(email).send(SENDER_EMAIL, COMMENTARY_SUBJECT, COMMENTARY_SUBJECT)
        assertThat(fixturesApiResult, Is(nullValue()))
    }

    @Test
    fun `'retrieveMatch' parses API result and returns object, sends email notifying that an API call is being made`() {
        val match = MatchFacts()
        val jsonResponse = ObjectMapper().writeValueAsString(match)

        every { client.executeRequest(any()).httpStatusCode } returns 200
        every { client.executeRequest(any()).httpResponseMessage } returns "OK"
        every { client.executeRequest(any()).data } returns jsonResponse.toByteArray()

        val apiResult = apiRequester.retrieveMatch("1")

        verify(email).send(SENDER_EMAIL, MATCH_SUBJECT, MATCH_SUBJECT)
        assertThat(apiResult, Is(match))
    }

    @Test
    fun `'retrieveMatch' returns null if API does not return OK, sends email notifying that an API call is being made`() {
        every { client.executeRequest(any()).httpStatusCode } returns 500
        every { client.executeRequest(any()).httpResponseMessage } returns "Internal Server Error"
        every { client.executeRequest(any()).data } returns "".toByteArray()

        val fixturesApiResult = apiRequester.retrieveMatch("1")

        verify(email).send(SENDER_EMAIL, MATCH_SUBJECT, MATCH_SUBJECT)
        assertThat(fixturesApiResult, Is(nullValue()))
    }
}