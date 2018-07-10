package com.cshep4.premierpredictor.service.user

import com.cshep4.premierpredictor.component.resetpassword.ResetEmail
import com.cshep4.premierpredictor.component.resetpassword.UserSignature
import com.cshep4.premierpredictor.constant.SecurityConstants
import com.cshep4.premierpredictor.data.ResetPassword
import com.cshep4.premierpredictor.entity.UserEntity
import com.cshep4.premierpredictor.repository.sql.UserRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

@RunWith(MockitoJUnitRunner::class)
internal class ResetPasswordServiceTest {
    companion object {
        const val PASSWORD_NOT_VALID = "Could not reset password, password is not valid. Please try again"
        const val PASSWORDS_DONT_MATCH = "Could not reset password, password and confirmation don't match. Please try again"
        const val COULD_NOT_RESET = "Could not reset password, Please try resending password reset email"
        const val SIGNATURE_EXPIRED = "Could not reset password, reset link has expired. Please try resending password reset email"
        const val PASSWORD_UPDATED = "Password has been reset successfully, please try logging in"
    }

    val signature = Jwts.builder()
            .setExpiration(Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.toByteArray())
            .compact()

    @Mock
    private lateinit var resetEmail: ResetEmail

    @Mock
    private lateinit var userSignature: UserSignature

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    @InjectMocks
    private lateinit var resetPasswordService: ResetPasswordService

    @Before
    fun init() {
        whenever(bCryptPasswordEncoder.encode(any())).thenReturn("pass")
        whenever(userRepository.findByEmail(any())).thenReturn(Optional.of(UserEntity()))
    }

    @Test
    fun `'sendPasswordRestEmail' creates a user signature then sends the email`() {
        val email = "test email"
        val signature = "test signature"

        whenever(userSignature.createUserSignature(email)).thenReturn(signature)

        resetPasswordService.sendPasswordRestEmail(email)

        verify(userSignature).createUserSignature(email)
        verify(resetEmail).send(email, signature)
    }

    @Test
    fun `'resetPassword' returns error message and does not update password if password is not valid`() {
        val resetPassword = ResetPassword(email = "email", signature = signature, password = "pass", conf = "pass")

        val result = resetPasswordService.resetPassword(resetPassword)

        verify(userRepository, times(0)).resetUserPassword(any(), any(), any())
        assertThat(result, `is`(PASSWORD_NOT_VALID))
    }

    @Test
    fun `'resetPassword' returns error message and does not update password if confirmation does not match`() {
        val resetPassword = ResetPassword(email = "email", signature = signature, password = "Pass123", conf = "Pass122")

        val result = resetPasswordService.resetPassword(resetPassword)

        verify(userRepository, times(0)).resetUserPassword(any(), any(), any())
        assertThat(result, `is`(PASSWORDS_DONT_MATCH))
    }

    @Test
    fun `'resetPassword' returns error message and does not update password if user does not exist`() {
        val resetPassword = ResetPassword(email = "email", signature = signature, password = "Pass123", conf = "Pass123")

        whenever(userRepository.findByEmail(resetPassword.email)).thenReturn(Optional.empty())

        val result = resetPasswordService.resetPassword(resetPassword)

        verify(userRepository, times(0)).resetUserPassword(any(), any(), any())
        assertThat(result, `is`(COULD_NOT_RESET))
    }

    @Test
    fun `'resetPassword' returns error message and does not update password if signature has expired`() {
        val expiredToken = Jwts.builder()
                .setExpiration(Date(0))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.toByteArray())
                .compact()

        val resetPassword = ResetPassword(email = "email", signature = expiredToken, password = "Pass123", conf = "Pass123")

        val result = resetPasswordService.resetPassword(resetPassword)

        verify(userRepository, times(0)).resetUserPassword(any(), any(), any())
        assertThat(result, `is`(SIGNATURE_EXPIRED))
    }

    @Test
    fun `'resetPassword' returns error message and does not update password if signature and email do not match a user`() {
        val resetPassword = ResetPassword(email = "email", signature = signature, password = "Pass123", conf = "Pass123")

        whenever(userRepository.resetUserPassword(any(), any(), any())).thenReturn(0)

        val result = resetPasswordService.resetPassword(resetPassword)

        verify(userRepository).resetUserPassword(any(), any(), any())
        assertThat(result, `is`(COULD_NOT_RESET))
    }

    @Test
    fun `'resetPassword' returns updated message and does update password if all input is valid`() {
        val resetPassword = ResetPassword(email = "email", signature = signature, password = "Pass123", conf = "Pass123")

        whenever(userRepository.resetUserPassword(any(), any(), any())).thenReturn(1)

        val result = resetPasswordService.resetPassword(resetPassword)

        verify(userRepository).resetUserPassword(any(), any(), any())
        assertThat(result, `is`(PASSWORD_UPDATED))
    }
}