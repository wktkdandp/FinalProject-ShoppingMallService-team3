package com.petpal.swimmer_seller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        auth = Firebase.auth

        val currentUser = auth.currentUser
        if (currentUser != null) {
            //이미 로그인돼있다면 mainFragment로 이동
            Log.d("user", currentUser.uid)
            navController.popBackStack(R.id.loginFragment, true)
            navController.navigate(R.id.mainFragment)
        }
    }
}