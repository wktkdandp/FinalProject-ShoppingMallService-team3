package com.petpal.swimmer_seller.ui.product

import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.petpal.swimmer_seller.R
import com.petpal.swimmer_seller.data.model.Product
import com.petpal.swimmer_seller.databinding.FragmentProductListBinding
import com.petpal.swimmer_seller.databinding.RowProductBinding
import java.text.NumberFormat
import java.util.Locale

class ProductListFragment : Fragment() {
    private lateinit var productViewModel: ProductViewModel
    private var _fragmentProductListBinding: FragmentProductListBinding? = null

    private val fragmentProductListBinding get() = _fragmentProductListBinding!!

    // 필터링, 정렬에 사용할 어댑터 객체
    private lateinit var productAdapter: ProductRecyclerViewAdapter

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

        productViewModel.run {
            productList.observe(viewLifecycleOwner){
                fragmentProductListBinding.recyclerViewProductList.adapter?.notifyDataSetChanged()
            }
        }

        fragmentProductListBinding.run {
            toolbarProductList.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            recyclerViewProductList.run {
                productAdapter = ProductRecyclerViewAdapter(productViewModel.productList.value!!)
                adapter = productAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(MaterialDividerItemDecoration(context, MaterialDividerItemDecoration.VERTICAL))
            }

            // 필터링
            autoTextViewFilter.setOnItemClickListener { parent, view, position, id ->
                val selectedItem = autoTextViewFilter.text.toString()
                productAdapter.filter(selectedItem)
            }

            // 정렬
            autoTextViewSort.setOnItemClickListener { parent, view, position, id ->
                val selectedItem = autoTextViewSort.text.toString()
                val sortArray = resources.getStringArray(R.array.productSort)
                when(selectedItem){
                    // 최신순
                    sortArray[0] -> { productAdapter.sort("regDate", true) }
                    // 등록순
                    sortArray[1] -> { productAdapter.sort("regDate", false) }
                    // 이름순
                    sortArray[2] -> { productAdapter.sort("name", false) }
                    // 가격순
                    sortArray[3] -> { productAdapter.sort("price", false) }
                }
            }
        }

        productViewModel.getAllProductBySellerUid(Firebase.auth.currentUser!!.uid)
        Log.d("hhl", productViewModel.productList.value?.joinToString(", ").toString())

    }

    inner class ProductRecyclerViewAdapter(private val productList: List<Product>): Adapter<ProductRecyclerViewAdapter.ProductViewHolder>() {
        // 필터링된 상품 리스트를 저장할 변수
        private var filteredProductList: List<Product> = productList

        inner class ProductViewHolder(rowProductBinding: RowProductBinding): ViewHolder(rowProductBinding.root){
            // todo productList[adapterPosition].productUid 로 구할 수 있을지도
            var productUid: String = ""
            val imageViewProduct = rowProductBinding.imageViewProduct
            val textViewProductName = rowProductBinding.textViewProductName
            val textViewProductCategory = rowProductBinding.textViewCategory
            val textViewProductRegDate = rowProductBinding.textViewRegDate
            val textViewProductHashTag = rowProductBinding.textViewProductHashTag
            val textViewProductPrice = rowProductBinding.textViewProductPrice

            init {
                // 상품 상세 화면으로 이동
                rowProductBinding.root.setOnClickListener {
                    val action = ProductListFragmentDirections.actionItemProductListToItemProductDetail(productUid)
                    findNavController().navigate(action)
                }
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
            return filteredProductList.size
        }

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            val product = filteredProductList[position]
            val category = product.category!!
            // 다음 화면으로 넘겨줄 uid
            holder.productUid = product.productUid.toString()

            // 서버로 부터 이미지를 내려받아 ImageView에 표시
            productViewModel.loadAndDisplayImage(product.mainImage?.get(0)!!, holder.imageViewProduct)

            holder.textViewProductName.text = product.name
            // 카테고리, 태그 분리 후 구분자 넣어서 결합
            holder.textViewProductCategory.text = listOfNotNull(category.main, category.mid, category.sub).joinToString(" > ")
            holder.textViewProductHashTag.text = product.hashTag?.joinToString(" ") { "#$it" }
            // 가격 천의 자리에 쉼표 찍기
            holder.textViewProductPrice.text = "${NumberFormat.getNumberInstance(Locale.getDefault()).format(product.price)}원"
            holder.textViewProductRegDate.text = "${product.regDate}"
        }

        // RecyclerView 데이터 필터링
        fun filter(category: String){
            filteredProductList = if (category == "전체") {
                productList
            } else {
                productList.filter { it.category?.main == category }
            }
            notifyDataSetChanged()
        }

        // RecyclerView 데이터 정렬 (정렬 기준 키, 내림차순 여부)
        fun sort(sortKey: String, isDescending: Boolean){
            filteredProductList = when (sortKey) {
                // todo regDate 잘 정렬 안되면 yyyyMMddhhmm 형태로 포맷팅하기
                "regDate" -> {
                    if (isDescending) {
                        filteredProductList.sortedByDescending { it.regDate }
                    } else {
                        filteredProductList.sortedBy { it.regDate }
                    }
                }
                "name" -> { filteredProductList.sortedBy { it.name } }
                "price" -> { filteredProductList.sortedBy { it.price }}
                else -> { productList }
            }

            notifyDataSetChanged()
        }
    }
}