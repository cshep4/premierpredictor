package com.cshep4.premierpredictor.controller

import com.cshep4.premierpredictor.data.*
import com.cshep4.premierpredictor.service.StandingsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/standings")
class StandingsController {
    @Autowired
    lateinit var standingsService: StandingsService

    @GetMapping("/userLeagues/{id}")
    fun getUsersLeagueList(@PathVariable(value = "id") id: Long) : ResponseEntity<StandingsOverview> {
        val standingsOverview = standingsService.retrieveStandingsOverview(id)

        return ResponseEntity.ok(standingsOverview)
    }

    @PostMapping("/join")
    fun joinUserLeague(@RequestBody userLeague: UserLeague) : ResponseEntity<UserLeagueOverview> {
        val userLeagueOverview = standingsService.joinLeague(userLeague)


        return when (userLeagueOverview) {
            null -> ResponseEntity.status(NOT_FOUND).build()
            else -> ResponseEntity.status(OK).body(userLeagueOverview)
        }
    }

    @PostMapping("/add")
    fun addUserLeague(@RequestBody addLeague: AddLeague) : ResponseEntity<League> {
        val league = standingsService.addLeague(addLeague.name, addLeague.userId)

        return ResponseEntity.status(CREATED).body(league)
    }

    @PostMapping("/leave")
    fun leaveUserLeague(@RequestBody userLeague: UserLeague) : ResponseEntity<UserLeagueOverview> {
        standingsService.leaveLeague(userLeague)
        return ResponseEntity.status(OK).build()
    }

    @PutMapping("/rename")
    fun renameLeague(@RequestBody league: League) : ResponseEntity<League> {
        val updatedLeague = standingsService.renameLeague(league)

        return ResponseEntity.status(OK).body(updatedLeague)
    }

    @GetMapping("/league/{pin}")
    fun getLeagueTable(@PathVariable(value = "pin") pin: Long) : ResponseEntity<List<LeagueTableUser>> {
        val table = standingsService.retrieveLeagueTable(pin)

        return ResponseEntity.ok(table)
    }

    @GetMapping("/overall")
    fun getOverallLeagueTable() : ResponseEntity<List<LeagueTableUser>> {
        val table = standingsService.retrieveOverallLeagueTable()

        return ResponseEntity.ok(table)
    }
}