package com.john.payment.payment.application.port.`in`

/**
 * @author yoonho
 * @since 2022.12.26
 */
interface ApproveUseCase {
    fun approve(state: String, pgToken: String, orderId: String): Any
}