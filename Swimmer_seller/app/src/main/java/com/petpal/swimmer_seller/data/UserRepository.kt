package com.petpal.swimmer_seller.data

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.functions.HttpsCallableResult
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.petpal.swimmer_seller.data.model.Seller

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class UserRepository {
    val auth = Firebase.auth
    val functions = Firebase.functions
    val sellerDatabase = Firebase.database.getReference("sellers")

    fun logout() {
        auth.signOut()
    }

    fun login(email: String, password: String, callback: (Task<AuthResult>) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(callback)
        //user를 살리려면 여기서 집어넣어줘야함
    }

    fun signUp(email: String, password: String, callback: (Task<AuthResult>) -> Unit) {
        //로그인된 유저가 없을 때만
        if (auth.currentUser == null) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(callback)
            //user를 살리려면 여기서도 집어넣어줘야함
        }
    }

    fun addSeller(seller: Seller, callback: (Task<Void>) -> Unit) {
        sellerDatabase.push().setValue(seller).addOnCompleteListener(callback)
    }

    fun sendPasswordResetEmail(email: String, callback: (Task<Void>) -> Unit) {
        //단말기 언어설정 따라가도록
        auth.useAppLanguage()
        auth.sendPasswordResetEmail(email).addOnCompleteListener(callback)
    }

    fun getSellerByEmail(email: String, callback: (Task<HttpsCallableResult>) -> Unit) {
        val data = hashMapOf(
            "email" to email
        )

        functions.getHttpsCallable("searchUserByEmail").call(data).addOnCompleteListener(callback)

    }
}

