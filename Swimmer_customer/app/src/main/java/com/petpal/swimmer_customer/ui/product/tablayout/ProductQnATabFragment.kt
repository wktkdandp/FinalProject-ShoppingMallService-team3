package com.petpal.swimmer_customer.ui.product.tablayout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.databinding.FragmentProductQnATabBinding

class ProductQnATabFragment : Fragment() {
    lateinit var productQnATabBinding: FragmentProductQnATabBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        productQnATabBinding = FragmentProductQnATabBinding.inflate(inflater)
        productQnATabBinding.qnaAnimationView.playAnimation()
        return productQnATabBinding.root

    }

}