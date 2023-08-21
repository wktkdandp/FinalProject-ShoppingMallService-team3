package com.petpal.swimmer_customer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.petpal.swimmer_customer.databinding.FragmentHomeBinding
import com.petpal.swimmer_customer.ui.main.MainFragmentDirections


class HomeFragment : Fragment() {
    lateinit var fragmentHomeBinding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentHomeBinding= FragmentHomeBinding.inflate(layoutInflater)

        // 자동로그인 로그아웃 구현용
        fragmentHomeBinding.buttonLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val action = MainFragmentDirections.actionMainFragmentToLoginFragment()
            findNavController().navigate(action)
        }


        return fragmentHomeBinding.root
    }


}