package com.john.payment.common.handler

import com.john.payment.common.dto.BaseResponse
import com.john.payment.common.exception.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * @author yoonho
 * @since 2022.12.26
 */
@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(InternalServerErrorException::class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    fun serverInternalErrorException(e: InternalServerErrorException): BaseResponse {
        return BaseResponse(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(BadRequestException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun badRequestException(e: BadRequestException): BaseResponse {
        return BaseResponse(e.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(PaymentTimeoutException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun paymentTimeoutException(e: PaymentTimeoutException): BaseResponse {
        return BaseResponse(e.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(PaymentCancelException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun paymentCancelException(e: PaymentCancelException): BaseResponse {
        return BaseResponse(e.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(PaymentFailException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun paymentFailException(e: PaymentFailException): BaseResponse {
        return BaseResponse(e.message, HttpStatus.BAD_REQUEST)
    }
}