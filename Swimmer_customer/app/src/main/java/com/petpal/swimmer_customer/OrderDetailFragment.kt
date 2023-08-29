package com.petpal.swimmer_customer

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.petpal.swimmer_customer.data.model.Order
import com.petpal.swimmer_customer.databinding.FragmentOrderDetailBinding
import com.petpal.swimmer_customer.ui.order.OrderViewModel
import com.petpal.swimmer_customer.ui.order.OrderViewModelFactory

class OrderDetailFragment : Fragment() {
    private lateinit var orderViewModel: OrderViewModel
    private var _fragmentOrderDetailBinding: FragmentOrderDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val fragmentOrderDetailBinding get() = _fragmentOrderDetailBinding!!
    private lateinit var order: Order

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _fragmentOrderDetailBinding = FragmentOrderDetailBinding.inflate(layoutInflater)

        //argument로 넘어온 order 받아오기
        order = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("order", Order::class.java)!!
        }else {
            arguments?.get("order") as Order
        }


        return fragmentOrderDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderViewModel =
            ViewModelProvider(this, OrderViewModelFactory())[OrderViewModel::class.java]
        Log.d("orderViewModel", orderViewModel.toString())
        orderViewModel.setOrder(order)

        orderViewModel.run {
            order.observe(viewLifecycleOwner) {
                //textView들 바뀜
            }
        }
    }

}