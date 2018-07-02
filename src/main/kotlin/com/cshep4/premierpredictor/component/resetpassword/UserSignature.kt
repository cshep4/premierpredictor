package com.cshep4.premierpredictor.component.resetpassword

import com.cshep4.premierpredictor.constant.SecurityConstants.PASSWORD_RESET_EXPIRATION_TIME
import com.cshep4.premierpredictor.constant.SecurityConstants.SECRET
import com.cshep4.premierpredictor.repository.sql.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserSignature {
    @Autowired
    private lateinit var userRepository: UserRepository

    fun createUserSignature(email: String): String? {
        val signature = Jwts.builder()
                .setIssuer(email)
                .setExpiration(Date(System.currentTimeMillis() + PASSWORD_RESET_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.toByteArray())
                .compact()

        val numberOfUpdatedRows = userRepository.setUserSignature(signature, email)

        return if(numberOfUpdatedRows == 0) {
            null
        } else {
            signature
        }
    }
}