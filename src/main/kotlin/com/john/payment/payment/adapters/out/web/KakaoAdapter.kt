package com.john.payment.payment.adapters.out.web

import com.john.payment.common.constants.CommCode
import com.john.payment.common.exception.BadRequestException
import com.john.payment.common.exception.InternalServerErrorException
import com.john.payment.common.exception.PaymentTimeoutException
import com.john.payment.payment.adapters.out.web.dto.KakaoCacheInfo
import com.john.payment.payment.adapters.`in`.web.dto.PaymentInput
import com.john.payment.payment.adapters.out.web.dto.KakaoApproveInfo
import com.john.payment.payment.adapters.out.web.dto.KakaoReadyInfo
import com.john.payment.payment.application.port.out.PaymentPort
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachePut
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder

/**
 * @author yoonho
 * @since 2022.12.26
 */
@Component
class KakaoAdapter(
    private val kapiWebClient: WebClient,
    private val cacheManager: CacheManager
): PaymentPort {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Value("\${payment.domain}")
    private lateinit var domain: String

    @Value("\${payment.apis.kakao}")
    private lateinit var url: String

    @Value("\${payment.keys.kakao}")
    private lateinit var adminKey: String

    override fun ready(input: PaymentInput): KakaoReadyInfo {
        try{
            val uriComponents = UriComponentsBuilder
                .fromHttpUrl(url + "/v1/payment/ready")
                .build(false)

            val params: MultiValueMap<String, String> = LinkedMultiValueMap()
            params.add("cid", input.cid)
            params.add("partner_order_id", input.orderId)
            params.add("partner_user_id", input.userId)
            params.add("item_name", input.itemName)
            params.add("quantity", input.quantity.toString())
            params.add("total_amount", input.totalAmount.toString())
            params.add("vat_amount", input.vat.toString())
            params.add("tax_free_amount", input.taxFree.toString())
            params.add("approval_url", domain + "/pay/approve/" + CommCode.Social.KAKAO.code + "/" + input.orderId)
            params.add("fail_url", domain + "/pay/fail/" + CommCode.Social.KAKAO.code + "/" + input.orderId)
            params.add("cancel_url", domain + "/pay/cancel/" + CommCode.Social.KAKAO.code + "/" + input.orderId)

            log.info(" >>> [ready] request - url: {}, body: {}", uriComponents.toUriString(), params.toString())
            val response = kapiWebClient.post()
                .uri(uriComponents.toUri())
                .header("Authorization", "KakaoAK " + adminKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(params))
                .exchangeToMono { response ->
                    if(response.statusCode().is4xxClientError) {
                        throw BadRequestException("카카오페이 결제페이지를 불러오는데 실패하였습니다.")
                    }else if(response.statusCode().is5xxServerError) {
                        throw InternalServerErrorException("일시적으로 카카오페이 서버를 이용할 수 없습니다.")
                    }else{
                        return@exchangeToMono response.toEntity(KakaoReadyInfo::class.java)
                    }
                }
                .block()
            log.info(" >>> [ready] response - body: {}, status: {}", response.body, response.statusCode)

            return response.body
        }catch (be: BadRequestException) {
            log.info(">>> [ready] BadRequestException - message: {}", be.message)
            throw be
        }catch(e: Exception){
            log.info(">>> [ready] Exception - message: {}", e.message)
            throw e
        }
    }

    override fun approve(pgToken: String, orderId: String): KakaoApproveInfo {
        try{
            // 캐시에 저장한 ready정보 조회
            val cache = cacheManager.getCache("KAKAO")
            val readyInfo = cache.get(orderId, KakaoCacheInfo::class.java)
                ?: throw PaymentTimeoutException("결제 가능한 시간을 초과하였습니다.")
            //

            val uriComponents = UriComponentsBuilder
                .fromHttpUrl(url + "/v1/payment/approve")
                .build(false)

            val params: MultiValueMap<String, String> = LinkedMultiValueMap()
            params.add("cid", readyInfo.cid)
            params.add("tid", readyInfo.tid)
            params.add("partner_order_id", readyInfo.orderId)
            params.add("partner_user_id", readyInfo.userId)
            params.add("pg_token", pgToken)
            params.add("total_amount", readyInfo.totalAmount.toString())

            log.info(" >>> [approve] request - url: {}, body: {}", uriComponents.toUriString(), params.toString())
            val response = kapiWebClient.post()
                .uri(uriComponents.toUri())
                .header("Authorization", "KakaoAK " + adminKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(params))
                .exchangeToMono { response ->
                    if(response.statusCode().is4xxClientError) {
                        throw BadRequestException("카카오페이 결제에 실패하였습니다.")
                    }else if(response.statusCode().is5xxServerError) {
                        throw InternalServerErrorException("일시적으로 카카오페이 서버를 이용할 수 없습니다.")
                    }else{
                        return@exchangeToMono response.toEntity(KakaoApproveInfo::class.java)
                    }
                }
                .block()
            log.info(" >>> [approve] response - body: {}, status: {}", response.body, response.statusCode)

            return response.body
        }catch (pe: PaymentTimeoutException) {
            log.info(">>> [approve] PaymentTimeoutException - message: {}", pe.message)
            throw pe
        }catch (be: BadRequestException) {
            log.info(">>> [approve] BadRequestException - message: {}", be.message)
            throw be
        }catch(e: Exception){
            log.info(">>> [approve] Exception - message: {}", e.message)
            throw e
        }
    }

    override fun support(state: String): Boolean = CommCode.Social.KAKAO.code.equals(state)

    @CachePut(value = ["KAKAO"], key = "#input.orderId", unless = "#result == null")
    fun setReadyInfo(input: KakaoCacheInfo): KakaoCacheInfo {
        log.info(" >>> [setReadyInfo] Ready 정보 저장 - response: {}", input)
        return input
    }
}