package com.petpal.swimmer_seller.ui.user

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.petpal.swimmer_seller.R
import com.petpal.swimmer_seller.databinding.FragmentFindEmailBinding

class FindEmailFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private var _fragmentFindEmailBinding: FragmentFindEmailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val fragmentFindEmailBinding get() = _fragmentFindEmailBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentFindEmailBinding = FragmentFindEmailBinding.inflate(layoutInflater)
        return fragmentFindEmailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(this, UserViewModelFactory())[UserViewModel::class.java]

        val textInputName = fragmentFindEmailBinding.textInputFindEmailName
        val editTextName = fragmentFindEmailBinding.editTextFindEmailName
        val textInputPhone = fragmentFindEmailBinding.textInputFindEmailPhone
        val editTextPhone = fragmentFindEmailBinding.editTextFindEmailPhone
        val buttonFindEmail = fragmentFindEmailBinding.buttonFindEmail
        val findEmailToolBar = fragmentFindEmailBinding.findEmailToolbar

        userViewModel.run {
            userResult.observe(viewLifecycleOwner) { result ->
                result ?: return@observe
                result.error?.let {
                    showFindEmailFailed(it)
                }
                result.successString?.let {
                    showEmailWithDialog(it)
                }
            }

            findEmailFormState.observe(viewLifecycleOwner) {
                if (it == null) {
                    return@observe
                }
                buttonFindEmail.isEnabled = it.isDataValid

                //이름 입력칸 에러 메세지
                if (it.nameError == null) {
                    textInputName.error = null
                } else {
                    textInputName.error = getString(it.nameError)
                }

                //폰번호 입력칸 에러 메세지
                if (it.phoneNumberError == null) {
                    textInputPhone.error = null
                } else {
                    textInputPhone.error = getString(it.phoneNumberError)
                }
            }
        }

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                userViewModel.findEmailDataChanged(
                    editTextName.text.toString(),
                    editTextPhone.text.toString()
                )
            }
        }
        editTextName.addTextChangedListener(afterTextChangedListener)
        editTextPhone.addTextChangedListener(afterTextChangedListener)
        editTextPhone.setOnEditorActionListener { textView, i, keyEvent ->
            findEmail(editTextName.text.toString(), editTextPhone.text.toString())
            false
        }

        buttonFindEmail.setOnClickListener {
            findEmail(editTextName.text.toString(), editTextPhone.text.toString())
        }

        //뒤로가기 버튼
        findEmailToolBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun findEmail(name: String, phone: String) {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (requireActivity().currentFocus != null) {
            imm.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, 0)
            requireActivity().currentFocus!!.clearFocus()
        }

        userViewModel.findEmail(name, phone)
    }

    private fun showEmailWithDialog(it: String) {
        //R.string.~~로 들어오는 it로 다이얼로그 보여주기 (it는 사용자 이메일)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("이메일은")
        builder.setMessage(it)
        builder.setPositiveButton("로그인으로 이동하기") { dialogInterface: DialogInterface, i: Int ->
            //action으로 바까주기
            findNavController().navigate(R.id.loginFragment)
        }
        builder.show()
    }

    private fun showFindEmailFailed(i: Int) {
        Toast.makeText(requireContext(), i, Toast.LENGTH_SHORT).show()
    }

}