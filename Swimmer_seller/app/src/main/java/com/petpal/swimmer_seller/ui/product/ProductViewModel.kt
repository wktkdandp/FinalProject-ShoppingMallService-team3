package com.petpal.swimmer_seller.ui.product

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_seller.data.model.Product
import com.petpal.swimmer_seller.data.repository.ProductRepository

class ProductViewModel(private val productRepository: ProductRepository):ViewModel() {

    fun addProduct(product: Product){
        productRepository.addProduct(product){}
    }

    fun uploadImages(images: MutableMap<Uri, String>){
        for (image in images){
            productRepository.uploadImage(image.key, image.value){}
        }
    }

}

