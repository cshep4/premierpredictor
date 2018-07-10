package com.cshep4.premierpredictor.service.user

import com.cshep4.premierpredictor.component.resetpassword.ResetEmail
import com.cshep4.premierpredictor.component.resetpassword.UserSignature
import com.cshep4.premierpredictor.constant.SecurityConstants.SECRET
import com.cshep4.premierpredictor.data.ResetPassword
import com.cshep4.premierpredictor.extension.isValidPassword
import com.cshep4.premierpredictor.repository.sql.UserRepository
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ResetPasswordService {
    companion object {
        const val PASSWORD_NOT_VALID = "Could not reset password, password is not valid. Please try again"
        const val PASSWORDS_DONT_MATCH = "Could not reset password, password and confirmation don't match. Please try again"
        const val COULD_NOT_RESET = "Could not reset password, Please try resending password reset email"
        const val SIGNATURE_EXPIRED = "Could not reset password, reset link has expired. Please try resending password reset email"
        const val PASSWORD_UPDATED = "Password has been reset successfully, please try logging in"
    }

    @Autowired
    private lateinit var resetEmail: ResetEmail

    @Autowired
    private lateinit var userSignature: UserSignature

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    fun sendPasswordRestEmail(email: String) {
        val caseInsensitiveEmail = email.toLowerCase()

        val signature = userSignature.createUserSignature(caseInsensitiveEmail)

        if (signature != null) {
            resetEmail.send(caseInsensitiveEmail, signature)
        }
    }

    fun resetPassword(resetPassword: ResetPassword): String {
        var isTokenExpired = false
        try {
            Jwts.parser().setSigningKey(SECRET.toByteArray()).parseClaimsJws(resetPassword.signature).body.expiration
        } catch (eje: ExpiredJwtException) {
            isTokenExpired = true
        }

        return when {
            isTokenExpired -> SIGNATURE_EXPIRED
            !resetPassword.password.isValidPassword() -> PASSWORD_NOT_VALID
            resetPassword.password != resetPassword.conf -> PASSWORDS_DONT_MATCH
            !userRepository.findByEmail(resetPassword.email).isPresent -> COULD_NOT_RESET
            else -> {
                val hashedPassword = bCryptPasswordEncoder.encode(resetPassword.password)
                if (userRepository.resetUserPassword(hashedPassword, resetPassword.email, resetPassword.signature) == 0) {
                    COULD_NOT_RESET
                } else {
                    PASSWORD_UPDATED
                }
            }
        }
    }
}