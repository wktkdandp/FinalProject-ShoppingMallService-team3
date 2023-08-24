package com.petpal.swimmer_seller.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.petpal.swimmer_seller.R
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.petpal.swimmer_seller.databinding.FragmentMainBinding

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

        // 특정 화면에서 BottomNavigationBar 숨기기
        navController.addOnDestinationChangedListener { _: NavController, navDestination: NavDestination, _: Bundle? ->
            if (navDestination.id == R.id.item_product_add || navDestination.id == R.id.item_manage_order || navDestination.id == R.id.productOptionFragment) {
                fragmentMainBinding.bottomNavigation.visibility = View.GONE
            } else {
                fragmentMainBinding.bottomNavigation.visibility = View.VISIBLE
            }
        }

        fragmentMainBinding.run {
            // 로그아웃 버튼은 일단 Home 화면에 옮겨뒀습니다.

            bottomNavigation.setupWithNavController(navController)

        }
        return fragmentMainBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("user", "mainFragment onDestroy")
    }
}