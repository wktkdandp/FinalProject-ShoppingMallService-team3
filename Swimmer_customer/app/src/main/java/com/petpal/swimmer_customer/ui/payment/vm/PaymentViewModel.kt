package com.petpal.swimmer_customer.ui.payment.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_customer.data.model.Items
import com.petpal.swimmer_customer.ui.payment.repository.PaymentRepository

class PaymentViewModel: ViewModel() {

    var itemList = MutableLiveData<MutableList<Items>>()
    init {
        itemList.value = mutableListOf()
    }

    fun getItems() {
        val tempList = mutableListOf<Items>()

        PaymentRepository.getItems {
            for (i in it.result.children) {
                val code = i.child("code").value as String
                val name = i.child("name").value as String
                val mainImage = i.child("mainImage").value as String
                val price = i.child("price").value as Long

                val item = Items(code, name, mainImage, price)
                tempList.add(item)
                Log.d("!!", "$name")
            }

            itemList.value = tempList
        }
    }
}
