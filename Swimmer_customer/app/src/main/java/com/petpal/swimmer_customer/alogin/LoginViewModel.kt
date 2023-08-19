package com.petpal.swimmer_customer.`1login`

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_customer.model.User
import com.petpal.swimmer_customer.repository.CustomerUserRepository

class LoginViewModel(private val customerUserRepository: CustomerUserRepository) : ViewModel() {

    fun register(uid: String?,email: String?, password:String?, nickName: String?, phoneNumber: String?,swimExp:String?): LiveData<Boolean?>? {
        val user = User(uid = uid,email=email,password=password, nickName = nickName, phoneNumber = phoneNumber, swimExp = swimExp)
        Log.d("viewmodel",(user.uid).toString())
        return customerUserRepository.addUser(user)
    }


    fun signIn(email: String, password: String): LiveData<Boolean?>? {
        Log.d("viewModel",email)
        Log.d("viewModel",password)
        return customerUserRepository.signInUser(email,password)
    }

    fun getUserByIdx(userIdx: String): LiveData<User?>? {
        return customerUserRepository.getUserByIdx(userIdx)
    }
    fun checkEmailDuplicate(email:String):LiveData<Boolean>{
        return customerUserRepository.checkEmailDuplicate(email)
    }

    fun findEmailByInfo(nickName: String?,phoneNumber: String?): LiveData<User?>? {
        return customerUserRepository.findEmailbyInfo(nickName,phoneNumber)
    }
    fun resetPassword(email:String,phoneNumber:String): LiveData<Boolean?>?{
        Log.d("viewmodel",email)
        Log.d("viewmodel",phoneNumber)
        return customerUserRepository.resetPassword(email,phoneNumber)
    }


//    fun modifyUser(userIdx: String, email: String, password: String): LiveData<Boolean> {
//        return customerUserRepository.modifyUser(userIdx,email,password)
//    }

    fun deleteUser(userIdx: String): LiveData<Boolean?>? {
        return customerUserRepository.deleteUser(userIdx)
    }
}

