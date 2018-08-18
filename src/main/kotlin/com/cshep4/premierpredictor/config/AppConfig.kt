package com.cshep4.premierpredictor.config

import com.cshep4.premierpredictor.constant.APIConstants.USER_ID
import com.cshep4.premierpredictor.constant.SecurityConstants.HEADER_STRING
import com.cshep4.premierpredictor.constant.SecurityConstants.Url.ALL_PATHS_URL
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import javax.servlet.Filter


@Configuration
class AppConfig {
    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun corsFilter(): Filter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOrigin("*")
        config.addAllowedHeader("Access-Control-Allow-Headers")
        config.addAllowedHeader("Content-Type")
        config.addAllowedHeader("x-xsrf-token")
        config.addAllowedHeader("Authorization")
        config.addAllowedHeader(HEADER_STRING)
        config.addAllowedHeader(USER_ID)
        config.addAllowedHeader("Origin")
        config.addAllowedHeader("Accept")
        config.addAllowedHeader("X-Requested-With")
        config.addAllowedHeader("Access-Control-Request-Method")
        config.addAllowedHeader("Access-Control-Request-Headers")
        config.addAllowedMethod("OPTIONS")
        config.addAllowedMethod("GET")
        config.addAllowedMethod("PUT")
        config.addAllowedMethod("POST")
        config.addAllowedMethod("DELETE")
        config.addExposedHeader(HEADER_STRING)
        config.addExposedHeader(USER_ID)
        source.registerCorsConfiguration(ALL_PATHS_URL, config)

        return CorsFilter(source)
    }
}