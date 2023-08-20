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
import androidx.navigation.findNavController
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
    lateinit var fragmentProductAddBinding: FragmentProductAddBinding
    lateinit var mainActivity: MainActivity

    // TODO 나중에 List에 담아서 대표이미지 여러 개 등록하기
    var mainImageUri: Uri? = null
    var descImageUri: Uri? = null

    lateinit var mainGalleryLauncher: ActivityResultLauncher<Intent>
    lateinit var descGalleryLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentProductAddBinding = FragmentProductAddBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        mainGalleryLauncher = gallerySetting(fragmentProductAddBinding.imageViewMain, true)
        descGalleryLauncher = gallerySetting(fragmentProductAddBinding.imageViewDesc, false)

        fragmentProductAddBinding.run {
            toolbarProductAdd.run {
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    it.findNavController().navigate(R.id.item_home)
                }
            }

            buttonCancel.setOnClickListener {
                it.findNavController().navigate(R.id.item_home)
            }

            // 입력시 실시간 유효성 검사
            editTextName.addTextChangedListener {
                validateEditText(it.toString(), textInputLayoutName, "상품명을 입력해 주세요")
            }
            editTextPrice.addTextChangedListener {
                validateEditText(it.toString(), textInputLayoutPrice, "가격을 입력해 주세요")
            }
            editTextDescription.addTextChangedListener {
                validateEditText(it.toString(), textInputLayoutDescription, "상품 설명을 입력해 주세요")
            }

            // 대, 중, 소 분류 자동 세팅
            editTextCategoryMain.addTextChangedListener {
                // 유효성 검사
                validateEditText(it.toString(), textInputLayoutCategoryMain, "대분류를 선택해 주세요")
                textInputLayoutCategoryMid.isEnabled = true

                // 선택한 대분류에 따라 중분류 다르게 표시, 대분류 먼저 선택해야 중분류 선택 활성화. 다시 공백 선택할 경우
                // 소분류는 아직 비활성화
                when (it.toString()) {
                    "여성" -> editTextCategoryMid.setSimpleItems(resources.getStringArray(R.array.categoryMid1))
                    "남성" -> editTextCategoryMid.setSimpleItems(resources.getStringArray(R.array.categoryMid2))
                    "아동" -> editTextCategoryMid.setSimpleItems(resources.getStringArray(R.array.categoryMid3))
                    "용품" -> editTextCategoryMid.setSimpleItems(resources.getStringArray(R.array.categoryMid4))
                    else -> textInputLayoutCategoryMid.isEnabled = false
                }
            }
            editTextCategoryMid.addTextChangedListener {
                validateEditText(it.toString(), textInputLayoutCategoryMid, "중분류를 선택해 주세요")
                // 선택한 중분류에 따라 소분류 다르게 표시
                val categoryMain = editTextCategoryMain.text.toString()
                val categoryMid = it.toString()

                textInputLayoutCategorySub.isEnabled = true

                when (categoryMain) {
                    "여성" -> {
                        when (categoryMid) {
                            "수영복" -> editTextCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub1_1))
                            "비키니" -> editTextCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub1_2))
                            "래쉬가드" -> editTextCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub1_3))
                            "비치웨어" -> editTextCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub1_4))
                        }
                    }
                    "남성" -> {
                        when (categoryMid) {
                            "수영복" -> editTextCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub2_1))
                            "래쉬가드" -> editTextCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub2_2))
                            "비치웨어" -> editTextCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub2_3))
                        }
                    }
                    "아동" -> {
                        when (categoryMid) {
                            "수영복" -> editTextCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub3_1))
                            "래쉬가드" -> editTextCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub3_2))
                        }
                    }
                    "용품" -> {
                        when(categoryMid){
                            "수경" -> editTextCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub4_1))
                            "수모" -> editTextCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub4_2))
                            "오리발" -> editTextCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub4_3))
                            "가방" -> editTextCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub4_4))
                            "물놀이용품" -> editTextCategorySub.setSimpleItems(resources.getStringArray(R.array.categorySub4_5))
                        }
                    }
                }
            }
            editTextCategorySub.addTextChangedListener {
                validateEditText(it.toString(), textInputLayoutCategorySub, "소분류를 선택해 주세요")
            }

            // ImageView 세팅
            imageViewMain.setImageBitmap(null)
            imageViewDesc.setImageBitmap(null)

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
                if (!validateEditText(editTextName.text.toString(), textInputLayoutName, "상품명을 입력해 주세요")){
                   return@setOnClickListener
                }
                if (!validateEditText(editTextCategoryMain.text.toString(), textInputLayoutCategoryMain, "대분류를 선택해 주세요")){
                    return@setOnClickListener
                }
                if (!validateEditText(editTextCategoryMid.text.toString(), textInputLayoutCategoryMid, "중분류를 선택해 주세요")){
                    return@setOnClickListener
                }
                if (!validateEditText(editTextCategorySub.text.toString(), textInputLayoutCategorySub, "소분류를 선택해 주세요")){
                    return@setOnClickListener
                }
                if(!validateEditText(editTextPrice.text.toString(), textInputLayoutPrice, "가격을 입력해 주세요")){
                    return@setOnClickListener
                }
                if (!validateEditText(editTextDescription.text.toString(), textInputLayoutDescription, "상품 설명을 입력해 주세요")){
                    return@setOnClickListener
                }
                if (descImageUri == null) {
                    buttonAddDescImage.requestFocus()
                    scrollView.scrollTo(0, buttonAddDescImage.bottom)
                    Snackbar.make(fragmentProductAddBinding.root, "상품 설명 이미지를 등록해 주세요", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // 입력한 정보 가져오기
                val name = editTextName.text.toString()
                val price = editTextPrice.text.toString()
                val description = editTextDescription.text.toString()

                // val inputHashTag = editTextHashTag.text.toString().split(",").map(String::trim)
                val hashTag = editTextHashTag.text.toString()

                val categoryMain = editTextCategoryMain.text.toString()
                val categoryMid = editTextCategoryMid.text.toString()
                val categorySub = editTextCategorySub.text.toString()

                // TODO products 테이블 uid 로 변경하기
                // 상품 식별 코드 (중복 방지를 위해 현시각으로 입력)
                val code = System.currentTimeMillis().toString()

                // 대표 이미지
                val mainImageFileName = "image/${code}_main_image.jpg"
                // 설명 이미지
                val descImageFileName = "image/${code}_description_image.jpg"

                val sizeList = mutableListOf<Long>()
                val colorList = mutableListOf<Long>()
                val category = Category(categoryMain, categoryMid, categorySub)
                val reviewList = mutableListOf<Review>()
                val orderCount = 0L
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val regDate = sdf.format(Date(System.currentTimeMillis()))

                val product = Product(
                    "", code, name, price.toLong(), mainImageFileName,
                    description, descImageFileName, mainActivity.loginSellerUid,
                    sizeList, colorList, hashTag, category, reviewList, orderCount, regDate
                )

                // 상품 정보 DB 저장
                ProductRepository.addProduct(product){
                    // 업로드 중 표시
                    progressBar.visibility = View.VISIBLE
                    // 이미지 업로드
                    ProductRepository.uploadImage(mainImageUri!!, mainImageFileName){
                        ProductRepository.uploadImage(descImageUri!!, descImageFileName){
                            Snackbar.make(fragmentProductAddBinding.root, "상품이 등록되었습니다.", Snackbar.LENGTH_SHORT).show()
                            // 상품등록 프레그먼드 제거하고 홈으로 이동
                            findNavController().popBackStack(R.id.item_product_add, true)
                            findNavController().navigate(R.id.item_home)
                        }
                    }
                }
            }
        }

        return fragmentProductAddBinding.root
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
                        descImageUri = uploadUri
                        fragmentProductAddBinding.buttonAddDescImage.text = "1/1"
                    }
                }
            }
        }

        return galleryLauncher
    }
}