package com.petpal.swimmer_customer.data.model

data class ItemsForCustomer (
    // String 으로 test 진행 후 static 처리로 long 타입 변환 예정
    val productUid: String, // 제품 uid
    val sellerUid: String, // 판매자 uid - 현재까진 값 임의 처리
    val name: String, // 제품명
    val mainImage: String, // 제품 상세페이지 메인이미지
    val price: Long, // 제품 가격

    // 임의 처리 필요한 데이터
    val quantity: Long, // 제품 수량 -> 상세 페이지에서 결정 후 전송
    val size: String, // 제품 사이즈 -> 상세 페이지에서 결정 후 전송
    val color: String, // 제품 색상 -> 상세 페이지에서 결정 후 전송
)