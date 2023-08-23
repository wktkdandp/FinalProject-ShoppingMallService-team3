package com.petpal.swimmer_seller.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.petpal.swimmer_seller.R
import com.petpal.swimmer_seller.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {
    private var _fragmentMenuBinding: FragmentMenuBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val fragmentMenuBinding get() = _fragmentMenuBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentMenuBinding = FragmentMenuBinding.inflate(layoutInflater)
        return fragmentMenuBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var selectedItem: MenuItem? = null

        fragmentMenuBinding.run {
            navigationView.setNavigationItemSelectedListener { item ->
                selectedItem?.isChecked = false // 이전 선택 항목의 isChecked 상태를 false로 변경

                when (item.itemId) {
                    R.id.item_product -> {
                        item.isChecked = true
                        linearLayoutProductManage.visibility = View.VISIBLE
                        linearLayoutCS.visibility = View.GONE
                    }

                    R.id.item_order -> {
                        item.isChecked = true
                        //주문관리 페이지로 이동

                    }

                    R.id.item_cs -> {
                        item.isChecked = true
                        linearLayoutProductManage.visibility = View.GONE
                        linearLayoutCS.visibility = View.VISIBLE
                    }

                    R.id.item_setting -> {
                        item.isChecked = true
                        //설정 페이지로 이동
                    }
                }
                selectedItem = item
                true
            }

            buttonGoToAddProduct.setOnClickListener {
                //상품 등록 페이지로 이동
                findNavController().navigate(R.id.action_item_menu_to_item_product_add)
            }

            buttonGoToModifyProduct.setOnClickListener {
                //상품 조회/수정 페이지로 이동
            }
            buttonGoToManageNotice.setOnClickListener {
                //공지관리 페이지로 이동
            }
            buttonGoToManageInq.setOnClickListener {
                //문의 관리 페이지로 이동
            }
        }
    }
}