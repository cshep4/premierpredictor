package com.cshep4.premierpredictor.extension

import com.cshep4.premierpredictor.constant.SecurityConstants
import com.cshep4.premierpredictor.constant.SecurityConstants.HEADER_STRING
import com.cshep4.premierpredictor.constant.SecurityConstants.TOKEN_PREFIX
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*
import javax.servlet.http.HttpServletResponse

fun HttpServletResponse.generateJwtToken(user: String, subject: String) {
    //add timestamp to make the token unique
    val subjectWithTimestamp = subject + System.currentTimeMillis()

    val token = Jwts.builder()
            .setSubject(subjectWithTimestamp)
            .setIssuer(user)
            .setExpiration(Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.toByteArray())
            .compact()

    this.addHeader(HEADER_STRING, TOKEN_PREFIX + token)
}