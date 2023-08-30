package com.petpal.swimmer_seller.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import com.petpal.swimmer_seller.R
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.setupWithNavController
import com.petpal.swimmer_seller.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var fragmentMainBinding: FragmentMainBinding
    private var currentTab: Int = 0     // 시작은 홈 화면

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
            if (navDestination.id == R.id.item_home || navDestination.id == R.id.item_menu || navDestination.id == R.id.item_mypage ) {
                fragmentMainBinding.bottomNavigation.visibility = View.VISIBLE
            } else {
                fragmentMainBinding.bottomNavigation.visibility = View.GONE
            }
        }

        fragmentMainBinding.run {
            bottomNavigation.run {
                setupWithNavController(navController)
                // navigationUI 덕에 menu & nav_graph id가 매칭되어서 자동으로 전환되지만 이러면 현재 item기준 순서를 체크할 수 없음. 동적 애니메이션 효과를 주기위해 수동 설정
                setOnItemSelectedListener {
                    // 눈으로 보이는 순서대로 item 인덱싱
                    val nextTab = when (it.itemId) {
                        R.id.item_menu -> 0
                        R.id.item_home -> 1
                        R.id.item_mypage -> 2
                        else -> currentTab
                    }

                    // 이동할 아이템이 현재보다 오른쪽에 위치할 때 애니메이션 : 왼 <- 오
                    val navOptions = if (nextTab > currentTab) {
                        NavOptions.Builder()
                            .setEnterAnim(R.anim.slide_in_right)
                            .setExitAnim(R.anim.slide_out_left)
                            .setPopEnterAnim(R.anim.slide_in_left)
                            .setPopExitAnim(R.anim.slide_out_right)
                            .build()
                    } else {
                        // 이동할 아이템이 현재보다 왼쪽에 위치할 때 애니메이션 : 왼 -> 오
                        NavOptions.Builder()
                            .setEnterAnim(R.anim.slide_in_left)
                            .setExitAnim(R.anim.slide_out_right)
                            .setPopEnterAnim(R.anim.slide_in_right)
                            .setPopExitAnim(R.anim.slide_out_left)
                            .build()
                    }
                    // 현재탭 세팅
                    currentTab = nextTab
                    when (it.itemId) {
                        R.id.item_menu -> {
                            navController.navigate(R.id.item_menu, null, navOptions)
                            true
                        }
                        R.id.item_home -> {
                            navController.navigate(R.id.item_home, null, navOptions)
                            true
                        }
                        R.id.item_mypage -> {
                            navController.navigate(R.id.item_mypage, null, navOptions)
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