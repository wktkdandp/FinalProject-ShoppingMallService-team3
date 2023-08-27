package com.petpal.swimmer_customer.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.databinding.ActivityMainBinding
import com.petpal.swimmer_customer.databinding.FragmentMainBinding
import com.petpal.swimmer_customer.util.AutoLoginUtil

class MainActivity : AppCompatActivity() {
    lateinit var activityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //installSplashScreen()
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)



        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView2, MainFragment())
                .commit()
        }
    }

}