package com.petpal.swimmer_customer.ui.deliverypointmanage

import android.app.AlertDialog
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.core.Context
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.data.model.Address
import com.petpal.swimmer_customer.databinding.FragmentDeliveryPointManageBinding
import com.petpal.swimmer_customer.util.NetworkStatus

class DeliveryPointManageFragment : Fragment() {

    lateinit var fragmentDeliveryPointManageBinding: FragmentDeliveryPointManageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        fragmentDeliveryPointManageBinding =
            FragmentDeliveryPointManageBinding.inflate(layoutInflater)

        // 받아온 데이터 처리
        val address = arguments?.getString("address")
        //val postcode= arguments?.getString("postcode")
        if(address!=null){
            val bundle = Bundle()
            bundle.putString("address", address)
            //bundle.putString("postcode",postcode)
            findNavController().navigate(R.id.DetailAddressFragment, bundle)
        }

//        //보냈다가 다시 돌아온 마지막 데이터
//        val addressUnit: Address? = arguments?.getSerializable("addressKey") as Address?
//        val name=addressUnit?.name
//        val finalAddress=addressUnit?.address
//        val phone=addressUnit?.phoneNumber
//
//        if (addressUnit != null) {
//            fragmentDeliveryPointManageBinding.textViewAddressName.text=name
//            fragmentDeliveryPointManageBinding.textViewAddress.text=finalAddress
//            fragmentDeliveryPointManageBinding.textViewAddressPhone.text=phone
//        } else {
//            Log.d("ReceivedData", "No data received")
//        }


        fragmentDeliveryPointManageBinding.toolbarDeliveryPointManage.run {
            title = getString(R.string.delivery_point_manage)
            setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener {
                //왜 popbackstack()하면 앱이 꺼지는가? 해결
                //findNavController().navigate(R.id.item_mypage)
                findNavController().popBackStack()
            }
        }

        fragmentDeliveryPointManageBinding.buttonFindAddress.setOnClickListener() {
            val status = NetworkStatus.getConnectivityStatus(requireContext())
            if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
                // Declare DialogFragment to launch address search web view
                findNavController().navigate(R.id.AddressDialogFragment)
            } else {
                Snackbar.make(
                    fragmentDeliveryPointManageBinding.root,
                    "인터넷 연결을 확인해주세요.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }



        return fragmentDeliveryPointManageBinding.root
    }

    override fun onResume() {
        super.onResume()
        //보냈다가 다시 돌아온 마지막 데이터

        val addressUnit = arguments?.getSerializable("addressKey") as Address?
        val name=addressUnit?.name
        val finalAddress=addressUnit?.address
        val phone=addressUnit?.phoneNumber
        if (name != null) {
            Log.d("testkoko12",name)
        }
        if (addressUnit != null) {
            fragmentDeliveryPointManageBinding.textViewAddressName.text=name
            fragmentDeliveryPointManageBinding.textViewAddress.text=finalAddress
            fragmentDeliveryPointManageBinding.textViewAddressPhone.text=phone
        } else {
            Log.d("ReceivedData", "No data received")
        }
    }
}
