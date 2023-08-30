package com.petpal.swimmer_customer.ui.modifyinfo

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository
import com.petpal.swimmer_customer.databinding.FragmentModifyInfoBinding

class ModifyInfoFragment : Fragment() {
    private lateinit var binding: FragmentModifyInfoBinding
    private lateinit var viewModel: ModifyInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentModifyInfoBinding.inflate(layoutInflater)
        viewModelSetup()
        toolbarSetup()
        observerSetup()
        clickListenerSetup()
        handleBackPress()

        return binding.root
    }
    //백버튼 제어
    private fun handleBackPress() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun viewModelSetup() {
        val factory = ModifyInfoModelFactory(CustomerUserRepository())
        viewModel = ViewModelProvider(this, factory).get(ModifyInfoViewModel::class.java)
    }

    private fun toolbarSetup() {
        binding.toolbarModifyInfo.run {
            title = getString(R.string.modifyinfo_title)
            setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener {
                findNavController().popBackStack()
                findNavController().popBackStack()
            }
        }
    }

    private fun observerSetup() {
        viewModel.withdrawalUserResult.observe(viewLifecycleOwner, Observer { isSuccess ->
            handleWithdrawalResult(isSuccess)
        })
    }
    //클릭 리스너 모음
    private fun clickListenerSetup() {
        binding.buttonModifyPassword.setOnClickListener { modifyPassword() }
        binding.buttonModifySwimExp.setOnClickListener { modifySwimExp() }
        binding.buttonModifyNickname.setOnClickListener { modifyNickname() }
        binding.buttonModifyPhone.setOnClickListener { modifyPhone() }
        binding.buttonWithdrawal.setOnClickListener { withdrawalUser() }
    }

    //회원탈퇴의 결과 처리
    private fun handleWithdrawalResult(isSuccess: Boolean?) {
        if (isSuccess == true) {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.title_notification))
                .setMessage(getString(R.string.withdrawal_success))
                .setPositiveButton(getString(R.string.action_confirm)) { _, _ ->
                    val action = ModifyInfoFragmentDirections.actionModifyInfoFragmentToMainFragment()
                    findNavController().navigate(action)
                }
                .show()
        } else {
            showToast(getString(R.string.withdrawal_user_failure))
        }
    }

    //유효성 검사 -> 비밀번호 변경
    private fun modifyPassword() {
        val currentPassword = binding.textInputEditTextCurrentPassword.text.toString()
        val newPassword = binding.textInputEditTextNewPassword.text.toString()

        when {
            !viewModel.isValidPassword(currentPassword) -> {
                showError(binding.textInputLayoutCurrentPassword, binding.textInputEditTextCurrentPassword, R.string.error_password_length)
            }
            !viewModel.isValidPassword(newPassword) -> {
                showError(binding.textInputLayoutNewPassword, binding.textInputEditTextNewPassword, R.string.error_newpassword_length)
            }
            currentPassword == newPassword -> {
                showError(binding.textInputLayoutNewPassword, binding.textInputEditTextNewPassword, R.string.duplicate_password)
            }
            else -> {
                viewModel.modifyPassword(currentPassword, newPassword).observe(viewLifecycleOwner, Observer { success ->
                    handleModifyPasswordResult(success)
                })
            }
        }
    }

    //수영 경력 변경
    private fun modifySwimExp() {
        val newSwimExp = newSwimExp()
        viewModel.getCurrentUser()?.observe(viewLifecycleOwner, Observer { currentUser ->
            currentUser?.let {
                it.swimExp = newSwimExp
                viewModel.ModifyUserInfo(it).observe(viewLifecycleOwner, Observer { updateSuccess ->
                    if (updateSuccess == true) {
                        showToast(getString(R.string.modify_swimexp_success))
                    } else {
                        showToast(getString(R.string.modify_swimexp_failure))
                    }
                })
            }
        })
    }
    //닉네임 변경
    private fun modifyNickname() {
        val newNickname = binding.textInputEditTextNewNickname.text.toString()
        if (viewModel.isNicknameEmpty(newNickname)) {
            showError(binding.textInputLayoutNewNickname,binding.textInputEditTextNewNickname,R.string.error_nickname_required)
        } else {
            viewModel.getCurrentUser()?.observe(viewLifecycleOwner, Observer { currentUser ->
                currentUser?.let {
                    it.nickName = newNickname
                    viewModel.ModifyUserInfo(it)
                        .observe(viewLifecycleOwner, Observer { updateSuccess ->
                            if (updateSuccess == true) {
                                binding.textInputEditTextNewNickname.text?.clear()
                                showToast(getString(R.string.modify_nickname_success))
                                hideKeyboard(requireContext(), binding.textInputEditTextNewNickname)
                            } else {
                                binding.textInputEditTextNewNickname.text?.clear()
                                showToast(getString(R.string.modify_nickname_failure))
                                hideKeyboard(requireContext(), binding.textInputEditTextNewNickname)
                            }
                        })
                }
            })
        }
    }
    //휴대폰 번호 변경
    private fun modifyPhone() {

        val newPhoneNumber = binding.textInputEditTextNewNickPhone.text.toString()
        if (viewModel.isNicknameEmpty(newPhoneNumber)) {
            showError(binding.textInputLayoutNewNickPhone,binding.textInputEditTextNewNickPhone,R.string.error_phone_required)
        } else {
            viewModel.getCurrentUser()?.observe(viewLifecycleOwner, Observer { currentUser ->
                currentUser?.let {
                    it.phoneNumber = newPhoneNumber
                    viewModel.ModifyUserInfo(it)
                        .observe(viewLifecycleOwner, Observer { updateSuccess ->
                            if (updateSuccess == true) {
                                binding.textInputEditTextNewNickPhone.text?.clear()
                                showToast(getString(R.string.modfiy_phone_success))
                                hideKeyboard(
                                    requireContext(),
                                    binding.textInputEditTextNewNickPhone
                                )
                            } else {
                                showToast(getString(R.string.modfiy_phone_failure))
                                binding.textInputEditTextNewNickPhone.text?.clear()
                                hideKeyboard(
                                    requireContext(),
                                    binding.textInputEditTextNewNickPhone
                                )
                            }
                        })
                }

            })
        }
    }

    private fun withdrawalUser() {
        viewModel.withdrawalUser()
    }

    private fun showError(layout: TextInputLayout, editText: TextInputEditText, errorResId: Int) {
        layout.error = getString(errorResId)
        Handler(Looper.getMainLooper()).postDelayed({
            layout.error = ""
            editText.text?.clear()
        }, 2000)
    }

    //비밀번호 변경 결과 처리
    private fun handleModifyPasswordResult(success: Boolean?) {
        if (success == true) {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.password_modify_success))
                .setMessage(getString(R.string.modifypassword_relogin))
                .setPositiveButton(getString(R.string.action_confirm)) { _, _ ->
                    binding.textInputEditTextNewPassword.text?.clear()
                    binding.textInputEditTextCurrentPassword.text?.clear()
                    viewModel.setAutoLoginEnabled(requireContext(), false)
                    viewModel.signOut()
                    val action = ModifyInfoFragmentDirections.actionModifyInfoFragmentToMainFragment()
                    findNavController().navigate(action)
                }
                .show()
        } else {
            showToast(getString(R.string.password_modify_failure))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    private fun newSwimExp():String {
        return when (binding.newSwimExpGroup.checkedChipId) {
            R.id.newSwimExp1-> getString(R.string.duration_less_than_1_year)
            R.id.newSwimExp2 -> getString(R.string.duration_less_than_3_years)
            R.id.newSwimExp3 -> getString(R.string.duration_less_than_5_years)
            R.id.newSwimExp4 -> getString(R.string.duration_more_than_5_years)
            else -> getString(R.string.duration_no_selection)
        }
    }
    //개인정보 변경 성공 또는 실패 시, 키보드 내리기
    fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

class ModifyInfoModelFactory(private val repository: CustomerUserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ModifyInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ModifyInfoViewModel(repository) as T
        }
        throw IllegalArgumentException("알 수 없는 ViewModel 클래스")
    }
}