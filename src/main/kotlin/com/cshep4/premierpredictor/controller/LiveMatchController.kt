package com.cshep4.premierpredictor.controller

import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.service.livematch.LiveMatchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/live")
class LiveMatchController {
    @Autowired
    private lateinit var liveMatchService: LiveMatchService

    @GetMapping("/{id}")
    fun getLiveMatchFacts(@PathVariable(value = "id") id: Long) : ResponseEntity<MatchFacts> {
        val matchFacts = liveMatchService.retrieveLiveMatchFacts(id)

        return when (matchFacts) {
            null -> ResponseEntity.notFound().build()
            else -> ResponseEntity.ok(matchFacts)
        }
    }
}