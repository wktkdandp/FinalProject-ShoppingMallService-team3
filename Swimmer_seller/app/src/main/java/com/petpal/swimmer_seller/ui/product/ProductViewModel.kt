package com.petpal.swimmer_seller.ui.product

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_seller.data.model.Image
import com.petpal.swimmer_seller.data.model.Product
import com.petpal.swimmer_seller.data.repository.ProductRepository

class ProductViewModel(private val productRepository: ProductRepository):ViewModel() {

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

