package com.petpal.swimmer_seller.ui.product

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.petpal.swimmer_seller.MainActivity
import com.petpal.swimmer_seller.R
import com.petpal.swimmer_seller.data.model.Category
import com.petpal.swimmer_seller.data.model.Product
import com.petpal.swimmer_seller.data.model.Review
import com.petpal.swimmer_seller.data.repository.ProductRepository
import com.petpal.swimmer_seller.databinding.FragmentProductAddBinding
import com.petpal.swimmer_seller.databinding.LayoutImageviewDeleteBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProductAddFragment : Fragment() {
    lateinit var productViewModel: ProductViewModel
    private lateinit var fragmentProductAddBinding: FragmentProductAddBinding
    lateinit var mainActivity: MainActivity

    // 대표 이미지 최대 5개, 설명 이미지 1개
    private var mainImageUriList = mutableListOf<Uri>()
    private var descriptionImageUri: Uri? = null

    lateinit var mainGalleryLauncher: ActivityResultLauncher<Intent>
    lateinit var descGalleryLauncher: ActivityResultLauncher<Intent>

    private val addHashTagList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentProductAddBinding = FragmentProductAddBinding.inflate(inflater)
        mainActivity = activity as MainActivity
        return fragmentProductAddBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ProductViewModelFactory(ProductRepository())
        productViewModel = ViewModelProvider(this, factory)[ProductViewModel::class.java]

        mainGalleryLauncher = multiGallerySetting()
        descGalleryLauncher = singleGallerySetting()

        addHashTagList.clear()

        fragmentProductAddBinding.run {
            toolbarProductAdd.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            buttonCancel.setOnClickListener {
                findNavController().popBackStack()
            }

            recyclerViewMainImage.run {
                adapter = MainImageRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }

            // 대 → 중 → 소 선택에 따라 드롭박스 단계별 활성화, 세팅
            dropdownCategoryMain.addTextChangedListener {
                // 유효성 검사
                validateEditText(it.toString(), textInputLayoutCategoryMain, "대분류를 선택해 주세요")
                textInputLayoutCategoryMid.isEnabled = true

                // 선택한 대분류에 따라 중분류 다르게 표시, 대분류 먼저 선택해야 중분류 선택 활성화. 다시 공백 선택할 경우
                // 소분류는 아직 비활성화
                when (it.toString()) {
                    getString(R.string.female) -> dropdownCategoryMid.setSimpleItems(resources.getStringArray(R.array.categoryMid1))
                    getString(R.string.male) -> dropdownCategoryMid.setSimpleItems(resources.getStringArray(R.array.categoryMid2))
                    getString(R.string.child) -> dropdownCategoryMid.setSimpleItems(resources.getStringArray(R.array.categoryMid3))
                    getString(R.string.supplies) -> dropdownCategoryMid.setSimpleItems(resources.getStringArray(R.array.categoryMid4))
                    else -> textInputLayoutCategoryMid.isEnabled = false
                }
            }
            dropdownCategoryMid.addTextChangedListener {
                validateEditText(it.toString(), textInputLayoutCategoryMid, "중분류를 선택해 주세요")
                // 선택한 중분류에 따라 소분류 다르게 표시
                val categoryMain = dropdownCategoryMain.text.toString()
                val categoryMid = it.toString()

                textInputLayoutCategorySub.isEnabled = true

                when (categoryMain) {
                    getString(R.string.female) -> {
                        when (categoryMid) {
                            getString(R.string.swimsuit) -> dropdownCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub1_1))
                            getString(R.string.bikini) -> dropdownCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub1_2))
                            getString(R.string.rashguard) -> dropdownCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub1_3))
                            getString(R.string.beachwear) -> dropdownCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub1_4))
                        }
                    }
                    getString(R.string.male) -> {
                        when (categoryMid) {
                            getString(R.string.swimsuit) -> dropdownCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub2_1))
                            getString(R.string.rashguard) -> dropdownCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub2_2))
                            getString(R.string.beachwear) -> dropdownCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub2_3))
                        }
                    }
                    getString(R.string.child) -> {
                        when (categoryMid) {
                            getString(R.string.swimsuit) -> dropdownCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub3_1))
                            getString(R.string.rashguard) -> dropdownCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub3_2))
                        }
                    }
                    getString(R.string.supplies) -> {
                        when(categoryMid){
                            getString(R.string.goggles) -> dropdownCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub4_1))
                            getString(R.string.cap) -> dropdownCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub4_2))
                            getString(R.string.flippers) -> dropdownCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub4_3))
                            getString(R.string.bag) -> dropdownCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub4_4))
                            getString(R.string.water_toy) -> dropdownCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub4_5))
                        }
                    }
                }
            }
            dropdownCategorySub.addTextChangedListener {
                validateEditText(it.toString(), textInputLayoutCategorySub, "소분류를 선택해 주세요")
            }

            // 입력시 실시간 유효성 검사
            textInputEditTextProductName.addTextChangedListener {
                validateEditText(it.toString(), textInputLayoutName, "상품명을 입력해 주세요")
            }
            textInputEditTextPrice.addTextChangedListener {
                validateEditText(it.toString(), textInputLayoutPrice, "가격을 입력해 주세요")
            }
            textInputEditTextDescription.addTextChangedListener {
                validateEditText(it.toString(), textInputLayoutDescription, "상품 설명을 입력해 주세요")
            }

            // 해시태그 Chip 추가
            textInputEditTextHashTag.setOnEditorActionListener { v, actionId, event ->
                addHashTag()
                true
            }
            buttonAddHashTag.setOnClickListener{
                addHashTag()
            }

            // 대표 이미지 추가
            buttonAddMainImage.setOnClickListener {
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                    type = "image/*"
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*"))
                }
                mainGalleryLauncher.launch(galleryIntent)
            }

            // 설명 이미지 추가
            buttonAddDescImage.setOnClickListener {
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                    type = "image/*"
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*"))
                }
                descGalleryLauncher.launch(galleryIntent)
            }

            buttonOK.setOnClickListener {
                // 최종 유효성 검사
                if (mainImageUriList.size < 1) {
                    buttonAddMainImage.requestFocus()
                    scrollView.scrollTo(0, buttonAddMainImage.top)
                    Snackbar.make(fragmentProductAddBinding.root, "상품 이미지를 1개 이상 등록해 주세요", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (!validateEditText(textInputEditTextProductName.text.toString(), textInputLayoutName, "상품명을 입력해 주세요")){
                    return@setOnClickListener
                }
                if (!validateEditText(dropdownCategoryMain.text.toString(), textInputLayoutCategoryMain, "대분류를 선택해 주세요")){
                    return@setOnClickListener
                }
                if (!validateEditText(dropdownCategoryMid.text.toString(), textInputLayoutCategoryMid, "중분류를 선택해 주세요")){
                    return@setOnClickListener
                }
                if (!validateEditText(dropdownCategorySub.text.toString(), textInputLayoutCategorySub, "소분류를 선택해 주세요")){
                    return@setOnClickListener
                }
                if(!validateEditText(textInputEditTextPrice.text.toString(), textInputLayoutPrice, "가격을 입력해 주세요")){
                    return@setOnClickListener
                }
                if (!validateEditText(textInputEditTextDescription.text.toString(), textInputLayoutDescription, "상품 설명을 입력해 주세요")){
                    return@setOnClickListener
                }
                if (descriptionImageUri == null) {
                    buttonAddDescImage.requestFocus()
                    scrollView.scrollTo(0, buttonAddDescImage.bottom)
                    Snackbar.make(fragmentProductAddBinding.root, "상품 설명 이미지를 등록해 주세요", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                progressBar.visibility = View.VISIBLE

                val category = Category(
                    dropdownCategoryMain.text.toString(),
                    dropdownCategoryMid.text.toString(),
                    dropdownCategorySub.text.toString()
                )
                val code = System.currentTimeMillis().toString()

                // DB에 저장될 이미지 파일들
                val uploadImagesMap = mutableMapOf<Uri, String>()
                // 메인 이미지 Uri 별로 파일명 설정
                for (idx in mainImageUriList.indices) {
                    uploadImagesMap[mainImageUriList[idx]] = "image/${code}_main_image${idx+1}.jpg"
                }

                val descriptionImageFileName = "image/${code}_description_image.jpg"

                val product = Product(
                    "",
                    code,
                    textInputEditTextProductName.text.toString(),
                    textInputEditTextPrice.text.toString().toLong(),
                    uploadImagesMap.values.toList(),
                    textInputEditTextDescription.text.toString(),
                    descriptionImageFileName,
                    mainActivity.loginSellerUid,
                    mutableListOf<Long>(),
                    mutableListOf<Long>(),
                    addHashTagList,
                    category,
                    mutableListOf<Review>(),
                    0L,
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                )

                // 상품 정보 DB 저장
                productViewModel.addProduct(product)

                // 이미지 업로드
                uploadImagesMap[descriptionImageUri!!] = descriptionImageFileName
                productViewModel.uploadImages(uploadImagesMap)

                Snackbar.make(fragmentProductAddBinding.root, "상품이 등록되었습니다.", Snackbar.LENGTH_SHORT).show()

                // 전부 완료되면 상품등록 프레그먼드 제거하고 홈으로 이동
                findNavController().popBackStack(R.id.item_product_add, true)
            }
        }
    }

    private fun addHashTag() {
        fragmentProductAddBinding.run {
            // 입력 문자열 태그로 분리, 중복 태그 & 이미 chip 생성된 태그 제외
            val inputHashTagList = textInputEditTextHashTag.text.toString()
                .split(",")
                .map(String::trim)
                .distinct()
                .filter { !addHashTagList.contains(it) }

            // 추가된 태그 저장
            addHashTagList.addAll(inputHashTagList)

            for (inputHashTag in inputHashTagList) {
                val chip = layoutInflater.inflate(R.layout.layout_chip_input, chipGroupHashTag, false) as Chip
                chip.apply {
                    text = inputHashTag
                    setOnCloseIconClickListener {
                        chipGroupHashTag.removeView(it)
                        addHashTagList.remove(inputHashTag)
                    }
                }
                chipGroupHashTag.addView(chip)
            }

            textInputEditTextHashTag.setText("")
        }
    }

    // EditText 유효성 검사
    private fun validateEditText(input: String, textInputLayout: TextInputLayout, errorMessage: String): Boolean {
        return if (input.isNotBlank()) {
            textInputLayout.isErrorEnabled = false
            textInputLayout.error = null
            true
        } else {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = errorMessage
            mainActivity.showSoftInput(textInputLayout)
            false
        }
    }
    
    // 다중 선택 갤러리 설정
    private fun multiGallerySetting(): ActivityResultLauncher<Intent>{
        val galleryContract = ActivityResultContracts.StartActivityForResult()
        val galleryLauncher = registerForActivityResult(galleryContract) {
            // 메인 이미지 최대 5개까지만 첨부 가능
            if (mainImageUriList.size < 6){
                if (it.resultCode == AppCompatActivity.RESULT_OK) {
                    if (it.data?.data != null) {
                        val uploadUri = it.data?.data
                        val previewCardView = layoutInflater.inflate(R.layout.layout_imageview_delete, fragmentProductAddBinding.linearMainImage, false) as CardView
                        val previewImageView = previewCardView.findViewById<ImageView>(R.id.imageViewDelete)
                        val previewButton = previewCardView.findViewById<Button>(R.id.buttonDelete)
                        previewButton.setOnClickListener {
                            // TODO 이 부분을 ViewModel에서 UI 파악할 수 있도록 빼기?
                            // 이미지 삭제, 리스트에서 제거
                            fragmentProductAddBinding.linearMainImage.removeView(previewCardView)
                            mainImageUriList.remove(uploadUri)
                            fragmentProductAddBinding.buttonAddMainImage.text = "${mainImageUriList.size}/5"
                        }
                        // 화면에 이미지 CardView 추가
                        fragmentProductAddBinding.linearMainImage.addView(previewCardView)
                        // 메인 이미지 리스트에 추가
                        mainImageUriList.add(uploadUri!!)

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            val source = ImageDecoder.createSource(mainActivity.contentResolver, uploadUri!!)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            previewImageView.setImageBitmap(bitmap)
                        } else {
                            val cursor = mainActivity.contentResolver.query(uploadUri!!, null, null, null, null)
                            if (cursor != null) {
                                cursor.moveToNext()

                                val colIdx = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                                val source = cursor.getString(colIdx)

                                val bitmap = BitmapFactory.decodeFile(source)
                                previewImageView.setImageBitmap(bitmap)
                            }
                        }
                        mainImageUriList.add(uploadUri)
                        fragmentProductAddBinding.buttonAddMainImage.text = "${mainImageUriList.size}/5"
                    }
                }
            }
        }

        return galleryLauncher
    }

    // 단일 선택 갤러리 설정
    private fun singleGallerySetting(): ActivityResultLauncher<Intent>{
        val galleryContract = ActivityResultContracts.StartActivityForResult()
        val galleryLauncher = registerForActivityResult(galleryContract) {

            if (descriptionImageUri == null) {
                if (it.resultCode == AppCompatActivity.RESULT_OK) {
                    if (it.data?.data != null) {
                        val uploadUri = it.data?.data
                        val previewCardView = layoutInflater.inflate(R.layout.layout_imageview_delete, fragmentProductAddBinding.linearMainImage, false) as CardView
                        val previewImageView = previewCardView.findViewById<ImageView>(R.id.imageViewDelete)
                        val previewButton = previewCardView.findViewById<Button>(R.id.buttonDelete)
                        previewButton.setOnClickListener {
                            // 이미지 삭제, 리스트에서 제거
                            fragmentProductAddBinding.linearMainImage.removeView(previewCardView)
                            descriptionImageUri = null
                            fragmentProductAddBinding.buttonAddDescImage.text = "0/1"
                        }
                        fragmentProductAddBinding.linearMainImage.addView(previewCardView)

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            val source = ImageDecoder.createSource(mainActivity.contentResolver, uploadUri!!)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            previewImageView.setImageBitmap(bitmap)
                        } else {
                            val cursor = mainActivity.contentResolver.query(uploadUri!!, null, null, null, null)
                            if (cursor != null) {
                                cursor.moveToNext()

                                val colIdx = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                                val source = cursor.getString(colIdx)

                                val bitmap = BitmapFactory.decodeFile(source)
                                previewImageView.setImageBitmap(bitmap)
                            }
                        }

                        descriptionImageUri = uploadUri
                        fragmentProductAddBinding.buttonAddDescImage.text = "1/1"
                    }
                }
            }
        }

        return galleryLauncher
    }

    inner class MainImageRecyclerViewAdapter: RecyclerView.Adapter<MainImageRecyclerViewAdapter.MainImageViewHolder>(){
        inner class MainImageViewHolder(imageViewBinding: LayoutImageviewDeleteBinding):RecyclerView.ViewHolder(imageViewBinding.root) {
            val imageViewMain: ImageView
            val buttonDeleteMain: Button
            var uri : Uri?

            init {
                imageViewMain = imageViewBinding.imageViewDelete
                buttonDeleteMain = imageViewBinding.buttonDelete
                uri = null

                buttonDeleteMain.setOnClickListener {
                    mainImageUriList.remove(uri)
                    fragmentProductAddBinding.buttonAddMainImage.text = "${mainImageUriList.count()}/5"
                    notifyDataSetChanged()
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainImageViewHolder {
            val imageViewBinding = LayoutImageviewDeleteBinding.inflate(layoutInflater)
            val mainImageViewHolder = MainImageViewHolder(imageViewBinding)

            imageViewBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            return mainImageViewHolder
        }

        override fun getItemCount(): Int {
            return mainImageUriList.size
        }

        override fun onBindViewHolder(holder: MainImageViewHolder, position: Int) {
            holder.uri = mainImageUriList[position]
            holder.imageViewMain.setImageURI(mainImageUriList[position])
        }

    }
}

//뷰모델 팩토리
class ProductViewModelFactory(private val repository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

