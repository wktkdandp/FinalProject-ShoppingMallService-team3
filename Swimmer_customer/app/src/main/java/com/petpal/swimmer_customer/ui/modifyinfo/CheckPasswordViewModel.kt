package com.petpal.swimmer_customer.ui.modifyinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository

class CheckPasswordViewModel(private val repository: CustomerUserRepository) : ViewModel() {

    fun checkPassword(password: String): LiveData<Boolean> {
        return repository.verifyPassword(password)
    }
}