package com.petpal.swimmer_customer.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_customer.data.model.User
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository

class RegisterViewModel(private val customerUserRepository: CustomerUserRepository) : ViewModel() {

    fun addUser(uid: String?, email: String?, password:String?, nickName: String?, phoneNumber: String?, swimExp: String?): LiveData<Boolean?>? {
        val user = User(uid = uid, email = email, password = password, nickName = nickName, phoneNumber = phoneNumber, swimExp = swimExp)
        return customerUserRepository.addUser(user)
    }

    fun checkEmailDuplicated(email: String): LiveData<Boolean> {
        //Log.d("koko12345",customerUserRepository.checkEmailDuplicated(email).value.toString() )
        return customerUserRepository.checkEmailDuplicated(email)
    }




}