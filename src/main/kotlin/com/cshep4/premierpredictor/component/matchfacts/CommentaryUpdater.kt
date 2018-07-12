package com.cshep4.premierpredictor.component.matchfacts

import com.cshep4.premierpredictor.component.api.ApiRequester
import com.cshep4.premierpredictor.component.time.Time
import com.cshep4.premierpredictor.data.api.live.commentary.Commentary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CommentaryUpdater {
    @Autowired
    private lateinit var fixtureApiRequester: ApiRequester

    @Autowired
    private lateinit var time: Time

    fun retrieveCommentaryFromApi(id: String): Commentary? {
        val apiResult = fixtureApiRequester.retrieveCommentary(id) ?: return null
        apiResult.lastUpdated = time.localDateTimeNow()

        return apiResult
    }
}