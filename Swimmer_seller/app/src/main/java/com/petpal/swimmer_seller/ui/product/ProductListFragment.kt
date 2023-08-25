package com.petpal.swimmer_seller.ui.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.petpal.swimmer_seller.R
import com.petpal.swimmer_seller.databinding.FragmentProductListBinding
import com.petpal.swimmer_seller.databinding.RowProductBinding

class ProductListFragment : Fragment() {
    private lateinit var productViewModel: ProductViewModel
    private var _fragmentProductListBinding: FragmentProductListBinding? = null

    private val fragmentProductListBinding get() = _fragmentProductListBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentProductListBinding = FragmentProductListBinding.inflate(inflater)
        productViewModel = ViewModelProvider(this, ProductViewModelFactory())[ProductViewModel::class.java]

        return fragmentProductListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentProductListBinding.run {
            toolbarProductList.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            recyclerViewProductList.run {
                adapter = ProductRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(MaterialDividerItemDecoration(context, MaterialDividerItemDecoration.VERTICAL))
            }

            // 필터링, 정렬 드롭다운 초기화
            val defaultFilter = resources.getStringArray(R.array.categoryMain)[0]
            autoTextViewFilter.setText(defaultFilter)

        }
    }

    inner class ProductRecyclerViewAdapter: Adapter<ProductRecyclerViewAdapter.ProductViewHolder>() {
        inner class ProductViewHolder(rowProductBinding: RowProductBinding): ViewHolder(rowProductBinding.root){
            val textViewProductName = rowProductBinding.textViewProductName
            val textViewProductCategory = rowProductBinding.textViewCategory
            val textViewProductRegDate = rowProductBinding.textViewRegDate
            val textViewProductHashTag = rowProductBinding.textViewProductHashTag
            val textViewProductPrice = rowProductBinding.textViewProductPrice

            init {
                // TODO 슬라이드하면 수정 버튼 표시 (일단 수정 기능x)

            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ProductViewHolder {
            val rowProductBinding = RowProductBinding.inflate(layoutInflater)
            rowProductBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return ProductViewHolder(rowProductBinding)
        }

        override fun getItemCount(): Int {
            return productViewModel.productList.value?.size!!
        }

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            val product = productViewModel.productList.value?.get(position)!!
            val category = product.category!!

            holder.textViewProductName.text = product.name
            holder.textViewProductCategory.text = listOfNotNull(category.main, category.mid, category.sub).joinToString(", ")
            holder.textViewProductHashTag.text = product.hashTag?.joinToString(" ") { "#$it" }
            holder.textViewProductPrice.text = "등록 가격 : ${product.price}원"
            holder.textViewProductRegDate.text = "등록일 : ${product.regDate}"
        }
    }
}