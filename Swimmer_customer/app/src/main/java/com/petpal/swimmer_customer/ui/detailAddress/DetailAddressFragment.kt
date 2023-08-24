package com.petpal.swimmer_customer.ui.detailAddress

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.data.model.Address
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository
import com.petpal.swimmer_customer.databinding.FragmentDetailAddressBinding

class DetailAddressFragment : Fragment() {
    private lateinit var binding: FragmentDetailAddressBinding
    private lateinit var viewModel: DetailAddressViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetailAddressBinding.inflate(layoutInflater)
        setupViewModel()
        binding.textViewApiAddress.text=arguments?.getString("address")
        setupUI()
        return binding.root
    }

    private fun setupViewModel() {
        val factory = DetailAddressModelFactory(CustomerUserRepository())
        viewModel = ViewModelProvider(this, factory).get(DetailAddressViewModel::class.java)

        viewModel.updateResult.observe(viewLifecycleOwner, Observer { isSuccess ->
            if (isSuccess == true) {
                showToast(getString(R.string.address_added_success))
                navigateToDeliveryPointManageFragment()
            } else {
                showToast(getString(R.string.address_added_failure))
                findNavController().popBackStack()
            }
        })
    }

    private fun setupUI() {
        setupToolbar()

        binding.ButtonSubmitAddress.setOnClickListener {
            val name = binding.textInputEditDetailAddressName.text.toString()
            val detailAddress = binding.textInputEditDetailAddress.text.toString()
            val phone = binding.textInputEditDetailAddressPhone.text.toString()
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            val address = Address(null, arguments?.getString("postcode")?.toLong(), "${binding.textViewApiAddress.text} $detailAddress", name, phone)

            if (!viewModel.isValidName(name)) {
                showError(binding.textInputLayoutDetailAddressName, binding.textInputEditDetailAddressName, getString(R.string.error_name_required))
                return@setOnClickListener
            }

            if (!viewModel.isValidDetailAddress(detailAddress)) {
                showError(binding.textInputLayoutDetailAddress, binding.textInputEditDetailAddress, getString(R.string.error_detail_delivery_point_required))
                return@setOnClickListener
            }

            if (!viewModel.isValidPhone(phone)) {
                showError(binding.textInputLayoutDetailAddressPhone, binding.textInputEditDetailAddressPhone, getString(R.string.error_phone_required))
                return@setOnClickListener
            }
            if (uid != null) {
                viewModel.addAddressForUser(uid, address)
            }
        }

        binding.ButtCancelAddress.setOnClickListener {
            cancelAction()
        }
    }

    private fun setupToolbar() {
        binding.toolbarDetailAddress.run{
            title = getString(R.string.detail_address_toolbar)
            inflateMenu(R.menu.mypage_toolbar_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_notification -> {}
                    R.id.item_shopping_cart -> {}
                }
                false
            }
            setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener {
                cancelAction()
            }
        }
    }



    private fun showError(textInputLayout: TextInputLayout, textInputEditText: TextInputEditText, errorMessage: String) {
        textInputLayout.error = errorMessage
        Handler(Looper.getMainLooper()).postDelayed({
            textInputLayout.error = ""
            textInputEditText.text?.clear()
            showKeyboard(textInputEditText)
        }, 2000)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToDeliveryPointManageFragment() {
        findNavController().navigate(R.id.DeliveryPointManageFragment)
    }

    private fun cancelAction() {
        showToast(getString(R.string.delevery_point_cancel))
        navigateToDeliveryPointManageFragment()
    }

    private fun showKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}

class DetailAddressModelFactory(private val repository: CustomerUserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailAddressViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailAddressViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}