package com.cshep4.premierpredictor.component.resetpassword

import com.cshep4.premierpredictor.constant.APIConstants.RESET_PASSWORD_LINK
import com.cshep4.premierpredictor.email.Email
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ResetEmail {
    private val subject = "Premier Predictor Password Reset"

    @Autowired
    private lateinit var emailSender: Email

    fun send(email: String, signature: String) {
        val message = buildMessage(email, signature)

        emailSender.send(email, subject, message)
    }

    private fun buildMessage(email: String, signature: String): String {
        val link = "$RESET_PASSWORD_LINK?email=$email&signature=$signature"

        return """Hi,

                |We have received a request to reset your password.

                |To reset your password click on the following link or copy and paste this URL into your browser (link expires in 24 hours):

                |$link

                |If you don't want to reset your password then please ignore this message.

                |Regards,

                |The Premier Predictor Team""".trimMargin()
    }
}