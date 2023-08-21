package com.petpal.swimmer_seller.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.petpal.swimmer_seller.data.repository.UserRepository

class UserViewModelFactory : ViewModelProvider.Factory {

    //Unchecked cast 경고 무시
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(UserRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}