package com.petpal.swimmer_customer.ui.mypage

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.petpal.swimmer_customer.data.model.User
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository
import com.petpal.swimmer_customer.util.AutoLoginUtil

class MypageViewModel(private val customerUserRepository: CustomerUserRepository) : ViewModel() {

    fun getCurrentUser(): LiveData<User?>? {
        return customerUserRepository.getCurrentUser()
    }

    fun uploadImageToFirebase(uri: Uri): LiveData<Boolean> {
        val userUID = FirebaseAuth.getInstance().currentUser?.uid
        return if (userUID != null) {
            customerUserRepository.uploadImageToFirebase(userUID, uri)
        } else {
            MutableLiveData<Boolean>().apply { value = false }
        }
    }

    fun loadProfileImage(): LiveData<Uri?> {
        val userUID = FirebaseAuth.getInstance().currentUser?.uid
        return if (userUID != null) {
            customerUserRepository.loadProfileImage(userUID)
        } else {
            MutableLiveData<Uri?>().apply { value = null }
        }
    }
    fun signOut() {
        customerUserRepository.signOut()
    }
    fun setAutoLoginEnabled(context: Context, enabled: Boolean) {
        customerUserRepository.setAutoLogin(context, enabled)
    }
}