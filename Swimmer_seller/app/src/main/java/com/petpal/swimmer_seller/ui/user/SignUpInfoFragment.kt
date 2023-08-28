package com.petpal.swimmer_seller.ui.user

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
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


        userViewModel.run {
            userResult.observe(viewLifecycleOwner) { result ->
                result ?: return@observe
                result.error?.let {
                    showSignUpFailed(it)
                }
                result.successInt?.let {
                    updateUiWithUser(it)
                }
            }

            //정보 입력 유효성 검사
            infoFormState.observe(viewLifecycleOwner) {
                if (it == null) {
                    return@observe
                }

                fragmentSignUpInfoBinding.run {
                    //_formComplete 상태에 따라 button enable 변경
                    buttonSignUp.isEnabled = it.isDataValid && checkBox.isChecked

                    if (it.businessRegNumberError == null) textInputLayoutCompanyRegNum.error = null
                    else textInputLayoutCompanyRegNum.error = getString(it.businessRegNumberError)

                    if (it.representNameError == null) textInputLayoutOwnerName.error = null
                    else textInputLayoutOwnerName.error = getString(it.representNameError)

                    if (it.brandNameError == null) textInputLayoutCompanyName.error = null
                    else textInputLayoutCompanyName.error = getString(it.brandNameError)

                    if (it.descriptionError == null) textInputLayoutCompanyDesc.error = null
                    else textInputLayoutCompanyDesc.error = getString(it.descriptionError)

                    if (it.addressError == null) textInputLayoutCompanyAddrs.error = null
                    else textInputLayoutCompanyAddrs.error = getString(it.addressError)

                    if (it.brandPhoneNumberError == null) textInputLayoutCompanyContact.error = null
                    else textInputLayoutCompanyContact.error = getString(it.brandPhoneNumberError)

                    if (it.bankNameError == null) textInputLayoutCompanyBank.error = null
                    else textInputLayoutCompanyBank.error = getString(it.bankNameError)

                    if (it.accountNumberError == null) textInputLayoutCompanyAccount.error = null
                    else textInputLayoutCompanyAccount.error = getString(it.accountNumberError)

                    if (it.contactNameError == null) textInputLayoutManagerName.error = null
                    else textInputLayoutManagerName.error = getString(it.contactNameError)

                    if (it.contactPhoneNumberError == null) textInputLayoutManagerContact.error =
                        null
                    else textInputLayoutManagerContact.error = getString(it.contactPhoneNumberError)

                    if (it.contactEmailError == null) textInputLayoutManagerEmail.error = null
                    else textInputLayoutManagerEmail.error = getString(it.contactEmailError)
                }
            }

            consentState.observe(viewLifecycleOwner) {
                if (it == null) {
                    return@observe
                }

                fragmentSignUpInfoBinding.buttonSignUp.isEnabled =
                    it && infoFormState.value?.isDataValid ?: false
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
                    email,
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
                    textInputEditTextManagerEmail.text.toString() + "@" + textViewManagerEmailDomain.text.toString()
                )

                //회원가입 하기
                userViewModel.signUpAndSave(email, password, seller)

            }

            val afterTextChangedListener = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                    // ignore
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    // ignore
                }

                override fun afterTextChanged(s: Editable) {
                    userViewModel.infoDataChanged(
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
                        textInputEditTextManagerEmail.text.toString(),
                    )
                }
            }
            textInputEditTextCompanyRegNum.addTextChangedListener(afterTextChangedListener)
            textInputEditTextOwnerName.addTextChangedListener(afterTextChangedListener)
            textInputEditTextCompanyName.addTextChangedListener(afterTextChangedListener)
            textInputEditTextCompanyDesc.addTextChangedListener(afterTextChangedListener)
            textInputEditTextCompanyAddrs.addTextChangedListener(afterTextChangedListener)
            textInputEditTextCompanyContact.addTextChangedListener(afterTextChangedListener)
            textInputEditTextCompanyAccount.addTextChangedListener(afterTextChangedListener)
            textInputEditTextManagerName.addTextChangedListener(afterTextChangedListener)
            textInputEditTextManagerContact.addTextChangedListener(afterTextChangedListener)
            textInputEditTextManagerEmail.addTextChangedListener(afterTextChangedListener)
            checkBox.setOnCheckedChangeListener { _, b ->
                if (b) showConsentDialog()
                else userViewModel.consentChanged(b)
            }
        }


    }

    private fun showConsentDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setTitle("개인정보 수집이용 약관")
            .setMessage(R.string.privacy_policy)
            .setPositiveButton("확인") { _, _ ->
                fragmentSignUpInfoBinding.checkBox.isChecked = true
                userViewModel.consentChanged(true)
            }
            .setNegativeButton("취소") { _, _ ->
                fragmentSignUpInfoBinding.checkBox.isChecked = false
                userViewModel.consentChanged(false)
            }
            .setOnCancelListener {
                fragmentSignUpInfoBinding.checkBox.isChecked = false
                userViewModel.consentChanged(false)
            }
        builder.show()
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