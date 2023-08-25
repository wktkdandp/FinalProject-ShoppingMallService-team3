package com.petpal.swimmer_seller.ui.user

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.petpal.swimmer_seller.R
import com.petpal.swimmer_seller.data.model.Seller
import com.petpal.swimmer_seller.data.repository.UserRepository

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _signUpForm = MutableLiveData<SignUpFormState>()
    val signUpFormState: LiveData<SignUpFormState> = _signUpForm

    private val _emailForm = MutableLiveData<EmailFormState>()
    val emailFormState: LiveData<EmailFormState> = _emailForm

    private val _findEmailForm = MutableLiveData<FindEmailFormState>()
    val findEmailFormState: LiveData<FindEmailFormState> = _findEmailForm

    private val _userResult = MutableLiveData<UserResult>()
    val userResult: LiveData<UserResult> = _userResult

    //이걸 저장하면 로그인할 때 여기에 저장하면 되는거 아닌가?
    private val _currentUser = MutableLiveData<Seller>()
    val currentUser: LiveData<Seller> = _currentUser

    private val _infoForm = MutableLiveData<InfoFormState>()
    val infoFormState: LiveData<InfoFormState> = _infoForm

    private val _consent = MutableLiveData<Boolean>()
    val consentState: LiveData<Boolean> = _consent

    fun login(email: String, password: String) {
        // can be launched in a separate asynchronous job
        userRepository.login(email, password) {
            if (it.isSuccessful && it.result.user != null) {
                _userResult.value = UserResult(successInt = R.string.login_succeed)
            } else {
                _userResult.value = UserResult(error = R.string.login_failed)
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

    fun logOut() {
        userRepository.logout()
    }

    fun signUpAndSave(email: String, password: String, seller: Seller) {
        userRepository.signUp(email, password) {
            if (it.isSuccessful && it.result.user != null) {
                _userResult.value = UserResult(successInt = R.string.signup_succeed)
                addSeller(seller)
            } else {
                Log.d("signup", it.exception.toString())
                _userResult.value = UserResult(error = R.string.signup_failed)
            }
        }
    }

    fun emailDataChanged(email: String) {
        if (!isEmailValid(email)) {
            _emailForm.value = EmailFormState(emailError = R.string.invalid_email)
        } else {
            _emailForm.value = EmailFormState(isDataValid = true)
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

    fun findEmailDataChanged(name: String, phoneNumber: String) {
        if (name.isEmpty()) {
            _findEmailForm.value = FindEmailFormState(nameError = R.string.invalid_name)
        } else if (phoneNumber.isEmpty()) {
            _findEmailForm.value =
                FindEmailFormState(phoneNumberError = R.string.invalid_phone_number)
        } else {
            _findEmailForm.value = FindEmailFormState(isDataValid = true)
        }
    }

    fun infoDataChanged(
        businessRegNumber: String,
        representName: String,
        brandName: String,
        description: String,
        address: String,
        brandPhoneNumber: String,
        bankName: String,
        accountNumber: String,
        contactName: String,
        contactPhoneNumber: String,
        contactEmail: String,
    ) {
        if (businessRegNumber.isEmpty()) {
            _infoForm.value = InfoFormState(businessRegNumberError = R.string.invalid_bus_reg_num)
        } else if (representName.isEmpty()) {
            _infoForm.value = InfoFormState(representNameError = R.string.invalid_represent_name)
        } else if (brandName.isEmpty()) {
            _infoForm.value = InfoFormState(brandNameError = R.string.invalid_brand_name)
        } else if (description.isEmpty()) {
            _infoForm.value = InfoFormState(descriptionError = R.string.invalid_description)
        } else if (address.isEmpty()) {
            _infoForm.value = InfoFormState(addressError = R.string.invalid_address)
        } else if (brandPhoneNumber.isEmpty()) {
            _infoForm.value =
                InfoFormState(brandPhoneNumberError = R.string.invalid_brand_phone_num)
        } else if (bankName.isEmpty()) {
            _infoForm.value = InfoFormState(bankNameError = R.string.invalid_bank_name)
        } else if (accountNumber.isEmpty()) {
            _infoForm.value = InfoFormState(accountNumberError = R.string.invalid_account_num)
        } else if (contactName.isEmpty()) {
            _infoForm.value = InfoFormState(contactNameError = R.string.invalid_contact_name)
        } else if (contactPhoneNumber.isEmpty()) {
            _infoForm.value =
                InfoFormState(contactPhoneNumberError = R.string.invalid_contact_phone_num)
        } else if (contactEmail.isEmpty()) {
            _infoForm.value = InfoFormState(contactEmailError = R.string.invalid_contact_email)
        } else {
            _infoForm.value = InfoFormState(isDataValid = true)
        }
    }

    fun consentChanged(isConsentAgreed: Boolean) {
        Log.d("consent", isConsentAgreed.toString())
        _consent.value = isConsentAgreed
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
        return password.length > 5
    }

    private fun isConfirmValid(password: String, confirm: String): Boolean {
        return password == confirm
    }

    private fun addSeller(seller: Seller) {
        userRepository.addSeller(seller) {
            if (it.isSuccessful) {
                _userResult.value = UserResult(successInt = R.string.signup_info_succeed)
            } else {
                _userResult.value = UserResult(error = R.string.signup_info_failed)
            }
        }
    }

    fun sendPasswordResetEmail(email: String) {
        userRepository.sendPasswordResetEmail(email) { task ->
            if (task.isSuccessful) {
                // 비밀번호 재설정 이메일이 성공적으로 전송됨
                _userResult.value = UserResult(successInt = R.string.send_email_succeed)
            } else {
                // 오류 처리
                if (task.exception is FirebaseAuthInvalidUserException) _userResult.value =
                    UserResult(error = R.string.user_cannot_found)
                else _userResult.value = UserResult(error = R.string.send_email_failed)
            }
        }
    }

    fun findEmail(name: String, phone: String) {
        userRepository.findEmail(name, phone) {
            //성공이면 userResult의 success를 사용자의 email로 바꿔주고
            //실패면 에러 string 넣어주기
            if (it.result.exists()) {
                Log.d("findEmail", "findEmail Succeed")
                for (snapShot in it.result.children) {
                    val email = snapShot.child("email").value as String
                    _userResult.value = UserResult(successString = email)
                }

            } else {
                Log.d("findEmail", "findEmail Failed")
                _userResult.value = UserResult(error = R.string.contact_phone_cannot_found)
            }
        }
    }

    fun getCurrentSellerInfo() {
        userRepository.getCurrentSellerInfo {
            if (it.result.exists()) {
                for (snapShot in it.result.children) {
                    val email = snapShot.child("email").value as String
                    val sellerAuthUid = snapShot.child("sellerAuthUid").value as String
                    val businessRegNumber = snapShot.child("businessRegNumber").value as String
                    val representName = snapShot.child("representName").value as String
                    val brandName = snapShot.child("brandName").value as String
                    val description = snapShot.child("description").value as String
                    val address = snapShot.child("address").value as String
                    val brandPhoneNumber = snapShot.child("brandPhoneNumber").value as String
                    val bankName = snapShot.child("bankName").value as String
                    val accountNumber = snapShot.child("accountNumber").value as String
                    val contactName = snapShot.child("contactName").value as String
                    val contactPhoneNumber = snapShot.child("contactPhoneNumber").value as String
                    val contactEmail = snapShot.child("contactEmail").value as String

                    _currentUser.value = Seller(
                        email,
                        businessRegNumber,
                        representName,
                        brandName,
                        description,
                        address,
                        brandPhoneNumber,
                        bankName,
                        accountNumber,
                        contactName,
                        contactPhoneNumber,
                        contactEmail
                    )
                    Log.d("currentUser", _currentUser.value.toString())
                }
            }
        }
    }
}

data class SignUpFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val confirmError: Int? = null,
    val isDataValid: Boolean = false,
)

data class LoginFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false,
)

data class EmailFormState(
    val emailError: Int? = null,
    val isDataValid: Boolean = false,
)

data class FindEmailFormState(
    val nameError: Int? = null,
    val phoneNumberError: Int? = null,
    val isDataValid: Boolean = false,
)

data class InfoFormState(
    val businessRegNumberError: Int? = null,
    val representNameError: Int? = null,
    val brandNameError: Int? = null,
    val descriptionError: Int? = null,
    val addressError: Int? = null,
    val brandPhoneNumberError: Int? = null,
    val bankNameError: Int? = null,
    val accountNumberError: Int? = null,
    val contactNameError: Int? = null,
    val contactPhoneNumberError: Int? = null,
    val contactEmailError: Int? = null,
    val isDataValid: Boolean = false,
)