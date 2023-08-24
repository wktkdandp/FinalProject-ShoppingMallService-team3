package com.petpal.swimmer_seller.ui.product

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_seller.data.model.Image
import com.petpal.swimmer_seller.data.model.Product
import com.petpal.swimmer_seller.data.repository.ProductRepository

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val _sizeList = MutableLiveData<MutableList<String>>()
    val sizeList: LiveData<MutableList<String>> = _sizeList

    private val _colorList = MutableLiveData<MutableList<String>>()
    val colorList: LiveData<MutableList<String>> = _colorList

    init {
        _sizeList.value = mutableListOf()
        _colorList.value = mutableListOf()
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

    fun uploadImage(uri: Uri, fileName: String) {
        productRepository.uploadImage(uri, fileName) {}
    }

    fun uploadImageList(images: MutableList<Image>) {
        for (image in images) {
            productRepository.uploadImage(image.uri, image.fileName) {}
        }
    }
}

