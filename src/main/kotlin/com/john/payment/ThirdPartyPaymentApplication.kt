package com.john.payment

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class ThirdPartyPaymentApplication

fun main(args: Array<String>) {
    runApplication<ThirdPartyPaymentApplication>(*args)
}
