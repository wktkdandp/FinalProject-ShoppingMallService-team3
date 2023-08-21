package com.petpal.swimmer_customer.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.databinding.FragmentProductDetailBinding
import com.petpal.swimmer_customer.data.model.exList


class ProductDetailFragment : Fragment() {

    lateinit var fragmentProductDetailBinding: FragmentProductDetailBinding
    private var isFavorite = false
    private lateinit var viewModel: ProductViewModel
    private lateinit var viewPagerAdapter: ProductDetailAdapter

    // 네비게이션 args 값 가져오기
    val args: ProductDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentProductDetailBinding = FragmentProductDetailBinding.inflate(inflater)

        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]

        viewModel.setProductDetailRanking(exList)

        fragmentProductDetailBinding.run {
            productDetailtoolbar()
            favorite()
            initViewPager2()
            observer()
            paymentButton()
            productDetailTabLayoutViewPage2()
            productDetailViewPager2.isUserInputEnabled = false

        }

        return fragmentProductDetailBinding.root
    }

    private fun FragmentProductDetailBinding.paymentButton() {
        paymentButton.setOnClickListener {
            Navigation.findNavController(fragmentProductDetailBinding.root)
                .navigate(R.id.action_itemDetailFragment_to_paymentFragment)
        }
    }

    private fun FragmentProductDetailBinding.favorite() {
        favoriteButton.setOnClickListener {
            isFavorite = !isFavorite
            updateFavoriteButtonState()
        }
    }

    private fun FragmentProductDetailBinding.productDetailtoolbar() {
        toolbarProductDetail.run {
            setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            val navigationIcon = navigationIcon
            navigationIcon?.setTint(ContextCompat.getColor(context, R.color.black))
            setNavigationOnClickListener {
                Navigation.findNavController(fragmentProductDetailBinding.root)
                    .navigate(R.id.action_itemDetailFragment_to_item_home)
            }

            inflateMenu(R.menu.home_toolbar_menu)

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_search -> {

                    }

                    R.id.item_shopping_cart -> {

                    }

                }
                false
            }
        }
    }

    private fun initViewPager2() {
        fragmentProductDetailBinding.itemDetailViewPager2.apply {
            viewPagerAdapter = ProductDetailAdapter()
            adapter = viewPagerAdapter
            fragmentProductDetailBinding.dotsIndicator.attachTo(this)

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            })
        }

    }

    private fun observer() {

        viewModel.productDetail.observe(viewLifecycleOwner) {
            fragmentProductDetailBinding.brandTextView.text = it[args.idx].brand
            fragmentProductDetailBinding.itemTitleTextView.text = it[args.idx].title
            fragmentProductDetailBinding.priceTextView.text = it[args.idx].price
            viewPagerAdapter.submitList(it[args.idx].productDetailItemList)
        }
    }

    private fun updateFavoriteButtonState() {
        val drawableRes = if (isFavorite) R.drawable.full_favorite_24 else R.drawable.favorite_24px
        fragmentProductDetailBinding.favoriteButton.setImageResource(drawableRes)
    }

    private fun productDetailTabLayoutViewPage2() {
        fragmentProductDetailBinding.productDetailTabLayout.tabRippleColor = null
        val adapter = ProductDetailTabLayoutAdapter(this,args.idx)
        fragmentProductDetailBinding.productDetailViewPager2.adapter = adapter

        val tabName = arrayOf("상품상세", "상품후기 (0)", "상품 Q&A")


        TabLayoutMediator(fragmentProductDetailBinding.productDetailTabLayout, fragmentProductDetailBinding.productDetailViewPager2) { tab, position ->
            tab.text = tabName[position]
        }.attach()



        fragmentProductDetailBinding.productDetailTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                fragmentProductDetailBinding.productDetailViewPager2.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
}