package com.petpal.swimmer_customer.ui.payment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.data.model.ItemsForCustomer
import com.petpal.swimmer_customer.databinding.FragmentPaymentBinding
import com.petpal.swimmer_customer.databinding.PaymentItemRowBinding
import com.petpal.swimmer_customer.ui.main.MainActivity
import com.petpal.swimmer_customer.ui.payment.repository.PaymentRepository
import com.petpal.swimmer_customer.ui.payment.vm.PaymentViewModel
import org.w3c.dom.Text

class PaymentFragment : Fragment() {

    lateinit var fragmentPaymentBinding: FragmentPaymentBinding
    lateinit var mainActivity: MainActivity
    lateinit var paymentViewModel: PaymentViewModel

    // spinner array
    val spinnerList = arrayOf("베송 메세지 선택","안전한 배송 부탁드려요.","빠른 배송 부탁드려요.","부재 시 전화주세요.","문 앞에 배송 부탁드려요.")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentPaymentBinding = FragmentPaymentBinding.inflate(inflater)
        mainActivity = activity as MainActivity
        paymentViewModel = ViewModelProvider(mainActivity)[PaymentViewModel::class.java]

        paymentViewModel.run {
            itemList.observe(mainActivity) {
                fragmentPaymentBinding.paymentViewPager.adapter?.notifyDataSetChanged()
            }
            paymentFee.observe(mainActivity) {
                fragmentPaymentBinding.paymentConfirmButton.text = "${it}원 결제하기"
                fragmentPaymentBinding.paymentCheck.text = "상품 금액 : ${it}원"
            }
        }

        fragmentPaymentBinding.run {

            //배송지 선택 button
            //배송지 관리 페이지 -> 배송지 선택-> 텍스트뷰 띄우기 구현을 했는데
            //결제하기 버튼 클릭시 앱이 종료되서 테스트를 못해봤습니다.
            paymentDeliveryButton.setOnClickListener {
                val bundle=Bundle()
                bundle.putString("FromOrder","FromOrder")
                findNavController().navigate(R.id.DeliveryPointManageFragment)
            }
            val name = arguments?.getString("name")
            val address = arguments?.getString("address")
            val phoneNumber = arguments?.getString("phoneNumber")
            if(name!=null && address !=null && phoneNumber!= null){
                paymentDeliveryPointLayout.visibility=View.VISIBLE
                paymentDeliveryButton.visibility=View.GONE
                paymentDeliveryPointName.text=name
                paymentDeliveryPoinAddress.text=address
                paymentDeliveryPointPhone.text=phoneNumber
            }

            // spinner
            paymentSpinner.run {
                val spinnerAdapter = ArrayAdapter<String>(
                    context,
                    android.R.layout.simple_spinner_item,
                    spinnerList
                )
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                adapter = spinnerAdapter

                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }
                }
            }

            // 상단 툴바
            toolbarPayment.run {
                title = "결제 페이지"
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                val navigationIcon = navigationIcon
                navigationIcon?.setTint(ContextCompat.getColor(context, R.color.black))
                setNavigationOnClickListener {
                    Navigation.findNavController(fragmentPaymentBinding.root)
                        .navigate(R.id.action_paymentFragment_to_itemDetailFragment)
                }
            }

            // 버튼
            paymentConfirmButton.run {
                setOnClickListener {
                    // 결제 완료 버튼
                    // 주문 완료 화면으로 이동하기
                    Navigation.findNavController(fragmentPaymentBinding.root)
                        .navigate(R.id.completeFragment)
                }
            }

            // repos -> vm -> item 목록 받기
            paymentViewModel.getItemAndCalculatePrice()

            // 상품 정보 viewPager2
            paymentViewPager.apply {
                adapter = ItemRecyclerAdapter()
            }
            // indicater 구성 tabLayout
            TabLayoutMediator(paymentTab, paymentViewPager) {
                    tab, position -> paymentViewPager.setCurrentItem(tab.position)
            }.attach()

        }

        return fragmentPaymentBinding.root
    }

    // viewPager2에 붙여줄 recyclerAdapter
    inner class ItemRecyclerAdapter: RecyclerView.Adapter<ItemRecyclerAdapter.ItemViewHolder>() {
        inner class ItemViewHolder(paymentItemRowBinding: PaymentItemRowBinding): RecyclerView.ViewHolder(paymentItemRowBinding.root) {
            val paymentItemImage: ImageView
            val paymentItemName: TextView
            val paymentItemPrice: TextView
            val paymentItemCount: TextView
            val paymentItemOption: TextView

            init {
                paymentItemImage = paymentItemRowBinding.paymentItemImage
                paymentItemName = paymentItemRowBinding.paymentItemName
                paymentItemPrice = paymentItemRowBinding.paymentItemPrice
                paymentItemCount = paymentItemRowBinding.paymentItemCount
                paymentItemOption = paymentItemRowBinding.paymentItemOption
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val paymentItemRowBinding = PaymentItemRowBinding.inflate(layoutInflater)
            val itemViewHolder = ItemViewHolder(paymentItemRowBinding)
            paymentItemRowBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            return itemViewHolder
        }

        override fun getItemCount(): Int {
            return paymentViewModel.itemList.value?.size!!
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.paymentItemName.text = "제품명 : ${paymentViewModel.itemList.value?.get(position)?.name}"
            holder.paymentItemPrice.text = "가격 : ${paymentViewModel.itemList.value?.get(position)?.price.toString()}"
            // 현재 데이터셋 없으므로 1 고정
            holder.paymentItemCount.text = "수량 : 1"
            holder.paymentItemOption.text = "옵션 : ${paymentViewModel.itemList.value?.get(position)?.option}"
            PaymentRepository.getItemImage(holder.paymentItemImage, paymentViewModel.itemList.value?.get(position)?.mainImage)
        }
    }

    // 총 결제 금액 표기용 메서드
    fun totalToButton() {

    }
}

