package com.petpal.swimmer_customer.data.model

enum class PaymentMethod(var code: Long, var str: String) {
    NAVER(1, "네이버페이"),
    KAKAO(2, "카카오페이"),
    CARD(3, "카드결제"),
    CONV(4, "간편결제"),
    PHONE(5, "핸드폰결제"),
    TRANS(6, "무통장입금")
}

fun getPaymentMethod(code: Long? = null, str: String? = null): PaymentMethod {
    if (code != null) {
        when (code) {
            1L -> return PaymentMethod.NAVER
            2L -> return PaymentMethod.KAKAO
            3L -> return PaymentMethod.CARD
            4L -> return PaymentMethod.CONV
            5L -> return PaymentMethod.PHONE
            6L -> return PaymentMethod.TRANS
        }
    } else if (str != null) {
        when (str) {
            "네이버페이" -> return PaymentMethod.NAVER
            "카카오페이" -> return PaymentMethod.KAKAO
            "카드결제" -> return PaymentMethod.CARD
            "간편결제" -> return PaymentMethod.CONV
            "핸드폰결제" -> return PaymentMethod.PHONE
            "무통장입금" -> return PaymentMethod.TRANS
        }
    }
    return PaymentMethod.TRANS
}