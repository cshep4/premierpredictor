package com.cshep4.premierpredictor.constant

object SecurityConstants {
    var SECRET: String = System.getenv("JWT_SECRET")
    const val EXPIRATION_TIME: Long = 8640000000 // 100 days
    const val TOKEN_PREFIX = "Bearer "
    const val HEADER_STRING = "X-Auth-Token"
    const val SIGN_UP_URL = "/users/sign-up"
    const val FIXTURES_UPDATE_URL = "/fixtures/update"
    const val LOGOUT_URL = "/users/logout"
    const val SET_USED_TOKEN_URL = "/token/used"
    const val PASSWORD_RESET_EXPIRATION_TIME: Long = 86400000 // 24 hours
}