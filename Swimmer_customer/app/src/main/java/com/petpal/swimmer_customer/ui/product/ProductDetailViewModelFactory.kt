package com.petpal.swimmer_customer.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProductDetailViewModelFactory (private val idx: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(idx) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}