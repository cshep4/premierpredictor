package com.cshep4.premierpredictor.config

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class DynamoDBConfig {

    @Value("\${amazon.aws.accesskey}")
    private val amazonAWSAccessKey: String? = null

    @Value("\${amazon.aws.secretkey}")
    private val amazonAWSSecretKey: String? = null

    fun amazonAWSCredentialsProvider(): AWSCredentialsProvider {
        return AWSStaticCredentialsProvider(amazonAWSCredentials())
    }

    @Bean
    fun amazonAWSCredentials(): AWSCredentials {
        return BasicAWSCredentials(amazonAWSAccessKey!!, amazonAWSSecretKey!!)
    }

    @Bean
    fun amazonDynamoDB(): AmazonDynamoDB {
        return AmazonDynamoDBClientBuilder.standard()
                .withCredentials(amazonAWSCredentialsProvider())
                .withRegion(Regions.US_EAST_1)
                .build()
    }
}