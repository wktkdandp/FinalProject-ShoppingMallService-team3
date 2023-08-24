package com.petpal.swimmer_customer.ui.login

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet.Motion
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository
import com.petpal.swimmer_customer.databinding.FragmentLoginBinding
import com.petpal.swimmer_customer.ui.main.MainActivity
import com.petpal.swimmer_customer.util.AutoLoginUtil

//로그인의 유효성검사를 실시하고 로그인 메서드 실행
//util의 AutoLoginUtil을 이용하여 자동 로그인 처리

class LoginFragment : Fragment() {
    lateinit var fragmentLoginBinding: FragmentLoginBinding
    lateinit var viewModel: LoginViewModel
    //lateinit var auth: FirebaseAuth
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

            findNavController().navigate(R.id.item_home)
            return fragmentLoginBinding.root
        }

            val factory = LoginViewModelFactory(CustomerUserRepository())
            viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

            //로그인 눌렀을 때
            fragmentLoginBinding.ButtonLogin.setOnClickListener {
                val email = fragmentLoginBinding.textInputEditTextLoginEmail.text.toString()
                val password = fragmentLoginBinding.textInputEditTextLoginPassword.text.toString()

                //유효성 검사 false -> 리스너 종료
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
            val bundle= bundleOf(getString(R.string.findinfokey) to getString(R.string.findid))
            findNavController().navigate(R.id.action_loginFragment_to_findInfoFragment, bundle)
        }
        //비밀번호찾기 눌렀을 때
        fragmentLoginBinding.ButtonResetPassword.setOnClickListener {
            val bundle= bundleOf(getString(R.string.findinfokey) to getString(R.string.resetpassword))
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
            fragmentLoginBinding.textInputLayoutAddUserEmail.error = getString(R.string.error_email_required)
            Handler(Looper.getMainLooper()).postDelayed({
                fragmentLoginBinding.textInputLayoutAddUserEmail.error = ""
                fragmentLoginBinding.textInputEditTextLoginEmail.text?.clear()
                showKeyboard(fragmentLoginBinding.textInputEditTextLoginEmail)
            }, 2000)
            return false
        }else if (!email.contains("@")) {
            fragmentLoginBinding.textInputLayoutAddUserEmail.error = getString(R.string.error_invalid_email_format)
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
            fragmentLoginBinding.textInputLayoutLoginPassword.error = getString(R.string.error_password_length)
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
            Toast.makeText(context, getString(R.string.login_success), Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.item_home)

            val isAutoLoginChecked = fragmentLoginBinding.checkboxAutoLogin.isChecked
            AutoLoginUtil.setAutoLogin(requireContext(), isAutoLoginChecked)
        } else {
            // 로그인 실패
            Toast.makeText(context, getString(R.string.login_failure), Toast.LENGTH_LONG).show()
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
