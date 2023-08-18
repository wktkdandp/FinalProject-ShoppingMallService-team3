package com.petpal.swimmer_customer.repository

import androidx.lifecycle.LiveData
import com.petpal.swimmer_customer.model.User

interface UserRepository {
    fun addUser(user: User?): LiveData<Boolean?>?
    fun getUserByIdx(userIdx: String?): LiveData<User?>?

    //LiveData<Boolean> modifyUser(String userIdx, String email, String password);
    fun deleteUser(userIdx: String?): LiveData<Boolean?>?

    //LiveData<Boolean> findPassword(String email);
    fun findEmailbyInfo(nickname: String?, phoneNumber: String?): LiveData<User?>?
    fun resetPassword(email: String?, phoneNumber: String?): LiveData<PasswordResetResult>?
}