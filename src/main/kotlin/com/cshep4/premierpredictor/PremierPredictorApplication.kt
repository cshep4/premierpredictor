package com.cshep4.premierpredictor

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication
@EnableScheduling
@EnableDynamoDBRepositories(basePackages = ["com.cshep4.premierpredictor.repository.dynamodb"])
@EnableJpaRepositories(basePackages = ["com.cshep4.premierpredictor.repository.sql"])
@EntityScan(basePackages = ["com.cshep4.premierpredictor.entity"])
class PremierPredictorApplication

fun main(args: Array<String>) {
    runApplication<PremierPredictorApplication>(*args)
}