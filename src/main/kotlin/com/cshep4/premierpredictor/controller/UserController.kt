package com.cshep4.premierpredictor.controller

import com.cshep4.premierpredictor.data.*
import com.cshep4.premierpredictor.service.user.ResetPasswordService
import com.cshep4.premierpredictor.service.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController {
    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var resetPasswordService: ResetPasswordService

    @PostMapping("/sign-up")
    fun signUp(@RequestBody signUpUser: SignUpUser) : ResponseEntity<User> {
        val savedUser = userService.createUser(signUpUser)

        return when (savedUser) {
            null -> ResponseEntity.status(BAD_REQUEST).build()
            else -> ResponseEntity.status(CREATED).body(savedUser)
        }
    }

    @GetMapping("/{id}")
    fun getUserInfo(@PathVariable(value = "id") id: Long) : ResponseEntity<User> {
        val user = userService.retrieveUserById(id)

        return when (user) {
            null -> ResponseEntity.status(NOT_FOUND).build()
            else -> ResponseEntity.status(OK).body(user)
        }
    }

    @GetMapping("/email/{email}")
    fun getUserByEmail(@PathVariable(value = "email") email: String) : ResponseEntity<User> {
        val user = userService.retrieveUserByEmail(email)

        return when (user) {
            null -> ResponseEntity.status(NOT_FOUND).build()
            else -> ResponseEntity.status(OK).body(user)
        }
    }

    @PostMapping("/logout")
    fun logout() : ResponseEntity<User> {
        return ResponseEntity.ok().build()
    }

    @PutMapping("/update")
    fun updateUserDetails(@RequestBody userDetails: UserDetails) : ResponseEntity<User> {
        val user = userService.updateUserDetails(userDetails)

        return when (user) {
            null -> ResponseEntity.badRequest().build()
            else -> ResponseEntity.noContent().build()
        }
    }

    @PutMapping("/updatePassword")
    fun updateUserPassword(@RequestBody userPasswords: UserPasswords) : ResponseEntity<User> {
        val user = userService.updateUserPassword(userPasswords)

        return when (user) {
            null -> ResponseEntity.badRequest().build()
            else -> ResponseEntity.noContent().build()
        }
    }

    @PostMapping("/sendResetPassword")
    fun sendPasswordResetEmail(@RequestBody email: String) : ResponseEntity<String> {
        resetPasswordService.sendPasswordRestEmail(email)

        return ResponseEntity.ok().build()
    }

    @PostMapping("/resetPassword")
    fun resetPassword(@ModelAttribute resetPassword: ResetPassword) : ResponseEntity<String> {
        val response = resetPasswordService.resetPassword(resetPassword)

        return ResponseEntity.ok().body(response)
    }
}