package com.petpal.swimmer_seller.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.petpal.swimmer_seller.R
import com.petpal.swimmer_seller.data.model.Seller
import com.petpal.swimmer_seller.databinding.FragmentSignUpInfoBinding

class SignUpInfoFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private var _fragmentSignUpInfoBinding: FragmentSignUpInfoBinding? = null

    private var email = ""
    private var password = ""

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val fragmentSignUpInfoBinding get() = _fragmentSignUpInfoBinding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _fragmentSignUpInfoBinding = FragmentSignUpInfoBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return fragmentSignUpInfoBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(this, UserViewModelFactory())[UserViewModel::class.java]

        email = arguments?.getString("email")!!
        password = arguments?.getString("password")!!

        val textInputEditTextCompanyRegNum = fragmentSignUpInfoBinding.textInputEditTextCompanyRegNum
        val textInputEditTextOwnerName = fragmentSignUpInfoBinding.textInputEditTextOwnerName
        val textInputEditTextCompanyName = fragmentSignUpInfoBinding.textInputEditTextCompanyName
        val textInputEditTextCompanyDesc = fragmentSignUpInfoBinding.textInputEditTextCompanyDesc
        val textInputEditTextCompanyAddrs = fragmentSignUpInfoBinding.textInputEditTextCompanyAddrs
        val textInputEditTextCompanyContact = fragmentSignUpInfoBinding.textInputEditTextCompanyContact
        val textInputEditTextCompanyAccount = fragmentSignUpInfoBinding.textInputEditTextCompanyAccount
        val textInputEditTextManagerName = fragmentSignUpInfoBinding.textInputEditTextManagerName
        val textInputEditTextManagerContact = fragmentSignUpInfoBinding.textInputEditTextManagerContact
        val textInputEditTextManagerEmail = fragmentSignUpInfoBinding.textInputEditTextManagerEmail

        userViewModel.run {
            userResult.observe(viewLifecycleOwner) { result ->
                result ?: return@observe
                result.error?.let {
                    showSignUpFailed(it)
                }
                result.success?.let {
                    updateUiWithUser(it)
                }
            }
        }

        fragmentSignUpInfoBinding.run {
            //은행 선택 드롭다운 세팅
            val banks = arrayOf(
                getString(R.string.kukminbank),
                getString(R.string.shinhanbank),
                getString(R.string.wooribank),
                getString(R.string.kakaobank),
                getString(R.string.ibkbank),
                getString(R.string.hanabank),
                getString(R.string.kdbbank)
            )
            textViewBank.setSimpleItems(banks)

            //이메일 도메인 드롭다운 세팅
            val emailDomains = arrayOf(
                getString(R.string.gmail),
                getString(R.string.naver),
                getString(R.string.daum),
                getString(R.string.nate),
                getString(R.string.prompt_manual_input)
            )
            textViewManagerEmailDomain.setSimpleItems(emailDomains)

            signUpInfoToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            buttonSignUp.setOnClickListener {

                val seller = Seller(
                    textInputEditTextCompanyRegNum.text.toString(),
                    textInputEditTextOwnerName.text.toString(),
                    textInputEditTextCompanyName.text.toString(),
                    textInputEditTextCompanyDesc.text.toString(),
                    textInputEditTextCompanyAddrs.text.toString(),
                    textInputEditTextCompanyContact.text.toString(),
                    fragmentSignUpInfoBinding.textViewBank.text.toString(),
                    textInputEditTextCompanyAccount.text.toString(),
                    textInputEditTextManagerName.text.toString(),
                    textInputEditTextManagerContact.text.toString(),
                    textInputEditTextManagerEmail.text.toString()+"@"+textViewManagerEmailDomain.text.toString()
                )

                //회원가입 하기
                userViewModel.signUpAndSave(email, password, seller)

            }

        }


    }


    private fun updateUiWithUser(@StringRes succeedString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, succeedString, Toast.LENGTH_LONG).show()
        //메인 화면으로 이동
        //로그인 프래그먼트까지 제거하고 메인화면으로 이동
        findNavController().popBackStack(R.id.loginFragment, true)
        findNavController().navigate(R.id.mainFragment)

    }

    private fun showSignUpFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }
}