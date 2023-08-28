package com.petpal.swimmer_customer.ui.detailAddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.petpal.swimmer_customer.data.model.Address
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository

class DetailAddressViewModel(private val userRepository: CustomerUserRepository) : ViewModel() {
    val updateResult: MutableLiveData<Boolean?> = MutableLiveData()

    fun isValidName(name: String): Boolean {
        return name.trim().isNotEmpty()
    }

    fun isValidDetailAddress(detailAddress: String): Boolean {
        return detailAddress.trim().isNotEmpty()
    }

    fun isValidPhone(phone: String): Boolean {
        return phone.isNotEmpty()
    }

    fun addAddressForUser(uid: String, address: Address) {
        userRepository.addAddressForUser(uid, address).observeForever { result ->
            updateResult.value = result
        }
    }

}