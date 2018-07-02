package com.cshep4.premierpredictor.entity

import com.cshep4.premierpredictor.data.SignUpUser
import com.cshep4.premierpredictor.data.User
import javax.persistence.*

@Entity
@Table(name = "Users")
data class UserEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var firstName: String= "",
    var surname: String = "",
    var email: String? = null,
    var password: String? = null,
    var predictedWinner: String = "",
    var score: Int = 0
){

    fun toDto(): User = User(
            id = this.id,
            firstName = this.firstName,
            surname = this.surname,
            email = this.email,
            password = this.password,
            predictedWinner = this.predictedWinner,
            score = this.score)

    companion object {
        fun fromDto(dto: User) = UserEntity(
                id = dto.id!!,
                firstName = dto.firstName,
                surname = dto.surname,
                email = dto.email,
                password = dto.password,
                predictedWinner = dto.predictedWinner,
                score = dto.score)

        fun fromDto(dto: SignUpUser) = UserEntity(
                id = dto.id!!,
                firstName = dto.firstName,
                surname = dto.surname,
                email = dto.email,
                password = dto.password,
                predictedWinner = dto.predictedWinner,
                score = dto.score)
    }
}