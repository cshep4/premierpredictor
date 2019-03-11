package com.cshep4.premierpredictor.controller.health

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class HealthController {
    @GetMapping("/_health")
    fun check() : ResponseEntity<Any> = ResponseEntity.ok().build()

    @GetMapping("/")
    fun default() : ResponseEntity<Any> = ResponseEntity.ok().build()
}