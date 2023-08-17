package com.petpal.swimmer_seller.ui.user

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_seller.R
import com.petpal.swimmer_seller.data.UserRepository

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _signUpForm = MutableLiveData<SignUpFormState>()
    val signUpFormState: LiveData<SignUpFormState> = _signUpForm

    //TODO : firebase로 변경할 수 없나?
    private val _userResult = MutableLiveData<UserResult>()
    val userResult: LiveData<UserResult> = _userResult

    fun login(email: String, password: String) {
        // can be launched in a separate asynchronous job
        userRepository.login(email, password) {
            if (it.isSuccessful && it.result.user != null) {
                _userResult.value = UserResult(success = R.string.signup_succeed)
            } else {
                _userResult.value = UserResult(error = R.string.signup_failed)
            }
        }

    }

    fun loginDataChanged(email: String, password: String) {
        if (!isEmailValid(email)) {
            _loginForm.value = LoginFormState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    fun signUp(email: String, password: String) {
        userRepository.signUp(email, password) {
            if (it.isSuccessful && it.result.user != null) {
                _userResult.value = UserResult(success = R.string.signup_succeed)
            } else {
                _userResult.value = UserResult(error = R.string.signup_failed)
            }
        }
    }

    fun signUpDataChanged(email: String, password: String, confirm: String) {
        if (!isEmailValid(email)) {
            _signUpForm.value = SignUpFormState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _signUpForm.value = SignUpFormState(passwordError = R.string.invalid_password)
        } else if (!isConfirmValid(password, confirm)) {
            _signUpForm.value = SignUpFormState(confirmError = R.string.invalid_confirm)
        } else {
            _signUpForm.value = SignUpFormState(isDataValid = true)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return if (email.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }

    private fun isConfirmValid(password: String, confirm: String): Boolean {
        return password == confirm
    }
}

data class SignUpFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val confirmError: Int? = null,
    val isDataValid: Boolean = false
)

data class LoginFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)