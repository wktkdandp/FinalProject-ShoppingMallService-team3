package com.petpal.swimmer_customer.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.data.model.Product
import com.petpal.swimmer_customer.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {

    lateinit var fragmentHomeFragmentBinding: FragmentHomeBinding
    private lateinit var viewPagerAdapter: BannerViewPager2Adapter
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var homeMainAdapter: HomeFragmentAdapter
    private val autoScrollHandler = Handler()
    private var autoScrollRunnable: Runnable? = null
    private val autoScrollInterval: Long = 4000
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentHomeFragmentBinding = FragmentHomeBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[HomeFragmentViewModel::class.java]

        lifecycleScope.launch {
            viewModel.fetchData()
        }
        viewModel.productListLiveData.observe(viewLifecycleOwner) { productList ->
            homeMainAdapter = HomeFragmentAdapter(requireContext(), productList)
            fragmentHomeFragmentBinding.homeMainRv.setAdapter(homeMainAdapter)
        }

        fragmentHomeFragmentBinding.run {
            fetchDataFromFirebase()
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
            homeMainAdapter = HomeFragmentAdapter(requireContext(), emptyList())
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
        fragmentHomeFragmentBinding.veilLayout.veil()
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            fragmentHomeFragmentBinding.veilLayout.unVeil()
        }, 2000)

        fragmentHomeFragmentBinding.itemDetailViewPager2.apply {
            viewPagerAdapter = BannerViewPager2Adapter(requireContext(), emptyList())
            adapter = viewPagerAdapter

            viewModel.homeFragmentItemList.observe(viewLifecycleOwner) {
                viewPagerAdapter.setItems(it)
            }


            fragmentHomeFragmentBinding.dotsIndicator.attachTo(this)
            offscreenPageLimit = 5// 이미지를 미리 로딩하는 메서드

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
        viewModel.homeFragmentItemList.observe(viewLifecycleOwner) { bannerItemList ->
            viewPagerAdapter.submitList(bannerItemList)
        }
    }

    private fun fetchDataFromFirebase() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val data = viewModel.productListLiveData.value
                if (data != null) {
                    updateUIWithData(data)
                }
                delay(2000) // 1.5초 대기
                withContext(Dispatchers.Main) {
                    fragmentHomeFragmentBinding.homeMainRv.unVeil()
                }
            } catch (_: Exception) {

            }
        }
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

    override fun onDestroyView() {
        // 메모리 누수 방지를 위해..
        autoScrollHandler.removeCallbacks(autoScrollRunnable!!)
        super.onDestroyView()
    }
}