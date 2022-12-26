package com.john.payment.payment.application

import com.john.payment.common.exception.BadRequestException
import com.john.payment.payment.adapters.`in`.web.dto.PaymentInput
import com.john.payment.payment.application.port.`in`.ReadyUseCase
import com.john.payment.payment.application.port.out.PaymentPort
import org.springframework.stereotype.Service

/**
 * @author yoonho
 * @since 2022.12.26
 */
@Service
class PaymentService(
    private val paymentPorts: List<PaymentPort>
): ReadyUseCase {

    override fun ready(state: String, input: PaymentInput): Any {
        return this.findPaymentPort(state)
                    .ready(input)
    }

    private fun findPaymentPort(state: String) =
        paymentPorts.find { it.support(state) } ?: throw BadRequestException(" >>> [findPaymentPort] 올바른 결제앱 대상이 아닙니다. - state: ${state}")
}