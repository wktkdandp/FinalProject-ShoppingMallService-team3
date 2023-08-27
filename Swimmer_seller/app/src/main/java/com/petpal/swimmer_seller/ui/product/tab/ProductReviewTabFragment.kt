package com.petpal.swimmer_seller.ui.product.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.petpal.swimmer_seller.R
import com.petpal.swimmer_seller.data.model.Product
import com.petpal.swimmer_seller.databinding.FragmentProductReviewTabBinding
import com.petpal.swimmer_seller.ui.product.ProductViewModel
import com.petpal.swimmer_seller.ui.product.ProductViewModelFactory

class ProductReviewTabFragment(product: Product) : Fragment() {
    private lateinit var productViewModel: ProductViewModel

    private var _fragmentProductReviewTabBinding: FragmentProductReviewTabBinding? = null
    private val fragmentProductReviewTabBinding get() = _fragmentProductReviewTabBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentProductReviewTabBinding = FragmentProductReviewTabBinding.inflate(inflater)
        productViewModel = ViewModelProvider(this, ProductViewModelFactory())[ProductViewModel::class.java]
        return fragmentProductReviewTabBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO 상품에 등록된 리뷰 정보 가져오기


    }

}