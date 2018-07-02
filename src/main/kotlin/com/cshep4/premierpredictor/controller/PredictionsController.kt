package com.cshep4.premierpredictor.controller

import com.cshep4.premierpredictor.data.Prediction
import com.cshep4.premierpredictor.data.PredictionSummary
import com.cshep4.premierpredictor.service.PredictionsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/predictions")
class PredictionsController {
    @Autowired
    lateinit var predictionsService: PredictionsService

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
}