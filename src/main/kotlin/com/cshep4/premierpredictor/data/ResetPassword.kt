package com.cshep4.premierpredictor.data

data class ResetPassword(val email: String = "", val signature: String = "", val password: String = "", val conf: String = "")