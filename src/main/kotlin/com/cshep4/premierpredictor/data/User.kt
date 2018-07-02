package com.cshep4.premierpredictor.data

data class User (val id: Long? = 0,
                 var firstName: String = "",
                 var surname: String = "",
                 var email: String? = null,
                 var password: String? = null,
                 var predictedWinner: String = "",
                 var score: Int = 0
)