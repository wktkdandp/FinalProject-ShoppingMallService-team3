package com.petpal.swimmer_seller.ui.user

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.petpal.swimmer_seller.databinding.FragmentResetPasswordBinding


class ResetPasswordFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private var _fragmentResetPasswordBinding: FragmentResetPasswordBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val fragmentResetPasswordBinding get() = _fragmentResetPasswordBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _fragmentResetPasswordBinding = FragmentResetPasswordBinding.inflate(layoutInflater)
        return fragmentResetPasswordBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(this, UserViewModelFactory())[UserViewModel::class.java]

        val resetPasswordToolbar = fragmentResetPasswordBinding.resetPasswordToolbar
        val textInputLayoutEmail = fragmentResetPasswordBinding.textInputLayoutResetPasswordEmail
        val editTextEmail = fragmentResetPasswordBinding.textInputEditTextResetPasswordEmail
        val buttonSendEmail = fragmentResetPasswordBinding.buttonSendEmail

        userViewModel.run {
            emailFormState.observe(viewLifecycleOwner) {
                if (it == null) {
                    return@observe
                }
                buttonSendEmail.isEnabled = it.isDataValid

                //email 입력칸 에러 메세지
                if (it.emailError == null) {
                    textInputLayoutEmail.error = null
                } else {
                    textInputLayoutEmail.error = getString(it.emailError)
                }

            }

            userResult.observe(viewLifecycleOwner) { result ->
                result ?: return@observe
                result.error?.let {
                    showSendEmailResult(it)
                }
                result.success?.let {
                    showSendEmailResult(it)
                }
            }
        }

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                userViewModel.emailDataChanged(editTextEmail.text.toString())
            }
        }
        editTextEmail.addTextChangedListener(afterTextChangedListener)
        editTextEmail.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendPasswordResetEmail(editTextEmail.text.toString())
            }
            false
        }

        //뒤로가기 버튼
        resetPasswordToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        //이메일 보내기 버튼
        buttonSendEmail.setOnClickListener {
            sendPasswordResetEmail(editTextEmail.text.toString())
        }
    }

    private fun sendPasswordResetEmail(email: String) {
        //TODO: 키보드 내리기
        userViewModel.sendPasswordResetEmail(email)
    }

    private fun showSendEmailResult(i: Int) {
        Snackbar.make(fragmentResetPasswordBinding.root, i, Snackbar.LENGTH_SHORT).show()
    }

}
