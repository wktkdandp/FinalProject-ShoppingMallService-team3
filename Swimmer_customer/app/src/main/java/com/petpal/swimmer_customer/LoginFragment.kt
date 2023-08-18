package com.petpal.swimmer_customer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.internal.ViewUtils.showKeyboard
import com.google.firebase.auth.FirebaseAuth
import com.petpal.swimmer_customer.databinding.FragmentLoginBinding
import com.petpal.swimmer_customer.main.MainActivity
import com.petpal.swimmer_customer.repository.CustomerUserRepository

class LoginFragment : Fragment() {
    lateinit var fragmentLoginBinding: FragmentLoginBinding
    lateinit var viewModel: LoginViewModel
    lateinit var auth: FirebaseAuth
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity=activity as MainActivity
        fragmentLoginBinding = FragmentLoginBinding.inflate(layoutInflater)
        val factory = LoginViewModelFactory(CustomerUserRepository())
        viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        fragmentLoginBinding.ButtonLogin.setOnClickListener {
            val email = fragmentLoginBinding.textInputEditTextLoginEmail.text.toString()
            val password = fragmentLoginBinding.textInputEditTextLoginPassword.text.toString()
            if (!checkEmail(email)||checkPassword(password)) {
                return@setOnClickListener
            }


            viewModel.signIn(email, password).observe(viewLifecycleOwner, Observer { success ->
                if (success) {
                    // 로그인 성공
                    fragmentLoginBinding.textView.text = "로그인 성공"

                    //startActivity(Intent(context, MainActivity::class.java))
                } else {
                    // 로그인 실패
                    //일단 토스트로 근데 어떻게 표현할건지 상의해야함
                    Toast.makeText(context, "로그인 실패 이메일 또는 비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show()
                        fragmentLoginBinding.textInputLayoutAddUserEmail.error = ""
                        fragmentLoginBinding.textInputEditTextLoginEmail.text?.clear()
                        fragmentLoginBinding.textInputEditTextLoginPassword.text?.clear()

                }
            })
        }
        fragmentLoginBinding.ButtonRegister.setOnClickListener {
            //mainActivity.replaceFragment(MainActivity.Register_Fragment, true, false)
//            val action = LoginFragmentDirections.actionLoginFragmentToFindInfoFragment(findId = "findId")

            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }
        //아이디찾기 눌렀을 때
        fragmentLoginBinding.ButtonFindId.setOnClickListener {
//            val action = LoginFragmentDirections.actionLoginFragmentToFindInfoFragment("findID")
//            findNavController().navigate(action)
                    val bundle= bundleOf("key" to "findId")
            findNavController().navigate(R.id.action_loginFragment_to_findInfoFragment, bundle)
            //mainActivity.replaceFragment(MainActivity.Find_Info_Fragment,true,false)
        }
        //비밀번호찾기 눌렀을 때
        fragmentLoginBinding.ButtonResetPassword.setOnClickListener {
            val bundle= bundleOf("key" to "resetPassword")
            findNavController().navigate(R.id.action_loginFragment_to_findInfoFragment, bundle)
            //mainActivity.replaceFragment(MainActivity.Find_Info_Fragment,true,false)

        }
        //카카오 로그인 눌렀을 때
        fragmentLoginBinding.ButtonKakaoLogin.setOnClickListener {

        }


        return fragmentLoginBinding.root
    }

    private fun checkEmail(email: String): Boolean {
        if (!email.contains("@")) {
            fragmentLoginBinding.textInputLayoutAddUserEmail.error = "이메일 양식이 올바르지 않습니다."
            Handler(Looper.getMainLooper()).postDelayed({
                fragmentLoginBinding.textInputLayoutAddUserEmail.error = ""
                fragmentLoginBinding.textInputEditTextLoginEmail.text?.clear()
                showKeyboard(fragmentLoginBinding.textInputEditTextLoginEmail)
            }, 2000)
            return false
        }
        return true
    }
    private fun checkPassword(password: String): Boolean {
   if (password.length < 6) {
            fragmentLoginBinding.textInputLayoutLoginPassword.error = "비밀번호는 6자 이상이어야 합니다."
            Handler(Looper.getMainLooper()).postDelayed({
                fragmentLoginBinding.textInputLayoutLoginPassword.error = ""
                fragmentLoginBinding.textInputEditTextLoginPassword.text?.clear()
                showKeyboard(fragmentLoginBinding.textInputEditTextLoginPassword)
            }, 2000)
            return false
        }
        return true
    }

        fun showKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}


class LoginViewModelFactory(private val repository: CustomerUserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
