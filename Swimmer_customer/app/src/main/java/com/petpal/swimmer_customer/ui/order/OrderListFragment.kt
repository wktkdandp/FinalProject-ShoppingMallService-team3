package com.petpal.swimmer_customer.ui.order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.data.model.getOrderState
import com.petpal.swimmer_customer.databinding.FragmentOrderListBinding
import com.petpal.swimmer_customer.databinding.RowOrderBinding

class OrderListFragment : Fragment() {
    private lateinit var orderViewModel: OrderViewModel
    private var _fragmentOrderListBinding: FragmentOrderListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val fragmentOrderListBinding get() = _fragmentOrderListBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _fragmentOrderListBinding = FragmentOrderListBinding.inflate(layoutInflater)
        orderViewModel =
            ViewModelProvider(this, OrderViewModelFactory())[OrderViewModel::class.java]
        Log.d("orderViewMode", orderViewModel.toString())

        handleBackPress()

        fragmentOrderListBinding.run {
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

                override fun onTabSelected(tab: TabLayout.Tab) {

                    when (tab.position) {
                        0 -> {
                            orderViewModel.getOrderByUserUid(
                                Firebase.auth.currentUser!!.uid
//            Firebase.auth.currentUser!!.uid
                            )
                        }

                        1 -> {
                            orderViewModel.getOrderByState(Firebase.auth.currentUser!!.uid, 1)

                        }

                        2 -> {
                            orderViewModel.getOrderByState(Firebase.auth.currentUser!!.uid, 3)
                        }

                        3 -> {
                            orderViewModel.getOrderByState(Firebase.auth.currentUser!!.uid, 4)
                        }

                        4 -> {
                            orderViewModel.getOrderByState(Firebase.auth.currentUser!!.uid, 5, 6, 7)
                        }
                    }
                }

                override fun onTabReselected(tab: TabLayout.Tab) {
                    // Handle tab reselect
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    // Handle tab unselect
                }
            })
        }

        return fragmentOrderListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //이 판매자가 올린 상품에 대한 주문 목록만 세팅하기
        orderViewModel.getOrderByUserUid(
            Firebase.auth.currentUser!!.uid
//            Firebase.auth.currentUser!!.uid
        )

        orderViewModel.run {
            orderList.observe(viewLifecycleOwner) {
                fragmentOrderListBinding.recyclerViewOrderList.adapter?.notifyDataSetChanged()
            }
        }

        fragmentOrderListBinding.run {

            recyclerViewOrderList.run {
                adapter = OrderListRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(
                    MaterialDividerItemDecoration(
                        context,
                        MaterialDividerItemDecoration.VERTICAL
                    )
                )
            }

            toolbarOrderList.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

        }
    }

/*    private fun FragmentOrderListBinding.setVisibility() {
        if (orderViewModel.orderList.value!!.isEmpty()) {
            linearLayoutNoOrder.visibility = View.VISIBLE
            linearLayoutRecyclerView.visibility = View.GONE
        } else {
            linearLayoutNoOrder.visibility = View.GONE
            linearLayoutRecyclerView.visibility = View.VISIBLE
        }
    }*/
private fun handleBackPress() {
    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().popBackStack()
        }
    }
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
}

    inner class OrderListRecyclerViewAdapter :
        Adapter<OrderListRecyclerViewAdapter.OrderListViewHolder>() {
        inner class OrderListViewHolder(rowOrderBinding: RowOrderBinding) :
            ViewHolder(rowOrderBinding.root) {
            val textViewOrderState = rowOrderBinding.textViewOrderState
            val textViewOrderNum = rowOrderBinding.textViewOrderNum
            val textViewOrderDate = rowOrderBinding.textViewOrderDate
            val imageView = rowOrderBinding.imageView2
            val textViewOrderProducts = rowOrderBinding.textViewOrderProducts
            val textViewOrderOption = rowOrderBinding.textViewOrderOption
            val textViewOrderPrice = rowOrderBinding.textViewOrderPrice

            init {
                rowOrderBinding.root.setOnClickListener {
                    //해당 주문상세로 넘어가기
                    val bundle = Bundle()
                    bundle.putParcelable("order", orderViewModel.orderList.value?.get(adapterPosition))
                    findNavController().navigate(R.id.action_orderListFragment_to_orderDetailFragment, bundle)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListViewHolder {
            val rowOrderBinding = RowOrderBinding.inflate(layoutInflater)
            rowOrderBinding.root.layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
            return OrderListViewHolder(rowOrderBinding)
        }

        override fun getItemCount(): Int {
            return orderViewModel.orderList.value?.size!!
        }

        override fun onBindViewHolder(holder: OrderListViewHolder, position: Int) {
            holder.textViewOrderState.text =
                getOrderState(orderViewModel.orderList.value!![position].state).str
            holder.textViewOrderNum.text = orderViewModel.orderList.value!![position].orderUid
            holder.textViewOrderDate.text = orderViewModel.orderList.value!![position].orderDate
//            holder.imageView.setImageBitmap(orderViewModel.orderList.value!![position].itemList[0].mainImage)
            holder.textViewOrderProducts.text =
                "${orderViewModel.orderList.value!![position].itemList[0].name} 외 ${orderViewModel.orderList.value!![position].itemList.size - 1}건"
            holder.textViewOrderOption.text =
                "${orderViewModel.orderList.value!![position].itemList[0].color}/${orderViewModel.orderList.value!![position].itemList[0].size}"
            holder.textViewOrderPrice.text =
                orderViewModel.orderList.value!![position].totalPrice.toString()
        }
    }

}