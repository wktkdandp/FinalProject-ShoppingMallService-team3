package com.petpal.swimmer_customer.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.petpal.swimmer_customer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
    }
}


