package com.petpal.swimmer_seller.ui.user

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.petpal.swimmer_seller.R
import com.petpal.swimmer_seller.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private var _fragmentLoginBinding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val fragmentLoginBinding get() = _fragmentLoginBinding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("user", "loginFragment onCreate")
        _fragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return fragmentLoginBinding.root

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("user", "loginFragment onDestroy")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel =
            ViewModelProvider(this, UserViewModelFactory())[UserViewModel::class.java]

        val emailTextInputLayout = fragmentLoginBinding.textInputLayoutLoginEmail
        val passwordTextInputLayout = fragmentLoginBinding.textInputLayoutLoginPassword
        val emailEditText = fragmentLoginBinding.textInputEditTextLoginEmail
        val passwordEditText = fragmentLoginBinding.textInputEditTextLoginPassword
        val loginButton = fragmentLoginBinding.buttonLogin
        val goToSignupButton = fragmentLoginBinding.buttonGoToSignup
        val findEmailButton = fragmentLoginBinding.buttonFindEmail
        val findPasswordButton = fragmentLoginBinding.buttonFindPassword

        userViewModel.run {
            loginFormState.observe(viewLifecycleOwner) { loginFormState ->
                if (loginFormState == null) {
                    return@observe
                }
                loginButton.isEnabled = loginFormState.isDataValid
                if (loginFormState.emailError == null) {
                    emailTextInputLayout.error = null // 에러 초기화
                } else {
                    emailTextInputLayout.error = getString(loginFormState.emailError)
                }
                if (loginFormState.passwordError == null) {
                    passwordTextInputLayout.error = null // 에러 초기화
                } else {
                    passwordTextInputLayout.error = getString(loginFormState.passwordError)
                }
            }

            //로그인 결과 값을 observe해서 ui를 변경해줌
            userResult.observe(viewLifecycleOwner) { loginResult ->
                loginResult ?: return@observe
                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                    updateUiWithUser(it)
                }
            }
        }


        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                userViewModel.loginDataChanged(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }
        emailEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                //로그인하기
                userViewModel.login(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }

        loginButton.setOnClickListener {
            //로그인하기
            userViewModel.login(
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }

        goToSignupButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        findEmailButton.setOnClickListener {
            //이메일 찾기로 이동
        }

        findPasswordButton.setOnClickListener {
            //비밀번호 재설정으로 이동
            findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }
    }

    //로그인 후 화면 업데이트
    private fun updateUiWithUser(@StringRes succeedString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, succeedString , Toast.LENGTH_LONG).show()
        //로그인 프래그먼트는 제거하고 메인화면으로 이동
        findNavController().popBackStack(R.id.loginFragment, true)
        findNavController().navigate(R.id.mainFragment)

    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentLoginBinding = null
    }
}