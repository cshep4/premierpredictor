package com.cshep4.premierpredictor.controller

import com.cshep4.premierpredictor.component.leaguetable.LeagueTableCollector
import com.cshep4.premierpredictor.data.LeagueTable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/leagueTable")
class LeagueTableController {
    @Autowired
    lateinit var leagueTableCollector: LeagueTableCollector

    @GetMapping("/current")
    fun getCurrentLeagueTable() : ResponseEntity<LeagueTable> {
        val leagueTable = leagueTableCollector.getCurrentLeagueTable()

        return ResponseEntity.ok(leagueTable)
    }

    @GetMapping("/predicted/{id}")
    fun getPredictedLeagueTable(@PathVariable(value = "id") id: Long) : ResponseEntity<LeagueTable> {
        val leagueTable = leagueTableCollector.getPredictedLeagueTable(id)

        return ResponseEntity.ok(leagueTable)
    }
}