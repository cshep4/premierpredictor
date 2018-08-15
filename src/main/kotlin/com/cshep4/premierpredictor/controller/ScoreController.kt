package com.cshep4.premierpredictor.controller

import com.cshep4.premierpredictor.data.UserRank
import com.cshep4.premierpredictor.service.user.ScoreService
import com.cshep4.premierpredictor.service.user.UserScoreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/score")
class ScoreController {
    @Autowired
    lateinit var scoreService: ScoreService

    @Autowired
    lateinit var userScoreService: UserScoreService

    @GetMapping("/scoreAndRank/{id}")
    fun getScoreAndRank(@PathVariable(value = "id") id: Long) : ResponseEntity<UserRank> {
        val userRank = scoreService.retrieveScoreAndRank(id)

        return ResponseEntity.ok(userRank)
    }

    @PutMapping("/update")
    fun updateScores() : ResponseEntity<Int> {
        val updatedUsers = userScoreService.updateScores().size

        return ResponseEntity.ok(updatedUsers)
    }
}