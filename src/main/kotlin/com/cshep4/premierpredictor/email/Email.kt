package com.cshep4.premierpredictor.email

import com.cshep4.premierpredictor.constant.APIConstants.SENDER_EMAIL
import com.cshep4.premierpredictor.constant.APIConstants.SEND_GRID_API_KEY
import com.sendgrid.*
import com.sendgrid.Email
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class Email {
    fun send(recipient: String, subject: String, emailContent: String) {
        val from = Email(SENDER_EMAIL)
        val to = Email(recipient)
        val content = Content("text/plain", emailContent)
        val mail = Mail(from, subject, to, content)

        val sg = SendGrid(SEND_GRID_API_KEY)
        val request = Request()
        try {
            request.method = Method.POST
            request.endpoint = "mail/send"
            request.body = mail.build()
            val response = sg.api(request)
            System.out.println(response.statusCode)
            System.out.println(response.body)
            System.out.println(response.headers)
        } catch (ex: IOException) {
            throw ex
        }
    }
}