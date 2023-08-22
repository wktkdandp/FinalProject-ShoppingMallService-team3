package com.petpal.swimmer_customer.ui.product.tablayout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.petpal.swimmer_customer.R

import com.petpal.swimmer_customer.databinding.FragmentProductReviewTabBinding


class ProductReviewTabFragment : Fragment() {

    private lateinit var fragmentProductReviewTabBinding: FragmentProductReviewTabBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentProductReviewTabBinding = FragmentProductReviewTabBinding.inflate(inflater)
        fragmentProductReviewTabBinding.run{


            startRatingChip.setOnClickListener {
                val bottomSheetDialog = BottomSheetDialog(requireContext())
                val bottomSheetView = layoutInflater.inflate(R.layout.fragment_product_review_tab_chip_bottom_sheet, null)
                bottomSheetDialog.setContentView(bottomSheetView)
                bottomSheetDialog.show()

            }


        }
        // Bottom Sheet 의 동작을 제어하는 객체


        return fragmentProductReviewTabBinding.root
    }


}


