package com.petpal.swimmer_customer.ui.product.tablayout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.petpal.swimmer_customer.databinding.FragmentProductDetailTabBinding
import com.petpal.swimmer_customer.ui.home.HomeFragmentViewModel
import com.petpal.swimmer_customer.ui.product.ProductViewModel
import kotlinx.coroutines.launch

class ProductDetailTabFragment(var idx: Int) : Fragment() {

    private lateinit var fragmentProductDetailTabBinding: FragmentProductDetailTabBinding
    private lateinit var viewModel: ProductViewModel
    private lateinit var homeViewModel: HomeFragmentViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentProductDetailTabBinding = FragmentProductDetailTabBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        homeViewModel = ViewModelProvider(this)[HomeFragmentViewModel::class.java]
        fragmentProductDetailTabBinding.vm = viewModel
        fragmentProductDetailTabBinding.lifecycleOwner = this
        lifecycleScope.launch {
            homeViewModel.fetchData()
        }
        homeViewModel.productListLiveData.observe(viewLifecycleOwner) { productList ->
            viewModel.productDetailImageUri(fragmentProductDetailTabBinding.productDetailImageView,productList[idx].descriptionImage)
        }

        return fragmentProductDetailTabBinding.root

    }
}