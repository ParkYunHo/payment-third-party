package com.john.payment.payment.application.port.`in`

import com.john.payment.payment.adapters.`in`.web.dto.PaymentInput

/**
 * @author yoonho
 * @since 2022.12.26
 */
interface ReadyUseCase {
    fun ready(state: String, input: PaymentInput): Any
}