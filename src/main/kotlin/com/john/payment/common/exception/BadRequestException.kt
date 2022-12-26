package com.john.payment.common.exception

/**
 * @author yoonho
 * @since 2022.12.26
 */
class BadRequestException: RuntimeException {
    constructor(msg: String?): super(msg)
    constructor(): super()
}