package com.cshep4.premierpredictor.service.user


import com.cshep4.premierpredictor.data.SignUpUser
import com.cshep4.premierpredictor.data.User
import com.cshep4.premierpredictor.data.UserPasswords
import com.cshep4.premierpredictor.entity.UserEntity
import com.cshep4.premierpredictor.extension.isValidEmailAddress
import com.cshep4.premierpredictor.extension.isValidPassword
import com.cshep4.premierpredictor.repository.sql.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService : UserDetailsService {
    @Autowired
    lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    @Autowired
    private lateinit var userRepository: UserRepository

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails? {
        val caseInsensitiveEmail = email.toLowerCase()

        val user = userRepository.findByEmail(caseInsensitiveEmail)
                .map { it.toDto() }
                .orElse(null) ?: throw UsernameNotFoundException("User not found")

        return user.toLoginUser()
    }

    fun createUser(signUpUser: SignUpUser): User? {
        signUpUser.score = 0

        signUpUser.email = signUpUser.email.toLowerCase()

        return when {
            !signUpUser.email.isValidEmailAddress() -> null
            userRepository.findByEmail(signUpUser.email).isPresent -> null
            !signUpUser.password.isValidPassword() -> null
            signUpUser.firstName.isBlank() -> null
            signUpUser.surname.isBlank() -> null
            signUpUser.password != signUpUser.confirmPassword -> null
            else -> {
                signUpUser.password = bCryptPasswordEncoder.encode(signUpUser.password)
                userRepository.save(UserEntity.fromDto(signUpUser)).toDto()
            }
        }
    }

    fun retrieveUserById(id: Long): User? = userRepository.findById(id)
            .map { it.toDto() }
            .orElse(null)

    fun retrieveUserByEmail(email: String): User? = userRepository.findByEmail(email)
            .map { it.toDto() }
            .orElse(null)

    fun updateUserDetails(userDetails: com.cshep4.premierpredictor.data.UserDetails): User? {
        val user = userRepository.findById(userDetails.id)
                .map { it.toDto() }
                .orElse(null) ?: return null

        userDetails.email = userDetails.email.toLowerCase()

        return when {
            !userDetails.email.isValidEmailAddress() -> null
            emailTakenByDifferentUser(userDetails.id, userDetails.email) -> null
            userDetails.firstName.isBlank() -> null
            userDetails.surname.isBlank() -> null
            else -> {
                user.email = userDetails.email
                user.firstName = userDetails.firstName
                user.surname = userDetails.surname
                userRepository.save(UserEntity.fromDto(user)).toDto()
            }
        }
    }

    private fun emailTakenByDifferentUser(id: Long, email: String) : Boolean {
        userRepository.findByEmail(email)
                .filter{ it.id != id }
                .map { it.toDto() }
                .orElse(null) ?: return false

        return true
    }

    fun updateUserPassword(userPasswords: UserPasswords): User? {
        val user = userRepository.findById(userPasswords.id)
                .map { it.toDto() }
                .orElse(null) ?: return null

        return when {
            !bCryptPasswordEncoder.matches(userPasswords.oldPassword, user.password) -> null
            userPasswords.newPassword != userPasswords.confirmPassword -> null
            !userPasswords.newPassword.isValidPassword() -> null
            else -> {
                user.password = bCryptPasswordEncoder.encode(userPasswords.newPassword)
                userRepository.save(UserEntity.fromDto(user)).toDto()
            }
        }
    }
}