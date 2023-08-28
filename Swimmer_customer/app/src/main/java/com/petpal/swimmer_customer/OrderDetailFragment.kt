package com.petpal.swimmer_customer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.petpal.swimmer_customer.databinding.FragmentOrderDetailBinding
import com.petpal.swimmer_customer.ui.order.OrderViewModel
import com.petpal.swimmer_customer.ui.order.OrderViewModelFactory

class OrderDetailFragment : Fragment() {
    private lateinit var orderViewModel: OrderViewModel
    private var _fragmentOrderDetailBinding: FragmentOrderDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val fragmentOrderDetailBinding get() = _fragmentOrderDetailBinding!!
    private var orderIdx: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _fragmentOrderDetailBinding = FragmentOrderDetailBinding.inflate(layoutInflater)

        //argument로 넘어온 order 받아오기
        orderIdx = arguments?.getInt("orderIdx",-1)!!


        return fragmentOrderDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderViewModel =
            ViewModelProvider(this, OrderViewModelFactory())[OrderViewModel::class.java]
        orderViewModel.setOrderWithIdx(orderIdx)

        orderViewModel.run {
            order.observe(viewLifecycleOwner) {
                //textView들 바뀜
            }
        }
    }

}