package com.petpal.swimmer_seller.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.petpal.swimmer_seller.data.repository.OrderRepository
import com.petpal.swimmer_seller.data.repository.ProductRepository

// 뷰모델 팩토리
class HomeViewModelFactory(private val productRepository: ProductRepository, private val orderRepository: OrderRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(productRepository, orderRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}