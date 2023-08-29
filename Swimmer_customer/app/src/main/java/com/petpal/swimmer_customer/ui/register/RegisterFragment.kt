package com.petpal.swimmer_customer.ui.register

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.databinding.FragmentRegisterBinding
import com.petpal.swimmer_customer.ui.main.MainActivity
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository

//

class RegisterFragment : Fragment() {
    lateinit var fragmentRegisterBinding: FragmentRegisterBinding
    lateinit var viewModel: RegisterViewModel
    lateinit var auth: FirebaseAuth
    lateinit var mainActivity: MainActivity
    //이메일 중복검사  성공여부 변수
    private var isEmailValid = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainActivity=activity as MainActivity
        fragmentRegisterBinding= FragmentRegisterBinding.inflate(layoutInflater)
        auth= FirebaseAuth.getInstance()

        setupToolbar()
        privacy_Policy_Checkbox()
        setupUI()
        setupViewModelNav()
        handleBackPress()

        return fragmentRegisterBinding.root
    }
    private fun handleBackPress() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
    private fun setupViewModelNav(){
        val factory = RegisterViewModelFactory(CustomerUserRepository())
        viewModel = ViewModelProvider(this, factory).get(RegisterViewModel::class.java)

        val navController = findNavController()
        NavigationUI.setupWithNavController(fragmentRegisterBinding.toolbarAddUser, navController)
    }
    private fun setupToolbar()
    {
        //툴바
        fragmentRegisterBinding.toolbarAddUser.run{
            title = getString(R.string.title_signup)
            setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
    private fun setupUI(){
        //회원가입 버튼
        fragmentRegisterBinding.buttonRegister.setOnClickListener {
            val email = fragmentRegisterBinding.textInputEditTextAddUserEmail.text.toString()
            val password = fragmentRegisterBinding.textInputEditTextAddUserPassword.text.toString()
            val confirmPassword = fragmentRegisterBinding.textInputEditTextAddUserPasswordRepeat.text.toString()
            val Nickname =fragmentRegisterBinding.textInputEditTextAddUserNickname.text.toString()
            val PhoneNumber = fragmentRegisterBinding.textInputEditTextAddUserPhoneNumber.text.toString()
            val swimExp=userSwimExp()
            val isChecked=fragmentRegisterBinding.checkboxPrivacyPolicy.isChecked
            //유효성 검사
            if(!validateCheck(email,password,confirmPassword,Nickname,PhoneNumber,isChecked)) {return@setOnClickListener}

            if(!isEmailValid)
            {
                fragmentRegisterBinding.textInputLayoutAddUserEmail.error = getString(R.string.error_check_id_duplication)
                fragmentRegisterBinding.buttonAddUserEmailDuplicateCheck.requestFocus()
                return@setOnClickListener
            }
            viewModel.addUser("사용자",null,email,password,Nickname,PhoneNumber, swimExp)?.observe(viewLifecycleOwner, Observer { success ->
                if (success!!) {
                    // 회원가입 성공
                    findNavController().popBackStack()
                    Toast.makeText(context, getString(R.string.signup_success), Toast.LENGTH_LONG).show()
                } else {
                    // 회원가입 실패
                    Toast.makeText(context, getString(R.string.signup_failure), Toast.LENGTH_LONG).show()
                    fragmentRegisterBinding.textInputEditTextAddUserEmail.text?.clear()
                    fragmentRegisterBinding.textInputEditTextAddUserPassword.text?.clear()
                    fragmentRegisterBinding.textInputEditTextAddUserPasswordRepeat.text?.clear()
                    fragmentRegisterBinding.textInputEditTextAddUserNickname.text?.clear()
                    fragmentRegisterBinding.textInputEditTextAddUserNickname.text?.clear()
                    fragmentRegisterBinding.textInputEditTextAddUserNickname.text?.clear()
                    fragmentRegisterBinding.textInputEditTextAddUserPhoneNumber.text?.clear()
                    fragmentRegisterBinding.checkboxPrivacyPolicy.isChecked=false

                }
            })
        }

        //중복검사 버튼
        fragmentRegisterBinding.buttonAddUserEmailDuplicateCheck.setOnClickListener {
            val email = fragmentRegisterBinding.textInputEditTextAddUserEmail.text.toString()
            if(viewModel.isEmailEmpty(email)){
                showError(fragmentRegisterBinding.textInputLayoutAddUserEmail,fragmentRegisterBinding.textInputEditTextAddUserEmail,getString(R.string.error_email_required))
                return@setOnClickListener
            }
            if(!viewModel.isValidEmailFormat(email)){
                showError(fragmentRegisterBinding.textInputLayoutAddUserEmail,fragmentRegisterBinding.textInputEditTextAddUserEmail,getString(R.string.error_invalid_email_format))
                return@setOnClickListener
            }
            viewModel.checkEmailDuplicated(email).observe(viewLifecycleOwner, Observer { isDuplicate ->
                if (isDuplicate) {
                    AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.title_notification))
                        .setMessage(getString(R.string.error_email_already_exists))
                        .setPositiveButton(getString(R.string.action_confirm)) { dialog, _ ->
                            fragmentRegisterBinding.textInputEditTextAddUserEmail.text?.clear()
                            showKeyboard(fragmentRegisterBinding.textInputEditTextAddUserEmail)
                        }
                        .show()
                } else {
                    isEmailValid=true
                    AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.title_notification))
                        .setMessage(getString(R.string.message_email_available))
                        .setPositiveButton("확인") { dialog, _ ->
                            showKeyboard(fragmentRegisterBinding.textInputEditTextAddUserPassword)
                        }
                        .show()
                }
            })
        }
    }
    private fun showError(textInputLayout: TextInputLayout, textInputEditText: TextInputEditText, errorMessage: String) {
        textInputLayout.error = errorMessage
        Handler(Looper.getMainLooper()).postDelayed({
            textInputLayout.error = ""
            textInputEditText.text?.clear()
            showKeyboard(textInputEditText)
        }, 2000)
    }
    private fun validateCheck(email:String,password:String,passwordRepeat:String,nicknName:String,phoneNumber:String,isChecked:Boolean):Boolean{
        if(viewModel.isEmailEmpty(email)){
           showError(fragmentRegisterBinding.textInputLayoutAddUserEmail,fragmentRegisterBinding.textInputEditTextAddUserEmail,getString(R.string.error_email_required))
            return false
        }
        if(!viewModel.isValidEmailFormat(email)){
            showError(fragmentRegisterBinding.textInputLayoutAddUserEmail,fragmentRegisterBinding.textInputEditTextAddUserEmail,getString(R.string.error_invalid_email_format))
            return false
        }
        if(!viewModel.isValidPassword(password)){
            showError(fragmentRegisterBinding.textInputLayoutAddUserPassword,fragmentRegisterBinding.textInputEditTextAddUserPassword,getString(R.string.error_password_length))
            return false
        }
        if(!viewModel.isPasswordMatched(password,passwordRepeat)){
            showError(fragmentRegisterBinding.textInputLayoutAddUserPasswordRepeat,fragmentRegisterBinding.textInputEditTextAddUserPasswordRepeat,getString(R.string.error_passwords_not_match))
            return false
        }
        if(viewModel.isNicknameEmpty(nicknName)){
            showError(fragmentRegisterBinding.textInputLayoutAddUserNickname,fragmentRegisterBinding.textInputEditTextAddUserNickname,getString(R.string.error_nickname_required))
            return false
        }
        if(viewModel.isPhoneNumberEmpty(phoneNumber)){
            showError(fragmentRegisterBinding.textInputLayoutAddUserPhoneNumber,fragmentRegisterBinding.textInputEditTextAddUserPhoneNumber,getString(R.string.error_phone_required))
            return false
        }
        if(!viewModel.isConsentGiven(isChecked)){
            fragmentRegisterBinding.textViewInfoAgree.text = getString(R.string.error_consent_required)
            Handler(Looper.getMainLooper()).postDelayed({
                fragmentRegisterBinding.textViewInfoAgree.text = ""
            }, 2000)
            return false
        }
        return true
    }
    //수영 경력
    private fun userSwimExp():String {
        return when (fragmentRegisterBinding.swimExpGroup.checkedChipId) {
            R.id.swimExp1-> getString(R.string.duration_less_than_1_year)
            R.id.swimExp2 -> getString(R.string.duration_less_than_3_years)
            R.id.swimExp3 -> getString(R.string.duration_less_than_5_years)
            R.id.swimExp4 -> getString(R.string.duration_more_than_5_years)
            else -> getString(R.string.duration_no_selection)
        }
    }

    //키보드 올리기
    private fun showKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
    private fun showCustomDialog() {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.custom_consent_dialog, null)

        val consentConfirmButton: Button = view.findViewById(R.id.consentConfirmButton)
        val consentCancelButton: Button = view.findViewById(R.id.consentCancelButton)

        AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
            .apply {
                //다이얼로그 내의 버튼에 따라 분기
                consentConfirmButton.setOnClickListener {
                    fragmentRegisterBinding.checkboxPrivacyPolicy.isChecked = true
                    dismiss()
                }

                consentCancelButton.setOnClickListener {
                    fragmentRegisterBinding.checkboxPrivacyPolicy.isChecked = false
                    dismiss()
                }
                show()
            }
    }
    //체크박스가 터치되면 if문 안의 모션이벤트와 체크여부에 따라 커스텀 다이얼로그 메서드 호출
    private fun privacy_Policy_Checkbox() {
        fragmentRegisterBinding.checkboxPrivacyPolicy.setOnTouchListener { v, event ->
            if(event.action == MotionEvent.ACTION_UP && !fragmentRegisterBinding.checkboxPrivacyPolicy.isChecked) {
                showCustomDialog()
                true
            } else {
                false
            }
        }
    }
}
class RegisterViewModelFactory(private val repository: CustomerUserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}