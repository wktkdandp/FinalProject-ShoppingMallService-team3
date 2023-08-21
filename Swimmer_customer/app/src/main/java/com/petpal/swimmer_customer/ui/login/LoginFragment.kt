package com.petpal.swimmer_customer.ui.login

import android.content.Context
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.databinding.FragmentLoginBinding
import com.petpal.swimmer_customer.ui.main.MainActivity
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository
import com.petpal.swimmer_customer.util.AutoLoginUtil

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

        val isAutoLogin = AutoLoginUtil.getAutoLogin(requireContext())
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (isAutoLogin && currentUser != null) {
            // 이미 로그인된 사용자가 있으면 바로 메인으로
            val action = LoginFragmentDirections.actionLoginFragmentToMainFragment()
            findNavController().navigate(action)
            return fragmentLoginBinding.root
        }

        val factory = LoginViewModelFactory(CustomerUserRepository())
        viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        //로그인 눌렀을 때
        fragmentLoginBinding.ButtonLogin.setOnClickListener {
            val email = fragmentLoginBinding.textInputEditTextLoginEmail.text.toString()
            val password = fragmentLoginBinding.textInputEditTextLoginPassword.text.toString()

            if (!checkEmail(email) || !checkPassword(password)) {
                return@setOnClickListener
            }
            viewModel.signIn(email, password)?.observe(viewLifecycleOwner, Observer { success ->
                handleLoginResult(success == true)

            })
        }
        fragmentLoginBinding.ButtonRegister.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }
        //아이디찾기 눌렀을 때
        fragmentLoginBinding.ButtonFindId.setOnClickListener {
            val bundle= bundleOf("key" to "findId")
            findNavController().navigate(R.id.action_loginFragment_to_findInfoFragment, bundle)
        }
        //비밀번호찾기 눌렀을 때
        fragmentLoginBinding.ButtonResetPassword.setOnClickListener {
            val bundle= bundleOf("key" to "resetPassword")
            findNavController().navigate(R.id.action_loginFragment_to_findInfoFragment, bundle)

        }
        //카카오 로그인 눌렀을 때
        fragmentLoginBinding.ButtonKakaoLogin.setOnClickListener {

        }
        return fragmentLoginBinding.root
    }

    //이메일 유효성검사
    private fun checkEmail(email: String): Boolean {

        if(email.isEmpty()){
            fragmentLoginBinding.textInputLayoutAddUserEmail.error = "이메일을 입력해주세요."
            Handler(Looper.getMainLooper()).postDelayed({
                fragmentLoginBinding.textInputLayoutAddUserEmail.error = ""
                fragmentLoginBinding.textInputEditTextLoginEmail.text?.clear()
                showKeyboard(fragmentLoginBinding.textInputEditTextLoginEmail)
            }, 2000)
        }else if (!email.contains("@")) {
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
    //비밀번호 유효성 검사
    private fun checkPassword(password: String): Boolean {
   if (password.length < 6 || password.isEmpty()) {
            fragmentLoginBinding.textInputLayoutLoginPassword.error = "비밀번호는 6자 이상이어야 합니다."
            Handler(Looper.getMainLooper()).postDelayed({
                fragmentLoginBinding.textInputLayoutLoginPassword.error = ""
                fragmentLoginBinding.textInputEditTextLoginPassword.text?.clear()
            }, 2000)
            return false
        }
        return true
    }
        //키보드 올리기
        fun showKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
    //로그인 메서드 관리
    private fun handleLoginResult(success: Boolean) {
        if (success) {
            // 로그인 성공
            Toast.makeText(context, "로그인 성공", Toast.LENGTH_LONG).show()
            val action = LoginFragmentDirections.actionLoginFragmentToMainFragment()
            findNavController().navigate(action)

            val isAutoLoginChecked = fragmentLoginBinding.checkboxAutoLogin.isChecked
            AutoLoginUtil.setAutoLogin(requireContext(), isAutoLoginChecked)
        } else {
            // 로그인 실패
            Toast.makeText(context, "로그인 실패 이메일 또는 비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show()
            fragmentLoginBinding.textInputLayoutAddUserEmail.error = ""
            fragmentLoginBinding.textInputEditTextLoginEmail.text?.clear()
            fragmentLoginBinding.textInputEditTextLoginPassword.text?.clear()
        }
    }
}

//뷰모델 팩토리
class LoginViewModelFactory(private val repository: CustomerUserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
