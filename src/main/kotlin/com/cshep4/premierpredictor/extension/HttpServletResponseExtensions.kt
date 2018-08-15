package com.cshep4.premierpredictor.extension

import com.cshep4.premierpredictor.constant.SecurityConstants
import com.cshep4.premierpredictor.constant.SecurityConstants.HEADER_STRING
import com.cshep4.premierpredictor.constant.SecurityConstants.TOKEN_PREFIX
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.GrantedAuthority
import java.util.*
import javax.servlet.http.HttpServletResponse
import kotlin.streams.toList

fun HttpServletResponse.generateJwtToken(user: String, subject: String, authorities: Collection<GrantedAuthority>) {
    //add timestamp to make the token unique
    val subjectWithTimestamp = subject + System.currentTimeMillis()

    val claims = Jwts.claims().setSubject(user)

    if (authorities.isNotEmpty()) {
        claims["ROLE"] = authorities.stream().toList()[0].authority
    }

    val token = Jwts.builder()
            .setClaims(claims)
            .setSubject(subjectWithTimestamp)
            .setIssuer(user)
            .setExpiration(Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.toByteArray())
            .compact()

    this.addHeader(HEADER_STRING, TOKEN_PREFIX + token)
}