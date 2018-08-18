package com.cshep4.premierpredictor.data

import com.cshep4.premierpredictor.constant.SecurityConstants.ADMIN_ROLE
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.time.LocalDateTime

data class User (val id: Long? = 0,
                 var firstName: String = "",
                 var surname: String = "",
                 var email: String? = null,
                 var password: String? = null,
                 var predictedWinner: String = "",
                 var score: Int = 0,
                 val joined: LocalDateTime? = null,
                 var admin: Boolean = false,
                 var adFree: Boolean = false
){
    fun toLoginUser(): LoginUser {
        return when {
            this.admin -> LoginUser(id = this.id!!, username = this.email!!, password = this.password!!, authorities = listOf(SimpleGrantedAuthority(ADMIN_ROLE)))
            else -> LoginUser(id = this.id!!, username = this.email!!, password = this.password!!, authorities = emptyList())
        }
    }
}