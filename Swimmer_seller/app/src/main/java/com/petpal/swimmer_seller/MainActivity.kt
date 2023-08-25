package com.petpal.swimmer_seller

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var loginSellerUid: String = ""
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 테스트용 판매자 uid
        // loginSellerUid = "-Nc6d4aE3D0UeN09R3XV"

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        auth = Firebase.auth

        val currentUser = auth.currentUser
        if (currentUser != null) {
            //이미 로그인돼있다면 mainFragment로 이동
            Log.d("user", currentUser.uid)
            // 로그인 판매자 uid 저장
            loginSellerUid = currentUser.uid
            navController.popBackStack(R.id.loginFragment, true)
            navController.navigate(R.id.mainFragment)
        }
    }

    // 입력 요소에 포커스를 주는 메서드
    fun showSoftInput(view: View) {
        view.requestFocus()

        val inputMethodManger = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        thread {
            SystemClock.sleep(200)
            inputMethodManger.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun navigate(id: Int) {
        navController.navigate(id)
    }
}