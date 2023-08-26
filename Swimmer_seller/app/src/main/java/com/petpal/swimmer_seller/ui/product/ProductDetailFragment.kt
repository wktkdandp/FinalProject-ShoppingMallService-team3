package com.petpal.swimmer_seller.ui.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.tabs.TabLayout
import com.petpal.swimmer_seller.R
import com.petpal.swimmer_seller.databinding.FragmentProductDetailBinding
import com.petpal.swimmer_seller.databinding.ItemImageSliderBinding
import java.text.NumberFormat
import java.util.Locale

class ProductDetailFragment : Fragment() {
    private lateinit var productViewModel: ProductViewModel
    
    private var _fragmentProductDetailBinding: FragmentProductDetailBinding? = null
    private val fragmentProductDetailBinding get() = _fragmentProductDetailBinding!!

    lateinit var productUid : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentProductDetailBinding = FragmentProductDetailBinding.inflate(inflater)

        val args = ProductDetailFragmentArgs.fromBundle(requireArguments())
        productUid = args.productUid

        productViewModel = ViewModelProvider(this, ProductViewModelFactory())[ProductViewModel::class.java]
        // 상품 목록에서 전달받은 uid
        productViewModel.getProduct(productUid)

        return fragmentProductDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentProductDetailBinding.run {
            // Toolbar 백버튼
            toolbarProductDetail.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            // TabLayout 세팅
            tabLayout.run {
                val tabItemProductInfo: TabLayout.Tab = tabLayout.newTab()
                tabItemProductInfo.text = "상품상세"
                addTab(tabItemProductInfo)

                val tabItemProductReview: TabLayout.Tab = tabLayout.newTab()
                tabItemProductReview.text = "상품리뷰"
                addTab(tabItemProductReview)

                val tabItemProductQnA: TabLayout.Tab = tabLayout.newTab()
                tabItemProductQnA.text = "상품QnA"
                addTab(tabItemProductQnA)

                addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
                    override fun onTabSelected(tab: TabLayout.Tab?) {

                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {

                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {

                    }
                })
            }

            // 메인 이미지 ViewPager
            viewPagerMainImage.run {
                adapter = ImageSliderAdapter()
                dotsIndicator.setViewPager2(viewPagerMainImage)
                viewPagerMainImage.adapter?.notifyDataSetChanged()
            }

            fabProductDetail.setOnClickListener {
                // TODO 화면 최상단으로 올리기
            }
        }

        // Observer
        productViewModel.run {
            product.observe(viewLifecycleOwner){
                fragmentProductDetailBinding.run {
                    if (it?.category != null) {
                        // 메인 이미지
                        viewPagerMainImage.adapter?.notifyDataSetChanged()

                        // 텍스트 정보
                        val category = it.category!!
                        textViewImageCount.text = " / ${it.mainImage?.size}"
                        textViewImageIdx.text = "1"
                        textViewProductCategory.text = listOfNotNull(category.main, category.mid, category.sub).joinToString(" > ")
                        textViewProductBarndName.text = it.brandName
                        textViewProductName.text = it.name
                        textViewProductPrice.text = "${NumberFormat.getNumberInstance(Locale.getDefault()).format(it.price)}원"
                        // 해시태그 추가
                        addHashTag(it.hashTag!!, chipGroupProductHashTag)
                    }
                }
            }
        }



    }

    private fun addHashTag(hashTagList: List<String>, chipGroup: ChipGroup){
        for(hashTag in hashTagList) {
            val chip = layoutInflater.inflate(R.layout.item_chip, chipGroup, false) as Chip
            chip.text = hashTag
            chipGroup.addView(chip)
        }
    }

    // 이미지의 Storage URL -> ImageView에 표시
    inner class ImageSliderAdapter(): RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>() {
        inner class ImageSliderViewHolder(binding: ItemImageSliderBinding): RecyclerView.ViewHolder(binding.root){
            val imageView: ImageView = binding.imageViewSlider
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
            val binding = ItemImageSliderBinding.inflate(layoutInflater)
            binding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            return ImageSliderViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return productViewModel.product.value?.mainImage!!.size
        }

        override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
            // 메인 이미지 다운 받아서 표시
            val imageStoragePath = productViewModel.product.value?.mainImage!![position]
            productViewModel.loadAndDisplayImage(imageStoragePath, holder.imageView)
        }
    }
}

