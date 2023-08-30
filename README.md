# FinalProject-ShoppingMallService-team3
테킷 앱스쿨:안드로이드1기 최종 프로젝트(쇼핑몰 서비스 개발) - 3팀
## 🩱 Swimmer

**SWIMMER**는 수영 입문자를 위한 수영용품 쇼핑몰 앱입니다. 여성, 남성, 아동을 대상으로 한 수영복과 다양한 수영용품을 구매할 수 있습니다.

**SWIMMER Seller**는 사업자를 위한 수영용품 판매 앱입니다. 자사의 상품을 쇼핑몰에 등록하고 관리할 수 있습니다.  등록된 상품은 SWIMMER에 연동되어 표시되며, SWIMMER에서 소비자가 상품을 구매하면 판매자앱에서 해당 주문 내역을 확인할 수 있습니다.


<p align="center"><img src="https://user-images.githubusercontent.com/86085387/263642030-c94608c2-002d-4f0a-8e67-7d63f8597266.jpg" width="900" /></p>

<p align="center"><img src="https://user-images.githubusercontent.com/86085387/263652689-6ef150de-e237-4fcb-b2ad-b060db59def9.png" width="900"/></p>

## 📜 프로젝트 기획 의도
- 시장조사를 통해 시중에 있는 수영용품 쇼핑몰들의 장단점을 파악하여 기존 서비스들의 단점을 개선한, 사용자들에게 깔끔하고 사용하기 쉬운 수영용품 종합 쇼핑몰 앱을 개발하고자 합니다.

## 🌱 팀원 소개
🦁 : 리더 <br>
🐯 : 부 리더 <br>
🐹 : 팀원 <br>

| Name   | Part        |Github|
|--------|-------------|---|
| 🦁 최가연 | android |[gayeon00](https://github.com/gayeon00)|
| 🐯 고진호 | android     |[wktkdandp](https://github.com/wktkdandp)|
| 🐹 이해현 | android     |[haehyun-lee](https://github.com/haehyun-lee)|
| 🐹 김진섭 | android     |[kimjinsub1217](https://github.com/kimjinsub1217)|
| 🐹 이지헌 | android     |[Jiheon-Lee98](https://github.com/Jiheon-Lee98)|
| 🐹 윤희서 | android     |[hailey-yoon10](https://github.com/hailey-yoon10)|

## 🗓️ 개발 기간
- 기획, 디자인, 설계 : 23년 8월 10일 ~ 23년 8월 16일
- 구현 : 23년 8월 17일 ~ 23년 8월 30일

<img src=image/img.png width="600" >

## 🔨 기술 스택
- 개발 IDE : Android Studio (Flamingo 2022.2.1)
- 개발 언어 : Kotlin
- SDK : 13 Tiramisu (API 33)
- Architecture: MVVM
- 서버 : FireBase(Realtime Database, Storage, Authentication)

## 📱 서비스 흐름 구조도

### 소비자용 서비스 구조도 
<img src= "https://github.com/APPSCHOOL2-Android/FinalProject-ShoppingMallService-team3/assets/68911884/20ff0dce-0df0-4cd7-8218-bdf7301774e0" width="600">
<img src="https://github.com/APPSCHOOL2-Android/FinalProject-ShoppingMallService-team3/assets/68911884/ff16f7e4-e6af-4f7f-929b-7b0d1c42d5c7" width="900" >

### 판매자용 서비스 구조도
<img src="https://github.com/APPSCHOOL2-Android/FinalProject-ShoppingMallService-team3/assets/68911884/acbe69cc-5008-4ae5-b3a3-84cd7aa84167" width="900" >
<img src="https://github.com/APPSCHOOL2-Android/FinalProject-ShoppingMallService-team3/assets/68911884/1f8878f8-be5d-4267-b862-bbd91f4bfa9c" width="900" >
<img src="https://github.com/APPSCHOOL2-Android/FinalProject-ShoppingMallService-team3/assets/68911884/ccf5ef07-e5ab-4e05-b7c7-88d01159e566" width="500" >


## 🎮 주요 기능 설명
### ⭐소비자용 서비스⭐

1. 메인화면
- 배너 이미지 - 기획전, 세일정보, 브랜드 이슈등이 보여진다.
- 랭킹, 초심자, 신상품, 이벤트 탭이 있다. (현재 랭킹탭만 구현)
- 판매자가 등록한 상품들이 보여진다.

<img src=image/image.png width="300"  ><img src=image/image1.png width="300" >


2. 상품 상세 화면
- 상품 이미지 가로 스크롤로 탐색
- 브랜드, 상품명, 가격 표시
- 해시태그를 통해 제품의 키워드를 한눈에 볼 수 있다.


<img src=image/img_12.png width="300" ><img src=image/img_13.png width="300" >

3. 주문 화면
- 배송지 선택하기 버튼을 누르면 배송지 목록이 뜨고, 배송지 추가도 가능하다. 목록 중 하나를 선택하면 배송지로 선택된다.
- 배송지 추가 받는 사람, 주소(우편번호(주소 검색), 나머지 주소)
- 배송 메모(리스트 다이얼로그로 몇 개 제공)
- 주문 상품 목록 표시 (상품명, 사이즈 , 색상, 가격 , 수량)
- 결제하기 버튼(총 금액 함께 보여주기)


<img src=image/image-2.png width="300" ><img src=image/image-3.png width="300" >


### ⭐판매자용 서비스⭐

1. 메인 화면
- 결제 완료, 배송 준비, 배송중, 배송 완료가 한눈에 보인다.
- 상품 등록 버튼을 누르면 상품을 등록할 수 있다.


<img src=image/img_17.png width="300" >

2. 상품 등록
- 상품 이미지를 최대 5개 등록할 수 있다.
- 상품명, 대분류, 중분류, 소분류, 해시태그, 가격, 상품 설명, 상품 설명 이미지를 입력받는다.
- 강조하고 싶은 키워드를 해시태그로 등록할 수 있다.


<img src=image/img_18.png width="300" ><img src=image/img_20.png width="300" >

3. 상품 등록 옵션
- 옵션 추가를 클릭하면 색상과 사이즈를 추가할 수 있다.
- 상품 등록 버튼을 누르면 Snackbar를 띄워 상품 등록이 완료됐다고 알려준다.


<img src=image/img_23.png width="300" ><img src=image/img_26.png width="300" >

4. 주문 내역 확인
- 판매자에게 들어온 주문내역을 확인할 수 있다.
- 주문의 상태에 따라 필터링해서 확인할 수 있다.


<img src="https://github.com/APPSCHOOL2-Android/FinalProject-ShoppingMallService-team3/assets/68911884/3e0a4f9a-df51-4e39-ba58-13eb82e389e2" width="300"><img src="https://github.com/APPSCHOOL2-Android/FinalProject-ShoppingMallService-team3/assets/68911884/f49a88ef-b98c-4c4e-8b23-2c4cdb538267" width="300">
