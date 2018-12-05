package com.cshep4.premierpredictor.component.resetpassword

import com.cshep4.premierpredictor.constant.APIConstants.RESET_PASSWORD_LINK
import com.cshep4.premierpredictor.email.Email
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class ResetEmailTest {
    private val subject = "Premier Predictor Password Reset"
    private val signature = "sign"
    private val emailAddress = "email"
    private val link = "$RESET_PASSWORD_LINK?email=$emailAddress&signature=$signature"

    private val emailContent = """Hi,

                |We have received a request to reset your password.

                |To reset your password click on the following link or copy and paste this URL into your browser (link expires in 24 hours):

                |$link

                |If you don't want to reset your password then please ignore this message.

                |Regards,

                |The Premier Predictor Team""".trimMargin()

    @Mock
    private lateinit var email: Email

    @InjectMocks
    private lateinit var resetEmail: ResetEmail

    @Test
    fun `'send' builds email content and sends email`() {
        resetEmail.send(emailAddress, signature)

        verify(email).send(emailAddress, subject, emailContent)
    }

}