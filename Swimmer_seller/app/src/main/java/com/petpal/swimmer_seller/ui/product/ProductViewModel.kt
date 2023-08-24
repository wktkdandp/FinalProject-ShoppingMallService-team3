package com.petpal.swimmer_seller.ui.product

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_seller.data.model.Image
import com.petpal.swimmer_seller.data.model.Product
import com.petpal.swimmer_seller.data.repository.ProductRepository

class ProductViewModel(private val productRepository: ProductRepository):ViewModel() {

    private val _sizeList = MutableLiveData<List<String>>()
    val sizeList: LiveData<List<String>> = _sizeList

    private val _colorList = MutableLiveData<List<String>>()
    val colorList:LiveData<List<String>> = _colorList

    // color - size 표시용
    private val _optionPairList = MutableLiveData<List<Pair<String, String>>>()
    val optionPairList: LiveData<List<Pair<String, String>>> = _optionPairList

    init {
        _sizeList.value = mutableListOf()
        _colorList.value = mutableListOf()
        _optionPairList.value = mutableListOf<Pair<String, String>>()
    }

    // colorList, sizeList를 기반으로 옵션 세트 매칭 -> RecyclerView 표시용
    fun setOptionPairList(){

    }

    fun addProduct(product: Product){
        productRepository.addProduct(product){}
    }

    fun uploadImage(uri: Uri, fileName: String) {
        productRepository.uploadImage(uri, fileName){}
    }

    fun uploadImageList(images: MutableList<Image>){
        for (image in images){
            productRepository.uploadImage(image.uri, image.fileName){}
        }
    }
}

