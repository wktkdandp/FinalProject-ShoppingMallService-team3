package com.petpal.swimmer_customer.ui.modifyinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository
import com.petpal.swimmer_customer.databinding.FragmentCheckPasswordBinding

class CheckPasswordFragment : Fragment() {
    lateinit var fragmentCheckPasswordBinding: FragmentCheckPasswordBinding
    lateinit var viewModel: CheckPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentCheckPasswordBinding = FragmentCheckPasswordBinding.inflate(layoutInflater)

        setupViewModel()
        setupToolbar()
        handleBackPress()
        setupPasswordCheck()

        return fragmentCheckPasswordBinding.root
    }

    private fun setupViewModel() {
        val factory = CheckPasswordModelFactory(CustomerUserRepository())
        viewModel = ViewModelProvider(this, factory).get(CheckPasswordViewModel::class.java)
    }

    //백버튼 제어
    private fun handleBackPress() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun setupToolbar() {
        fragmentCheckPasswordBinding.toolbarCheckPassword.run {
            title = getString(R.string.check_passswordpage_title)
            setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
    //마이페이지 진입을 위한 비밀번호 검사
    private fun setupPasswordCheck() {
        fragmentCheckPasswordBinding.ButtonCheckPassword.setOnClickListener {
            val password = fragmentCheckPasswordBinding.textInputEditTextCheckPassword.text.toString()

            viewModel.checkPassword(password).observe(viewLifecycleOwner, Observer { isValid ->
                if (isValid) {
                    navigateToModifyInfoFragment()
                } else {
                    showPasswordError()
                }
            })
        }
    }

    private fun navigateToModifyInfoFragment() {
        val action = CheckPasswordFragmentDirections.actionCheckPasswordFragmentToModifyInfoFragment()
        findNavController().navigate(action)
    }

    private fun showPasswordError() {
        fragmentCheckPasswordBinding.textInputEditTextCheckPassword.text?.clear()
        fragmentCheckPasswordBinding.textInputLayoutCheckPassword.error = getString(R.string.error_password)
    }
}

class CheckPasswordModelFactory(private val repository: CustomerUserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckPasswordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CheckPasswordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}