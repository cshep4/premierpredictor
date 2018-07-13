package com.cshep4.premierpredictor.config

import com.cshep4.premierpredictor.constant.MatchConstants.LIVE_MATCH_SUBSCRIPTION
import com.cshep4.premierpredictor.constant.MatchConstants.UPCOMING_SUBSCRIPTION
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/socket")
                .setAllowedOrigins("*")
                .withSockJS()
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.setApplicationDestinationPrefixes("/app")
                .enableSimpleBroker(UPCOMING_SUBSCRIPTION, LIVE_MATCH_SUBSCRIPTION)
    }
}