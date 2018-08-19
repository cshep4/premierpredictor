package com.cshep4.premierpredictor.component.api

import com.cshep4.premierpredictor.constant.APIConstants.API_KEY
import com.cshep4.premierpredictor.constant.APIConstants.API_URL
import com.cshep4.premierpredictor.constant.APIConstants.API_URL_COMMENTARY
import com.cshep4.premierpredictor.constant.APIConstants.COMP_ID
import com.cshep4.premierpredictor.constant.APIConstants.FROM_DATE
import com.cshep4.premierpredictor.constant.APIConstants.TO_DATE
import com.cshep4.premierpredictor.data.api.live.commentary.Commentary
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.httpGet
import org.springframework.stereotype.Component

@Component
class ApiRequester {
    fun retrieveFixtures(): List<MatchFacts> {
        val url = "$API_URL?from_date=$FROM_DATE&to_date=$TO_DATE&comp_id=$COMP_ID&Authorization=$API_KEY"
        val (_, _, result) = url.httpGet().responseString()

        return result.fold({ data ->
            return@fold ObjectMapper().readValue(data, Array<MatchFacts>::class.java).toList().map { it.toSantisedMatchFacts() }
        }, {
            return@fold emptyList()
        })
    }

    fun retrieveCommentary(id: String): Commentary? {
        val url = "$API_URL_COMMENTARY$id?Authorization=$API_KEY"
        val (_, _, result) = url.httpGet().responseString()

        return result.fold({ data ->
            return@fold ObjectMapper().readValue(data, Commentary::class.java)
        }, {
            return@fold null
        })
    }

    fun retrieveMatch(id: String): MatchFacts? {
        val url = "$API_URL$id?Authorization=$API_KEY"
        val (_, _, result) = url.httpGet().responseString()

        return result.fold({ data ->
            return@fold ObjectMapper().readValue(data, MatchFacts::class.java)
        }, {
            return@fold null
        })
    }

}