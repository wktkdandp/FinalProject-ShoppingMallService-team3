package com.petpal.swimmer_customer.ui.main

import android.os.Bundle

import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.databinding.FragmentMainBinding
import com.petpal.swimmer_customer.ui.HomeFragmentDirections
import com.petpal.swimmer_customer.ui.deliverypointmanage.DeliveryPointManageFragmentDirections
import com.petpal.swimmer_customer.ui.mypage.MypageFragmentDirections
import com.petpal.swimmer_customer.util.AutoLoginUtil

class MainFragment : Fragment() {
    lateinit var fragmentMainBinding: FragmentMainBinding
    private lateinit var navController:NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMainBinding = FragmentMainBinding.inflate(layoutInflater)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController


        val navOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_out_right)
            .build()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
               R.id.MainFragment, R.id.LoginFragment, R.id.RegisterFragment, R.id.FindInfoFragment,R.id.DetailAddressFragment,R.id.AddressDialogFragment -> {
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
                        navController.navigate(R.id.item_home,null,navOptions)
                        true
                    }

                    R.id.item_category -> {
                        //카테고리 페이지
                        navController.navigate(R.id.item_category,null,navOptions)
                        true
                    }

                    R.id.item_favorite -> {
                        //찜 페이지 이동
                        navController.navigate(R.id.item_favorite,null,navOptions)
                        true
                    }

                    R.id.item_history -> {
                        //히스토리 페이지 이동
                        navController.navigate(R.id.item_history,null,navOptions)
                        true
                    }

                    R.id.item_mypage -> {
                        // 마이페이지 이동

                        navController.navigate(R.id.item_mypage,null,navOptions)
                        true
                    }

                    else -> false
                }
            }
        }

        return fragmentMainBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AutoLogin()
    }
    private fun AutoLogin(){
        val isAutoLogin = AutoLoginUtil.getAutoLogin(requireContext())
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (!isAutoLogin) {
            navController.navigate(R.id.LoginFragment)
        }
    }
}