package com.petpal.swimmer_customer.ui.modifyinfo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository
import kotlinx.coroutines.launch

class ModifyInfoViewModel(private val repository: CustomerUserRepository) : ViewModel() {

    fun modifyPassword(currentPassword: String, newPassword: String): LiveData<Boolean?> {
        return repository.modifyPassword(currentPassword, newPassword)
    }
    fun signOut() {
        repository.signOut()
    }
    fun setAutoLoginEnabled(context: Context, enabled: Boolean) {
        repository.setAutoLogin(context, enabled)
    }
}