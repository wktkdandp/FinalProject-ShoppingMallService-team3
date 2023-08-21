package com.petpal.swimmer_customer.data.repository

import androidx.lifecycle.LiveData
import com.petpal.swimmer_customer.data.model.User

interface UserRepository {
    fun addUser(user: User?): LiveData<Boolean?>?
    //fun getUserByIdx(userIdx: String?): LiveData<User?>?
    //LiveData<Boolean> modifyUser(String userIdx, String email, String password);
    fun deleteUser(userIdx: String?): LiveData<Boolean?>?

    fun findEmailbyInfo(nickname: String?, phoneNumber: String?): LiveData<User?>?
    fun resetPassword(email: String?, phoneNumber: String?): LiveData<Boolean?>?

    fun signInUser(email: String, password: String): LiveData<Boolean?>
    fun checkEmailDuplicated(email: String?): LiveData<Boolean>
}