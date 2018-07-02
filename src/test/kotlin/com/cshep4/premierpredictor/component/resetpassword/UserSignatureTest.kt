package com.cshep4.premierpredictor.component.resetpassword

import com.cshep4.premierpredictor.constant.SecurityConstants
import com.cshep4.premierpredictor.constant.SecurityConstants.SECRET
import com.cshep4.premierpredictor.repository.sql.UserRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.jsonwebtoken.Jwts
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.lessThan
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class UserSignatureTest {
    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userSignature: UserSignature

    @Test
    fun `'createUserSignature' should generate a token, valid for 24 hours with the users email as the issuer and store it to the db`() {
        val email = "this is a test email"

        whenever(userRepository.setUserSignature(any(), any())).thenReturn(1)

        val result = userSignature.createUserSignature(email)

        verify(userRepository).setUserSignature(result!!, email)

        val issuer = Jwts.parser()
                .setSigningKey(SECRET.toByteArray())
                .parseClaimsJws(result)
                .body
                .issuer

        val expiration = Jwts.parser()
                .setSigningKey(SECRET.toByteArray())
                .parseClaimsJws(result)
                .body
                .expiration

        val twentyFourHoursTime = System.currentTimeMillis() + SecurityConstants.PASSWORD_RESET_EXPIRATION_TIME

        assertThat(issuer, `is`(email))
        assertThat(expiration.time, lessThan(twentyFourHoursTime))

    }

}