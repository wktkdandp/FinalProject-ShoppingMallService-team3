package com.petpal.swimmer_customer.ui.product.tablayout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.data.model.Review
import com.petpal.swimmer_customer.data.model.exReviewList
import com.petpal.swimmer_customer.databinding.FragmentProductReviewTabBinding


class ProductReviewTabFragment : Fragment() {

    private lateinit var fragmentProductReviewTabBinding: FragmentProductReviewTabBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentProductReviewTabBinding = FragmentProductReviewTabBinding.inflate(inflater)
        fragmentProductReviewTabBinding.run {
            recycerlviewExData()
            bottomSheet()
            recyclerView()
        }

        return fragmentProductReviewTabBinding.root
    }

    private fun recycerlviewExData() {
        exReviewList.clear()
        exReviewList.add(
            Review(
                0,
                0,
                170,
                60,
                3.5,
                "23.08.22",
                "신축성이 좋아요",
                "https://image.ohou.se/i/bucketplace-v2-development/uploads/productions/images/168914930378782519.jpg?gif=1&w=640&h=640&c=c&webp=1",
                95,
                "Black"
            )
        )

        exReviewList.add(
            Review(
                1,
                1,
                150,
                60,
                5.0,
                "23.08.22",
                "우선 배송부분 에서 약속잡기부터 매우친절했고 설치도 완벽하게 빠르게 해주시고 가셨어요.친절한설명도 있었고요. 제품성능에서는 제가 컴퓨터 모니터로 사용하고OT서비스 이용하려고 구입했는데 화질 음량 모두 매우만족입니다. 좋은시기에 저렴하게 잘산것 같아요",
                "",
                95,
                "Black"
            )
        )

        exReviewList.add(
            Review(
                2,
                2,
                180,
                70,
                5.0,
                "23.08.22",
                "심플하게 입기 좋아요 재질도 좋은거 같아요 사이즈도 적당합니다",
                "https://image.ohou.se/i/bucketplace-v2-development/uploads/productions/images/168914930378782519.jpg?gif=1&w=640&h=640&c=c&webp=1",
                95,
                "Black"
            )
        )
    }

    private fun FragmentProductReviewTabBinding.recyclerView() {
        reviewRv.run {
            val reviewAdapter = ReviewAdapter(requireContext(), exReviewList)
            reviewRv.adapter = reviewAdapter
            reviewRv.layoutManager = LinearLayoutManager(requireContext())
            reviewRv.isNestedScrollingEnabled = false
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun FragmentProductReviewTabBinding.bottomSheet() {
        startRatingChip.setOnClickListener {
            bottomSheetOpen(R.layout.fragment_product_review_tab_chip_bottom_sheet_star_rating)

        }

        heightChip.setOnClickListener {
            bottomSheetOpen(R.layout.fragment_product_review_tab_chip_bottom_sheet_height)
        }

        weightChip.setOnClickListener {
            bottomSheetOpen(R.layout.fragment_product_review_tab_chip_bottom_sheet_weight)
        }

        defaultSizeChip.setOnClickListener {
            bottomSheetOpen(R.layout.fragment_product_review_tab_chip_bottom_sheet_size)
        }
    }

    private fun bottomSheetOpen(layout: Int) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(
            layout,
            null
        )
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }
}


