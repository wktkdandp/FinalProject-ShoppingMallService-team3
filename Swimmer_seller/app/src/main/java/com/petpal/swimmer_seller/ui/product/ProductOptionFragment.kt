package com.petpal.swimmer_seller.ui.product

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.petpal.swimmer_seller.R
import com.petpal.swimmer_seller.data.model.Image
import com.petpal.swimmer_seller.data.model.Product
import com.petpal.swimmer_seller.data.repository.ProductRepository
import com.petpal.swimmer_seller.databinding.FragmentProductOptionBinding
import com.petpal.swimmer_seller.databinding.LayoutBottomSheetOptionBinding
import com.petpal.swimmer_seller.databinding.RowProductOptionBinding

class ProductOptionFragment : Fragment() {
    private lateinit var productViewModel: ProductViewModel
    private lateinit var fragmentProductOptionBinding: FragmentProductOptionBinding

    lateinit var product: Product
    lateinit var imageArray: Array<Image>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentProductOptionBinding = FragmentProductOptionBinding.inflate(inflater)
        productViewModel = ViewModelProvider(this, ProductViewModelFactory(ProductRepository()))[ProductViewModel::class.java]

        fragmentProductOptionBinding.run {
            textViewOptionGuide.visibility = View.VISIBLE
            linearLayoutOption.visibility = View.INVISIBLE
        }

        return fragmentProductOptionBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("hhl", "ProductOptionFragment onViewCreated()")
        val args = ProductOptionFragmentArgs.fromBundle(requireArguments())
        product = args.Product
        imageArray = args.Image
        Log.d("hhl", product.name.toString())
        Log.d("hhl", imageArray.first().fileName.toString())

        fragmentProductOptionBinding.run {
            productViewModel.run {
                // 추가된 색상, 사이즈 옵션이 없을 경우 안내 문구 표시
                colorList.observe(viewLifecycleOwner){
                    if (productViewModel.sizeList.value!!.isEmpty() && productViewModel.colorList.value!!.isEmpty()) {
                        textViewOptionGuide.visibility = View.VISIBLE
                        linearLayoutOption.visibility =View.INVISIBLE
                    } else {
                        textViewOptionGuide.visibility = View.GONE
                        linearLayoutOption.visibility =View.VISIBLE
                    }
                    recyclerViewColor.adapter?.notifyDataSetChanged()
                }
                sizeList.observe(viewLifecycleOwner){
                    if (productViewModel.sizeList.value!!.isEmpty() && productViewModel.colorList.value!!.isEmpty()) {
                        textViewOptionGuide.visibility = View.VISIBLE
                        linearLayoutOption.visibility =View.INVISIBLE
                    } else {
                        textViewOptionGuide.visibility = View.GONE
                        linearLayoutOption.visibility =View.VISIBLE
                    }
                    recyclerViewSize.adapter?.notifyDataSetChanged()
                }
            }

            buttonShowBottomSheet.setOnClickListener {
                // ModalBottomSheet에서 추가 버튼 클릭시 처리 리스너 등록
                val modalBottomSheet = ModalBottomSheet()
                modalBottomSheet.setOnOptionSelectionListener(object : ModalBottomSheet.OnOptionSelectedListener {
                    override fun onOptionSelected(colorList: List<String>, sizeList: List<String>) {
                        Log.d("product", colorList.joinToString(","))
                        Log.d("product", sizeList.joinToString(","))

                        // 색상, 사이즈 리스트 저장
                        productViewModel.addColorAndSizeOption(colorList, sizeList)
                    }
                })

                modalBottomSheet.show(requireActivity().supportFragmentManager, modalBottomSheet.tag)
            }

            recyclerViewColor.run {
                adapter = ColorRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            }

            recyclerViewSize.run {
                adapter = SizeRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            }

            // 상품 등록
            buttonRegisterProduct.setOnClickListener {
                progressBarOption.visibility = View.VISIBLE
                // TODO product 객체 받아서 colorList, sizeList 세팅 후 DB 저장

                product.colorList = productViewModel.colorList.value
                product.sizeList = productViewModel.sizeList.value

                // 상품 정보 DB 저장
                productViewModel.addProduct(product)

                // 이미지 업로드
                productViewModel.uploadImageList(imageArray)

                Snackbar.make(fragmentProductOptionBinding.root, "상품이 등록되었습니다.", Snackbar.LENGTH_SHORT).show()
                // 홈 화면 전환
                findNavController().popBackStack(R.id.item_home, false)
            }
        }

    }

    // 선택한 색상, 사이즈 목록 표시
    // 색상 목록 RecyclerView Adapter
    inner class ColorRecyclerViewAdapter() : RecyclerView.Adapter<ColorRecyclerViewAdapter.ColorViewHolder>() {
        inner class ColorViewHolder(rowOptionBinding:RowProductOptionBinding): RecyclerView.ViewHolder(rowOptionBinding.root){
            val textViewOption = rowOptionBinding.textViewOption
            val buttonDeleteOption = rowOptionBinding.buttonDeleteOption

            init {
                buttonDeleteOption.setOnClickListener {
                    // 색상 삭제
                    productViewModel.deleteColorOption(adapterPosition)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
            val rowOptionBinding = RowProductOptionBinding.inflate(layoutInflater)
            val colorViewHolder = ColorViewHolder(rowOptionBinding)
            rowOptionBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            return colorViewHolder
        }

        override fun getItemCount(): Int {
            return productViewModel.colorList.value!!.size
        }

        override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
            holder.textViewOption.text = productViewModel.colorList.value!![position]
        }
    }

    // 사이즈 목록 RecyclerView Adapter
    inner class SizeRecyclerViewAdapter() : RecyclerView.Adapter<SizeRecyclerViewAdapter.SizeViewHolder>() {
        inner class SizeViewHolder(rowOptionBinding:RowProductOptionBinding): RecyclerView.ViewHolder(rowOptionBinding.root){
            val textViewOption = rowOptionBinding.textViewOption
            val buttonDeleteOption = rowOptionBinding.buttonDeleteOption
            init {
                buttonDeleteOption.setOnClickListener {
                    // 색상 삭제
                    productViewModel.deleteSizeOption(adapterPosition)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
            val rowOptionBinding = RowProductOptionBinding.inflate(layoutInflater)
            val sizeViewHolder = SizeViewHolder(rowOptionBinding)
            rowOptionBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            return sizeViewHolder
        }

        override fun getItemCount(): Int {
            return productViewModel.sizeList.value!!.size
        }

        override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
            holder.textViewOption.text = productViewModel.sizeList.value!![position]
        }
    }
}

// 색상, 사이즈 선택 Modal BottomSheet
class ModalBottomSheet: BottomSheetDialogFragment(){
    private lateinit var modalBottomSheetBinding: LayoutBottomSheetOptionBinding
    private lateinit var listener : OnOptionSelectedListener

    fun setOnOptionSelectionListener(listener: OnOptionSelectedListener){
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        modalBottomSheetBinding = LayoutBottomSheetOptionBinding.inflate(inflater, container, false)
        return modalBottomSheetBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 옵션 추가 이벤트 리스너
        modalBottomSheetBinding.run {
            buttonAddOption.setOnClickListener {
                // 체크된 색상 이름 추출
                val checkedColorList: List<String> = chipGroupColor.children
                    .filterIsInstance<Chip>()
                    .filter { it.isChecked }
                    .map { it.contentDescription.toString() }
                    .toList()

                // 체크된 사이즈 이름 추출
                val checkedSizeList: List<String> = chipGroupSize.children
                    .filterIsInstance<Chip>()
                    .filter { it.isChecked }
                    .map { it.text.toString() }
                    .toList()

                // ProductOptionFragment에서 등록된 리스너 실행
                listener.onOptionSelected(checkedColorList, checkedSizeList)

                // 닫기
                dismiss()
            }
        }
    }

    // ModalBottomSheet 에서 입력된 데이터를 ParentFragment로 전달하기 위한 인터페이스
    interface OnOptionSelectedListener {
        fun onOptionSelected(colorList: List<String>, sizeList : List<String>)
    }
}

