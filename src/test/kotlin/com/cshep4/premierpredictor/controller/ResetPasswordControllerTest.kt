package com.cshep4.premierpredictor.controller

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

internal class ResetPasswordControllerTest {
    val resetPasswordController = ResetPasswordController()

    @Test
    fun `'welcome' returns index`() {
        val result = resetPasswordController.welcome()

        assertThat(result, `is`("index"))
    }

}