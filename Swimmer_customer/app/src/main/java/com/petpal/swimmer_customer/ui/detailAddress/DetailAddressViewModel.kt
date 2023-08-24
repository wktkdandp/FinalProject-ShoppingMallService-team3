package com.petpal.swimmer_customer.ui.detailAddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.petpal.swimmer_customer.data.model.Address
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository

class DetailAddressViewModel(private val customerUserRepository: CustomerUserRepository) : ViewModel() {

    private val userRepository = CustomerUserRepository()
    val updateResult: MutableLiveData<Boolean?> = MutableLiveData()

//    fun updateUserAddress(uid: String, address: Address) {
//        userRepository.updateUserAddress(uid, address).observeForever { result ->
//            updateResult.value = result
//        }
//    }
    fun addAddressForUser(uid: String, address: Address) {
        userRepository.addAddressForUser(uid, address).observeForever { result ->
            updateResult.value = result
        }
    }

}