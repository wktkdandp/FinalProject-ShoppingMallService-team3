package com.petpal.swimmer_customer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.petpal.swimmer_customer.databinding.FragmentFindInfoBinding
import com.petpal.swimmer_customer.main.MainActivity
import com.petpal.swimmer_customer.repository.CustomerUserRepository

class FindInfoFragment : Fragment() {
    lateinit var fragmentFindInfoBinding: FragmentFindInfoBinding
    lateinit var viewModel: LoginViewModel
    lateinit var auth: FirebaseAuth
    lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentFindInfoBinding= FragmentFindInfoBinding.inflate(layoutInflater)
        mainActivity=activity as MainActivity

        val factory = LoginViewModelFactory(CustomerUserRepository())
        viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        val bundle = arguments
        val findId = bundle?.getString("key")

        arguments?.let {
            when (it.getString("key")) {
                "findId" -> {
                    fragmentFindInfoBinding.statusHeader.text = "아이디 찾기"
                    fragmentFindInfoBinding.idFindLayout.visibility=View.VISIBLE
                    fragmentFindInfoBinding.passwordResetLayout.visibility=View.GONE
                }
                "resetPassword" -> {
                    fragmentFindInfoBinding.statusHeader.text = "비밀번호 재설정"
                    fragmentFindInfoBinding.idFindLayout.visibility=View.GONE
                    fragmentFindInfoBinding.passwordResetLayout.visibility=View.VISIBLE
                }
            }
        }
        fragmentFindInfoBinding.ButtonFindId.setOnClickListener {
            val nickname=fragmentFindInfoBinding.editTextText3.text.toString()
            val phoneNumber=fragmentFindInfoBinding.editTextText4.text.toString()
            viewModel.findEmailByInfo(nickname,phoneNumber)?.observe(viewLifecycleOwner, Observer{
                if(it !=null){
                    fragmentFindInfoBinding.textView6.text="당신의 이메일은 ${it.email}입니다"
                }else{
                    fragmentFindInfoBinding.textView6.text="이메일 찾기 실패"
                }
            })
        }
        fragmentFindInfoBinding.ButtonResetPassword.setOnClickListener {

            val email = fragmentFindInfoBinding.editTextText.text.toString()
            val phoneNumber = fragmentFindInfoBinding.editTextText4.text.toString()

            viewModel.resetPassword(email, phoneNumber)?.observe(viewLifecycleOwner, Observer { result ->
                if (result.isSuccess) {
                    fragmentFindInfoBinding.textView5.text = "비밀번호 재설정 이메일이 발송되었습니다."
                } else {
                    // 실패 메시지를 PasswordResetResult에서 가져옵니다.
                    fragmentFindInfoBinding.textView5.text = result.message ?: "비밀번호 재설정 실패했다리.."
                }
            })
        }








        return fragmentFindInfoBinding.root
    }

}

