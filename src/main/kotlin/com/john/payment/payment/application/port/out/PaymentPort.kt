package com.john.payment.payment.application.port.out

import com.john.payment.payment.adapters.`in`.web.dto.PaymentInput

/**
 * @author yoonho
 * @since 2022.12.26
 */
interface PaymentPort {
    fun ready(input: PaymentInput): Any
    fun approve(pgToken: String, orderId: String): Any
    fun support(state: String): Boolean
}