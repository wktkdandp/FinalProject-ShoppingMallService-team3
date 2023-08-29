package com.petpal.swimmer_customer.ui.order

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.petpal.swimmer_customer.data.model.Order
import com.petpal.swimmer_customer.data.model.OrderState
import com.petpal.swimmer_customer.data.model.getOrderState
import com.petpal.swimmer_customer.databinding.FragmentOrderDetailBinding
import com.petpal.swimmer_customer.databinding.RowOrderDetailBinding

class OrderDetailFragment : Fragment() {
    private lateinit var orderViewModel: OrderViewModel
    private var _fragmentOrderDetailBinding: FragmentOrderDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val fragmentOrderDetailBinding get() = _fragmentOrderDetailBinding!!
    private lateinit var orderData: Order

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _fragmentOrderDetailBinding = FragmentOrderDetailBinding.inflate(layoutInflater)

        //argument로 넘어온 order 받아오기
        orderData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("order", Order::class.java)!!
        } else {
            arguments?.get("order") as Order
        }


        return fragmentOrderDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderViewModel =
            ViewModelProvider(this, OrderViewModelFactory())[OrderViewModel::class.java]
        Log.d("orderViewModel", orderViewModel.toString())
        orderViewModel.setOrder(orderData)

        orderViewModel.run {
            order.observe(viewLifecycleOwner) {
                //textView들 바뀜
                fragmentOrderDetailBinding.textViewOrderDetailState.text =
                    getOrderState(it.state).str
                fragmentOrderDetailBinding.textViewOrderDetailNum.text = it.orderUid
                fragmentOrderDetailBinding.textViewOrderDetailDate.text = it.orderDate
                //TODO: 일단 id박아주기
                fragmentOrderDetailBinding.textViewOrderDetailReceiverName.text = it.userUid
                fragmentOrderDetailBinding.textViewOrderDetailReceiverContact.text = it.userUid
                fragmentOrderDetailBinding.textViewOrderAddress.text = it.address
                fragmentOrderDetailBinding.textViewOrderDetailMessage.text = it.message

                fragmentOrderDetailBinding.textViewOrderDetailPrice.text = it.totalPrice.toString()
                //TODO: 적절한 paymethod로 text넣어주기
                fragmentOrderDetailBinding.textViewOrderDetailPayment.text = it.payMethod.toString()
                fragmentOrderDetailBinding.textViewOrderDetailPaid.text = it.totalPrice.toString()

                when (getOrderState(it.state)) {
                    OrderState.PAYMENT -> {
                        fragmentOrderDetailBinding.buttonCancel.visibility = View.VISIBLE
                        fragmentOrderDetailBinding.buttonChange.visibility = View.GONE
                        fragmentOrderDetailBinding.buttonReturn.visibility = View.GONE
                    }
                    OrderState.READY, OrderState.DELIVERY, OrderState.COMPLETE -> {
                        fragmentOrderDetailBinding.buttonCancel.visibility = View.GONE
                        fragmentOrderDetailBinding.buttonChange.visibility = View.VISIBLE
                        fragmentOrderDetailBinding.buttonReturn.visibility = View.VISIBLE
                    }
                    OrderState.CANCEL, OrderState.EXCHANGE, OrderState.REFUND -> {
                        fragmentOrderDetailBinding.buttonCancel.visibility = View.GONE
                        fragmentOrderDetailBinding.buttonChange.visibility = View.GONE
                        fragmentOrderDetailBinding.buttonReturn.visibility = View.GONE
                    }
                }
            }
        }

        fragmentOrderDetailBinding.run {
            toolbarOrderList2.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            recyclerViewOrderDetail.run {
                adapter = ItemRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(
                    MaterialDividerItemDecoration(
                        context,
                        MaterialDividerItemDecoration.VERTICAL
                    )
                )
            }
        }
    }

    inner class ItemRecyclerViewAdapter : Adapter<ItemRecyclerViewAdapter.ItemViewHolder>() {
        inner class ItemViewHolder(rowOrderDetailBinding: RowOrderDetailBinding) :
            ViewHolder(rowOrderDetailBinding.root) {
            val image = rowOrderDetailBinding.imageView4
            val itemName = rowOrderDetailBinding.textViewOrderDetailName
            val itemOption = rowOrderDetailBinding.textViewOrderDetailOption
            val itemCount = rowOrderDetailBinding.textViewOrderDetailCount
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val rowOrderDetailBinding = RowOrderDetailBinding.inflate(layoutInflater)
            rowOrderDetailBinding.root.layoutParams = ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            return ItemViewHolder(rowOrderDetailBinding)
        }

        override fun getItemCount(): Int {
            return orderViewModel.order.value!!.itemList.size
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            //TODO: 이미지 넣어주기
//            holder.image.setImageBitmap(orderViewModel.order.value!!.itemList[position].mainImage)
            holder.itemName.text = orderViewModel.order.value!!.itemList[position].name
            holder.itemOption.text =
                "${orderViewModel.order.value!!.itemList[position].color}, ${orderViewModel.order.value!!.itemList[position].size}"
            holder.itemCount.text = "${orderViewModel.order.value!!.itemList[position].quantity}개"
        }
    }

}