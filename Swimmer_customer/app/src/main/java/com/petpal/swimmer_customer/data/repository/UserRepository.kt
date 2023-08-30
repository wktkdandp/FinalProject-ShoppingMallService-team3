package com.petpal.swimmer_customer.data.repository

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import com.petpal.swimmer_customer.data.model.Address
import com.petpal.swimmer_customer.data.model.User

interface UserRepository {
    fun addUser(user: User?): LiveData<Boolean?>?


    fun findEmailbyInfo(nickname: String?, phoneNumber: String?): LiveData<User?>?
    fun resetPassword(email: String?, phoneNumber: String?): LiveData<Boolean?>?

    fun signInUser(email: String, password: String): LiveData<Boolean?>
    fun checkEmailDuplicated(email: String?): LiveData<Boolean>
    fun withdrawalUser(): LiveData<Boolean?>
    fun ModifyUserInfo(user: User): LiveData<Boolean?>
    fun modifyPassword(currentPassword: String, newPassword: String): LiveData<Boolean?>
    fun setAutoLogin(context: Context, value: Boolean)
    fun signOut()
    fun loadProfileImage(uid: String): LiveData<Uri?>
    fun uploadImageToFirebase(uid: String, uri: Uri): LiveData<Boolean>
    fun verifyPassword(password: String): LiveData<Boolean>
    fun getCurrentUser(): LiveData<User?>?
    fun getAllAddressesForUser(uid: String): LiveData<List<Address>>
    fun addAddressForUser(uid: String, address: Address): LiveData<Boolean?>
}