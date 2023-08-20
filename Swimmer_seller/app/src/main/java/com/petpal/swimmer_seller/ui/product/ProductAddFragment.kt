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
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.petpal.swimmer_seller.MainActivity
import com.petpal.swimmer_seller.R
import com.petpal.swimmer_seller.data.model.Category
import com.petpal.swimmer_seller.data.model.Product
import com.petpal.swimmer_seller.data.model.Review
import com.petpal.swimmer_seller.data.repository.ProductRepository
import com.petpal.swimmer_seller.databinding.FragmentProductAddBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProductAddFragment : Fragment() {
    lateinit var productViewModel: ProductViewModel
    lateinit var fragmentProductAddBinding: FragmentProductAddBinding
    lateinit var mainActivity: MainActivity

    // TODO 나중에 List에 담아서 대표이미지 여러 개 등록하기
    private var mainImageUri: Uri? = null
    private var descriptionImageUri: Uri? = null

    lateinit var mainGalleryLauncher: ActivityResultLauncher<Intent>
    lateinit var descGalleryLauncher: ActivityResultLauncher<Intent>

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

        mainGalleryLauncher = gallerySetting(fragmentProductAddBinding.imageViewMain, true)
        descGalleryLauncher = gallerySetting(fragmentProductAddBinding.imageViewDescription, false)

        fragmentProductAddBinding.run {
            toolbarProductAdd.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            buttonCancel.setOnClickListener {
                findNavController().popBackStack()
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
                if (mainImageUri == null) {
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
                val mainImageFileName = "image/${code}_main_image.jpg"
                val descriptionImageFileName = "image/${code}_description_image.jpg"

                val product = Product(
                    "",
                    code,
                    textInputEditTextProductName.text.toString(),
                    textInputEditTextPrice.text.toString().toLong(),
                    mainImageFileName,
                    textInputEditTextDescription.text.toString(),
                    descriptionImageFileName,
                    mainActivity.loginSellerUid,
                    mutableListOf<Long>(),
                    mutableListOf<Long>(),
                    textInputEditTextHashTag.text.toString(),
                    category,
                    mutableListOf<Review>(),
                    0L,
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                )

                // 상품 정보 DB 저장
                productViewModel.addProduct(product)

                // 이미지 업로드
                productViewModel.uploadImages(mutableMapOf<Uri, String>(
                    mainImageUri!! to mainImageFileName,
                    descriptionImageUri!! to descriptionImageFileName
                ))

                Snackbar.make(fragmentProductAddBinding.root, "상품이 등록되었습니다.", Snackbar.LENGTH_SHORT).show()

                // 전부 완료되면 상품등록 프레그먼드 제거하고 홈으로 이동
                findNavController().popBackStack(R.id.item_product_add, true)
            }
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
    
    // 갤러리 설정
    private fun gallerySetting(previewImageView: ImageView, isMainImage: Boolean): ActivityResultLauncher<Intent>{
        val galleryContract = ActivityResultContracts.StartActivityForResult()
        val galleryLauncher = registerForActivityResult(galleryContract) {

            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                if (it.data?.data != null) {
                    val uploadUri = it.data?.data

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

                    if (isMainImage) {
                        // TODO 차후 메인 이미지 여러 개 저장시 List<Uri>로 저장하는 방식으로 변경하기
                        mainImageUri = uploadUri
                        fragmentProductAddBinding.buttonAddMainImage.text = "1/5"
                    } else {
                        descriptionImageUri = uploadUri
                        fragmentProductAddBinding.buttonAddDescImage.text = "1/1"
                    }
                }
            }
        }

        return galleryLauncher
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