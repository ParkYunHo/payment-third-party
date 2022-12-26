package com.john.payment.payment.adapters.`in`.web

import com.john.payment.payment.application.port.`in`.ReadyUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

/**
 * @author yoonho
 * @since 2022.12.26
 */
@RestController
class PaymentController(
    private val readyUseCase: ReadyUseCase
) {

    @GetMapping("/pay/ready/{state}")
    fun ready(@PathVariable state: String) {
        readyUseCase.ready(state)
    }

    @GetMapping("/pay/approve/{state}")
    fun approve(@PathVariable state: String) {

    }
}