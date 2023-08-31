package com.petpal.swimmer_customer.ui.mypage

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository
import com.petpal.swimmer_customer.databinding.FragmentMypageBinding
import com.petpal.swimmer_customer.ui.findinfo.FindInfoViewModel
import com.petpal.swimmer_customer.ui.findinfo.FindInfoViewModelFactory
import com.petpal.swimmer_customer.ui.login.LoginFragmentDirections
import com.petpal.swimmer_customer.ui.mypage.MypageFragmentDirections.Companion.actionItemMypageToOrderListFragment
import com.petpal.swimmer_customer.util.AutoLoginUtil
import com.squareup.picasso.Picasso

//배송지 관리로 가는 마이페이지 프래그먼트
//util의 AutoLoginUtil을 이용하여 로그아웃시, 자동로그인 비활성화


class MypageFragment : Fragment() {

    companion object {
        private val PICK_IMAGE_REQUEST = 111
        private val REQUEST_STORAGE_PERMISSION = 112
    }

    lateinit var fragmentMypageBinding: FragmentMypageBinding
    lateinit var viewModel: MypageViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMypageBinding = FragmentMypageBinding.inflate(layoutInflater)
        setupViewModel()
        setupProfileImage()
        setupNickname()
        setupToolbar()
        setupCouponSpinner()
        setupClickListeners()
        handleBackPress()
        return fragmentMypageBinding.root
    }
    //백버튼 제어
    private fun handleBackPress() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val action=MypageFragmentDirections.actionItemMypageToItemHome()
                findNavController().navigate(action)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }


    private fun setupViewModel() {
        val factory = MypageViewModelFactory(CustomerUserRepository())
        viewModel = ViewModelProvider(this, factory).get(MypageViewModel::class.java)
    }

    //프로필 이미지 받아서 표시
    private fun setupProfileImage() {
        //.veilLayout.veil()
        viewModel.loadProfileImage().observe(viewLifecycleOwner, Observer { uri ->
            if (uri != null) {
                //fragmentMypageBinding.veilLayout.unVeil()
                Picasso.get().load(uri).into(fragmentMypageBinding.imageViewProfilePhoto)
            } else {
                //showToast(getString(R.string.profile_failure))
            }
        })
    }
    //닉네임 받아서 표시
    private fun setupNickname() {
        viewModel.getCurrentUser()?.observe(viewLifecycleOwner, Observer {
            fragmentMypageBinding.textViewNickname.text = it?.let {
                "${it.nickName}님 안녕하세요!"
            } ?: getString(R.string.error_nickname_none)
        })
    }

    private fun setupToolbar() {
        fragmentMypageBinding.toolbarMypage.run {
            title = getString(R.string.mypage_title)
            inflateMenu(R.menu.mypage_toolbar_menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.item_notification -> {
                        // 알림 클릭 처리
                    }
                    R.id.item_shopping_cart -> {
                        // 장바구니 클릭 처리
                    }
                }
                false
            }
        }
    }

    private fun setupCouponSpinner() {
        val coupons = listOf("쿠폰", "10% 할인쿠폰", " 5,000원 할인쿠폰", "무료 배송 쿠폰")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, coupons)
        fragmentMypageBinding.couponSpinner.adapter = adapter
        fragmentMypageBinding.couponSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                fragmentMypageBinding.couponSpinner.setSelection(0)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
    //버튼 클릭 리스너 모음
    private fun setupClickListeners() {
        fragmentMypageBinding.buttonModifyInfo.setOnClickListener { navigateToCheckPasswordFragment() }
        fragmentMypageBinding.buttonDeliveryPointManage.setOnClickListener { navigateToDeliveryPointManageFragment() }
        fragmentMypageBinding.imageViewProfilePhoto.setOnClickListener { openGallery() }
        fragmentMypageBinding.buttonLogOut.setOnClickListener { handleLogOut() }
        fragmentMypageBinding.cardViewShipping.setOnClickListener { navigateToOrderListFragment() }
        fragmentMypageBinding.cardViewOrderComplete.setOnClickListener { navigateToOrderListFragment() }
        fragmentMypageBinding.cardViewDeliveryCompletedCount.setOnClickListener { navigateToOrderListFragment() }
    }

    //이동제어
    private fun navigateToCheckPasswordFragment() {
        val action=MypageFragmentDirections.actionItemMypageToCheckPasswordFragment()
        findNavController().navigate(action)
    }
    //이동제어
    private fun navigateToDeliveryPointManageFragment() {
        val action = MypageFragmentDirections.actionItemMypageToDeliveryPointManageFragment()
        findNavController().navigate(action)
    }
    //이동제어
    private fun navigateToOrderListFragment() {
        val action = MypageFragmentDirections.actionItemMypageToOrderListFragment()
        findNavController().navigate(action)
    }
    //로그아웃
    private fun handleLogOut() {
        viewModel.setAutoLoginEnabled(requireContext(), false)
        viewModel.signOut()
        showLogOutDialog()
    }

    private fun showLogOutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.title_notification))
            .setMessage(getString(R.string.alert_message_logout_completed))
            .setPositiveButton(getString(R.string.action_confirm)) { dialog, _ ->
                navigateToMainFragment()
                dialog.dismiss()
            }
            .show()
    }

    private fun navigateToMainFragment() {
        val action = MypageFragmentDirections.actionItemMypageToMainFragment()
        findNavController().navigate(action)
    }

    //권한 요청, 갤러리의 이미지 선택
    private fun openGallery() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_STORAGE_PERMISSION
            )
        } else {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }

    //권한 요청의 결과에 따라 메서드 실행 / 토스트
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                } else {
                    Toast.makeText(context,getString(R.string.permission_denied) , Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    //이미지 선택 -> firebase에 업로드 -> 성공시 이미지 뷰 설정
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri = data.data
            viewModel.uploadImageToFirebase(selectedImageUri!!).observe(viewLifecycleOwner, Observer { success ->
                if (success) {
                    fragmentMypageBinding.imageViewProfilePhoto.setImageURI(selectedImageUri)
                } else {
                }
            })
        }
    }




}
class MypageViewModelFactory(private val repository: CustomerUserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MypageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MypageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
