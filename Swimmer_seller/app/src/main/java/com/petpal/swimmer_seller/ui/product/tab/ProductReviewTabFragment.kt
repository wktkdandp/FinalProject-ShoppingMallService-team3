package com.petpal.swimmer_seller.ui.product.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.petpal.swimmer_seller.R
import com.petpal.swimmer_seller.data.model.Product
import com.petpal.swimmer_seller.databinding.FragmentProductReviewTabBinding
import com.petpal.swimmer_seller.ui.product.ProductViewModel
import com.petpal.swimmer_seller.ui.product.ProductViewModelFactory
import com.petpal.swimmer_seller.data.model.Review
import com.petpal.swimmer_seller.databinding.FragmentProductReviewTabItemBinding

class ProductReviewTabFragment(product: Product) : Fragment() {
    private lateinit var productViewModel: ProductViewModel

    private var _fragmentProductReviewTabBinding: FragmentProductReviewTabBinding? = null
    private val fragmentProductReviewTabBinding get() = _fragmentProductReviewTabBinding!!

    val reviewList = mutableListOf<Review>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentProductReviewTabBinding = FragmentProductReviewTabBinding.inflate(inflater)
        productViewModel = ViewModelProvider(this, ProductViewModelFactory())[ProductViewModel::class.java]
        return fragmentProductReviewTabBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSampleData()

        // TODO 상품에 등록된 리뷰 정보 가져오기

        // 클릭한 Chip 버튼에 따라 BottomSheetDialog 표시
        fragmentProductReviewTabBinding.run {
            textViewReviewCount.text = "(${reviewList.size})"

            chipStarRating.setOnClickListener {
                showBottomSheet(R.layout.fragment_product_review_tab_bottom_sheet_star)
            }
            chipHeight.setOnClickListener {
                showBottomSheet(R.layout.fragment_product_review_tab_bottom_sheet_height)
            }
            chipWeight.setOnClickListener {
                showBottomSheet(R.layout.fragment_product_review_tab_bottom_sheet_weight)
            }
            chipDefaultSize.setOnClickListener {
                showBottomSheet(R.layout.fragment_product_review_tab_bottom_sheet_size)
            }

            recyclerViewReview.run {
                adapter = ReviewRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(
                    MaterialDividerItemDecoration(
                        requireContext(),
                        MaterialDividerItemDecoration.VERTICAL
                    )
                )
            }
        }
    }

    private fun setSampleData(){
        // TODO 리뷰 기능 구현시 작성자 이름 대신 cutomerUid 값으로 데이터 읽기
        reviewList.add(Review(
            "1",
            "사자",
            180L,
            90L,
            4.0,
            "2023.08.25",
            "몸에 딱 맞아요. 잘 산 것 같습니다!",
            "image/review_sample_image.jpg",
            "XL",
            "블랙"
        ))
        reviewList.add(Review(
            "2",
            "이멋사",
            163L,
            60L,
            3.5,
            "2023.08.25",
            "품질이 최고입니다ㅎㅎ",
            "image/review_sample_image2.jpg",
            "M",
            "그린"
        ))
    }

    // 리뷰 필터링 BottomSheet 표시
    private fun showBottomSheet(layout: Int) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(layout)
        bottomSheetDialog.show()
    }

    inner class ReviewRecyclerViewAdapter: RecyclerView.Adapter<ReviewRecyclerViewAdapter.ReviewViewHolder>() {
        inner class ReviewViewHolder(private val binding: FragmentProductReviewTabItemBinding): RecyclerView.ViewHolder(binding.root) {
            fun bind(review: Review){
                // TODO UserViewModel 사용해서 리뷰 작성자 uid로 고객 정보 얻기
                binding.textViewReviewNickname.text = review.customerUid
                binding.textViewReviewDate.text = review.date
                binding.ratingBarReview.rating = review.rating.toFloat()
                productViewModel.loadAndDisplayImage(review.Image!!, binding.imageViewReview)
                binding.textViewReviewInfo.text = "${review.height}cm · ${review.weight}kg"
                binding.textViewReviewOption.text = "${review.color} [${review.size}]"
                binding.textViewReviewContent.text = review.content
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
            val binding = FragmentProductReviewTabItemBinding.inflate(layoutInflater)
            binding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            return ReviewViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return reviewList.size
        }

        override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
            val review = reviewList[position]
            holder.bind(review)
        }
    }
}