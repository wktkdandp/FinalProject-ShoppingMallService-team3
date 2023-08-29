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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository
import com.petpal.swimmer_customer.databinding.FragmentModifyInfoBinding
import com.petpal.swimmer_customer.ui.mypage.MypageFragmentDirections


class ModifyInfoFragment : Fragment() {
    lateinit var fragmentModifyInfoBinding: FragmentModifyInfoBinding
    lateinit var viewModel:ModifyInfoViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentModifyInfoBinding = FragmentModifyInfoBinding.inflate(layoutInflater)

        val factory = ModifyInfoModelFactory(CustomerUserRepository())
        viewModel = ViewModelProvider(this, factory).get(ModifyInfoViewModel::class.java)


        setupToolbar()

        viewModel.withdrawalUserResult.observe(viewLifecycleOwner, Observer { isSuccess ->
            if (isSuccess==true) {
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.title_notification))
                    .setMessage(getString(R.string.withdrawal_success))
                    .setPositiveButton(getString(R.string.action_confirm)) { dialog, _ ->
                        val action = ModifyInfoFragmentDirections.actionModifyInfoFragmentToMainFragment()
                        findNavController().navigate(action)
                    }
                    .show()
            } else {
                // 실패 메시지 표시
                showToast("Account deletion failed")
            }
        })


        fragmentModifyInfoBinding.buttonModifyPassword.setOnClickListener {
            val currentPassword=fragmentModifyInfoBinding.textInputEditTextCurrentPassword.text.toString()
            val newPassword=fragmentModifyInfoBinding.textInputEditTextNewPassword.text.toString()

            if(!viewModel.isValidPassword(currentPassword)){
                fragmentModifyInfoBinding.textInputLayoutCurrentPassword.error=getString(R.string.error_password_length)
                Handler(Looper.getMainLooper()).postDelayed({
                    fragmentModifyInfoBinding.textInputLayoutCurrentPassword.error = ""
                    fragmentModifyInfoBinding.textInputEditTextCurrentPassword.text?.clear()
                }, 2000)

                return@setOnClickListener
            }
            if(!viewModel.isValidPassword(newPassword)){
                fragmentModifyInfoBinding.textInputLayoutNewPassword.error=getString(R.string.error_password_length)
                Handler(Looper.getMainLooper()).postDelayed({
                    fragmentModifyInfoBinding.textInputLayoutNewPassword.error = ""
                    fragmentModifyInfoBinding.textInputEditTextNewPassword.text?.clear()
                }, 2000)
                return@setOnClickListener
            }
            if(currentPassword == newPassword){
                fragmentModifyInfoBinding.textInputLayoutNewPassword.error=getString(R.string.duplicate_password)
                Handler(Looper.getMainLooper()).postDelayed({
                    fragmentModifyInfoBinding.textInputLayoutNewPassword.error = ""
                    fragmentModifyInfoBinding.textInputEditTextNewPassword.text?.clear()
                }, 2000)
                return@setOnClickListener
            }

            viewModel.modifyPassword(currentPassword,newPassword).observe(viewLifecycleOwner, Observer { success ->
                if (success == true) {
                    AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.password_modify_success))
                        .setMessage(getString(R.string.modifypassword_relogin))
                        .setPositiveButton(getString(R.string.action_confirm)) { dialog, _ ->
                            fragmentModifyInfoBinding.textInputEditTextNewPassword.text?.clear()
                            fragmentModifyInfoBinding.textInputEditTextCurrentPassword.text?.clear()
                            viewModel.setAutoLoginEnabled(requireContext(), false)
                            viewModel.signOut()
                            val action = ModifyInfoFragmentDirections.actionModifyInfoFragmentToMainFragment()
                            findNavController().navigate(action)
                        }
                        .show()
                } else {
                    // 업데이트 실패
                    Toast.makeText(requireContext(), getString(R.string.password_modify_failure), Toast.LENGTH_SHORT).show()
                }
            })

        }
        fragmentModifyInfoBinding.buttonModifySwimExp.setOnClickListener {
            val newSwimExp = NewSwimExp()
            viewModel.getCurrentUser()?.observe(viewLifecycleOwner, Observer { currentUser ->
                currentUser?.let {
                    it.swimExp = newSwimExp
                    viewModel.ModifyUserInfo(it).observe(viewLifecycleOwner, Observer { updateSuccess ->
                        if (updateSuccess == true) {
                            showToast("수영 경력이 수정되었습니다.")
                        } else {
                            showToast("수영 경력 수정에 실패했습니다.")
                        }
                    })
                }
            })
        }
        fragmentModifyInfoBinding.buttonModifyNickname.setOnClickListener {
            val newNickname=fragmentModifyInfoBinding.textInputEditTextNewNickname.text.toString()
            viewModel.getCurrentUser()?.observe(viewLifecycleOwner, Observer { currentUser ->
                currentUser?.let {
                    it.nickName = newNickname
                    viewModel.ModifyUserInfo(it).observe(viewLifecycleOwner, Observer { updateSuccess ->
                        if (updateSuccess == true) {
                            showToast("닉네임이 수정되었습니다.")
                            hideKeyboard(requireContext(),fragmentModifyInfoBinding.textInputEditTextNewNickname)
                        } else {
                            showToast("닉네임 수정에 실패했습니다.")
                            hideKeyboard(requireContext(),fragmentModifyInfoBinding.textInputEditTextNewNickname)
                        }
                    })
                }
            })
        }
        fragmentModifyInfoBinding.buttonModifyPhone.setOnClickListener {
            val newPhoneNumber=fragmentModifyInfoBinding.textInputEditTextNewNickPhone.text.toString()
            viewModel.getCurrentUser()?.observe(viewLifecycleOwner, Observer { currentUser->
                currentUser?.let{
                    it.phoneNumber=newPhoneNumber
                    viewModel.ModifyUserInfo(it).observe(viewLifecycleOwner, Observer {updateSuccess ->
                        if (updateSuccess == true) {
                            showToast("휴대폰 번호가 수정되었습니다.")
                            hideKeyboard(requireContext(),fragmentModifyInfoBinding.textInputEditTextNewNickPhone)
                        } else {
                            showToast("휴대폰 번호 수정에 실패했습니다.")
                            hideKeyboard(requireContext(),fragmentModifyInfoBinding.textInputEditTextNewNickPhone)
                        }
                    })
                }

            })
        }
        fragmentModifyInfoBinding.buttonWithdrawal.setOnClickListener {
            viewModel.withdrawalUser()
        }


        return fragmentModifyInfoBinding.root
    }
    private fun setupToolbar() {
        fragmentModifyInfoBinding.toolbarModifyInfo.run {
            title = getString(R.string.modifyinfo_title)
            setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener{
                findNavController().popBackStack()
            }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    private fun NewSwimExp():String {
        return when (fragmentModifyInfoBinding.newSwimExpGroup.checkedChipId) {
            R.id.newSwimExp1-> getString(R.string.duration_less_than_1_year)
            R.id.newSwimExp2 -> getString(R.string.duration_less_than_3_years)
            R.id.newSwimExp3 -> getString(R.string.duration_less_than_5_years)
            R.id.newSwimExp4 -> getString(R.string.duration_more_than_5_years)
            else -> getString(R.string.duration_no_selection)
        }
    }
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
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}