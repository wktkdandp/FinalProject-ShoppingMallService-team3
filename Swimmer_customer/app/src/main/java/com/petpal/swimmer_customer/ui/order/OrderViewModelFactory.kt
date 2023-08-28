package com.petpal.swimmer_customer.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.petpal.swimmer_customer.data.repository.OrderRepository

class OrderViewModelFactory : ViewModelProvider.Factory {

    //Unchecked cast 경고 무시
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            return OrderViewModel(OrderRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}