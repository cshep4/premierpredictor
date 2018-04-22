package com.cshep4.wcpredictor.security

import com.cshep4.wcpredictor.constant.SecurityConstants.FIXTURES_UPDATE_URL
import com.cshep4.wcpredictor.constant.SecurityConstants.SET_USED_TOKEN_URL
import com.cshep4.wcpredictor.constant.SecurityConstants.SIGN_UP_URL
import com.cshep4.wcpredictor.service.UsedTokenService
import com.cshep4.wcpredictor.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod.*
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@EnableWebSecurity
class WebSecurity : WebSecurityConfigurerAdapter() {
    @Autowired
    private lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var usedTokenService: UsedTokenService

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(POST, SIGN_UP_URL).permitAll()
                .antMatchers(PUT, FIXTURES_UPDATE_URL).permitAll()
                .antMatchers(PUT, SET_USED_TOKEN_URL).permitAll()
                .antMatchers(GET, "console/").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(JWTAuthenticationFilter(authenticationManager()))
                .addFilter(JWTAuthorisationFilter(authenticationManager(),usedTokenService))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Throws(Exception::class)
    public override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder)
    }

    @Bean
    internal fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", CorsConfiguration().applyPermitDefaultValues())
        return source
    }
}