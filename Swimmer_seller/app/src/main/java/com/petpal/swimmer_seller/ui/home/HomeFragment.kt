package com.petpal.swimmer_seller.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.petpal.swimmer_seller.MainActivity
import com.petpal.swimmer_seller.R
import com.petpal.swimmer_seller.data.repository.OrderRepository
import com.petpal.swimmer_seller.data.repository.ProductRepository
import com.petpal.swimmer_seller.databinding.FragmentHomeBinding
import com.petpal.swimmer_seller.ui.user.UserViewModel
import com.petpal.swimmer_seller.ui.user.UserViewModelFactory

class HomeFragment : Fragment() {
    lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var mainActivity: MainActivity

    private lateinit var userViewModel: UserViewModel
    lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        // ViewModel 초기화
        val homeViewModelFactory = HomeViewModelFactory(ProductRepository(), OrderRepository())
        homeViewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
        userViewModel = ViewModelProvider(this, UserViewModelFactory())[UserViewModel::class.java]

        // LiveData 관찰
        homeViewModel.run {
            paymentCount.observe(viewLifecycleOwner){
                fragmentHomeBinding.textViewPaymentCount.text = it.toString()
            }
            readyCount.observe(viewLifecycleOwner){
                fragmentHomeBinding.textViewReadyCount.text = it.toString()
            }
            processCount.observe(viewLifecycleOwner){
                fragmentHomeBinding.textViewProcessCount.text = it.toString()
            }
            completeCount.observe(viewLifecycleOwner){
                fragmentHomeBinding.textViewCompleteCount.text = it.toString()
            }
            cancelCount.observe(viewLifecycleOwner){
                fragmentHomeBinding.textViewCancelCount.text = "${it}건"
            }
            exchangeCount.observe(viewLifecycleOwner){
                fragmentHomeBinding.textViewExchangeCount.text = "${it}건"
            }
            refundCount.observe(viewLifecycleOwner){
                fragmentHomeBinding.textViewRefundCount.text = "${it}건"
            }

            productCount.observe(viewLifecycleOwner){
                fragmentHomeBinding.textViewProductCount.text = "등록된 상품이 ${it}건 있습니다."
            }
        }
        
        fragmentHomeBinding.run {
            toolbarHome.run {
                // 툴바 타이틀 폰트 설정
                setTitleTextAppearance(mainActivity, R.style.HsbombaramTextAppearance)
            }

            buttonRegProduct.setOnClickListener {
                // 상품 등록 화면으로 이동
                it.findNavController().navigate(R.id.action_item_home_to_item_product_add)
            }

            linearGuide.setOnClickListener {
                // 판매자 가이드 화면으로 이동
                findNavController().navigate(R.id.action_item_home_to_guideFragment)
            }
            // 홈 화면에 로그아웃 버튼 위젯이 안보여서 일단 주석처리 해둘게요
//            buttonLogout.setOnClickListener {
//                userViewModel.logOut()
//                //메인 프래그먼트는 제거하고 로그인 프래그먼트로 이동
//                findNavController().popBackStack(R.id.mainFragment, true)
//                findNavController().navigate(R.id.loginFragment)
//            }
        }

        // 로그인 판매자가 관련된 주문들 주문상태별로 개수 표시
        homeViewModel.getOrderCountByState(mainActivity.loginSellerUid)
        
        // 로그인 판매자가 등록한 상품 개수 표시
        homeViewModel.getProductCount(mainActivity.loginSellerUid)

        return fragmentHomeBinding.root
    }
}

