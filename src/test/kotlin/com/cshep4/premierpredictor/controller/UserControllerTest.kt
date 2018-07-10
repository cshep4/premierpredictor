package com.cshep4.premierpredictor.controller

import com.cshep4.premierpredictor.data.*
import com.cshep4.premierpredictor.service.user.ResetPasswordService
import com.cshep4.premierpredictor.service.user.UserService
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus.*
import org.hamcrest.CoreMatchers.`is` as Is

@RunWith(MockitoJUnitRunner::class)
internal class UserControllerTest {
    @Mock
    lateinit var userService: UserService

    @Mock
    lateinit var resetPasswordService: ResetPasswordService

    @InjectMocks
    lateinit var userController: UserController

    val userInput = SignUpUser(firstName = "first", surname = "surname", email = "email", password = "pass", confirmPassword = "pass", predictedWinner = "France")
    val user = User()

    @Test
    fun `'signUp' returns CREATED with the user in the request body when user is added to db`() {
        whenever(userService.createUser(userInput)).thenReturn(user)

        val result = userController.signUp(userInput)

        assertThat(result.statusCode, Is(CREATED))
        assertThat(result.body, Is(user))
    }

    @Test
    fun `'signUp' returns BAD_REQUEST user is not added to db`() {
        whenever(userService.createUser(userInput)).thenReturn(null)

        val result = userController.signUp(userInput)

        assertThat(result.statusCode, Is(BAD_REQUEST))
        assertThat(result.body, Is(nullValue()))
    }

    @Test
    fun `'getUserInfo' returns user info in the request body when user is found with id`() {
        whenever(userService.retrieveUserById(1)).thenReturn(user)

        val result = userController.getUserInfo(1)

        assertThat(result.statusCode, Is(OK))
        assertThat(result.body, Is(user))
    }

    @Test
    fun `'getUserInfo' returns NOT_FOUND when no user is found`() {
        whenever(userService.retrieveUserById(1)).thenReturn(null)

        val result = userController.getUserInfo(1)

        assertThat(result.statusCode, Is(NOT_FOUND))
        assertThat(result.body, Is(nullValue()))
    }

    @Test
    fun `'getUserByEmail' returns user info in the request body when user is found with email`() {
        whenever(userService.retrieveUserByEmail("test")).thenReturn(user)

        val result = userController.getUserByEmail("test")

        assertThat(result.statusCode, Is(OK))
        assertThat(result.body, Is(user))
    }

    @Test
    fun `'getUserByEmail' returns NOT_FOUND when no user is found`() {
        whenever(userService.retrieveUserByEmail("test")).thenReturn(null)

        val result = userController.getUserByEmail("test")

        assertThat(result.statusCode, Is(NOT_FOUND))
        assertThat(result.body, Is(nullValue()))
    }

    @Test
    fun `'logout' returns OK`() {
        val result = userController.logout()

        assertThat(result.statusCode, Is(OK))
        assertThat(result.body, Is(nullValue()))
    }

    @Test
    fun `'updateUserDetails' returns NO_CONTENT when user details are updated`() {
        val userDetails = UserDetails()

        whenever(userService.updateUserDetails(userDetails)).thenReturn(user)

        val result = userController.updateUserDetails(userDetails)

        assertThat(result.statusCode, Is(NO_CONTENT))
        assertThat(result.body, Is(nullValue()))
    }

    @Test
    fun `'updateUserDetails' returns BAD_REQUEST when user details are not updated`() {
        val userDetails = UserDetails()

        whenever(userService.updateUserDetails(userDetails)).thenReturn(null)

        val result = userController.updateUserDetails(userDetails)

        assertThat(result.statusCode, Is(BAD_REQUEST))
        assertThat(result.body, Is(nullValue()))
    }

    @Test
    fun `'updateUserPassword' returns NO_CONTENT when user password is updated`() {
        val userPasswords = UserPasswords()

        whenever(userService.updateUserPassword(userPasswords)).thenReturn(user)

        val result = userController.updateUserPassword(userPasswords)

        assertThat(result.statusCode, Is(NO_CONTENT))
        assertThat(result.body, Is(nullValue()))
    }

    @Test
    fun `'updateUserPassword' returns BAD_REQUEST when user password is not updated`() {
        val userPasswords = UserPasswords()

        whenever(userService.updateUserPassword(userPasswords)).thenReturn(null)

        val result = userController.updateUserPassword(userPasswords)

        assertThat(result.statusCode, Is(BAD_REQUEST))
        assertThat(result.body, Is(nullValue()))
    }

    @Test
    fun `'sendPasswordResetEmail' sends reset password email and returns OK`() {
        val email = "test email"
        val result = userController.sendPasswordResetEmail(email)

        verify(resetPasswordService).sendPasswordRestEmail(email)
        
        assertThat(result.statusCode, Is(OK))
    }

    @Test
    fun `'resetPassword' calls the reset password service and returns the message`() {
        val response = "password updated"
        val resetPassword = ResetPassword()

        whenever(resetPasswordService.resetPassword(resetPassword)).thenReturn(response)

        val result = userController.resetPassword(resetPassword)

        assertThat(result.statusCode, Is(OK))
        assertThat(result.body, Is(response))
    }
}