package com.john.payment.payment.adapters.out.web.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

/**
 * @author yoonho
 * @since 2022.12.26
 */
data class KakaoReadyInfo (
   val tid: String,
   @JsonProperty("next_redirect_app_url")
   val nextRedirectAppUrl: String?,
   @JsonProperty("next_redirect_mobile_url")
   val nextRedirectMobileUrl: String?,
   @JsonProperty("next_redirect_pc_url")
   val nextRedirectPcUrl: String?,
   @JsonProperty("android_app_scheme")
   val androidAppScheme: String?,
   @JsonProperty("ios_app_scheme")
   val iosAppScheme: String?,
   @JsonProperty("created_at")
   val createdAt: LocalDateTime?,
)
