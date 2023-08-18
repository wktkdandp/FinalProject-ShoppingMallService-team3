package com.petpal.swimmer_customer.ui.product

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.petpal.swimmer_customer.ui.product.tablayout.ProductDetailTabFragment
import com.petpal.swimmer_customer.ui.product.tablayout.ProductQnATabFragment
import com.petpal.swimmer_customer.ui.product.tablayout.ProductReviewTabFragment

private const val NUM_PAGES = 3

class ProductDetailTabLayoutAdapter(fragment: Fragment, val idx: Int) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProductDetailTabFragment(idx)
            1 -> ProductReviewTabFragment()
            2 -> ProductQnATabFragment()
            else -> ProductDetailTabFragment(idx)
        }
    }
}