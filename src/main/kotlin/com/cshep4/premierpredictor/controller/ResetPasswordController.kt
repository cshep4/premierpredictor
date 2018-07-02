package com.cshep4.premierpredictor.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping


@Controller
class ResetPasswordController {
    @RequestMapping("/reset-password")
    fun welcome(): String {
        return "index"
    }
}