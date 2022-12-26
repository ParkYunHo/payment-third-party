package com.john.payment.payment.adapters.`in`.web

import com.john.payment.common.dto.BaseResponse
import com.john.payment.payment.adapters.`in`.web.dto.ApproveInput
import com.john.payment.payment.adapters.`in`.web.dto.PaymentInput
import com.john.payment.payment.application.port.`in`.ApproveUseCase
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
    private val readyUseCase: ReadyUseCase,
    private val approveUseCase: ApproveUseCase
) {

    @GetMapping("/pay/ready/{state}")
    fun ready(@PathVariable state: String, input: PaymentInput): BaseResponse {
        val result = readyUseCase.ready(state, input)
        return BaseResponse().success(result)
    }

    @GetMapping("/pay/approve/{state}/{pgToken}")
    fun approve(@PathVariable state: String, @PathVariable pgToken: String, input: ApproveInput): BaseResponse {
        // TODO:
        //  - ready로부터 결제상태를 직접 polling (time-out 설정 필요)
        //  - cid: 서버 내부적으로 가지고 있어야 함
        //  - tid, partner_order_id, partner_user_id, total_amount: ready상태일때 전달받은 값을 기억하고 있어야 함
        //  - ------ Redis에 임시적으로 저장하는게 좋을듯? -------
        //  - pg_token: redirect시 query parameter로 전달받음

        input.pgToken = pgToken

        val result = approveUseCase.approve(state, input)
        return BaseResponse().success(result)
    }

    @GetMapping("/pay/fail/{state}")
    fun fail(@PathVariable state: String) {

    }

    @GetMapping("/pay/cancel/{state}")
    fun cancel(@PathVariable state: String) {

    }
}