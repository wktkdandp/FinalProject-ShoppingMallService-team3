package com.petpal.swimmer_customer.ui

import android.os.Bundle
import android.os.Handler
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
import com.petpal.swimmer_customer.data.model.Product
import com.petpal.swimmer_customer.data.model.ProductDetailModel
import com.petpal.swimmer_customer.data.model.productList
import com.petpal.swimmer_customer.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlin.math.max


class HomeFragment : Fragment() {

    lateinit var fragmentHomeFragmentBinding: FragmentHomeBinding
    private lateinit var viewPagerAdapter: BannerViewPager2Adapter
    private lateinit var viewModel: HomeFragmentViewModel
    var homeFragmentItemList: MutableList<ProductDetailModel> = mutableListOf()

    private val MIN_SCALE = 0.85f // 뷰가 몇퍼센트로 줄어들 것인지
    private val MIN_ALPHA = 0.5f // 어두워지는 정도

    private val autoScrollHandler = Handler()
    private var autoScrollRunnable: Runnable? = null
    private val autoScrollInterval: Long = 3000 // 3초
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentHomeFragmentBinding = FragmentHomeBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[HomeFragmentViewModel::class.java]



        for (i in 1..5) {
            homeFragmentItemList.add(ProductDetailModel("bannerImage$i.jpg"))
        }

        Log.d("쵸파", homeFragmentItemList.toString())
        viewModel.setProductDetail(homeFragmentItemList)

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
            viewPagerAdapter = BannerViewPager2Adapter(requireContext(), homeFragmentItemList)
            adapter = viewPagerAdapter
            setPageTransformer(ZoomOutPageTransformer())
            fragmentHomeFragmentBinding.dotsIndicator.attachTo(this)

            // 페이지 변경 콜백 등록
            // registerOnPageChangeCallback :  ViewPager2의 페이지 변경 사항을 감지 메서드
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {

                    // 페이지가 변경되면 이전의 autoScrollRunnable을 제거
                    autoScrollHandler.removeCallbacks(autoScrollRunnable!!)

                    val lastIndex = adapter?.itemCount?.minus(1)
                    if (position == lastIndex) {
                        // 마지막 페이지면 3초 후에 첫 번째 페이지로 이동
                        autoScrollRunnable = Runnable {
                            currentItem = 0
                        }
                        // 지정된 시간( autoScrollInterval(3초) )이  지난 후 currentItem 이동
                        autoScrollHandler.postDelayed(autoScrollRunnable!!, autoScrollInterval)
                    } else {
                        // 마지막 페이지가 아니면 다음 페이지로 자동 이동
                        autoScrollRunnable = Runnable {
                            currentItem += 1
                        }
                        autoScrollHandler.postDelayed(autoScrollRunnable!!, autoScrollInterval)
                    }
                }
            })

            // 초기 페이지 변경 로직 실행
            autoScrollRunnable = Runnable {
                currentItem += 1
            }
            autoScrollHandler.postDelayed(autoScrollRunnable!!, autoScrollInterval)
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
        val colorList: MutableList<String> = mutableListOf()
        val sizeList: MutableList<String> = mutableListOf()

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
            val brandName = a1.child("brandName").value
            val color = a1.child("colorList").value
            val size = a1.child("sizeList").value


            hashTagList.add(hashTag.toString())
            mainImageList.add(mainImage.toString())
            colorList.add(color.toString())
            sizeList.add(size.toString())

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
                    brandName.toString(),
                    colorList,
                    sizeList
                )
            )
            Log.d("루피", productList.toString())
        }

        return productList
    }

    private fun updateUIWithData(productList2: List<Product>) {

        fragmentHomeFragmentBinding.homeMainRv.run {

            val homeMainAdapter = HomeFragmentAdapter(
                requireContext(),
                productList2
            )
            fragmentHomeFragmentBinding.homeMainRv.setAdapter(homeMainAdapter)
            fragmentHomeFragmentBinding.homeMainRv.setLayoutManager(
                GridLayoutManager(
                    requireContext(),
                    3
                )
            )

        }
    }

    inner class ZoomOutPageTransformer : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }

                    position <= 1 -> { // [-1,1]
                        // Modify the default slide transition to shrink the page as well
                        val scaleFactor = max(MIN_SCALE, 1 - abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        // Scale the page down (between MIN_SCALE and 1)
                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        // Fade the page relative to its size.
                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }

                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        // 메모리 누수 방지를 위해..
        autoScrollHandler.removeCallbacks(autoScrollRunnable!!)
        super.onDestroyView()
    }

}