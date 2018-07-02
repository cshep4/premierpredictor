package com.cshep4.premierpredictor.data

data class SignUpUser (val id: Long? = 0,
                       var firstName: String,
                       var surname: String,
                       var email: String,
                       var password: String,
                       var confirmPassword: String,
                       var predictedWinner: String,
                       var score: Int = 0)