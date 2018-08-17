package com.cshep4.premierpredictor.controller

import com.cshep4.premierpredictor.data.TeamForm
import com.cshep4.premierpredictor.service.team.TeamService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/team")
class TeamController {
    @Autowired
    private lateinit var teamService: TeamService

    @GetMapping("/form")
    fun getRecentForms() : ResponseEntity<Map<String, TeamForm>> {
        val forms = teamService.retrieveRecentForms()

        return ResponseEntity.ok(forms)
    }
}