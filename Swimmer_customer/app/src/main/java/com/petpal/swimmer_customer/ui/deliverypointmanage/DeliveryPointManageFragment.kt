package com.petpal.swimmer_customer.ui.deliverypointmanage

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.core.Context
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.data.model.Address
import com.petpal.swimmer_customer.data.repository.CustomerUserRepository
import com.petpal.swimmer_customer.databinding.FragmentDeliveryPointManageBinding
import com.petpal.swimmer_customer.databinding.FragmentMainBinding
import com.petpal.swimmer_customer.databinding.ItemDeliveryPointBinding
import com.petpal.swimmer_customer.util.NetworkStatus

//배송지 목록을 리사이클러뷰로 띄우고 관리하는 배송지 관리 프래그먼트

class DeliveryPointManageFragment : Fragment() {

    lateinit var fragmentDeliveryPointManageBinding: FragmentDeliveryPointManageBinding
    lateinit var viewModel:DeliveryPointManageViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentDeliveryPointManageBinding = FragmentDeliveryPointManageBinding.inflate(layoutInflater)
        // 받아온 데이터 처리
        handleReceivedData()
        setupViewModel()
        setupRecyclerView()
        setupFindAddressButton()
        fetchUserAddresses()

        return fragmentDeliveryPointManageBinding.root
    }
    private fun handleReceivedData() {
        val address = arguments?.getString("address")
        val postcode= arguments?.getString("postcode")
        if(address!=null){
            val bundle = Bundle()
            bundle.putString("address", address)
            bundle.putString("postcode",postcode)
            findNavController().navigate(R.id.DetailAddressFragment, bundle)
        }

        val argument = arguments?.getString("FromOrder")
        if (argument?.equals("FromOrder") == true) {
            (fragmentDeliveryPointManageBinding.recyclerViewDeliveryPoint.adapter as RecyclerViewAdapter).setOnItemClickListener { selectedAddress ->
                val bundle = Bundle().apply {
                    putString("name", selectedAddress.name)
                    putString("address", selectedAddress.address)
                    putString("phoneNumber", selectedAddress.phoneNumber)
                }
                findNavController().navigate(R.id.paymentFragment, bundle)
            }
        }
    }
    private fun setupToolbar() {
        fragmentDeliveryPointManageBinding.toolbarDeliveryPointManage.run {
            title = getString(R.string.delivery_point_manage)
            setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener {
                findNavController().navigate(R.id.item_mypage)
            }
        }
    }
    private fun setupRecyclerView() {
        fragmentDeliveryPointManageBinding.recyclerViewDeliveryPoint.run {
            adapter = RecyclerViewAdapter()
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
    private fun setupFindAddressButton() {
        setupToolbar()
        fragmentDeliveryPointManageBinding.buttonFindAddress.setOnClickListener() {
            val status = NetworkStatus.getConnectivityStatus(requireContext())
            if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
                findNavController().navigate(R.id.AddressDialogFragment)
            } else {
                Snackbar.make(
                    fragmentDeliveryPointManageBinding.root,
                    "인터넷 연결을 확인해주세요.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun fetchUserAddresses() {
        val currentUserUID = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserUID != null) {
            viewModel.fetchAddressesForUser(currentUserUID)
        }
    }
    private fun setupViewModel(){

        val factory = DeliveryPointManageModelFactory(CustomerUserRepository())
        viewModel = ViewModelProvider(this, factory).get(DeliveryPointManageViewModel::class.java)

        viewModel.addresses.observe(viewLifecycleOwner, Observer { addresses ->
            // RecyclerView 데이터 업데이트
            (fragmentDeliveryPointManageBinding.recyclerViewDeliveryPoint.adapter as RecyclerViewAdapter).submitAddresses(addresses)
        })

        viewModel.deleteResult.observe(viewLifecycleOwner, Observer { result ->
            if (result == true) {
                Snackbar.make(fragmentDeliveryPointManageBinding.root, "배송지가 삭제되었습니다.", Snackbar.LENGTH_SHORT).show()
                // Optionally refresh the list or remove the deleted item from the RecyclerView
                val currentUserUID = FirebaseAuth.getInstance().currentUser?.uid
                if (currentUserUID != null) {
                    viewModel.fetchAddressesForUser(currentUserUID)
                }
            } else {
                Snackbar.make(fragmentDeliveryPointManageBinding.root, "배송지 삭제에 실패하였습니다.", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderClass>(){
        var addresses = listOf<Address>()
        var currentlyCheckedPosition= -1
        var onItemClickListener:((Address)->Unit)?=null
        fun submitAddresses(newAddresses: List<Address>) {
            addresses = newAddresses
            notifyDataSetChanged()
        }
        @JvmName("setOnItemClickListenerFunction")
        fun setOnItemClickListener(listener: (Address) ->Unit){
            onItemClickListener=listener
        }
        inner class ViewHolderClass(rowBinding: ItemDeliveryPointBinding) : RecyclerView.ViewHolder(rowBinding.root){


            val textViewAddressName: TextView
            val textViewIsDefaultDeliveryPoint:TextView
            val textViewAddress: TextView
            val checkboxDefaultDeliveryPoint: CheckBox
            val textViewAddressPhone: TextView
            val buttonDeleteDeliveryPoint: Button

            init{
                textViewAddressName=rowBinding.textViewAddressName
                textViewAddress=rowBinding.textViewAddress
                textViewIsDefaultDeliveryPoint=rowBinding.textViewIsDefaultDeliveryPoint
                checkboxDefaultDeliveryPoint=rowBinding.checkboxDefaultDeliveryPoint
                textViewAddressPhone=rowBinding.textViewAddressPhone
                buttonDeleteDeliveryPoint=rowBinding.ButtonDeleteDeliveryPoint
                rowBinding.root.setOnClickListener {
                    onItemClickListener?.invoke(addresses[adapterPosition])
                }
                buttonDeleteDeliveryPoint.setOnClickListener {

                    val addressToDelete = addresses[adapterPosition]

                    val currentUserUID = FirebaseAuth.getInstance().currentUser?.uid
                    if (currentUserUID != null) {
                        // Call the ViewModel's delete function
                        addressToDelete.addressIdx?.let { it1 ->
                            viewModel.deleteAddressForUser(currentUserUID, it1)
                        }
                    }
                }
                checkboxDefaultDeliveryPoint.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        if (currentlyCheckedPosition != adapterPosition) {
                            notifyItemChanged(currentlyCheckedPosition)
                            currentlyCheckedPosition = adapterPosition
                        }
                    } else if (currentlyCheckedPosition == adapterPosition) {
                        currentlyCheckedPosition = -1
                    }
                    textViewIsDefaultDeliveryPoint.visibility = if (isChecked) View.VISIBLE else View.GONE
                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {

            val rowBinding = ItemDeliveryPointBinding.inflate(layoutInflater)
            val viewHolderClass = ViewHolderClass(rowBinding)

            val params = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
            rowBinding.root.layoutParams = params

            return viewHolderClass
        }

        override fun getItemCount(): Int {
            return addresses.size

        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
            val currentAddress = addresses[position]
            holder.textViewAddress.text = currentAddress.address
            holder.textViewAddressName.text = currentAddress.name
            holder.textViewAddressPhone.text = getString(R.string.delivery_phone_is)+currentAddress.phoneNumber
            holder.checkboxDefaultDeliveryPoint.isChecked = position == currentlyCheckedPosition
        }
    }
}
class DeliveryPointManageModelFactory(private val repository: CustomerUserRepository) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(DeliveryPointManageViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return DeliveryPointManageViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
    }
}
