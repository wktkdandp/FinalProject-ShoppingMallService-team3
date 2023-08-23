package com.petpal.swimmer_seller.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.petpal.swimmer_seller.MainActivity
import com.petpal.swimmer_seller.R
import com.petpal.swimmer_seller.data.repository.ProductRepository
import com.petpal.swimmer_seller.databinding.FragmentHomeBinding
import com.petpal.swimmer_seller.ui.product.ProductViewModel
import com.petpal.swimmer_seller.ui.product.ProductViewModelFactory
import com.petpal.swimmer_seller.ui.user.UserViewModel
import com.petpal.swimmer_seller.ui.user.UserViewModelFactory

class HomeFragment : Fragment() {
    lateinit var fragmentHomeBinding: FragmentHomeBinding
    lateinit var mainActivity: MainActivity

    private lateinit var userViewModel: UserViewModel
    lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        val factory = HomeViewModelFactory(ProductRepository())
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        userViewModel = ViewModelProvider(this, UserViewModelFactory())[UserViewModel::class.java]

        homeViewModel.run {
            productCount.observe(mainActivity){
                fragmentHomeBinding.textViewProductCount.text = "등록된 상품이 ${it}건 있습니다."
            }
        }
        
        fragmentHomeBinding.run {
            toolbarHome.run {
                // 툴바 타이틀 폰트 설정
                setTitleTextAppearance(mainActivity, R.style.HsbombaramTextAppearance)
            }

            buttonRegProduct.setOnClickListener {
                // 상품 등록화면으로 이동
                it.findNavController().navigate(R.id.action_item_home_to_item_product_add)
            }
        }

        // 로그인한 판매자가 등록한 상품 개수 가져오기
        homeViewModel.getProductCount(mainActivity.loginSellerUid)

        return fragmentHomeBinding.root
    }
}

// 뷰모델 팩토리
class HomeViewModelFactory(private val repository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}