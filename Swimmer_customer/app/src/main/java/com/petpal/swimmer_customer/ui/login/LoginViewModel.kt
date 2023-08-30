package com.petpal.swimmer_customer.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_customer.data.model.User
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository

class LoginViewModel(private val customerUserRepository: CustomerUserRepository) : ViewModel() {

    fun signIn(email: String, password: String): LiveData<Boolean?>? {
        return customerUserRepository.signInUser(email,password)
    }

    fun isEmailEmpty(email: String): Boolean {
        return email.isEmpty()
    }

    fun isValidEmailFormat(email: String): Boolean {
        return email.contains("@")
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6 && password.isNotEmpty()
    }
    fun getCurrentUser(): LiveData<User?>? {
        return customerUserRepository.getCurrentUser()
    }


}

