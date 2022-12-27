package com.john.payment.common.aop

import com.john.payment.payment.adapters.out.web.dto.KakaoCacheInfo
import com.john.payment.payment.adapters.`in`.web.dto.PaymentInput
import com.john.payment.payment.adapters.out.web.KakaoAdapter
import com.john.payment.payment.adapters.out.web.dto.KakaoReadyInfo
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * @author yoonho
 * @since 2022.12.27
 */
@Aspect
@Component
class KakaoReadyAspect(
    private val kakaoAdapter: KakaoAdapter,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Pointcut("execution(* com.john.payment.payment.*.*.*.Kakao*.ready(..))")
    fun onKakaoReady() {}

    @AfterReturning(pointcut = "onKakaoReady()", returning = "response")
    fun kakaoReady(joinPoint: JoinPoint, response: KakaoReadyInfo) {
        val input = joinPoint.args[0] as PaymentInput
        val param = KakaoCacheInfo(
            cid = input.cid,
            tid = response.tid,
            orderId = input.orderId,
            userId = input.userId,
            totalAmount = input.totalAmount
        )

        // Ready정보 Redis 저장
        log.info(" >>> [kakaoReady] param: {}, input: {}, response: {}", param, input, response)
        kakaoAdapter.setReadyInfo(param)
        //
    }
}