package com.petpal.swimmer_customer.ui.modifyinfo

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        fragmentModifyInfoBinding.buttonModifyPassword.setOnClickListener {
            val currentPassword=fragmentModifyInfoBinding.textInputEditTextCurrentPassword.text.toString()
            val newPassword=fragmentModifyInfoBinding.textInputEditTextNewPassword.text.toString()
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