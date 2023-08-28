package com.petpal.swimmer_customer.ui.payment.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_customer.data.model.ItemsForCustomer
import com.petpal.swimmer_customer.ui.payment.repository.PaymentRepository
class PaymentViewModel: ViewModel() {

    var itemList = MutableLiveData<MutableList<ItemsForCustomer>>()
    var paymentFee = MutableLiveData<String>()

    init {
        itemList.value = mutableListOf()
    }

    fun getItemAndCalculatePrice() {
        val tempList1 = mutableListOf<ItemsForCustomer>()
        var tempCalculate: Long = 0

        // 아이템 정보 추출
        PaymentRepository.getCartItems {
            for (i in it.result.children) {
                val productUid = i.child("productUid").value as String
                val sellerUid = i.child("sellerUid").value as String
                val name = i.child("name").value as String
                val mainImage = i.child("mainImage").value as String
                val price = i.child("price").value as Long

                // 수량, 사이즈, 컬러는 data 생성된 후에 추가 테스트 진행 예정
                // dummy data 입력 완료
                val quantity = i.child("quantity").value as Long
                val size = i.child("size").value as String
                val color = i.child("color").value as String
                val buyerUid = i.child("buyerUid").value as String

                val itemModel = ItemsForCustomer(productUid, sellerUid, name, mainImage, price, quantity, size, color,buyerUid)

                tempList1.add(itemModel)
            }

            itemList.value = tempList1

            // 추출된 정보 중 갯수와 가격으로 mutablelivedata 넣기
            for (i in itemList.value!!) {
                tempCalculate +=  i.price * i.quantity
            }
            paymentFee.value = tempCalculate.toString()
        }
    }
}
