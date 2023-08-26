package com.petpal.swimmer_seller.ui.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.petpal.swimmer_seller.R

class ProductDetailFragment : Fragment() {
    lateinit var currentProductUid : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // 상품 목록에서 전달받은 uid
        val args = ProductDetailFragmentArgs.fromBundle(requireArguments())
        currentProductUid = args.productUid

        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}