package com.cshep4.premierpredictor.security

import com.cshep4.premierpredictor.data.LoginUser
import com.cshep4.premierpredictor.entity.UserEntity
import com.cshep4.premierpredictor.extension.generateJwtToken
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthenticationFilter(private val authManager: AuthenticationManager) : UsernamePasswordAuthenticationFilter() {
    @Throws(AuthenticationException::class, IOException::class)
    override fun attemptAuthentication(req: HttpServletRequest, res: HttpServletResponse): Authentication {
        val credentials = ObjectMapper().readValue(req.inputStream, UserEntity::class.java)

        val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(credentials.email, credentials.password, ArrayList<GrantedAuthority>())

        val auth = authManager.authenticate(usernamePasswordAuthenticationToken)

        val user = auth.principal as LoginUser
        res.setHeader("userId", user.id.toString())

        return auth
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain, auth: Authentication) {
        res.generateJwtToken((auth.principal as User).username, req.requestURI, (auth.principal as User).authorities)
    }
}