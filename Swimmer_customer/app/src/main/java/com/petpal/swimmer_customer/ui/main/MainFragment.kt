package com.petpal.swimmer_customer.ui.main

import android.os.Bundle

import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

import androidx.navigation.ui.setupWithNavController
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    lateinit var fragmentMainBinding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMainBinding = FragmentMainBinding.inflate(layoutInflater)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.LoginFragment, R.id.RegisterFragment, R.id.FindInfoFragment -> {
                    fragmentMainBinding.bottomNavigation.visibility = View.GONE
                }
                else -> {
                    fragmentMainBinding.bottomNavigation.visibility = View.VISIBLE
                }
            }
        }

        fragmentMainBinding.bottomNavigation.run {
            setupWithNavController(navController)
            //화면 세팅
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.item_home -> {
                        // 홈 이동
                        navController.navigate(R.id.item_home)
                        true
                    }

                    R.id.item_category -> {
                        //카테고리 페이지
                        navController.navigate(R.id.item_category)
                        true
                    }

                    R.id.item_favorite -> {
                        //찜 페이지 이동
                        navController.navigate(R.id.item_favorite)
                        true
                    }

                    R.id.item_history -> {
                        //히스토리 페이지 이동
                        navController.navigate(R.id.item_history)
                        true
                    }

                    R.id.item_mypage -> {
                        // 마이페이지 이동
                        navController.navigate(R.id.item_mypage)
                        true
                    }

                    else -> false
                }
            }
        }

        return fragmentMainBinding.root
    }
}