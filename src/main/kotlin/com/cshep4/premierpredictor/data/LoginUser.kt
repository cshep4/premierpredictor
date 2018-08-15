package com.cshep4.premierpredictor.data

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class LoginUser(val id: Long, username: String, password: String, authorities: Collection<GrantedAuthority>) : User(username, password, authorities)
