package com.petpal.swimmer_customer.ui.mypage

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository
import com.petpal.swimmer_customer.databinding.FragmentMypageBinding
import com.petpal.swimmer_customer.ui.findinfo.FindInfoViewModel
import com.petpal.swimmer_customer.ui.findinfo.FindInfoViewModelFactory
import com.petpal.swimmer_customer.util.AutoLoginUtil


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


        fragmentMypageBinding.toolbarMypage.run {
            title = "마이 페이지"
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

        viewModel.getCurrentUser()?.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                fragmentMypageBinding.textViewNickname.text="닉네임 : ${user.nickName} "
            } else {
                //로그인하지않고 마이페이지 들어왔을때 일단 구현만 해놓음
                AlertDialog.Builder(requireContext())
                    .setTitle("알림")
                    .setMessage("로그인이 필요한 서비스입니다.")
                    .setPositiveButton("확인") { dialog, _ ->
                        findNavController().navigate(R.id.action_item_mypage_to_LoginFragment)
                        dialog.dismiss()
                    }
                    .show()
            }
        })


        val coupons = listOf("쿠폰","10% 할인쿠폰", " 5,000원 할인쿠폰", "무료 배송 쿠폰")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, coupons)
        val spinner: Spinner = fragmentMypageBinding.couponSpinner
        spinner.adapter = adapter

        //보여주기 용이라 특정 쿠폰을 선택해도 선택창으로 돌아오도록 세팅
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                spinner.setSelection(0)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        //주문 조회로 이동
        fragmentMypageBinding.buttonOrderComplete.setOnClickListener {

        }
        //주문 조회로 이동
        fragmentMypageBinding.buttonShipping.setOnClickListener {

        }
        //주문 조회로 이동
        fragmentMypageBinding.buttonDeliveryCompleted.setOnClickListener {

        }

        //내 리뷰 보기로 이동
        fragmentMypageBinding.buttonShowMyReview.setOnClickListener {

        }
        //로그아웃
        fragmentMypageBinding.buttonLogOut.setOnClickListener {
           viewModel.signOut()
            //자동로그인을 꺼줘야 함
            AutoLoginUtil.setAutoLogin(requireContext(), false)
            AlertDialog.Builder(requireContext())
                .setTitle("알림")
                .setMessage("로그아웃 완료되었습니다.")
                .setPositiveButton("확인") { dialog, _ ->
                    findNavController().navigate(R.id.action_item_mypage_to_LoginFragment)
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
