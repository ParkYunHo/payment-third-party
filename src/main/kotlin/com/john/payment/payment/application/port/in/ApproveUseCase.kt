package com.john.payment.payment.application.port.`in`

import com.john.payment.payment.adapters.`in`.web.dto.ApproveInput

/**
 * @author yoonho
 * @since 2022.12.26
 */
interface ApproveUseCase {
    fun approve(state: String, input: ApproveInput): Any
}