package com.cshep4.premierpredictor.service.fixtures

import com.cshep4.premierpredictor.component.api.ApiRequester
import com.cshep4.premierpredictor.data.api.live.commentary.Commentary
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.core.Client
import com.github.kittinunf.fuel.core.FuelManager
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as Is

internal class ApiRequesterTest {
    private val fixturesApiService = ApiRequester()

    private lateinit var client: Client

    @Before
    fun init() {
        client = mockk()
        FuelManager.instance.client = client
    }

    @Test
    fun `'retrieveFixtures' parses API result and returns object`() {
        val matches = listOf(MatchFacts(), MatchFacts())
        val jsonResponse = ObjectMapper().writeValueAsString(matches)

        every { client.executeRequest(any()).httpStatusCode } returns 200
        every { client.executeRequest(any()).httpResponseMessage } returns "OK"
        every { client.executeRequest(any()).data } returns jsonResponse.toByteArray()

        val fixturesApiResult = fixturesApiService.retrieveFixtures()

        assertThat(fixturesApiResult, Is(matches))
    }

    @Test
    fun `'retrieveFixtures' returns empty list if API does not return OK`() {
        every { client.executeRequest(any()).httpStatusCode } returns 500
        every { client.executeRequest(any()).httpResponseMessage } returns "Internal Server Error"
        every { client.executeRequest(any()).data } returns "".toByteArray()

        val fixturesApiResult = fixturesApiService.retrieveFixtures()

        assertThat(fixturesApiResult, Is(emptyList()))
    }

    @Test
    fun `'retrieveCommentary' retrieves commentary and returns in object form`() {
        val commentary = Commentary()
        val jsonResponse = ObjectMapper().writeValueAsString(commentary)

        every { client.executeRequest(any()).httpStatusCode } returns 200
        every { client.executeRequest(any()).httpResponseMessage } returns "OK"
        every { client.executeRequest(any()).data } returns jsonResponse.toByteArray()

        val fixturesApiResult = fixturesApiService.retrieveCommentary("1")

        assertThat(fixturesApiResult, Is(commentary))
    }

    @Test
    fun `'retrieveCommentary' returns null if commentary is not found`() {
        val json = "{ \"status\": \"error\",\"message\": \"We did not find commentaries for the provided match\",\"code\": 404 }"

        every { client.executeRequest(any()).httpStatusCode } returns 404
        every { client.executeRequest(any()).httpResponseMessage } returns "Not Found"
        every { client.executeRequest(any()).data } returns json.toByteArray()

        val fixturesApiResult = fixturesApiService.retrieveCommentary("1")

        assertThat(fixturesApiResult, Is(nullValue()))
    }

    @Test
    fun `'retrieveCommentary' returns null if there is an error`() {
        every { client.executeRequest(any()).httpStatusCode } returns 500
        every { client.executeRequest(any()).httpResponseMessage } returns "Internal Server Error"
        every { client.executeRequest(any()).data } returns "".toByteArray()

        val fixturesApiResult = fixturesApiService.retrieveCommentary("1")

        assertThat(fixturesApiResult, Is(nullValue()))
    }
}