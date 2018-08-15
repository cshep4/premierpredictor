package com.cshep4.premierpredictor.constant

object APIConstants {
    var API_URL: String = System.getenv("API_URL")
    var API_URL_COMMENTARY: String = System.getenv("API_URL_COMMENTARY")
    var API_KEY: String = System.getenv("API_KEY")
    const val FROM_DATE: String = "2018-08-10"
    const val TO_DATE: String = "2019-05-20"
    const val COMP_ID: String = "1204"

    const val USER_ID = "userId"

    var SEND_GRID_API_KEY: String = System.getenv("SEND_GRIP_API_KEY")
    const val RESET_PASSWORD_LINK = "https://premierpredictor.herokuapp.com/reset-password"
    const val SENDER_EMAIL = "shepapps4@gmail.com"
}