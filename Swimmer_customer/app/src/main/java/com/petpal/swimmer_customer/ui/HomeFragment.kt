package com.petpal.swimmer_customer.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.databinding.FragmentHomeBinding
import com.petpal.swimmer_customer.data.model.Ranking
import com.petpal.swimmer_customer.data.model.exList
import com.petpal.swimmer_customer.data.model.HomeFragmentItemList
import com.petpal.swimmer_customer.data.model.ProductDetailModel
import com.petpal.swimmer_customer.ui.product.ProductDetailAdapter
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.petpal.swimmer_customer.ui.main.MainFragmentDirections


class HomeFragment : Fragment() {

    lateinit var fragmentHomeFragmentBinding: FragmentHomeBinding
    private lateinit var viewPagerAdapter: ProductDetailAdapter
    private lateinit var viewModel: HomeFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentHomeFragmentBinding = FragmentHomeBinding.inflate(inflater)
        Log.d("uid",FirebaseAuth.getInstance().currentUser?.uid!!)
        viewModel = ViewModelProvider(this)[HomeFragmentViewModel::class.java]
        viewModel.setProductDetail(HomeFragmentItemList)

        exList.clear()
        exList.add(
            Ranking(
                0,
                "https://image.ohou.se/i/bucketplace-v2-development/uploads/productions/168612566216377860.png?gif=1&w=640&h=640&c=c&webp=1",
                "에이프릴랩스",
                "마린웨이브 서핑복 루즈핏 래쉬가드 비치웨어 수영복 상하의 세트",
                "29,700원",
                "https://gi.esmplus.com/globalsb1/32.JPG",
                productDetailItemList = listOf(
                    ProductDetailModel("https://image.ohou.se/i/bucketplace-v2-development/uploads/productions/168612566216377860.png?gif=1&w=640&h=640&c=c&webp=1"),
                    ProductDetailModel("https://image.ohou.se/i/bucketplace-v2-development/uploads/productions/images/168612567371053790.jpg?gif=1&w=640&h=640&c=c&webp=1"),
                    ProductDetailModel("https://image.ohou.se/i/bucketplace-v2-development/uploads/productions/images/168612567915821325.jpg?gif=1&w=640&h=640&c=c&webp=1"),
                    ProductDetailModel("https://image.ohou.se/i/bucketplace-v2-development/uploads/productions/images/168612568461835366.jpg?gif=1&w=640&h=640&c=c&webp=1"),
                    ProductDetailModel("https://image.ohou.se/i/bucketplace-v2-development/uploads/productions/images/168612569355170147.jpg?gif=1&w=640&h=640&c=c&webp=1"),
                )
            )
        )
        exList.add(
            Ranking(
                1,
                "https://image.ohou.se/i/bucketplace-v2-development/uploads/openapi/20077881/1677747251369.jpg?gif=1&w=640&h=640&c=c&webp=1",
                "피닉스",
                "디자인 우레탄 아동 수영모 (상어고래)",
                "9,000원",
                "https://gi.esmplus.com/ph0137/2022/%EC%83%81%EC%84%B8+%EC%8D%B8%EB%84%A4%EC%9D%BC_%EB%8B%A8%ED%92%88/%EC%88%98%EC%98%81%EB%AA%A8/%EB%94%94%EC%9E%90%EC%9D%B8%20%EC%9A%B0%EB%A0%88%ED%83%84%20%EC%95%84%EB%8F%99%20%EC%88%98%EC%98%81%EB%AA%A8/%EC%83%81%EC%96%B4%EA%B3%A0%EB%9E%98/%EB%AC%B6%EC%9D%8C/KC%ED%8F%AC%ED%95%A8/%EC%9A%B0%EB%A0%88%ED%83%84%EC%88%98%EC%98%81%EB%AA%A8_%EC%83%81%EC%96%B4%EA%B3%A0%EB%9E%98__%EC%83%81%EC%84%B8_%EB%AC%B6%EC%9D%8C.jpg",
                productDetailItemList = listOf(
                    ProductDetailModel("https://image.ohou.se/i/bucketplace-v2-development/uploads/openapi/20077881/1677747251369.jpg?gif=1&w=640&h=640&c=c&webp=1"),
                    ProductDetailModel("https://image.ohou.se/i/bucketplace-v2-development/uploads/productions/images/167887008622878540.jpg?gif=1&w=640&h=640&c=c&webp=1"),
                    ProductDetailModel("https://image.ohou.se/i/bucketplace-v2-development/uploads/productions/images/167887008347346260.jpg?gif=1&w=640&h=640&c=c&webp=1")
                )
            )
        )
        exList.add(
            Ranking(
                2,
                "https://image.ohou.se/i/bucketplace-v2-development/uploads/productions/167757240686307346.jpg?gif=1&w=640&h=640&c=c&webp=1",
                "비상",
                "361도 타이탄 고글 와이드 뷰 수경 수영 물안경",
                "19,900원",
                "https://ai.esmplus.com/syj9867/%EC%98%A4%EB%8A%98%EC%9D%98%EC%A7%91/361%EB%8F%84/361%EB%8F%84%20%ED%83%80%EC%9D%B4%ED%83%84%20%EA%B3%A0%EA%B8%80%20%EC%88%98%EA%B2%BD/SLY216011.jpg",
                productDetailItemList = listOf(
                    ProductDetailModel("https://image.ohou.se/i/bucketplace-v2-development/uploads/productions/167757240686307346.jpg?gif=1&w=640&h=640&c=c"),
                    ProductDetailModel("https://thumbnail.10x10.co.kr/webimage/image/add1/530/A005302630_01.jpg?cmd=thumb&w=400&h=400&fit=true&ws=false")
                )
            )
        )
        exList.add(
            Ranking(
                3,
                "https://image.ohou.se/i/bucketplace-v2-development/uploads/openapi/19926434/1684368601871.jpg?gif=1&w=640&h=640&c=c&webp=1",
                "아블로스",
                "오리발 롱핀 스킨스쿠버 수영용품 블루",
                "17,900원",
                "https://exit.ohou.se/1de6ff45bac0e54f3e58b53cc7708e14fa9d1da9/www.ibag.nuri.cc/2016s/AB/AB0-PC01B.jpg",
                productDetailItemList = listOf(
                    ProductDetailModel("https://image.ohou.se/i/bucketplace-v2-development/uploads/openapi/19926434/1684368601871.jpg?gif=1&w=640&h=640&c=c&webp=1"),
                    ProductDetailModel("https://image.ohou.se/i/bucketplace-v2-development/uploads/cards/snapshots/169029156385377419.jpeg?w=720"),
                )
            )
        )
        exList.add(
            Ranking(
                4,
                "https://image.ohou.se/i/bucketplace-v2-development/uploads/productions/168197809231398031.jpg?gif=1&w=640&h=640&c=c&webp=1",
                "수영가방 수영장 가방 방수 베이직 가방",
                "메이리앤",
                "8,900원",
                "https://gi.esmplus.com/weekshop1/overseas/FASHION/CLOTHES/SBG/SBG190520/01.jpg",
                productDetailItemList = listOf(
                    ProductDetailModel("https://image.ohou.se/i/bucketplace-v2-development/uploads/productions/168197809231398031.jpg?gif=1&w=640&h=640&c=c&webp=1"),
                    ProductDetailModel("https://image.ohou.se/i/bucketplace-v2-development/uploads/productions/images/168197810608386760.jpg?gif=1&w=640&h=640&c=c&webp=1"),
                    ProductDetailModel("https://image.ohou.se/i/bucketplace-v2-development/uploads/productions/images/168197811009920295.jpg?gif=1&w=640&h=640&c=c&webp=1")
                )
            )
        )
        exList.add(
            Ranking(
                5,
                "https://image.ohou.se/i/bucketplace-v2-development/uploads/productions/167869362064906184.jpg?gif=1&w=640&h=640&c=c&webp=1",
                "메이리앤",
                "C20 엠팩클래식 카드포켓 스마트폰 방수팩",
                "11,000원",
                "https://gi.esmplus.com/sea2711/detail/C20_Classic/new_c20/NEW_C20_01.jpg",
                productDetailItemList = listOf(
                    ProductDetailModel("https://image.ohou.se/i/bucketplace-v2-development/uploads/productions/167869362064906184.jpg?gif=1&w=640&h=640&c=c&webp=1"),
                    ProductDetailModel("https://image.ohou.se/i/bucketplace-v2-development/uploads/productions/images/167869364267653217.jpg?gif=1&w=640&h=640&c=c&webp=1"),
                    ProductDetailModel("https://image.ohou.se/i/bucketplace-v2-development/uploads/productions/images/167704520649040686.jpg?gif=1&w=640&h=640&c=c&webp=1"),
                    ProductDetailModel("https://image.ohou.se/i/bucketplace-v2-development/uploads/productions/images/167704520807611831.jpg?gif=1&w=640&h=640&c=c&webp=1")
                )
            )
        )

        fragmentHomeFragmentBinding.run {
            tablayout()
            recyclerView()
            toolbar()
            initViewPager2()
            viewPage2Observe()
        }



        return fragmentHomeFragmentBinding.root
    }

    private fun FragmentHomeBinding.recyclerView() {
        homeMainRv.run {
            val homeMainAdapter = HomeFragmentAdapter(requireContext(), exList)
            homeMainRv.adapter = homeMainAdapter
            homeMainRv.layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    private fun FragmentHomeBinding.tablayout() {
        tabLayout.tabRippleColor = null
        tabLayout.addTab(tabLayout.newTab().setText("랭킹"))
        tabLayout.addTab(tabLayout.newTab().setText("초심자"))
        tabLayout.addTab(tabLayout.newTab().setText("신상품"))
        tabLayout.addTab(tabLayout.newTab().setText("이벤트"))

        // 스크롤 할때 마다 탭이 이동하는 코드
//        nestedScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//            val scrollProgress = scrollY.toFloat() / v.height.toFloat()
//
//            if (scrollProgress >= 1f) {
//                tabLayout.getTabAt(1)?.select() // Select Tab 2
//            } else {
//                tabLayout.getTabAt(0)?.select() // Select Tab 1
//            }
//
//            motionLayout.progress = scrollProgress
//
//        }
    }


    private fun FragmentHomeBinding.toolbar() {
        toolbarHomeMain.run {
            title = "SWIMMER"
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
        fragmentHomeFragmentBinding.itemDetailViewPager2.apply {
            viewPagerAdapter = ProductDetailAdapter()
            adapter = viewPagerAdapter
            fragmentHomeFragmentBinding.dotsIndicator.attachTo(this)

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            })
        }

    }

    private fun viewPage2Observe() {
        viewModel.productDetailList.observe(viewLifecycleOwner) { bannerItemList ->
            viewPagerAdapter.submitList(bannerItemList)
        }
    }

}