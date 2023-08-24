package com.petpal.swimmer_seller.ui.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.petpal.swimmer_seller.R
import com.petpal.swimmer_seller.data.repository.ProductRepository
import com.petpal.swimmer_seller.databinding.FragmentProductOptionBinding
import com.petpal.swimmer_seller.databinding.RowProductOptionBinding

class ProductOptionFragment : Fragment() {
    private lateinit var productViewModel: ProductViewModel
    private lateinit var fragmentProductOptionBinding: FragmentProductOptionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentProductOptionBinding = FragmentProductOptionBinding.inflate(inflater)
        productViewModel = ViewModelProvider(this, ProductViewModelFactory(ProductRepository()))[ProductViewModel::class.java]

        fragmentProductOptionBinding.run {
            textViewOptionGuid.visibility = View.VISIBLE
            linearLayoutOptionList.visibility = View.INVISIBLE
        }

        return fragmentProductOptionBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productViewModel.run {
            // 추가된 색상, 사이즈 옵션이 없을 경우 안내 문구 표시
            colorList.observe(viewLifecycleOwner){
                if (productViewModel.sizeList.value!!.isEmpty() && productViewModel.colorList.value!!.isEmpty()) {
                    fragmentProductOptionBinding.textViewOptionGuid.visibility = View.VISIBLE
                    fragmentProductOptionBinding.linearLayoutOptionList.visibility =View.INVISIBLE
                } else {
                    fragmentProductOptionBinding.textViewOptionGuid.visibility = View.INVISIBLE
                    fragmentProductOptionBinding.linearLayoutOptionList.visibility =View.VISIBLE
                }
            }
            sizeList.observe(viewLifecycleOwner){
                if (productViewModel.sizeList.value!!.isEmpty() && productViewModel.colorList.value!!.isEmpty()) {
                    fragmentProductOptionBinding.textViewOptionGuid.visibility = View.VISIBLE
                    fragmentProductOptionBinding.linearLayoutOptionList.visibility =View.INVISIBLE
                } else {
                    fragmentProductOptionBinding.textViewOptionGuid.visibility = View.INVISIBLE
                    fragmentProductOptionBinding.linearLayoutOptionList.visibility =View.VISIBLE
                }
            }
        }

        fragmentProductOptionBinding.run {
            buttonShowBottomSheet.setOnClickListener {
                val modalBottomSheet = ModalBottomSheet()
                modalBottomSheet.show(requireActivity().supportFragmentManager, modalBottomSheet.tag)
            }
        }

    }

    // 옵션 목록 RecyclerView Adapter
    inner class OptionRecyclerViewAdapter() : RecyclerView.Adapter<OptionRecyclerViewAdapter.OptionViewHolder>() {
        inner class OptionViewHolder(rowOptionBinding:RowProductOptionBinding): RecyclerView.ViewHolder(rowOptionBinding.root){


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
            TODO("Not yet implemented")
        }

        override fun getItemCount(): Int {
            TODO("Not yet implemented")
        }

        override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
            TODO("Not yet implemented")
        }
    }
}

// 색상, 사이즈 선택 Modal BottomSheet
class ModalBottomSheet: BottomSheetDialogFragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.layout_modal_bottom_sheet_option, container, false)

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // 뷰 초기화, 이벤트 리스너 설정
    }
}