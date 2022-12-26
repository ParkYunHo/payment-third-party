package com.john.payment.payment.adapters.`in`.web.dto

/**
 * @author yoonho
 * @since 2022.12.26
 */
data class ApproveInput(
    val cid: String?,
    val cidSecret: String?,
    val tid: String?,
    val partnerOrderId: String?,
    val partnerUserId: String?,
    var pgToken: String?,
    val totalAmount: String?,
)
