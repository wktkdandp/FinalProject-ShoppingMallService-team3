package com.petpal.swimmer_customer.ui.product

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.FirebaseDatabase
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.data.model.ItemsForCustomer
import com.petpal.swimmer_customer.data.model.ProductDetailModel
import com.petpal.swimmer_customer.data.model.productList

import com.petpal.swimmer_customer.databinding.FragmentProductDetailBinding
import java.text.NumberFormat
import java.util.Locale


class ProductDetailFragment : Fragment() {

    lateinit var fragmentProductDetailBinding: FragmentProductDetailBinding
    private var isFavorite = false
    private lateinit var viewModel: ProductViewModel
    private lateinit var viewPagerAdapter: ProductDetailAdapter
    var bottomSheetSize = ""
    var bottomSheetColor = ""
    val imageList: MutableList<ProductDetailModel> = mutableListOf()
    val hashTagList: MutableList<String> = mutableListOf()
    val sizeDataList = arrayOf(
        "사이즈를 선택해 주세요", "XS", "S", "M", "L", "XL"
    )

    val colorDataList = arrayOf(
        "색상을 선택해 주세요", "WHITE", "BLUE", "BLACK", "RED", "ORANGE"
    )

    // 네비게이션 args 값 가져오기
    val args: ProductDetailFragmentArgs by navArgs()
    var firebaseSize = ""
    var firebaseColor = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentProductDetailBinding = FragmentProductDetailBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        fragmentProductDetailBinding.vm = viewModel
        fragmentProductDetailBinding.lifecycleOwner = this

        viewModel.setProductDetailRanking(productList)

        fragmentProductDetailBinding.run {
            productDetailToolbar()
            favorite()
            initViewPager2()
//            observer()

            paymentButtonBottomSheet()
            productDetailTabLayoutViewPage2()
            productDetailViewPager2.isUserInputEnabled = false

            val paths = productList[args.idx].mainImage[args.idx].substring(
                1,
                productList[args.idx].mainImage[args.idx].length - 1
            ).split(",")

            for (path in paths) {
                val cleanedPath = path.trim() // 좌우 공백 제거
                imageList.add(ProductDetailModel(cleanedPath))
            }

            val hashTagsPaths = productList[args.idx].hashTag[args.idx].substring(
                1,
                productList[args.idx].hashTag[args.idx].length - 1
            ).split(",")

            for (path in hashTagsPaths) {
                val cleanedPath = path.trim() // 좌우 공백 제거
                hashTagList.add(cleanedPath)
            }

            viewPagerAdapter.submitList(imageList)
            viewModel.rankingText(
                productList[args.idx].category.mid,
                productList[args.idx].name,
                productList[args.idx].price
            )

            for (text in hashTagList) {
                val chip = createChip(text)
                hashTagChipGroup.addView(chip)
            }

            viewModel.sizePosition.observe(viewLifecycleOwner) {

            }

            viewModel.colorPosition.observe(viewLifecycleOwner) {
                Log.d("나미", it.toString())
            }
        }
        return fragmentProductDetailBinding.root
    }

    private fun FragmentProductDetailBinding.paymentButtonBottomSheet() {
        paymentButton.setOnClickListener {
            Navigation.findNavController(fragmentProductDetailBinding.root)
                .navigate(R.id.action_itemDetailFragment_to_paymentFragment)
        }
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(
            R.layout.fragment_product_detail_payment_button_bottom_sheet,
            null
        )

        bottomSheetDialog.setContentView(bottomSheetView)
        paymentButton.setOnClickListener {
            bottomSheetDialog.show()
        }

        val bottomSheetSizeSpinner =
            bottomSheetView.findViewById<Spinner>(R.id.bottomSheetSizeSpinner)

        val bottomSheetColorSpinner =
            bottomSheetView.findViewById<Spinner>(R.id.bottomSheetColorSpinner)

        val bottomSheetPaymentButton =
            bottomSheetView.findViewById<Button>(R.id.bottomSheetPaymentButton)
        var bottomSheetItemCount =
            bottomSheetView.findViewById<TextView>(R.id.bottomSheetItemCountTextView)
        val bottomSheetPlusButton = bottomSheetView.findViewById<ImageButton>(R.id.plusButton)
        val bottomSheetMinusButton = bottomSheetView.findViewById<ImageButton>(R.id.minusButton)
        val bottomSheetTotalAmountTextView =
            bottomSheetView.findViewById<TextView>(R.id.totalAmountTextView)

        sizeSpinner(bottomSheetSizeSpinner, bottomSheetPaymentButton)
        colorSpinner(bottomSheetColorSpinner, bottomSheetPaymentButton)

        viewModel.isButtonEnabled.observe(viewLifecycleOwner) {
            bottomSheetPaymentButton.isEnabled = it
        }


        bottomSheetPlusButton.setOnClickListener {
            viewModel.bottomSheetItemCountTextViewPlus()
        }

        bottomSheetMinusButton.setOnClickListener {
            viewModel.bottomSheetItemCountTextViewMinus()
        }

        viewModel.bottomSheetItemCountTextView.observe(viewLifecycleOwner) { count ->
            val updatedValue = maxOf(count, 0)
            bottomSheetItemCount.text = updatedValue.toString()
            viewModel.productDetail.observe(viewLifecycleOwner) {
                if (count >= 0) {
                    bottomSheetTotalAmountTextView.text =
                        formatPrice(it[args.idx].price.times(count))
                }
                bottomSheetPaymentButton.isEnabled = count != 0

            }


        }

        bottomSheetPaymentButton.setOnClickListener {

            // 데이터 가공
            var firebaseMainImage = ""
            val _longPrice: String = bottomSheetTotalAmountTextView.text.toString()
            val longPrice: Long = _longPrice.replace("[^\\d]".toRegex(), "").toLong()

            val startIndex = productList[args.idx].mainImage[args.idx].indexOf("[image/")
            val endIndex = productList[args.idx].mainImage[args.idx].indexOf("]", startIndex)
            if (startIndex != -1 && endIndex != -1) {
                val extracted =
                    productList[args.idx].mainImage[args.idx].substring(startIndex + 1, endIndex)
                firebaseMainImage = extracted.split(",")[0].trim()

            }

            // 데이터 대입
            val productUid: String = productList[args.idx].productUid
            val sellerUid: String = productList[args.idx].sellerUid
            val name: String = productList[args.idx].name
            val mainImage: String = firebaseMainImage
            val price: Long = longPrice

            // 임의 처리 필요한 데이터
            val quantity: Long = bottomSheetItemCount.text.toString().toLong()
            val size: String = firebaseSize
            val color: String = firebaseColor

            val itemsForCustomer = ItemsForCustomer(
                productUid,
                sellerUid,
                name,
                mainImage,
                price,
                quantity,
                size,
                color
            )

            // firebase 객체를 생성한다.
            val database = FirebaseDatabase.getInstance()

            // TestData에 접근한다.
            val DataRef = database.getReference("itemsForCustomer")
            DataRef.push().setValue(itemsForCustomer)

            paymentButton.setOnClickListener {
                Navigation.findNavController(fragmentProductDetailBinding.root)
                    .navigate(R.id.action_itemDetailFragment_to_paymentFragment)
            }
            bottomSheetDialog.dismiss()
        }
    }


    private fun sizeSpinner(
        bottomSheetSpinner: Spinner?,
        bottomSheetPaymentButton: Button
    ) {
        bottomSheetSpinner?.run {
            val a1 = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                sizeDataList
            )

            // Spinner가 펼쳐져 있을 때의 항목 모양
            a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            adapter = a1

            // 항목을 선택하면 동작하는 리스너
            // 3번째 : 선택한 항목의 순서값 (0부터)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    firebaseSize = sizeDataList[position]
                    bottomSheetSize = sizeDataList[position]
                    viewModel.setSize(position)

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
        }
    }

    private fun colorSpinner(
        bottomSheetSpinner: Spinner?,
        bottomSheetPaymentButton: Button
    ) {
        bottomSheetSpinner?.run {
            val a1 = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                colorDataList
            )

            // Spinner가 펼쳐져 있을 때의 항목 모양
            a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            adapter = a1

            // 항목을 선택하면 동작하는 리스너
            // 3번째 : 선택한 항목의 순서값 (0부터)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // 1번 아이템만 바로 결제 버튼 비활성화


                    firebaseColor = colorDataList[position]
                    bottomSheetColor = colorDataList[position]
                    viewModel.setColor(position)

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
        }
    }

    private fun FragmentProductDetailBinding.favorite() {
        favoriteButton.setOnClickListener {
            isFavorite = !isFavorite
            updateFavoriteButtonState()
        }
    }

    private fun FragmentProductDetailBinding.productDetailToolbar() {
        toolbarProductDetail.run {
            setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            val navigationIcon = navigationIcon
            navigationIcon?.setTint(ContextCompat.getColor(context, R.color.black))
            setNavigationOnClickListener {
                Navigation.findNavController(fragmentProductDetailBinding.root)
                    .navigate(R.id.action_itemDetailFragment_to_item_home)
            }

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
        fragmentProductDetailBinding.itemDetailViewPager2.apply {
            viewPagerAdapter = ProductDetailAdapter(requireContext(), imageList)
            adapter = viewPagerAdapter
            fragmentProductDetailBinding.dotsIndicator.attachTo(this)
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                fragmentProductDetailBinding.veilLayout.unVeil()
            }, 2000)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            })
        }

    }

    private fun updateFavoriteButtonState() {
        val drawableRes = if (isFavorite) R.drawable.full_favorite_24 else R.drawable.favorite_24px
        fragmentProductDetailBinding.favoriteButton.setImageResource(drawableRes)
    }

    private fun productDetailTabLayoutViewPage2() {
        fragmentProductDetailBinding.productDetailTabLayout.tabRippleColor = null
        val adapter = ProductDetailTabLayoutAdapter(this, args.idx)
        fragmentProductDetailBinding.productDetailViewPager2.adapter = adapter

        val tabName = arrayOf("상품상세", "상품후기 (0)", "상품 Q&A")


        TabLayoutMediator(
            fragmentProductDetailBinding.productDetailTabLayout,
            fragmentProductDetailBinding.productDetailViewPager2
        ) { tab, position ->
            tab.text = tabName[position]
        }.attach()



        fragmentProductDetailBinding.productDetailTabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                fragmentProductDetailBinding.productDetailViewPager2.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    fun formatPrice(price: Int): String {
        val formattedPrice = NumberFormat.getNumberInstance(Locale.getDefault()).format(price)
        return "$formattedPrice 원"
    }

    private fun createChip(text: String): Chip {
        val chip = Chip(requireContext())
        chip.text = text
        chip.isClickable = false
        chip.isCheckable = false
        chip.backgroundTintList = null
        chip.chipStrokeWidth = 0f
        return chip
    }


}