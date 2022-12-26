package com.john.payment.config

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.tcp.TcpClient
import java.util.concurrent.TimeUnit

/**
 * @author yoonho
 * @since 2022.12.26
 */
@Configuration
class WebClientConfig {

    @Bean(name = ["kapiWebClient"])
    fun webClient(): WebClient {
        val tcpClient = TcpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
            .doOnConnected { connection ->
                connection
                    .addHandlerLast(ReadTimeoutHandler(3000, TimeUnit.MILLISECONDS))
                    .addHandlerLast(WriteTimeoutHandler(3000, TimeUnit.MILLISECONDS))
            }

        val connector = ReactorClientHttpConnector(HttpClient.from(tcpClient))
        return WebClient.builder()
            .defaultHeader("User-Agent", "Third-party Payment")
            .clientConnector(connector)
            .build()
    }
}