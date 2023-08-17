package com.petpal.swimmer_seller.data

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.petpal.swimmer_seller.data.model.LoggedInUser
import kotlinx.coroutines.tasks.await
import java.io.IOException

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class UserRepository {

    // in-memory cache of the loggedInUser object
    //TODO : firebase로 변경할 수 없나?
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    val auth = Firebase.auth

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        auth.signOut()
    }

    fun login(email: String, password: String, callback: (Task<AuthResult>) -> Unit){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(callback)
        //user를 살리려면 여기서 집어넣어줘야함
    }

    fun signUp(email: String, password: String, callback: (Task<AuthResult>) -> Unit) {
        //로그인된 유저가 없을 때만
        if (user == null) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(callback)
            //user를 살리려면 여기서도 집어넣어줘야함
        }
    }
}