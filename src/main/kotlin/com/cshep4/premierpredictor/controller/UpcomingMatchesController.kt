package com.cshep4.premierpredictor.controller

import com.cshep4.premierpredictor.constant.MatchConstants.UPCOMING_SUBSCRIPTION
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller

@Controller
class UpcomingMatchesController {
    @Autowired
    private lateinit var template: SimpMessagingTemplate

    @MessageMapping
    fun test() {
        this.template.convertAndSend(UPCOMING_SUBSCRIPTION, MatchFacts())
    }
}