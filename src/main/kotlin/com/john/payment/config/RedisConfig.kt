package com.john.payment.config

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import redis.embedded.RedisServer
import java.time.Duration

/**
 * @author yoonho
 * @since 2022.12.27
 */
@Configuration
class RedisConfig {

    @Value("\${spring.data.redis.port}")
    private lateinit var redisPort: String
    @Value("\${spring.data.redis.host}")
    private lateinit var redisHost: String

    private lateinit var redisServer: RedisServer

    @PostConstruct
    fun startRedis() {
        redisServer = RedisServer(redisPort.toInt())
        redisServer.start()
    }

    @Bean
    fun factory(): LettuceConnectionFactory {
        val config = RedisStandaloneConfiguration(redisHost, redisPort.toInt())
        return LettuceConnectionFactory(config)
    }

    @Bean
    fun cacheManager(): CacheManager =
        RedisCacheManager
            .RedisCacheManagerBuilder
            .fromConnectionFactory(factory())
            .cacheDefaults(defaultConfiguration())
            .build()

    private fun defaultConfiguration() =
        RedisCacheConfiguration
            .defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(5))

    @PreDestroy
    fun stopRedis() {
        redisServer.stop()
    }
}