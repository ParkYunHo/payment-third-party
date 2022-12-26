package com.john.payment.payment.adapters.out.web

import com.john.payment.common.constants.CommCode
import com.john.payment.common.exception.BadRequestException
import com.john.payment.common.exception.InternalServerErrorException
import com.john.payment.payment.application.port.out.PaymentPort
import jakarta.annotation.PostConstruct
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
    private val webClientBuilder: WebClient.Builder
): PaymentPort {
    private val log = LoggerFactory.getLogger(this::class.java)
    private lateinit var webClient: WebClient

    @Value("\${payment.apis.kakao}")
    private lateinit var url: String

    @Value("\${payment.keys.kakao}")
    private lateinit var adminKey: String

    @PostConstruct
    fun initWebClient() {
        this.webClient = webClientBuilder.build()
    }

    override fun ready() {
        try{
            val uriComponents = UriComponentsBuilder
                .fromHttpUrl(url + "/v1/payment/ready")
                .build(false)

            val params: MultiValueMap<String, String> = LinkedMultiValueMap()
            params.add("cid", "TC0ONETIME")
            params.add("partner_order_id", "partner_order_id")
            params.add("partner_user_id", "partner_user_id")
            params.add("item_name", "초코파이")
            params.add("quantity", "1")
            params.add("total_amount", "2200")
            params.add("vat_amount", "200")
            params.add("tax_free_amount", "0")
            // TEST
            params.add("approval_url", "http://localhost:8080/success")
            params.add("fail_url", "http://localhost:8080/success")
            params.add("cancel_url", "http://localhost:8080/success")
            //

            log.info(" >>> [ready] request - url: {}, body: {}", uriComponents.toUriString(), params.toString())
            val response = webClient.post()
                .uri(uriComponents.toUri())
                .header("Authorization", "KakaoAK " + adminKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(params))
                .exchangeToMono { response ->
                    if(response.statusCode().is4xxClientError) {
                        throw BadRequestException("카카오 결제페이지를 불러오는데 실패하였습니다.")
                    }else if(response.statusCode().is5xxServerError) {
                        throw InternalServerErrorException()
                    }else{
                        return@exchangeToMono response.toEntity(String::class.java)
                    }
                }
                .block()
            log.info(" >>> [ready] response - body: {}, status: {}", response.body, response.statusCode)

        }catch (be: BadRequestException) {
            log.info(">>> [ready] BadRequestException - message: {}", be.message)
            throw be
        }catch(e: Exception){
            log.info(">>> [ready] Exception - message: {}", e.message)
            throw e
        }
    }

    override fun approve() {
        TODO("Not yet implemented")
    }

    override fun support(state: String): Boolean = CommCode.Social.KAKAO.code.equals(state)
}