package com.petpal.swimmer_seller.ui.product

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_seller.data.model.Image
import com.petpal.swimmer_seller.data.model.Product
import com.petpal.swimmer_seller.data.repository.ProductRepository

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {
    private val _productList =  MutableLiveData<MutableList<Product>>()
    val productList : LiveData<MutableList<Product>> = _productList

    private val _productCount = MutableLiveData<Long>()
    val productCount: LiveData<Long> = _productCount

    private val _product = MutableLiveData<Product>()
    val currentProduct: LiveData<Product> = _product

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

    fun getProduct(productUid: String){
        productRepository.getProductByProductUid(productUid){

        }
    }

    // 판매자가 등록한 상품 리스트 (데이터 표시용)
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

    fun uploadImage(image: Image) {
        val uri = Uri.parse(image.uriString)
        productRepository.uploadImage(uri, image.fileName!!) {}
    }

    fun uploadImageList(images: Array<Image>) {
        for (image in images) {
            val uri = Uri.parse(image.uriString)
            productRepository.uploadImage(uri, image.fileName!!) {}
        }
    }
}

