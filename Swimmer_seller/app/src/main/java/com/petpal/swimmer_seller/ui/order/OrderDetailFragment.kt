package com.petpal.swimmer_seller.ui.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.petpal.swimmer_seller.databinding.FragmentOrderDetailBinding
import com.petpal.swimmer_seller.databinding.RowOrderItemBinding

class OrderDetailFragment : Fragment() {
    private lateinit var orderViewModel: OrderViewModel
    private var _fragmentOrderDetailBinding: FragmentOrderDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val fragmentOrderDetailBinding get() = _fragmentOrderDetailBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentOrderDetailBinding = FragmentOrderDetailBinding.inflate(layoutInflater)
        return fragmentOrderDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderViewModel = ViewModelProvider(this, OrderViewModelFactory())[OrderViewModel::class.java]

        fragmentOrderDetailBinding.run {
            reyclerViewOrderProductList.run {
                adapter = OrderItemRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(MaterialDividerItemDecoration(context, MaterialDividerItemDecoration.VERTICAL))
            }
            toolbarOrderDetail.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    inner class OrderItemRecyclerViewAdapter:
        Adapter<OrderItemRecyclerViewAdapter.OrderItemViewHolder>() {
        inner class OrderItemViewHolder(rowOrderItemBinding: RowOrderItemBinding):ViewHolder(rowOrderItemBinding.root) {
            val textViewOrderProductUid = rowOrderItemBinding.textViewOrderProductUid
            val textViewOrderProductCount = rowOrderItemBinding.textViewOrderProductCount
            val textViewOrderProductName = rowOrderItemBinding.textViewOrderProductName
            val textViewOrderProductOption = rowOrderItemBinding.textViewOrderProductOption
            val textViewOrderProductPrice = rowOrderItemBinding.textViewOrderProductPrice
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
            val rowOrderItemBinding = RowOrderItemBinding.inflate(layoutInflater)
            rowOrderItemBinding.root.layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
            return OrderItemViewHolder(rowOrderItemBinding)
        }

        override fun getItemCount(): Int {
            return 100
        }

        override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
            holder.textViewOrderProductUid.text = position.toString()

        }
    }

}
