package com.cshep4.premierpredictor.extension

fun String?.isValidPassword(): Boolean {
    // must contain 1 digit 0-9, 1 uppercase letter, 1 lowercase letter and between 6-20 characters
    return this?.matches(Regex("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})")) ?: false
}

fun String?.isValidEmailAddress(): Boolean {
    return this?.matches(Regex("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$")) ?: false
}