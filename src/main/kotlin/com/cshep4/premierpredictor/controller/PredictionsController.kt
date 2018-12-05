package com.cshep4.premierpredictor.controller

import com.cshep4.premierpredictor.data.DuplicateSummary
import com.cshep4.premierpredictor.data.Prediction
import com.cshep4.premierpredictor.data.PredictionSummary
import com.cshep4.premierpredictor.data.PredictorData
import com.cshep4.premierpredictor.enum.DuplicateSearch
import com.cshep4.premierpredictor.enum.DuplicateSearch.QUICK
import com.cshep4.premierpredictor.service.prediction.PredictionCleanerService
import com.cshep4.premierpredictor.service.prediction.PredictionsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/predictions")
class PredictionsController {
    @Autowired
    private lateinit var predictionsService: PredictionsService

    @Autowired
    private lateinit var predictionCleanerService: PredictionCleanerService

    @PostMapping("/update")
    fun updatePredictions(@RequestBody predictions: List<Prediction>) : ResponseEntity<List<Prediction>> {
        val updatedPredictions = predictionsService.savePredictions(predictions)

        return when {
            updatedPredictions.isEmpty() -> ResponseEntity.badRequest().build()
            else -> ResponseEntity.ok(updatedPredictions)
        }
    }

    @GetMapping("/user/{id}")
    fun getPredictionsByUserId(@PathVariable(value = "id") id: Long) : ResponseEntity<List<Prediction>> {
        val predictions = predictionsService.retrievePredictionsByUserId(id)

        return when {
            predictions.isEmpty() -> ResponseEntity.notFound().build()
            else -> ResponseEntity.ok(predictions)
        }
    }

    @GetMapping("/summary/{id}")
    fun getPredictionsSummaryByUserId(@PathVariable(value = "id") id: Long) : ResponseEntity<PredictionSummary> {
        val predictions = predictionsService.retrievePredictionsSummaryByUserId(id)

        return ResponseEntity.ok(predictions)
    }

//    @DeleteMapping("/deduplicate")
    fun removeDuplicatesIfAnyExist(@RequestParam("mode", required = true, defaultValue = "QUICK") mode: DuplicateSearch = QUICK) : ResponseEntity<DuplicateSummary> {
        val duplicateSummary = predictionCleanerService.removeDuplicatesIfAnyExist(mode)

        return ResponseEntity.ok(duplicateSummary)
    }

    @GetMapping("/{id}")
    fun getAllPredictedMatchesWithForm(@PathVariable(value = "id") id: Long) : ResponseEntity<PredictorData> {
        val data = predictionsService.retrievePredictorData(id)

        return when {
            data.predictions.isEmpty() -> ResponseEntity.notFound().build()
            else -> ResponseEntity.ok(data)
        }
    }
}