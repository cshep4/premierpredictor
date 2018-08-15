package com.cshep4.premierpredictor.controller.admin

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus.OK

@RunWith(MockitoJUnitRunner::class)
internal class UserAdminControllerTest {
    @InjectMocks
    private lateinit var userAdminController: UserAdminController

    @Test
    fun `'isUserAdmin' returns OK`() {
        val result = userAdminController.isUserAdmin()

        assertThat(result.statusCode, `is`(OK))
        assertThat(result.body, `is`(nullValue()))
    }

}