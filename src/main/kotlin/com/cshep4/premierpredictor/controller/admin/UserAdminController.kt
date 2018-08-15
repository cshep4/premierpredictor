package com.cshep4.premierpredictor.controller.admin

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class UserAdminController {
    @GetMapping
    fun isUserAdmin() : ResponseEntity<Any> {
        return ResponseEntity.ok().build()
    }
}