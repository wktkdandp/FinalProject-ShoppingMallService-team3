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

class MainFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    lateinit var fragmentMainBinding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("user", "mainFragment onCreate")
        userViewModel =
            ViewModelProvider(this, UserViewModelFactory())[UserViewModel::class.java]

        fragmentMainBinding = FragmentMainBinding.inflate(layoutInflater)
        fragmentMainBinding.button.setOnClickListener {
            userViewModel.logOut()
            //메인 프래그먼트는 제거하고 로그인 프래그먼트로 이동
            findNavController().popBackStack(R.id.mainFragment, true)
            findNavController().navigate(R.id.loginFragment)
        }
        return fragmentMainBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("user", "mainFragment onDestroy")
    }

}