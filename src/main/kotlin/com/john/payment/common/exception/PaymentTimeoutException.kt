package com.john.payment.common.exception

/**
 * @author yoonho
 * @since 2022.12.27
 */
class PaymentTimeoutException: RuntimeException {
    constructor(msg: String?): super(msg)
    constructor(): super()
}