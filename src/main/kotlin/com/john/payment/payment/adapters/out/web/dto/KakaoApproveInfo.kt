package com.john.payment.payment.adapters.out.web.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

/**
 * @author yoonho
 * @since 2022.12.26
 */
data class KakaoApproveInfo (
   val aid: String?,
   val tid: String,
   val cid: String?,
   val sid: String?,
   @JsonProperty("partner_order_id")
   val partnerOrderId: String?,
   @JsonProperty("partner_user_id")
   val partnerUserId: String?,
   @JsonProperty("payment_method_type")
   val paymentMethodType: String?,
   val amount: Amount?,
   @JsonProperty("card_info")
   val cardInfo: CardInfo?,
   @JsonProperty("item_name")
   val itemName: String?,
   @JsonProperty("item_code")
   val itemCode: String?,
   val quantity: Int?,
   @JsonProperty("created_at")
   val createdAt: LocalDateTime?,
   @JsonProperty("approved_at")
   val approvedAt: LocalDateTime?,
   val payload: String?
) {
   data class Amount(
      val total: Int,
      @JsonProperty("tax_free")
      val taxFree: Int,
      val vat: Int,
      val point: Int,
      val discount: Int,
      @JsonProperty("green_deposit")
      val greenDeposit: Int,
   )

   data class CardInfo(
      @JsonProperty("purchase_corp")
      val purchaseCorp: String,
      @JsonProperty("purchase_corp_code")
      val purchaseCorpCode: String,
      @JsonProperty("issuer_corp")
      val issuerCorp: String,
      @JsonProperty("issuer_corp_code")
      val issuerCorpCode: String,
      @JsonProperty("kakaopay_purchase_corp")
      val kakaopayPurchaseCorp: String,
      @JsonProperty("kakaopay_purchase_corp_code")
      val kakaopayPurchaseCorpCode: String,
      @JsonProperty("kakaopay_issuer_corp")
      val kakaopayIssuerCorp: String,
      @JsonProperty("kakaopay_issuer_corp_code")
      val kakaopayIssuerCorpCode: String,
      val bin: String,
      @JsonProperty("card_type")
      val cardType: String,
      @JsonProperty("install_month")
      val installMonth: String,
      @JsonProperty("approved_id")
      val approvedId: String,
      @JsonProperty("card_mid")
      val cardMid: String,
      @JsonProperty("interest_free_install")
      val interestFreeInstall: String,
      @JsonProperty("card_item_code")
      val cardItemCode: String,
   )
}
