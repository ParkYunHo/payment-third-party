package com.john.payment

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ThirdPartyPaymentApplication

fun main(args: Array<String>) {
    runApplication<ThirdPartyPaymentApplication>(*args)
}
