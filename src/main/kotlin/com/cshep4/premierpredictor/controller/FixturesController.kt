package com.cshep4.premierpredictor.controller

import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.PredictedMatch
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.service.fixtures.FixturesService
import com.cshep4.premierpredictor.service.fixtures.ResultsService
import com.cshep4.premierpredictor.service.user.UserScoreService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/fixtures")
class FixturesController {
    @Autowired
    lateinit var fixturesService: FixturesService

    @Autowired
    lateinit var userScoreService: UserScoreService

    @Autowired
    lateinit var resultsService: ResultsService

    private fun doScoreUpdate(score: Boolean?): Boolean = score != null && score

    @PutMapping("/update")
    fun updateFixtures(@RequestParam("score") score: Boolean?) : ResponseEntity<List<Match>> {
        val fixtures = resultsService.update()

        GlobalScope.launch {
            if (!fixtures.isEmpty() && doScoreUpdate(score)) {
                userScoreService.updateScores()
            }
        }

        return when {
            fixtures.isEmpty() -> ResponseEntity.status(INTERNAL_SERVER_ERROR).build()
            else -> ResponseEntity.ok(fixtures)
        }
    }

    @GetMapping
    fun getAllMatches() : ResponseEntity<List<Match>> {
        val matches = fixturesService.retrieveAllMatches()

        return when {
            matches.isEmpty() -> ResponseEntity.notFound().build()
            else -> ResponseEntity.ok(matches)
        }
    }

    @GetMapping("/predicted/{id}")
    fun getAllPredictedMatches(@PathVariable(value = "id") id: Long) : ResponseEntity<List<PredictedMatch>> {
        val matches = fixturesService.retrieveAllMatchesWithPredictions(id)

        return when {
            matches.isEmpty() -> ResponseEntity.notFound().build()
            else -> ResponseEntity.ok(matches)
        }
    }

    @GetMapping("/upcoming")
    fun getUpcomingFixtures() : ResponseEntity<Map<LocalDate, List<MatchFacts>>> {
        val fixtures = fixturesService.retrieveAllUpcomingFixtures()

        return ResponseEntity.ok(fixtures)
    }

    @GetMapping("/liveScore/{id}")
    fun getLiveScoreForMatch(@PathVariable(value = "id") id: Long) : ResponseEntity<MatchFacts> {
        val match = fixturesService.retrieveLiveScoreForMatch(id.toString()) ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(match)
    }
}