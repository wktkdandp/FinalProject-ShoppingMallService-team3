package com.petpal.swimmer_seller.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.petpal.swimmer_seller.MainActivity
import com.petpal.swimmer_seller.R
import com.petpal.swimmer_seller.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    lateinit var fragmentMainBinding: FragmentMainBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMainBinding = FragmentMainBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        // navigation
        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        fragmentMainBinding.run {

            // 하단 네비바 이동
            bottomNavigation.run {
                // NavController 등록
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

}