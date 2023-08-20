package com.petpal.swimmer_customer.ui.findinfo


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_customer.data.model.User
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository

class FindInfoViewModel(private val customerUserRepository: CustomerUserRepository) : ViewModel() {

    fun findEmailByInfo(nickName: String?, phoneNumber: String?): LiveData<User?>? {
        return customerUserRepository.findEmailbyInfo(nickName, phoneNumber)
    }

    fun resetPassword(email: String, phoneNumber: String): LiveData<Boolean?>? {
        return customerUserRepository.resetPassword(email, phoneNumber)
    }
}