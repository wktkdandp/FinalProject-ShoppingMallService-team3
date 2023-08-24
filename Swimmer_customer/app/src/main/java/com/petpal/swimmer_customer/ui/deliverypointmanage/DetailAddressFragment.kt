package com.petpal.swimmer_customer.ui.deliverypointmanage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.data.model.Address
import com.petpal.swimmer_customer.databinding.FragmentDetailAddressBinding

class DetailAddressFragment : Fragment() {
    lateinit var fragmentDetailAddressBinding: FragmentDetailAddressBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentDetailAddressBinding= FragmentDetailAddressBinding.inflate(layoutInflater)

        // 받아온 데이터 처리
        val address = arguments?.getString("address")
        val postcode= arguments?.getString("postcode")


        fragmentDetailAddressBinding.toolbarDetailAddress.run{
            title = getString(R.string.detail_address_toolbar)
            setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
        fragmentDetailAddressBinding.run{
            textView2.text=address
            //recylcerview에 ui맞춰서 정보 뿌리기
            ButtonSubmitAddress.setOnClickListener {
                val bundle=Bundle()
                val name=textInputEditDetailAddressName.text.toString()
                val detailAddress=textInputEditDetailAddress.text.toString()
                val phone=textInputEditDetailAddressPhone.text.toString()
                val address1=Address(null, postcode?.toLong(),"$address $detailAddress",name,phone)
                Log.d("testkoko",address1.name!!)
                bundle.putSerializable("addressKey",address1)
                findNavController().navigate(R.id.DeliveryPointManageFragment,bundle)
            }

        }

        return fragmentDetailAddressBinding.root
    }


}