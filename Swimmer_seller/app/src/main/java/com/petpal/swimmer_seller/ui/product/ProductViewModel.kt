package com.petpal.swimmer_seller.ui.product

import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.petpal.swimmer_seller.R
import com.petpal.swimmer_seller.data.model.Image
import com.petpal.swimmer_seller.data.model.Product
import com.petpal.swimmer_seller.data.repository.ProductRepository

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {
    private val _productList =  MutableLiveData<MutableList<Product>>()
    val productList : LiveData<MutableList<Product>> = _productList

    private val _productCount = MutableLiveData<Long>()
    val productCount: LiveData<Long> = _productCount

    private val _currentProduct = MutableLiveData<Product>()
    val currentProduct: LiveData<Product> = _currentProduct

    private val _sizeList = MutableLiveData<MutableList<String>>()
    val sizeList: LiveData<MutableList<String>> = _sizeList

    private val _colorList = MutableLiveData<MutableList<String>>()
    val colorList: LiveData<MutableList<String>> = _colorList

    init {
        _productList.value = mutableListOf()
        _productCount.value = 0L
        _sizeList.value = mutableListOf()
        _colorList.value = mutableListOf()
    }

    // 상품 정보 가져오기 (상품 상세, 수정용)
    fun getProduct(productUid: String){
        productRepository.getProductByProductUid(productUid){
            if (it.result.exists()) {
                val product = it.result.getValue(Product::class.java)
                if (product != null) {
                    _currentProduct.value = product!!
                }
            }
        }
    }

    // 판매자가 등록한 상품 리스트 가져오기 (상품 목록용)
    fun getAllProductBySellerUid(sellerUid: String){
        productRepository.getProductBySellerUid(sellerUid){
            val tempProductList = mutableListOf<Product>()

            for (productSnapshot in it.result.children) {
                val product = productSnapshot.getValue(Product::class.java)
                if (product != null) {
                    tempProductList.add(product)
                }
            }
            _productList.value = tempProductList
        }
    }

    // 로그인 판매자가 등록한 상품 건 수 가져오기 (빠른 카운팅용)
    fun getProductCount(sellerUid: String){
        productRepository.getProductBySellerUid(sellerUid){
            _productCount.value = it.result.childrenCount
        }
    }

    // 색상, 사이즈 리스트 값 저장
    fun addColorAndSizeOption(colorList: List<String>, sizeList: List<String>) {
        // 기존 리스트에 추가 후 중복 제거
        val newColorList = this.colorList.value
        newColorList?.addAll(colorList.filter { !newColorList.contains(it) })
        _colorList.value = newColorList!!

        val newSizeList = this.sizeList.value
        newSizeList?.addAll(sizeList.filter { !newSizeList.contains(it) })
         _sizeList.value = newSizeList!!
    }

    // RecyclerView에서 삭제 버튼을 누른 순번의 옵션 제거
    fun deleteColorOption(colorIdx: Int) {
        val newColorList = this.colorList.value
        newColorList?.removeAt(colorIdx)
        _colorList.value = newColorList!!
    }
    fun deleteSizeOption(sizeIdx: Int) {
        val newSizeList = this.sizeList.value
        newSizeList?.removeAt(sizeIdx)
        _sizeList.value = newSizeList!!
    }

    fun addProduct(product: Product) {
        productRepository.addProduct(product) {}
    }

    // Storage 이미지 업로드
    fun uploadImage(image: Image) {
        val uri = Uri.parse(image.uriString)
        productRepository.uploadImage(uri, image.fileName!!) {}
    }

    // Storage 이미지 여러 개 업로드
    fun uploadImageList(images: Array<Image>) {
        for (image in images) {
            val uri = Uri.parse(image.uriString)
            productRepository.uploadImage(uri, image.fileName!!) {}
        }
    }

    // Storage 이미지 ImageView에 매칭 (Glide 라이브러리)
    fun loadAndDisplayImage(storagePath: String, imageView: ImageView) {
        productRepository.downloadImage(storagePath){ task ->
            if (task.isSuccessful) {
                val imageUrl = task.result
                Glide.with(imageView.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.loading_placeholder)
                    .fitCenter()
                    .into(imageView)
            } else {
                Toast.makeText(imageView.context, "서버로 부터 이미지를 가져오지 못했습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

