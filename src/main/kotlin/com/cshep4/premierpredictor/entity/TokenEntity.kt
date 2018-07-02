package com.cshep4.premierpredictor.entity

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "Token")
data class TokenEntity (
    @Id
    val token: String = ""
)