package com.john.payment.payment.application.port.out

/**
 * @author yoonho
 * @since 2022.12.26
 */
interface PaymentPort {
    fun ready()
    fun approve()
    fun support(state: String): Boolean
}