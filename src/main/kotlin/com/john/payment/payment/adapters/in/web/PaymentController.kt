package com.john.payment.payment.adapters.`in`.web

import com.john.payment.common.constants.CommCode
import com.john.payment.common.dto.BaseResponse
import com.john.payment.common.exception.PaymentCancelException
import com.john.payment.common.exception.PaymentFailException
import com.john.payment.payment.adapters.`in`.web.dto.PaymentInput
import com.john.payment.payment.application.port.`in`.ApproveUseCase
import com.john.payment.payment.application.port.`in`.ReadyUseCase
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
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
    private val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/pay/ready/{state}")
    fun ready(@PathVariable state: String, input: PaymentInput): BaseResponse {
        val result = readyUseCase.ready(state, input)
        return BaseResponse().success(result)
    }

    @GetMapping("/pay/approve/{state}/{orderId}")
    fun approve(@PathVariable state: String, @PathVariable orderId: String, @RequestParam(name = "pg_token") pgToken: String): BaseResponse {
        val result = approveUseCase.approve(state, pgToken, orderId)
        return BaseResponse().success(result)
    }

    @GetMapping("/pay/fail/{state}/{orderId}")
    fun fail(@PathVariable state: String, @PathVariable orderId: String) {
        log.info(" >>> [fail] Fail Payment - state: {}, orderId: {}", state, orderId)
        when(state) {
            CommCode.Social.KAKAO.code -> throw PaymentFailException("카카오페이 결제가 실패하였습니다.")
            CommCode.Social.NAVER.code -> throw PaymentFailException("네이버페이 결제가 실패하였습니다.")
            else -> throw PaymentFailException("결제가 실패하였습니다.")
        }
    }

    @GetMapping("/pay/cancel/{state}/{orderId}")
    fun cancel(@PathVariable state: String, @PathVariable orderId: String) {
        log.info(" >>> [cancel] Cancel Payment - state: {}, orderId: {}", state, orderId)
        when(state) {
            CommCode.Social.KAKAO.code -> throw PaymentCancelException("카카오페이 결제가 취소되었습니다.")
            CommCode.Social.NAVER.code -> throw PaymentCancelException("네이버페이 결제가 취소되었습니다.")
            else -> throw PaymentCancelException("결제가 취소되었습니다.")
        }
    }
}