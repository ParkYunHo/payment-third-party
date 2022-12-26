package com.john.payment.payment.adapters.out.web

import com.john.payment.common.constants.CommCode
import com.john.payment.common.exception.BadRequestException
import com.john.payment.common.exception.InternalServerErrorException
import com.john.payment.payment.adapters.`in`.web.dto.ApproveInput
import com.john.payment.payment.adapters.`in`.web.dto.PaymentInput
import com.john.payment.payment.adapters.out.web.dto.KakaoApproveInfo
import com.john.payment.payment.adapters.out.web.dto.KakaoReadyInfo
import com.john.payment.payment.application.port.out.PaymentPort
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
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
    private val kapiWebClient: WebClient
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
            params.add("cid", "TC0ONETIME")
            params.add("partner_order_id", "partner_order_id")
            params.add("partner_user_id", "partner_user_id")
            params.add("item_name", input.itemName)
            params.add("quantity", input.quantity.toString())
            params.add("total_amount", input.totalAmount.toString())
            params.add("vat_amount", input.vat.toString())
            params.add("tax_free_amount", input.taxFree.toString())
            params.add("approval_url", domain + "/pay/approve/" + CommCode.Social.KAKAO.code + "/testPgToken")
            params.add("fail_url", domain + "/pay/fail/" + CommCode.Social.KAKAO.code + "/testPgToken")
            params.add("cancel_url", domain + "/pay/cancel/" + CommCode.Social.KAKAO.code + "/testPgToken")

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

    override fun approve(input: ApproveInput): KakaoApproveInfo {
        try{
            val uriComponents = UriComponentsBuilder
                .fromHttpUrl(url + "/v1/payment/approve")
                .build(false)

            val params: MultiValueMap<String, String> = LinkedMultiValueMap()
            params.add("cid", "TC0ONETIME")
            if(!input.cidSecret.isNullOrEmpty()) {
                params.add("cid_secret", input.cidSecret)
            }
            params.add("tid", input.tid)
            params.add("partner_order_id", "partner_order_id")
            params.add("partner_user_id", "partner_user_id")
            params.add("pg_token", input.pgToken)
            params.add("payload", "")
            params.add("total_amount", input.totalAmount)

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
        }catch (be: BadRequestException) {
            log.info(">>> [approve] BadRequestException - message: {}", be.message)
            throw be
        }catch(e: Exception){
            log.info(">>> [approve] Exception - message: {}", e.message)
            throw e
        }
    }

    override fun support(state: String): Boolean = CommCode.Social.KAKAO.code.equals(state)
}