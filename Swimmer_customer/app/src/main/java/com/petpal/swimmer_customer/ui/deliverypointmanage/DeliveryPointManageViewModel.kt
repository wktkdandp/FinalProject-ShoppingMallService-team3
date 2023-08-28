package com.petpal.swimmer_customer.ui.deliverypointmanage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_customer.data.model.Address
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository

class DeliveryPointManageViewModel(private val repository: CustomerUserRepository) : ViewModel() {

    // MutableLiveData to hold addresses
    private val _addresses = MutableLiveData<List<Address>>()
    val addresses: LiveData<List<Address>>
        get() = _addresses

    private val _deleteResult = MutableLiveData<Boolean?>()
    val deleteResult: LiveData<Boolean?>
        get() = _deleteResult

    // Fetch addresses for a user
    fun fetchAddressesForUser(uid: String) {
        val addressLiveData = repository.getAllAddressesForUser(uid)
        addressLiveData.observeForever { addresses ->
            _addresses.value = addresses
        }
    }
    fun deleteAddressForUser(uid: String, addressIdx: String) {
        val deleteLiveData = repository.deleteAddressForUser(uid, addressIdx)
        deleteLiveData.observeForever { result ->
            _deleteResult.value = result
        }
    }
}