package com.petpal.swimmer_seller.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.petpal.swimmer_seller.MainActivity
import com.petpal.swimmer_seller.R
import com.petpal.swimmer_seller.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var fragmentHomeBinding: FragmentHomeBinding
    lateinit var mainActivity: MainActivity

    lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        homeViewModel = ViewModelProvider(mainActivity)[HomeViewModel::class.java]
        homeViewModel.run {
            productCount.observe(mainActivity){
                fragmentHomeBinding.textViewProductCount.text = "등록된 상품이 ${it}건 있습니다."
            }
        }
        
        fragmentHomeBinding.run {
            buttonRegProduct.setOnClickListener {
                // 상품 등록화면으로 이동
                it.findNavController().navigate(R.id.action_item_home_to_item_product_add)

            }
        }

        // 로그인한 판매자가 등록한 상품 개수 가져오기
         homeViewModel.getProductCount(mainActivity.loginSeller.sellerIdx)

        return fragmentHomeBinding.root
    }
}