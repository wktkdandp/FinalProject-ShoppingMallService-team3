package com.petpal.swimmer_seller.ui.order

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.petpal.swimmer_seller.data.model.Order
import com.petpal.swimmer_seller.data.model.getOrderState
import com.petpal.swimmer_seller.databinding.FragmentOrderDetailBinding
import com.petpal.swimmer_seller.databinding.RowOrderItemBinding

class OrderDetailFragment : Fragment() {
    private lateinit var orderViewModel: OrderViewModel
    private var _fragmentOrderDetailBinding: FragmentOrderDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val fragmentOrderDetailBinding get() = _fragmentOrderDetailBinding!!
    private lateinit var order: Order

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentOrderDetailBinding = FragmentOrderDetailBinding.inflate(layoutInflater)

        //argument로 넘어온 order 받아오기
        order = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("order", Order::class.java)!!
        } else {
            arguments?.getParcelable("order")!!
        }

        return fragmentOrderDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderViewModel =
            ViewModelProvider(this, OrderViewModelFactory())[OrderViewModel::class.java]

        fragmentOrderDetailBinding.run {
            reyclerViewOrderProductList.run {
                adapter = OrderItemRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(
                    MaterialDividerItemDecoration(
                        context,
                        MaterialDividerItemDecoration.VERTICAL
                    )
                )
            }
            toolbarOrderDetail.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            textViewOrderState2.text = getOrderState(order.state).str
            textViewOrderUid.text = order.orderUid
            textViewOrderDate.text = order.orderDate
            //TODO: UserID 도 필요
//            textViewOrderUserId.text = order.userUid
            //TODO: UserID로 userContact도 필요
//            textViewOrderUserContact.text = order.userContact
            textViewOrderTotalCount.text = "${order.itemList.size}개"
            textViewOrderTotalPrice.text = "${orderViewModel.calculateTotalPrice(order.itemList)}원"
            //TODO: 수령인도 필요 or userId로 userName으로 대체
//            textViewOrderReceiver.text = order.receiver
//            textViewOrderReceiverContact.text = order.receiverContact
            //TODO: Address 클래스로 변경 필요
            textViewOrderShippingPostCode.text = order.address
            textViewOrderShippingAddress1.text = order.address
            textViewOrderShippingAddress2.text = order.address
            textViewOrderShippingMessage.text = order.message
        }

    }

    inner class OrderItemRecyclerViewAdapter :
        Adapter<OrderItemRecyclerViewAdapter.OrderItemViewHolder>() {
        inner class OrderItemViewHolder(rowOrderItemBinding: RowOrderItemBinding) :
            ViewHolder(rowOrderItemBinding.root) {
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
            return order.itemList.size
        }

        override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
            holder.textViewOrderProductUid.text = order.itemList[position].productUid
            holder.textViewOrderProductCount.text = "${order.itemList[position].quantity}개"
            holder.textViewOrderProductName.text = order.itemList[position].name
            holder.textViewOrderProductOption.text = "${order.itemList[position].color}, ${order.itemList[position].size}"
            holder.textViewOrderProductPrice.text = "${order.itemList[position].price}원"

        }
    }

}
