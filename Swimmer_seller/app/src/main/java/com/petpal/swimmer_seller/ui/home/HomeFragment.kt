package com.petpal.swimmer_seller.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.petpal.swimmer_seller.MainActivity
import com.petpal.swimmer_seller.R
import com.petpal.swimmer_seller.data.model.Order
import com.petpal.swimmer_seller.data.repository.OrderRepository
import com.petpal.swimmer_seller.data.repository.ProductRepository
import com.petpal.swimmer_seller.databinding.FragmentHomeBinding
import com.petpal.swimmer_seller.ui.order.OrderState
import com.petpal.swimmer_seller.ui.order.OrderViewModel
import com.petpal.swimmer_seller.ui.order.OrderViewModelFactory
import com.petpal.swimmer_seller.ui.product.ProductViewModel
import com.petpal.swimmer_seller.ui.product.ProductViewModelFactory
import com.petpal.swimmer_seller.ui.user.UserViewModel
import com.petpal.swimmer_seller.ui.user.UserViewModelFactory

class HomeFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var productViewModel: ProductViewModel
    private lateinit var orderViewModel: OrderViewModel

    private lateinit var _fragmentHomeBinding: FragmentHomeBinding
    private val fragmentHomeBinding get() = _fragmentHomeBinding

    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentHomeBinding = FragmentHomeBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        // ViewModel
        productViewModel = ViewModelProvider(this, ProductViewModelFactory())[ProductViewModel::class.java]
        userViewModel = ViewModelProvider(this, UserViewModelFactory())[UserViewModel::class.java]
        orderViewModel = ViewModelProvider(this, OrderViewModelFactory())[OrderViewModel::class.java]

        // Observer
        productViewModel.run {
            productCount.observe(viewLifecycleOwner){
                fragmentHomeBinding.textViewProductCount.text = "등록된 상품이 ${it}건 있습니다."
            }
        }

        orderViewModel.run {
            orderList.observe(viewLifecycleOwner){
                fragmentHomeBinding.run {
                    // 주문 상태 건 수 표시
                    textViewPaymentCount.text = it.filter {
                        it.state == OrderState.PAYMENT.code
                    }.size.toString()

                    textViewReadyCount.text = it.filter {
                        it.state == OrderState.READY.code
                    }.size.toString()

                    textViewProcessCount.text = it.filter {
                        it.state == OrderState.PROCESS.code
                    }.size.toString()

                    textViewCompleteCount.text = it.filter {
                        it.state == OrderState.COMPLETE.code
                    }.size.toString()

                    textViewExchangeCount.text = it.filter {
                        it.state == OrderState.EXCHANGE.code
                    }.size.toString()

                    textViewCancelCount.text = it.filter {
                        it.state == OrderState.CANCEL.code
                    }.size.toString()

                    textViewRefundCount.text = it.filter {
                        it.state == OrderState.REFUND.code
                    }.size.toString()
                }
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

                findNavController().navigate(R.id.action_item_home_to_item_guide)
            }
        }

        // 로그인 판매자가 관련된 주문들 건 수 표시
        orderViewModel.getOrderBySellerUid(Firebase.auth.currentUser!!.uid)
        
        // 로그인 판매자가 등록한 상품 개수 표시
        productViewModel.getProductCount(Firebase.auth.currentUser!!.uid)

        return fragmentHomeBinding.root
    }
}

