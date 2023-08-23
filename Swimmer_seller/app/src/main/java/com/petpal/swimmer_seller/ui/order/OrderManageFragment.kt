package com.petpal.swimmer_seller.ui.order

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.petpal.swimmer_seller.databinding.FragmentOrderManageBinding
import com.petpal.swimmer_seller.databinding.RowOrderBinding

class OrderManageFragment : Fragment() {
    private var _fragmentOrderManageBinding: FragmentOrderManageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val fragmentOrderManageBinding get() = _fragmentOrderManageBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentOrderManageBinding = FragmentOrderManageBinding.inflate(layoutInflater)
        return fragmentOrderManageBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentOrderManageBinding.run {
            recyclerViewOrder.run {
                adapter = OrderRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            }
        }
    }

    inner class OrderRecyclerViewAdapter:
        Adapter<OrderRecyclerViewAdapter.OrderRecyclerViewHolder>() {

        inner class OrderRecyclerViewHolder(rowOrderBinding: RowOrderBinding): ViewHolder(rowOrderBinding.root) {

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderRecyclerViewHolder {
            val rowOrderBinding = RowOrderBinding.inflate(layoutInflater)
            rowOrderBinding.root.layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            return OrderRecyclerViewHolder(rowOrderBinding)
        }

        override fun getItemCount(): Int {
            return 100
        }

        override fun onBindViewHolder(holder: OrderRecyclerViewHolder, position: Int) {

        }
    }

}

class RecyclerViewDecoration(private val divHeight: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = divHeight
    }
}