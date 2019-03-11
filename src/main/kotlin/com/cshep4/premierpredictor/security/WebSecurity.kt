package com.cshep4.premierpredictor.security

import com.cshep4.premierpredictor.constant.SecurityConstants.ADMIN_ROLE
import com.cshep4.premierpredictor.constant.SecurityConstants.Url.ADMIN_URL
import com.cshep4.premierpredictor.constant.SecurityConstants.Url.ALL_PATHS_URL
import com.cshep4.premierpredictor.constant.SecurityConstants.Url.DB_CONSOLE_URL
import com.cshep4.premierpredictor.constant.SecurityConstants.Url.DEDUPLICATE_URL
import com.cshep4.premierpredictor.constant.SecurityConstants.Url.FIXTURES_UPDATE_URL
import com.cshep4.premierpredictor.constant.SecurityConstants.Url.PROCESS_PASSWORD_RESET_URL
import com.cshep4.premierpredictor.constant.SecurityConstants.Url.RESET_PASSWORD_URL
import com.cshep4.premierpredictor.constant.SecurityConstants.Url.SCORE_UPDATE_URL
import com.cshep4.premierpredictor.constant.SecurityConstants.Url.SEND_RESET_PASSWORD_URL
import com.cshep4.premierpredictor.constant.SecurityConstants.Url.SET_USED_TOKEN_URL
import com.cshep4.premierpredictor.constant.SecurityConstants.Url.SIGN_UP_URL
import com.cshep4.premierpredictor.constant.SecurityConstants.Url.SOCKET_URL
import com.cshep4.premierpredictor.service.user.UserService
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

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(GET, "/").permitAll()
                .antMatchers(GET, "/_health").permitAll()
                .antMatchers(POST, SIGN_UP_URL).permitAll()
                .antMatchers(PUT, FIXTURES_UPDATE_URL).permitAll()
                .antMatchers(PUT, SET_USED_TOKEN_URL).permitAll()
                .antMatchers(PUT, SCORE_UPDATE_URL).permitAll()
                .antMatchers(GET, RESET_PASSWORD_URL).permitAll()
                .antMatchers(POST, SEND_RESET_PASSWORD_URL).permitAll()
                .antMatchers(POST, PROCESS_PASSWORD_RESET_URL).permitAll()
                .antMatchers(GET, DB_CONSOLE_URL).permitAll()
                .antMatchers(GET, SOCKET_URL).permitAll()
                .antMatchers(DELETE, DEDUPLICATE_URL).permitAll()
                .antMatchers(ADMIN_URL).hasAuthority(ADMIN_ROLE)
                .anyRequest().authenticated()
                .and()
                .addFilter(JWTAuthenticationFilter(authenticationManager()))
                .addFilter(JWTAuthorisationFilter(authenticationManager()))
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
        source.registerCorsConfiguration(ALL_PATHS_URL, CorsConfiguration().applyPermitDefaultValues())
        return source
    }
}