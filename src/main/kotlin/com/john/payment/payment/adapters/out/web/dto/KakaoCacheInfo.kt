package com.john.payment.payment.adapters.out.web.dto

import java.io.Serializable

/**
 * @author yoonho
 * @since 2022.12.26
 */
data class KakaoCacheInfo(
    val cid: String?,
    val tid: String?,
    val orderId: String?,
    val userId: String?,
    val totalAmount: Int?,
): Serializable
