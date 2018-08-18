package com.cshep4.premierpredictor.constant

object SecurityConstants {
    var SECRET: String = System.getenv("JWT_SECRET")
    const val EXPIRATION_TIME: Long = 8640000000 // 100 days
    const val TOKEN_PREFIX = "Bearer "
    const val HEADER_STRING = "X-Auth-Token"
    const val PASSWORD_RESET_EXPIRATION_TIME: Long = 86400000 // 24 hours
    const val ADMIN_ROLE = "ADMIN"
    const val NON_ADMIN_ROLE = "USER"

    object Url {
        const val ALL_PATHS_URL = "/**"
        const val SIGN_UP_URL = "/users/sign-up"
        const val FIXTURES_UPDATE_URL = "/fixtures/update"
        const val LOGOUT_URL = "/users/logout"
        const val SET_USED_TOKEN_URL = "/token/used"
        const val SCORE_UPDATE_URL = "/score/update"
        const val RESET_PASSWORD_URL = "/reset-password"
        const val SEND_RESET_PASSWORD_URL = "/users/sendResetPassword"
        const val PROCESS_PASSWORD_RESET_URL = "/users/resetPassword"
        const val DB_CONSOLE_URL = "console/"
        const val SOCKET_URL = "/socket/**"
        const val DEDUPLICATE_URL = "/predictions/deduplicate"
        const val ADMIN_URL = "/admin/**"
    }
}