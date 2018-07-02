package com.cshep4.premierpredictor.data

data class UserPasswords (val id: Long = 0, var oldPassword: String = "", var newPassword: String = "", var confirmPassword: String = "")