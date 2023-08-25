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
import com.google.firebase.database.FirebaseDatabase
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.data.model.Category
import com.petpal.swimmer_customer.data.model.HomeFragmentItemList
import com.petpal.swimmer_customer.data.model.Product
import com.petpal.swimmer_customer.data.model.productList
import com.petpal.swimmer_customer.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.petpal.swimmer_customer.data.model.ProductDetailModel
import com.petpal.swimmer_customer.ui.product.ProductDetailAdapter
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.petpal.swimmer_customer.ui.main.MainFragmentDirections


class HomeFragment : Fragment() {

    lateinit var fragmentHomeFragmentBinding: FragmentHomeBinding
    private var categoryList: MutableList<Category> = mutableListOf()
    private lateinit var viewPagerAdapter: BannerViewPager2Adapter
    private lateinit var viewModel: HomeFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentHomeFragmentBinding = FragmentHomeBinding.inflate(inflater)
        Log.d("uid",FirebaseAuth.getInstance().currentUser?.uid!!)
        viewModel = ViewModelProvider(this)[HomeFragmentViewModel::class.java]
        viewModel.setProductDetail(HomeFragmentItemList)

        firebase()


        fragmentHomeFragmentBinding.run {
            rankingTextView.setOnClickListener {
                Log.d("확인용", productList.size.toString())

            }
            Log.d("확인용", productList.size.toString())
            fetchDataFromFirebase()
            tablayout()
            recyclerView()
            toolbar()
            initViewPager2()
            viewPage2Observe()


        }



        return fragmentHomeFragmentBinding.root
    }

    private fun firebase() {


    }

    private fun FragmentHomeBinding.recyclerView() {
        homeMainRv.run {


            val homeMainAdapter = HomeFragmentAdapter(requireContext(), productList)
            homeMainRv.setAdapter(homeMainAdapter)
            homeMainRv.setLayoutManager(GridLayoutManager(requireContext(), 3))
            homeMainRv.addVeiledItems(7)
            homeMainRv.veil()

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

            viewPagerAdapter = BannerViewPager2Adapter()
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

    private fun fetchDataFromFirebase() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val data = withContext(Dispatchers.IO) {
                    fetchProductDataFromFirebase()
                }
                updateUIWithData(data)
                fragmentHomeFragmentBinding.homeMainRv.unVeil()
            } catch (e: Exception) {

            }
        }
    }

    private suspend fun fetchProductDataFromFirebase(): List<Product> {
        productList.clear()
        val hashTagList: MutableList<String> = mutableListOf()
        val mainImageList: MutableList<String> = mutableListOf()
        // firebase 객체를 생성한다.
        val database = FirebaseDatabase.getInstance()
        // TestData에 접근한다.
        val testDataRef = database.getReference("products")

        // 전부를 가져온다.
        val dataSnapshot = testDataRef.get().await()

        // 가져온 데이터의 수 만큼 반복한다.
        for (a1 in dataSnapshot.children) {
            // 데이터를 가져온다.
            val categoryMain = a1.child("category").child("main").value
            val categoryMid = a1.child("category").child("mid").value
            val categorySub = a1.child("category").child("sub").value
            val code = a1.child("code").value
            val description = a1.child("description").value
            val descriptionImage = a1.child("descriptionImage").value
            val hashTag = a1.child("hashTag").value
            val mainImage = a1.child("mainImage").value
            val name = a1.child("name").value
            val orderCounter = a1.child("orderCount").value
            val price = a1.child("price").value
            val productUid = a1.child("productUid").value
            val regDate = a1.child("regDate").value
            val sellerUid = a1.child("sellerUid").value
            val infoImage = a1.child("infoImage").value


            hashTagList.add(hashTag.toString())
            mainImageList.add(mainImage.toString())

            productList.add(
                Product(
                    Category(
                        categoryMain.toString(),
                        categoryMid.toString(),
                        categorySub.toString()
                    ),
                    code.toString(),
                    description.toString(),
                    descriptionImage.toString(),
                    hashTagList,
                    mainImageList,
                    name.toString(),
                    orderCounter.toString().toInt(),
                    price.toString().toInt(),
                    productUid.toString(),
                    regDate.toString(),
                    sellerUid.toString(),
                    infoImage.toString()
                )
            )
        }

        return productList
    }

    private fun updateUIWithData(productList2: List<Product>) {

        fragmentHomeFragmentBinding.homeMainRv.run {

            val homeMainAdapter = HomeFragmentAdapter(
                requireContext(),
                productList2
            )
            fragmentHomeFragmentBinding.homeMainRv.setAdapter( homeMainAdapter)
            fragmentHomeFragmentBinding.homeMainRv.setLayoutManager(  GridLayoutManager(requireContext(), 3))

        }
    }

}