package com.petpal.swimmer_customer

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.petpal.swimmer_customer.databinding.FragmentRegisterBinding
import com.petpal.swimmer_customer.`1login`.LoginViewModel
import com.petpal.swimmer_customer.`1login`.LoginViewModelFactory
import com.petpal.swimmer_customer.main.MainActivity
import com.petpal.swimmer_customer.repository.CustomerUserRepository

class RegisterFragment : Fragment() {
    lateinit var fragmentRegisterBinding: FragmentRegisterBinding
    lateinit var viewModel: LoginViewModel
    lateinit var auth: FirebaseAuth
    lateinit var mainActivity: MainActivity
    private val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity=activity as MainActivity
        fragmentRegisterBinding= FragmentRegisterBinding.inflate(layoutInflater)
        auth= FirebaseAuth.getInstance()

        val factory = LoginViewModelFactory(CustomerUserRepository())
        viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        val navController = findNavController()
        NavigationUI.setupWithNavController(fragmentRegisterBinding.toolbarAddUser, navController)

        fragmentRegisterBinding.toolbarAddUser.run{
            title = "회원가입"
            setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        fragmentRegisterBinding.buttonAddUserEmailDuplicateCheck.setOnClickListener {
            val email=fragmentRegisterBinding.textInputEditTextAddUserEmail.text.toString()
            viewModel.checkEmailDuplicate(email).observe(viewLifecycleOwner, Observer { isDuplicate ->
                if (isDuplicate) {
                    Log.d("koko","이메일 중복이야")
                    fragmentRegisterBinding.textInputLayoutAddUserEmail.error = "해당 이메일이 이미 존재합니다."

                    Handler(Looper.getMainLooper()).postDelayed({
                        fragmentRegisterBinding.textInputLayoutAddUserEmail.error = null
                        fragmentRegisterBinding.textInputEditTextAddUserEmail.text?.clear()
                        showKeyboard(fragmentRegisterBinding.textInputEditTextAddUserEmail)
                    }, 2000)
                }
            })
        }

        fragmentRegisterBinding.buttonAddUserInfoSubmit.setOnClickListener {
            val email = fragmentRegisterBinding.textInputEditTextAddUserEmail.text.toString()
            val password = fragmentRegisterBinding.textInputEditTextAddUserPassword.text.toString()
            val confirmPassword = fragmentRegisterBinding.textInputEditTextAddUserPasswordRepeat.text.toString()
            val uid=auth.currentUser?.uid
            val Nickname =fragmentRegisterBinding.textInputEditTextAddUserNickname.text.toString()
            val PhoneNumber = fragmentRegisterBinding.textInputEditTextAddUserPhoneNumber.text.toString()
            val swimExp=UserSwimExp()
            //유효성 검사
            if (!checkEmail(email) || !checkPassword(password, confirmPassword)|| !isCheckboxChecked()) {
                return@setOnClickListener
            }
            //Log.d("koko", "Email: $email, Password: $password, Nickname: $Nickname, PhoneNumber: $PhoneNumber")
            viewModel.register(uid,email,password,Nickname,PhoneNumber, swimExp)?.observe(viewLifecycleOwner, Observer { success ->
                if (success!!) {
                    // 회원가입 성공
                    findNavController().popBackStack()
                    Toast.makeText(context, "회원가입에 성공했습니다. 로그인해주세요.", Toast.LENGTH_LONG).show()
                } else {
                    // 회원가입 실패
                    Log.d("koko","회원가입 실패")
                }
            })
        }

            return fragmentRegisterBinding.root
    }
    //수영 경력
    private fun UserSwimExp():String {
        return when (fragmentRegisterBinding.swimExpGroup.checkedChipId) {
            R.id.swimExp1 -> "1년 이하"
            R.id.swimExp2 -> "3년 이하"
            R.id.swimExp3 -> "5년 이하"
            R.id.swimExp4 -> "5년 이상"
            else -> "선택안함"
        }
    }
    private fun checkEmail(email: String): Boolean {
        if (!email.contains("@")) {
            fragmentRegisterBinding.textInputLayoutAddUserEmail.error = "이메일 양식이 올바르지 않습니다."
            Handler(Looper.getMainLooper()).postDelayed({
                fragmentRegisterBinding.textInputLayoutAddUserEmail.error = ""
                fragmentRegisterBinding.textInputEditTextAddUserEmail.text?.clear()
                showKeyboard(fragmentRegisterBinding.textInputEditTextAddUserEmail)
            }, 2000)
            return false
        }
        return true
    }

    private fun checkPassword(password: String, confirmPassword: String): Boolean {
        // 비밀번호의 길이 검사
        if (password.length < 6) {
            fragmentRegisterBinding.textInputLayoutAddUserPassword.error = "비밀번호는 6자 이상이어야 합니다."
            Handler(Looper.getMainLooper()).postDelayed({
                fragmentRegisterBinding.textInputLayoutAddUserPassword.error = ""
                fragmentRegisterBinding.textInputEditTextAddUserPassword.text?.clear()
                fragmentRegisterBinding.textInputEditTextAddUserPasswordRepeat.text?.clear()
                showKeyboard(fragmentRegisterBinding.textInputEditTextAddUserPassword)
            }, 2000)
            return false
        }
        // 비밀번호 일치 검사
        else if (password != confirmPassword) {
            fragmentRegisterBinding.textInputLayoutAddUserPasswordRepeat.error = "비밀번호가 일치하지 않습니다."
            Handler(Looper.getMainLooper()).postDelayed({
                fragmentRegisterBinding.textInputLayoutAddUserPasswordRepeat.error = ""
                fragmentRegisterBinding.textInputEditTextAddUserPassword.text?.clear()
                fragmentRegisterBinding.textInputEditTextAddUserPasswordRepeat.text?.clear()
                showKeyboard(fragmentRegisterBinding.textInputEditTextAddUserPassword)
            }, 2000)
            return false
        }
        return true
    }
    //정보 동의 체크박스
    private fun isCheckboxChecked(): Boolean {
        if (!fragmentRegisterBinding.infoAgree.isChecked) {
            fragmentRegisterBinding.textViewInfoAgree.text = "정보 동의를 체크해주세요."
            Handler(Looper.getMainLooper()).postDelayed({
                fragmentRegisterBinding.textViewInfoAgree.text = ""
            }, 2000)
            return false
        }
        return true
    }
    //키보드 올리기
     public fun showKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }


}