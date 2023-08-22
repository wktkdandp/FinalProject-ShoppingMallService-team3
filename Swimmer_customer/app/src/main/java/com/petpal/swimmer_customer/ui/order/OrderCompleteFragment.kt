package com.petpal.swimmer_customer.ui.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.databinding.FragmentOrderCompleteBinding

class OrderCompleteFragment : Fragment() {

    lateinit var fragmentOrderCompleteBinding: FragmentOrderCompleteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentOrderCompleteBinding = FragmentOrderCompleteBinding.inflate(layoutInflater)

        fragmentOrderCompleteBinding.run {
            /*orderCompleteToolbar.run {
                title="주문 화면"
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                val navigationIcon = navigationIcon
                navigationIcon?.setTint(ContextCompat.getColor(context, R.color.black))
                setNavigationOnClickListener {
                    Navigation.findNavController(fragmentOrderCompleteBinding.root)
                        .navigate(R.id.action_orderCompleteFragment_to_item_home)
                }
            }*/
        }

        return fragmentOrderCompleteBinding.root
    }
}