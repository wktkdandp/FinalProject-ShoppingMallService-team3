package com.petpal.swimmer_seller.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.petpal.swimmer_seller.R
import com.petpal.swimmer_seller.databinding.FragmentMainBinding
import com.petpal.swimmer_seller.ui.user.UserViewModel
import com.petpal.swimmer_seller.ui.user.UserViewModelFactory
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController

class MainFragment : Fragment() {
    private lateinit var fragmentMainBinding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMainBinding = FragmentMainBinding.inflate(inflater)

        Log.d("user", "mainFragment onCreate")

        // navigation
        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragmentMainContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        fragmentMainBinding.run {
            // 로그아웃 기능은 일단 홈에 배치하고 나중에 DrawerLayout 메뉴에 넣기

            bottomNavigation.run {
                setupWithNavController(navController)
                // 처음 화면 세팅
                selectedItemId = R.id.item_home

                setOnItemSelectedListener {
                    when (it.itemId) {
                        R.id.item_home -> {
                            // 홈 이동
                            navController.navigate(R.id.item_home)
                            true
                        }
                        R.id.item_menu -> {
                            // 메뉴 열기
                            true
                        }
                        R.id.item_mypage -> {
                            // 마이페이지 이동
                            true
                        }
                        else -> false
                    }
                }
            }
        }
        return fragmentMainBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("user", "mainFragment onDestroy")
    }
}