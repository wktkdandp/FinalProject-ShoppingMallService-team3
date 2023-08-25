package com.petpal.swimmer_seller.data.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
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
    private val auth = Firebase.auth
    private val functions = Firebase.functions
    private val sellerDatabase = Firebase.database.getReference("sellers")

    fun logout() {
        auth.signOut()
    }

    fun login(email: String, password: String, callback: (Task<AuthResult>) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(callback)
    }

    fun signUp(email: String, password: String, callback: (Task<AuthResult>) -> Unit) {
        //로그인된 유저가 없을 때만
        if (auth.currentUser == null) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(callback)
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

    fun findEmail(name: String, phone: String, callback: (Task<DataSnapshot>) -> Unit) {
        //db에서 name과 phone번호 둘다 일치하는 user의 email을 가지고 온다
        sellerDatabase.orderByChild("contactPhoneNumber").equalTo(phone).get().addOnCompleteListener(callback)
    }

    fun getCurrentSellerInfo(callback: (Task<DataSnapshot>) -> Unit) {
        //현재 user의 email정보를 sellers db에서 찾아서 그 정보 가져오기
        val currentUserEmail = auth.currentUser!!.email
        Log.d("currentUser", currentUserEmail!!)
        sellerDatabase.orderByChild("email").equalTo(currentUserEmail).get().addOnCompleteListener(callback)
    }
}

