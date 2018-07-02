package com.cshep4.premierpredictor.controller

import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.PredictedMatch
import com.cshep4.premierpredictor.entity.MatchFactsEntity
import com.cshep4.premierpredictor.repository.dynamodb.MatchFactsRepository
import com.cshep4.premierpredictor.service.FixturesService
import com.cshep4.premierpredictor.service.UserScoreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.BAD_REQUEST
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
    lateinit var matchFactsRepository: MatchFactsRepository

    private fun doScoreUpdate(score: Boolean?): Boolean = score != null && score

    @PutMapping("/update")
    fun updateFixtures(@RequestParam("score") score: Boolean?) : ResponseEntity<List<Match>> {
        val fixtures = fixturesService.update()

        if (!fixtures.isEmpty() && doScoreUpdate(score)) {
            userScoreService.updateScores()
        }

        return when {
            fixtures.isEmpty() -> ResponseEntity.status(BAD_REQUEST).build()
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
    fun getUpcomingFixtures() : ResponseEntity<Map<LocalDate, List<Match>>> {
        val fixtures = fixturesService.retrieveAllUpcomingFixtures()

        return ResponseEntity.ok(fixtures)
    }

    @GetMapping("/facts")
    fun getFacts() : MatchFactsEntity? {
//        val url = "$API_URL?from_date=$FROM_DATE&to_date=$TO_DATE&comp_id=$COMP_ID&Authorization=$API_KEY"
//        val (req, res, result) = url.httpGet().responseString()
//
//        val matches = result.fold({ data ->
//            return@fold ObjectMapper().readValue(data, Array<MatchFacts>::class.java)
//        }, { _ ->
//            return@fold null
//        }) ?: return null
//
//        matches?.map { it.lastUpdated = LocalDateTime.now() }
//
//        val matchEntities = matches?.map { MatchFactsEntity.fromDto(it) }!!.toMutableList()
//
//        matchFactsRepository.saveAll(matchEntities)

//        val match = matchFactsRepository.findById("2378479").get()
//
//        match.commentary = Commentary()
//
//        matchFactsRepository.save(match)

        return matchFactsRepository.findById("2378479").get()
    }
}