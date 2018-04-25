package com.cshep4.wcpredictor.controller

import com.cshep4.wcpredictor.data.UserRank
import com.cshep4.wcpredictor.service.ScoreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/score")
class ScoreController {
    @Autowired
    lateinit var scoreService: ScoreService

    @GetMapping("/scoreAndRank/{id}")
    fun getScoreAndRank(@PathVariable(value = "id") id: Long) : ResponseEntity<UserRank> {
        val userRank = scoreService.retrieveScoreAndRank(id)

        return ResponseEntity.ok(userRank)
    }
}