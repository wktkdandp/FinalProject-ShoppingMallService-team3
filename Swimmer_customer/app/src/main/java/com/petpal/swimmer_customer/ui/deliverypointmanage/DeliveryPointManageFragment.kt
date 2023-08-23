package com.petpal.swimmer_customer.ui.deliverypointmanage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.databinding.FragmentDeliveryPointManageBinding

class DeliveryPointManageFragment : Fragment() {

    lateinit var fragmentDeliveryPointManageBinding: FragmentDeliveryPointManageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentDeliveryPointManageBinding= FragmentDeliveryPointManageBinding.inflate(layoutInflater)




        fragmentDeliveryPointManageBinding.toolbarDeliveryPointManage.run{
            title = getString(R.string.delivery_point_manage)
            setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener {
                //왜 popbackstack()하면 앱이 꺼지는가? 해결
                //findNavController().navigate(R.id.item_mypage)
                findNavController().popBackStack()
            }
        }

        fragmentDeliveryPointManageBinding.buttonFindAddress.setOnClickListener {

        }



        return fragmentDeliveryPointManageBinding.root
    }


}