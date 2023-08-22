package com.petpal.swimmer_customer.ui.payment.vm

import android.content.ClipData.Item
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_customer.data.model.Items
import com.petpal.swimmer_customer.data.model.PaymentItems
import com.petpal.swimmer_customer.ui.payment.repository.PaymentRepository

class PaymentViewModel: ViewModel() {

    var itemList = MutableLiveData<MutableList<Items>>()
    var paymentFee = MutableLiveData<String>()

    init {
        itemList.value = mutableListOf()
    }

    fun getItemAndCalculatePrice() {
        val tempList1 = mutableListOf<Items>()
        var tempCalculate: Long = 0

        // 아이템 정보 추출
        PaymentRepository.getCartItems {
            for (i in it.result.children) {
                val name = i.child("name").value as String
                val mainImage = i.child("mainImage").value as String
                val price = i.child("price").value as Long

                // 옵션과 수량은 데이터 생기면 로딩
                val option = "test option"
                val count: Long = 1

                val item = Items(name, mainImage, price, count, option)

                tempList1.add(item)
            }

            itemList.value = tempList1

            // 추출된 정보 중 갯수와 가격으로 mutablelivedata 넣기
            for (i in itemList.value!!) {
                tempCalculate += i.count * i.price
            }
            paymentFee.value = tempCalculate.toString()
        }
    }
}
