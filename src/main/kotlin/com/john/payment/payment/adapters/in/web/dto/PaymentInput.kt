package com.john.payment.payment.adapters.`in`.web.dto

/**
 * @author yoonho
 * @since 2022.12.26
 */
data class PaymentInput(
    val itemName: String,
    val quantity: Int,
    val totalAmount: Int,
    val vat: Int,
    val taxFree: Int,
)
