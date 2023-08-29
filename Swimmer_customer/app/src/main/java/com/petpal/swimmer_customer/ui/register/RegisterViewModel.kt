package com.petpal.swimmer_customer.ui.register

import android.content.Context
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.data.model.User
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository

class RegisterViewModel(private val customerUserRepository: CustomerUserRepository) : ViewModel() {

    fun addUser(role:String?,uid: String?, email: String?, password:String?, nickName: String?, phoneNumber: String?, swimExp: String?): LiveData<Boolean?>? {
        val user = User(role =role,uid = uid, email = email, password = password, nickName = nickName, phoneNumber = phoneNumber, swimExp = swimExp)
        return customerUserRepository.addUser(user)
    }

    fun checkEmailDuplicated(email: String): LiveData<Boolean> {
        //Log.d("koko12345",customerUserRepository.checkEmailDuplicated(email).value.toString() )
        return customerUserRepository.checkEmailDuplicated(email)
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
    fun isNicknameEmpty(nickName: String):Boolean{
        return nickName.isEmpty()
    }
    fun isPhoneNumberEmpty(phoneNumber: String):Boolean{
        return phoneNumber.isEmpty()
    }
    fun isPasswordMatched(password:String,passwordRepeat:String):Boolean{
        if(password==passwordRepeat) {
            return true;
        }
        return false
    }

    fun isConsentGiven(isChecked: Boolean): Boolean {
        return isChecked
    }




}