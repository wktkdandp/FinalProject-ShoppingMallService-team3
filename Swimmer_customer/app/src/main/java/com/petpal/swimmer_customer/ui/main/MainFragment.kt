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
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.alogin.LoginFragmentDirections
import com.petpal.swimmer_customer.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    lateinit var fragmentMainBinding: FragmentMainBinding
    lateinit var mainActivity: MainActivity
    //lateinit var auth:FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //자동로그인시 현재 사용자 인식하는지 확인

        fragmentMainBinding = FragmentMainBinding.inflate(layoutInflater)
        mainActivity=activity as MainActivity

        Log.d("currentUser", FirebaseAuth.getInstance().currentUser?.email!!)
        fragmentMainBinding.buttonLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val action = MainFragmentDirections.actionMainFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        fragmentMainBinding.bottomNavigation.setupWithNavController(navController)

        return fragmentMainBinding.root
    }

}