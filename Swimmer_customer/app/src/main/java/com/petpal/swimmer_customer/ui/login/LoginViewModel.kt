package com.petpal.swimmer_customer.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_customer.data.model.User
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository

class LoginViewModel(private val customerUserRepository: CustomerUserRepository) : ViewModel() {

    fun signIn(email: String, password: String): LiveData<Boolean?>? {
        return customerUserRepository.signInUser(email,password)
    }

    fun getUserByIdx(userIdx: String): LiveData<User?>? {
        return customerUserRepository.getUserByIdx(userIdx)
    }




//    fun modifyUser(userIdx: String, email: String, password: String): LiveData<Boolean> {
//        return customerUserRepository.modifyUser(userIdx,email,password)
//    }

//    fun deleteUser(userIdx: String): LiveData<Boolean?>? {
//        return customerUserRepository.deleteUser(userIdx)
//    }
}

