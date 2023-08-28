package com.petpal.swimmer_customer.ui.mypage

import android.app.AlertDialog
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
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository
import com.petpal.swimmer_customer.databinding.FragmentMypageBinding
import com.petpal.swimmer_customer.ui.findinfo.FindInfoViewModel
import com.petpal.swimmer_customer.ui.findinfo.FindInfoViewModelFactory
import com.petpal.swimmer_customer.ui.login.LoginFragmentDirections
import com.petpal.swimmer_customer.util.AutoLoginUtil

//배송지 관리로 가는 마이페이지 프래그먼트
//util의 AutoLoginUtil을 이용하여 로그아웃시, 자동로그인 비활성화


class MypageFragment : Fragment() {

    lateinit var fragmentMypageBinding: FragmentMypageBinding
    lateinit var viewModel: MypageViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMypageBinding= FragmentMypageBinding.inflate(layoutInflater)

        val factory = MypageViewModelFactory(CustomerUserRepository())
        viewModel = ViewModelProvider(this, factory).get(MypageViewModel::class.java)
        val uid=FirebaseAuth.getInstance().currentUser?.uid
        viewModel.getCurrentUser(uid!!).observe(viewLifecycleOwner, Observer {
            if(it!=null){
                fragmentMypageBinding.textViewNickname.text="닉네임 : "
            }else{
                fragmentMypageBinding.textViewNickname.text=getString(R.string.error_nickname_none)
            }
        })
        fragmentMypageBinding.toolbarMypage.run {
            title = getString(R.string.mypage_title)
            inflateMenu(R.menu.mypage_toolbar_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_notification -> {

                    }

                    R.id.item_shopping_cart -> {

                    }

                }
                false
            }
        }

        val coupons = listOf("쿠폰","10% 할인쿠폰", " 5,000원 할인쿠폰", "무료 배송 쿠폰")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, coupons)
        val spinner: Spinner = fragmentMypageBinding.couponSpinner
        spinner.adapter = adapter

        //보여주기 용이라 특정 쿠폰을 선택해도 선택창으로 돌아오도록 세팅
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                spinner.setSelection(0)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        //내 정보 수정으로 이동
        fragmentMypageBinding.buttonModifyInfo.setOnClickListener {
            findNavController().navigate(R.id.CheckPasswordFragment)
        }
        //주문 조회로 이동
        fragmentMypageBinding.textViewCompleteCount.setOnClickListener {

        }
        //주문 조회로 이동
        fragmentMypageBinding.textViewShippingCount.setOnClickListener {

        }
        //주문 조회로 이동
        fragmentMypageBinding.textViewDeliveryCompletedCount.setOnClickListener {

        }

        //배송지 관리로 이동
        fragmentMypageBinding.buttonDeliveryPointManage.setOnClickListener {
            val action = MypageFragmentDirections.actionItemMypageToDeliveryPointManageFragment()
            findNavController().navigate(action)
        }
        //로그아웃
        fragmentMypageBinding.buttonLogOut.setOnClickListener {
            viewModel.signOut()
            // 자동 로그인을 꺼줘야 함
            AutoLoginUtil.setAutoLogin(requireContext(), false)
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.title_notification))
                .setMessage(getString(R.string.alert_message_logout_completed))
                .setPositiveButton(getString(R.string.action_confirm)) { dialog, _ ->
                    val action = MypageFragmentDirections.actionItemMypageToMainFragment()
                    findNavController().navigate(action)
                    dialog.dismiss()
                }
                .show()
        }
        return fragmentMypageBinding.root
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
