package com.petpal.swimmer_customer.ui.findinfo

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract.CommonDataKinds.Nickname
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.internal.ViewUtils.showKeyboard
import com.google.firebase.auth.FirebaseAuth
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.databinding.FragmentFindInfoBinding
import com.petpal.swimmer_customer.ui.main.MainActivity
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository

class FindInfoFragment : Fragment() {
    lateinit var fragmentFindInfoBinding: FragmentFindInfoBinding
    lateinit var viewModel: FindInfoViewModel
    lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentFindInfoBinding = FragmentFindInfoBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        val factory = FindInfoViewModelFactory(CustomerUserRepository())
        viewModel = ViewModelProvider(this, factory).get(FindInfoViewModel::class.java)


        val navController = findNavController()
        NavigationUI.setupWithNavController(fragmentFindInfoBinding.toolbarFindInfo, navController)

        arguments?.let {
            when (it.getString(getString(R.string.findinfokey))) {
                getString(R.string.findid) -> {
                    fragmentFindInfoBinding.toolbarFindInfo.run {
                        title = getString(R.string.title_find_id)
                        setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                        setNavigationOnClickListener {
                            findNavController().popBackStack()
                        }
                    }
                    fragmentFindInfoBinding.idFindLayout.visibility = View.VISIBLE
                    fragmentFindInfoBinding.passwordResetLayout.visibility = View.GONE
                    fragmentFindInfoBinding.ButtonFindInfoToLogin.visibility = View.GONE
                    fragmentFindInfoBinding.ButtonToFindPassword.visibility = View.GONE
                    fragmentFindInfoBinding.doneImage.visibility = View.GONE
                }

                getString(R.string.resetpassword) -> {
                    fragmentFindInfoBinding.toolbarFindInfo.run {
                        title = getString(R.string.title_reset_password)
                        setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                        setNavigationOnClickListener {
                            findNavController().popBackStack()
                        }
                    }
                    fragmentFindInfoBinding.idFindLayout.visibility = View.GONE
                    fragmentFindInfoBinding.passwordResetLayout.visibility = View.VISIBLE
                }
            }
        }
        //비밀번호 찾으러 가기 버튼
        fragmentFindInfoBinding.ButtonToFindPassword.setOnClickListener {
            fragmentFindInfoBinding.idFindLayout.visibility = View.GONE
            fragmentFindInfoBinding.passwordResetLayout.visibility = View.VISIBLE
            fragmentFindInfoBinding.ButtonFindInfoToLogin.visibility = View.GONE
            fragmentFindInfoBinding.ButtonToFindPassword.visibility = View.GONE
        }
        //이메일 찾기 버튼
        fragmentFindInfoBinding.ButtonFindId.setOnClickListener {
            val nickname = fragmentFindInfoBinding.editTextFinIdNickname.text.toString()
            val phoneNumber = fragmentFindInfoBinding.editTextFindIdPhoneNumber.text.toString()

            if (!checkNickname(nickname) || !checkPhoneNumberId(phoneNumber)) {
                return@setOnClickListener
            }

            viewModel.findEmailByInfo(nickname, phoneNumber)?.observe(viewLifecycleOwner, Observer {
                val userEmail=getString(R.string.message_email_template,it?.email)
                if (it != null) {
                    fragmentFindInfoBinding.editTextFinIdNickname.text.clear()
                    fragmentFindInfoBinding.editTextFindIdPhoneNumber.text.clear()
                    fragmentFindInfoBinding.textViewFoundId.text = getString(R.string.message_your_email_is)
                    fragmentFindInfoBinding.textViewFoundId2.text =userEmail
                    fragmentFindInfoBinding.editTextFinIdNickname.visibility = View.GONE
                    fragmentFindInfoBinding.editTextFindIdPhoneNumber.visibility = View.GONE
                    fragmentFindInfoBinding.ButtonFindId.visibility = View.GONE
                    fragmentFindInfoBinding.ButtonFindInfoToLogin.visibility = View.VISIBLE
                    fragmentFindInfoBinding.ButtonToFindPassword.visibility = View.VISIBLE
                    fragmentFindInfoBinding.doneImage.visibility = View.VISIBLE


                } else {
                    fragmentFindInfoBinding.textViewFoundId.text = getString(R.string.error_no_user_matching_info)
                    fragmentFindInfoBinding.editTextFinIdNickname.text.clear()
                    fragmentFindInfoBinding.editTextFindIdPhoneNumber.text.clear()
                }
            })
        }
        //로그인하러가기 버튼
        fragmentFindInfoBinding.ButtonFindInfoToLogin.setOnClickListener {
            findNavController().popBackStack()
        }
        //비밀번호 재설정 버튼
        fragmentFindInfoBinding.ButtonResetPassword.setOnClickListener {

            val email = fragmentFindInfoBinding.editTextFindPwEmail.text.toString()
            val phoneNumber = fragmentFindInfoBinding.editTextFindPwPhoneNumber.text.toString()

            if(!checkEmail(email) || !checkPhoneNumberPw(phoneNumber)){
                return@setOnClickListener
            }
            viewModel.resetPassword(email, phoneNumber)?.observe(viewLifecycleOwner, Observer {
                if (it == true) {
                    // 다이얼로그 표시
                    AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.title_notification))
                        .setMessage(getString(R.string.message_password_reset_email_sent))
                        .setPositiveButton("확인") { dialog, _ ->
                            findNavController().navigate(R.id.action_findInfoFragment_to_LoginFragment)
                            dialog.dismiss()
                        }
                        .show()
                } else {

                    Toast.makeText(requireContext(), getString(R.string.error_password_reset_failed), Toast.LENGTH_SHORT).show()
                }
            })
        }

        return fragmentFindInfoBinding.root
    }

    private fun checkNickname(nickname: String): Boolean {
        if (nickname.isEmpty()) {
            fragmentFindInfoBinding.editTextFinIdNickname.error = getString(R.string.error_nickname_required)
            Handler(Looper.getMainLooper()).postDelayed({
                fragmentFindInfoBinding.editTextFinIdNickname.error = ""
                fragmentFindInfoBinding.editTextFinIdNickname.text?.clear()
                showKeyboard(fragmentFindInfoBinding.editTextFinIdNickname)
            }, 2000)
            return false
        }
        return true
    }

    private fun checkPhoneNumberId(phoneNumber: String): Boolean {
        if (phoneNumber.isEmpty()) {
            fragmentFindInfoBinding.editTextFindIdPhoneNumber.error = getString(R.string.error_phone_required)
            Handler(Looper.getMainLooper()).postDelayed({
                fragmentFindInfoBinding.editTextFindIdPhoneNumber.error = ""
                fragmentFindInfoBinding.editTextFindIdPhoneNumber.text?.clear()
                showKeyboard(fragmentFindInfoBinding.editTextFindIdPhoneNumber)
            }, 2000)
            return false
        }
        return true
    }
    private fun checkEmail(email:String):Boolean{
        if(email.isEmpty()){
            fragmentFindInfoBinding.editTextFindPwEmail.error=getString(R.string.error_email_required)
            Handler(Looper.getMainLooper()).postDelayed({
                fragmentFindInfoBinding.editTextFindPwEmail.error = ""
                fragmentFindInfoBinding.editTextFindPwEmail.text?.clear()
                showKeyboard(fragmentFindInfoBinding.editTextFindPwEmail)
            }, 2000)
        }else if(!email.contains("@")){
            fragmentFindInfoBinding.editTextFindPwEmail.error=getString(R.string.error_invalid_email_format)
            Handler(Looper.getMainLooper()).postDelayed({
                fragmentFindInfoBinding.editTextFindPwEmail.error = ""
                fragmentFindInfoBinding.editTextFindPwEmail.text?.clear()
                showKeyboard(fragmentFindInfoBinding.editTextFindPwEmail)
            }, 2000)
        }

        return true
    }
    private fun checkPhoneNumberPw(phoneNumber: String): Boolean {
        if (phoneNumber.isEmpty()) {
            fragmentFindInfoBinding.editTextFindPwPhoneNumber.error = getString(R.string.error_phone_required)
            Handler(Looper.getMainLooper()).postDelayed({
                fragmentFindInfoBinding.editTextFindPwPhoneNumber.error = ""
                fragmentFindInfoBinding.editTextFindPwPhoneNumber.text?.clear()
                showKeyboard(fragmentFindInfoBinding.editTextFindPwPhoneNumber)
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

class FindInfoViewModelFactory(private val repository: CustomerUserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FindInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FindInfoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
