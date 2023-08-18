package com.petpal.swimmer_seller.ui.user

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.petpal.swimmer_seller.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private var _fragmentSignUpBinding: FragmentSignUpBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val fragmentSignUpBinding get() = _fragmentSignUpBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentSignUpBinding = FragmentSignUpBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return fragmentSignUpBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel =
            ViewModelProvider(this, UserViewModelFactory())[UserViewModel::class.java]

        val emailTextInputLayout = fragmentSignUpBinding.textInputLayoutSignupEmail
        val passwordTextInputLayout = fragmentSignUpBinding.textInputLayoutSignupPassword
        val confirmTextInputLayout = fragmentSignUpBinding.textInputLayoutSignupPasswordConfirm
        val emailEditText = fragmentSignUpBinding.textInputEditTextSignupEmail
        val passwordEditText = fragmentSignUpBinding.textInputEditTextSignupPassword
        val confirmEditText = fragmentSignUpBinding.textInputEditTextSignupPasswordConfirm
        val buttonSignup = fragmentSignUpBinding.buttonSignup
        val signUpToolbar = fragmentSignUpBinding.signUpToolbar

        userViewModel.run {
            signUpFormState.observe(viewLifecycleOwner) {
                if (it == null) {
                    return@observe
                }
                buttonSignup.isEnabled = it.isDataValid

                //email 입력칸 에러 메세지
                if (it.emailError == null) {
                    emailTextInputLayout.error = null
                } else {
                    emailTextInputLayout.error = getString(it.emailError)
                }

                //비밀번호 입력칸 에러 메세지
                if (it.passwordError == null) {
                    passwordTextInputLayout.error = null
                } else {
                    passwordTextInputLayout.error = getString(it.passwordError)
                }

                //비밀번호 확인칸 에러 메세지
                if (it.confirmError == null) {
                    confirmTextInputLayout.error = null
                } else {
                    confirmTextInputLayout.error = getString(it.confirmError)
                }
            }

            userResult.observe(viewLifecycleOwner) { result ->
                result ?: return@observe
                result.error?.let {
                    //로그인, 회원가입 모두 이 메서드를 쓰고있네... 고쳐야할 것 같은데
                    showSignUpFailed(it)
                }
                result.success?.let {
                    updateUiWithUser(it)
                }
            }
        }

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                userViewModel.signUpDataChanged(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString(),
                    confirmEditText.text.toString()
                )
            }
        }

        emailEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        confirmEditText.addTextChangedListener(afterTextChangedListener)
        confirmEditText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if(actionId==EditorInfo.IME_ACTION_DONE) {
                //회원가입 하기
                userViewModel.signUp(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }

        buttonSignup.setOnClickListener {
            //회원가입 하기
            userViewModel.signUp(
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }

        signUpToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun updateUiWithUser(@StringRes succeedString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, succeedString , Toast.LENGTH_LONG).show()
        //정보 입력 화면으로 이동
        findNavController().navigate(R.id.action_signUpFragment_to_signUpInfoFragment)

    }

    private fun showSignUpFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }
}