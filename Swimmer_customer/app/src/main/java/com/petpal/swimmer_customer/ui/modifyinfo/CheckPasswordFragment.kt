package com.petpal.swimmer_customer.ui.modifyinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.databinding.FragmentCheckPasswordBinding

//비밀번호만 체크하고 ModifyInfoFragment로 이동
class CheckPasswordFragment : Fragment() {
    lateinit var fragmentCheckPasswordBinding: FragmentCheckPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentCheckPasswordBinding= FragmentCheckPasswordBinding.inflate(layoutInflater)
        return fragmentCheckPasswordBinding.root
    }

}