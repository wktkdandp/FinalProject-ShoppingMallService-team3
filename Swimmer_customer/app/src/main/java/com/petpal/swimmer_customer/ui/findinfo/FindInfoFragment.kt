package com.petpal.swimmer_customer.ui.findinfo

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.view.Gravity
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
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.databinding.FragmentFindInfoBinding
import com.petpal.swimmer_customer.ui.main.MainActivity
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository

//이메일 찾기, 비밀번호 재설정의 유효성 검사를 실시하고 이메일 찾기, 비밀번호 재설정 메서드 실행

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


        //LoginFragment에서 보낸 arguments를 받아서 findid-> 아이디 찾기 visible
        //                                       resetpassword-> 비밀번호 찾기 visible
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
        //비밀번호 찾으러 가기 버튼 두 레이아웃의 visible gone의 처리를 바꾼다. 버튼 따로
        fragmentFindInfoBinding.ButtonToFindPassword.setOnClickListener {
            fragmentFindInfoBinding.idFindLayout.visibility = View.GONE
            fragmentFindInfoBinding.passwordResetLayout.visibility = View.VISIBLE
            fragmentFindInfoBinding.ButtonFindInfoToLogin.visibility = View.GONE
            fragmentFindInfoBinding.ButtonToFindPassword.visibility = View.GONE
        }
        //이메일 찾기 버튼
        fragmentFindInfoBinding.ButtonFindId.setOnClickListener {
            val nickname = fragmentFindInfoBinding.textInputEditFindIdNickname.text.toString()
            val phoneNumber = fragmentFindInfoBinding.textInputEditFindIdPhone.text.toString()

            //유효성 검사 false시 리스너 종료
            if (!checkNickname(nickname) || !checkPhoneNumberId(phoneNumber)) {
                return@setOnClickListener
            }

            //user로 반환되는 값이 null이 아닐경우 이메일을 보여주는 탭 + 로그인 하러 가기,비밀번호 찾으러 가기 visible
            viewModel.findEmailByInfo(nickname, phoneNumber)?.observe(viewLifecycleOwner, Observer {
                val userEmail=getString(R.string.message_email_template,it?.email)
                if (it != null) {
                    fragmentFindInfoBinding.textInputEditFindIdNickname.text?.clear()
                    fragmentFindInfoBinding.textInputEditFindIdPhone.text?.clear()
                    fragmentFindInfoBinding.textViewFoundId.text = getString(R.string.message_your_email_is)
                    fragmentFindInfoBinding.textViewFoundId2.text =userEmail
                    fragmentFindInfoBinding.textInputEditFindIdNickname.visibility = View.GONE
                    fragmentFindInfoBinding.textInputEditFindIdPhone.visibility = View.GONE
                    fragmentFindInfoBinding.ButtonFindId.visibility = View.GONE
                    fragmentFindInfoBinding.ButtonFindInfoToLogin.visibility = View.VISIBLE
                    fragmentFindInfoBinding.ButtonToFindPassword.visibility = View.VISIBLE
                    fragmentFindInfoBinding.doneImage.visibility = View.VISIBLE


                } else {
                    fragmentFindInfoBinding.ButtonFindId.gravity=Gravity.BOTTOM
                    fragmentFindInfoBinding.textViewFoundId.text = getString(R.string.error_no_user_matching_info)
                    fragmentFindInfoBinding.textInputEditFindIdNickname.text?.clear()
                    fragmentFindInfoBinding.textInputEditFindIdPhone.text?.clear()
                    fragmentFindInfoBinding.textInputEditFindIdNickname.requestFocus()
                }
            })
        }
        //로그인하러가기 버튼
        fragmentFindInfoBinding.ButtonFindInfoToLogin.setOnClickListener {
            findNavController().popBackStack()
        }
        //비밀번호 재설정 버튼
        fragmentFindInfoBinding.ButtonResetPassword.setOnClickListener {

            val email = fragmentFindInfoBinding.textInputEditResetPasswordEmail.text.toString()
            val phoneNumber = fragmentFindInfoBinding.textInputEditResetPasswordPhone.text.toString()

            //유효성 검사 false시 리스너 종료
            if(!checkEmail(email) || !checkPhoneNumberPw(phoneNumber)){
                return@setOnClickListener
            }
            //Boolean값으로 true가 들어오면 완료되었다는 메세지 띄움(dismiss) positive 버튼 누를 시 로그인화면으로 이동
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
                    //실패시
                    Toast.makeText(requireContext(), getString(R.string.error_password_reset_failed), Toast.LENGTH_SHORT).show()
                    fragmentFindInfoBinding.textInputEditResetPasswordEmail.text?.clear()
                    fragmentFindInfoBinding.textInputEditResetPasswordPhone.text?.clear()
                    fragmentFindInfoBinding.textInputLayoutResetPasswordEmail.requestFocus()
                }
            })
        }

        return fragmentFindInfoBinding.root
    }

    private fun validateInput(input: String, error: String, layout: TextInputLayout, editText: TextInputEditText): Boolean {
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

    private fun checkNickname(nickname: String): Boolean {
        return validateInput(nickname, getString(R.string.error_nickname_required),
            fragmentFindInfoBinding.textInputLayoutFindIdNickname,
            fragmentFindInfoBinding.textInputEditFindIdNickname)
    }

    private fun checkPhoneNumberId(phoneNumber: String): Boolean {
        return validateInput(phoneNumber, getString(R.string.error_phone_required),
            fragmentFindInfoBinding.textInputLayoutFindIdPhone,
            fragmentFindInfoBinding.textInputEditFindIdPhone)
    }

    private fun checkEmail(email: String): Boolean {
        if (!validateInput(email, getString(R.string.error_email_required),
                fragmentFindInfoBinding.textInputLayoutResetPasswordEmail,
                fragmentFindInfoBinding.textInputEditResetPasswordEmail)) return false
        if(!email.contains("@")){
            fragmentFindInfoBinding.textInputLayoutResetPasswordEmail.error = getString(R.string.error_invalid_email_format)
            Handler(Looper.getMainLooper()).postDelayed({
                fragmentFindInfoBinding.textInputLayoutResetPasswordEmail.error = ""
                fragmentFindInfoBinding.textInputEditResetPasswordEmail.text?.clear()
                showKeyboard(fragmentFindInfoBinding.textInputEditResetPasswordEmail)
            }, 2000)
            return false
        }
        return true
    }

    private fun checkPhoneNumberPw(phoneNumber: String): Boolean {
        return validateInput(phoneNumber, getString(R.string.error_phone_required),
            fragmentFindInfoBinding.textInputLayoutResetPasswordPhone,
            fragmentFindInfoBinding.textInputEditResetPasswordPhone)
    }


    //키보드 올리기
    fun showKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}

//뷰모델 팩토리
class FindInfoViewModelFactory(private val repository: CustomerUserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FindInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FindInfoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
