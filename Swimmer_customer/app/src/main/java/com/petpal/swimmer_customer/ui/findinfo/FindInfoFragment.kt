package com.petpal.swimmer_customer.ui.findinfo

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.databinding.FragmentFindInfoBinding
import com.petpal.swimmer_customer.ui.main.MainActivity
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository

class FindInfoFragment : Fragment() {

    private lateinit var binding: FragmentFindInfoBinding
    private lateinit var viewModel: FindInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFindInfoBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this, FindInfoViewModelFactory(CustomerUserRepository())).get(
            FindInfoViewModel::class.java
        )

        setupNavigation()
        handleArguments()
        handleButtonClicks()

        return binding.root
    }

    //네비게이션 설정 메서드
    private fun setupNavigation() {
        NavigationUI.setupWithNavController(binding.toolbarFindInfo, findNavController())
    }

    //전달된 인수를 처리하는 메서드
    private fun handleArguments() {
        arguments?.getString(getString(R.string.findinfokey))?.let {
            when (it) {
                getString(R.string.findid) -> configureForFindId()
                getString(R.string.resetpassword) -> configureForResetPassword()
            }
        }
    }

    //버튼 클릭 이벤트를 처리하는 메서드
    private fun handleButtonClicks() {
        binding.run {
            ButtonToFindPassword.setOnClickListener {
                idFindLayout.visibility = View.GONE
                passwordResetLayout.visibility = View.VISIBLE
                ButtonFindInfoToLogin.visibility = View.GONE
                ButtonToFindPassword.visibility = View.GONE
            }
            ButtonFindId.setOnClickListener { handleFindIdClick() }
            ButtonFindInfoToLogin.setOnClickListener { findNavController().popBackStack() }
            ButtonResetPassword.setOnClickListener { handleResetPasswordClick() }
        }
    }

    //아이디 찾기 설정 메서드
    private fun configureForFindId() {
        binding.run {
            toolbarFindInfo.title = getString(R.string.title_find_id)
            toolbarFindInfo.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            toolbarFindInfo.setNavigationOnClickListener { findNavController().popBackStack() }
            idFindLayout.visibility = View.VISIBLE
            passwordResetLayout.visibility = View.GONE
            ButtonFindInfoToLogin.visibility = View.GONE
            ButtonToFindPassword.visibility = View.GONE
            doneImage.visibility = View.GONE
        }
    }

    //비밀번호 재설정 설정 메서드
    private fun configureForResetPassword() {
        binding.run {
            toolbarFindInfo.title = getString(R.string.title_reset_password)
            toolbarFindInfo.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            toolbarFindInfo.setNavigationOnClickListener { findNavController().popBackStack() }
            idFindLayout.visibility = View.GONE
            passwordResetLayout.visibility = View.VISIBLE
        }
    }

    //아이디 찾기 버튼 클릭 시 호출되는 메서드
    private fun handleFindIdClick() {
        val nickname = binding.textInputEditFindIdNickname.text.toString()
        val phoneNumber = binding.textInputEditFindIdPhone.text.toString()

        if (checkNickname(nickname) && checkPhoneNumberId(phoneNumber)) {
            viewModel.findEmailByInfo(nickname, phoneNumber)
                ?.observe(viewLifecycleOwner, Observer { user ->
                    user?.let {
                        binding.run {
                            textViewFoundId.text = getString(R.string.message_your_email_is)
                            textViewFoundId2.text =
                                getString(R.string.message_email_template, it.email)
                            textInputEditFindIdNickname.text?.clear()
                            textInputEditFindIdPhone.text?.clear()
                            textInputEditFindIdNickname.visibility = View.GONE
                            textInputEditFindIdPhone.visibility = View.GONE
                            ButtonFindId.visibility = View.GONE
                            ButtonFindInfoToLogin.visibility = View.VISIBLE
                            ButtonToFindPassword.visibility = View.VISIBLE
                            doneImage.visibility = View.VISIBLE
                        }
                    } ?: run {
                        binding.run {
                            //ButtonFindId.gravity = Gravity.BOTTOM
                            textViewFoundId.text = getString(R.string.error_no_user_matching_info)
                            textInputEditFindIdNickname.text?.clear()
                            textInputEditFindIdPhone.text?.clear()
                            textInputEditFindIdNickname.requestFocus()
                        }
                    }
                })
        }
    }

    //비밀번호 재설정 버튼 클릭 시 호출되는 메서드
    private fun handleResetPasswordClick() {
        val email = binding.textInputEditResetPasswordEmail.text.toString()
        val phoneNumber = binding.textInputEditResetPasswordPhone.text.toString()

        if (checkEmail(email) && checkPhoneNumberPw(phoneNumber)) {
            viewModel.resetPassword(email, phoneNumber)
                ?.observe(viewLifecycleOwner, Observer { isSuccess ->
                    if (isSuccess == true) {
                        AlertDialog.Builder(requireContext())
                            .setTitle(getString(R.string.title_notification))
                            .setMessage(getString(R.string.message_password_reset_email_sent))
                            .setPositiveButton("확인") { dialog, _ ->
                                findNavController().navigate(R.id.action_findInfoFragment_to_LoginFragment)
                                dialog.dismiss()
                            }
                            .show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.error_password_reset_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.apply {
                            textInputEditResetPasswordEmail.text?.clear()
                            textInputEditResetPasswordPhone.text?.clear()
                            textInputLayoutResetPasswordEmail.requestFocus()
                        }
                    }
                })
        }
    }

    //입력 유효성 검사 메서드
    private fun validateInput(
        input: String,
        error: String,
        layout: TextInputLayout,
        editText: TextInputEditText
    ): Boolean {
        if (input.isEmpty()) {
            layout.error = error
            Handler(Looper.getMainLooper()).postDelayed({
                layout.error = ""
                editText.text?.clear()
                showKeyboard(editText)
            }, 2000)
            return false
        }
        return true
    }

    //닉네임 유효성 검사 메서드
    private fun checkNickname(nickname: String): Boolean {
        return validateInput(
            nickname, getString(R.string.error_nickname_required),
            binding.textInputLayoutFindIdNickname,
            binding.textInputEditFindIdNickname
        )
    }

    //전화번호 유효성 검사 메서드
    private fun checkPhoneNumberId(phoneNumber: String): Boolean {
        return validateInput(
            phoneNumber, getString(R.string.error_phone_required),
            binding.textInputLayoutFindIdPhone,
            binding.textInputEditFindIdPhone
        )
    }

    //이메일 유효성 검사 메서드
    private fun checkEmail(email: String): Boolean {
        if (!validateInput(
                email, getString(R.string.error_email_required),
                binding.textInputLayoutResetPasswordEmail,
                binding.textInputEditResetPasswordEmail
            )
        ) return false
        if (!email.contains("@")) {
            binding.textInputLayoutResetPasswordEmail.error =
                getString(R.string.error_invalid_email_format)
            Handler(Looper.getMainLooper()).postDelayed({
                binding.textInputLayoutResetPasswordEmail.error = ""
                binding.textInputEditResetPasswordEmail.text?.clear()
                showKeyboard(binding.textInputEditResetPasswordEmail)
            }, 2000)
            return false
        }
        return true
    }

    //전화번호 유효성 검사 메서드
    private fun checkPhoneNumberPw(phoneNumber: String): Boolean {
        return validateInput(
            phoneNumber, getString(R.string.error_phone_required),
            binding.textInputLayoutResetPasswordPhone,
            binding.textInputEditResetPasswordPhone
        )
    }


    //키보드 올리기 메서드
    fun showKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}

//뷰모델 팩토리
class FindInfoViewModelFactory(private val repository: CustomerUserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FindInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FindInfoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
