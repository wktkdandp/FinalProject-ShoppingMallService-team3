package com.petpal.swimmer_customer.ui.modifyinfo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petpal.swimmer_customer.data.model.User
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository
import kotlinx.coroutines.launch

class ModifyInfoViewModel(private val repository: CustomerUserRepository) : ViewModel() {
    val withdrawalUserResult: MutableLiveData<Boolean?> = MutableLiveData()

    fun modifyPassword(currentPassword: String, newPassword: String): LiveData<Boolean?> {
        return repository.modifyPassword(currentPassword, newPassword)
    }
    fun signOut() {
        repository.signOut()
    }
    fun setAutoLoginEnabled(context: Context, enabled: Boolean) {
        repository.setAutoLogin(context, enabled)
    }
    fun getCurrentUser(): LiveData<User?>? {
        return repository.getCurrentUser()
    }
    fun isValidPassword(password: String): Boolean {
        return password.length >= 6 && password.isNotEmpty()
    }
    fun ModifyUserInfo(user: User): LiveData<Boolean?> {
        return repository.ModifyUserInfo(user)
    }
    fun withdrawalUser() {
        viewModelScope.launch {
            repository.withdrawalUser().observeForever { result ->
                withdrawalUserResult.postValue(result)
            }
        }
    }
}