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
import com.petpal.swimmer_customer.data.model.Order
import com.petpal.swimmer_customer.databinding.FragmentPaymentBinding
import com.petpal.swimmer_customer.databinding.PaymentItemRowBinding
import com.petpal.swimmer_customer.ui.main.MainActivity
import com.petpal.swimmer_customer.ui.payment.repository.PaymentRepository
import com.petpal.swimmer_customer.ui.payment.vm.PaymentViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PaymentFragment : Fragment() {

    lateinit var fragmentPaymentBinding: FragmentPaymentBinding
    lateinit var mainActivity: MainActivity
    lateinit var paymentViewModel: PaymentViewModel

    // mvvm 패턴으로 변경 시 vm으로 이동 시킬 변수들
    lateinit var totalFee: String
    private lateinit var orderItemList: MutableList<ItemsForCustomer>
    lateinit var spinnerSelect: String
    var chipSelect: Long = 0

    // -R&D-
    // spinner, chipgroup, button -> mvvm 패턴을 위해 vm으로 메서드 이전 후 데이터 처리
    // 사용자로부터 값을 받지 못한 부분에 대한 error dialog 혹은 null 값 처리


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // spinner array
        // getResources 활용 하려면 oncreate 이후에 실행 가능 기억하기
        val spinnerItems = resources.getStringArray(R.array.spinner_items)
        val spinnerList = arrayOf(spinnerItems[0], spinnerItems[1], spinnerItems[2], spinnerItems[3], spinnerItems[4])

        fragmentPaymentBinding = FragmentPaymentBinding.inflate(inflater)
        mainActivity = activity as MainActivity
        paymentViewModel = ViewModelProvider(mainActivity)[PaymentViewModel::class.java]

        paymentViewModel.run {

            itemList.observe(mainActivity) {
                fragmentPaymentBinding.paymentViewPager.adapter?.notifyDataSetChanged()
                orderItemList = it
            }

            paymentFee.observe(mainActivity) {
                fragmentPaymentBinding.paymentConfirmButton.text = "${it}원 결제하기"
                fragmentPaymentBinding.paymentCheck.text = "총 상품 금액 : ${it}원"
                totalFee = it
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
                        Log.d("!!", "$selectedItem")
                        // 선택된 item -> Order.message 에 넣어주기
                        spinnerSelect = selectedItem.toString()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // dialog 를 통해 사용자에게 알려준다.
                    }
                }
            }

            // 상단 툴바
            toolbarPayment.run {
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                val navigationIcon = navigationIcon
                navigationIcon?.setTint(ContextCompat.getColor(context, R.color.black))
                setNavigationOnClickListener {
                    // 백 버튼 문제 해결하기 -> 완료
                    Navigation.findNavController(fragmentPaymentBinding.root).popBackStack()
                }
            }

            // 버튼
            paymentConfirmButton.run {
                setOnClickListener {
                    // 결제 완료 버튼

                    // seller한테 넘겨줄 order객체 서버로 전송하는 메서드 구현 예정
                    val sdfDate = SimpleDateFormat("yyyy.MM.dd hh:mm", Locale.getDefault())
                    val orderDate = sdfDate.format(Date(System.currentTimeMillis()))

                    val sdfUid = SimpleDateFormat("MMddhhmmss", Locale.getDefault())
                    val orderUid = sdfUid.format(Date(System.currentTimeMillis()))

                    val order = Order(1, orderUid, orderDate, spinnerSelect, chipSelect,
                        //TODO: test_user_uid
                        totalFee.toLong(), orderItemList, "test_address", "test_coupon_item", 1000, "test_user_uid")
                    PaymentRepository.sendOrderToSeller(order) {

                        it.addOnCanceledListener {
                            // canceled -> error dialog
                        }
                        it.addOnCompleteListener {
                            // complete -> 주문 완료 화면
                            // 주문 완료 화면으로 이동하기
                            Navigation.findNavController(fragmentPaymentBinding.root)
                                .navigate(R.id.action_paymentFragment_to_completeFragment)
                        }

                    }
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

            // chipGroup 제어 부분
            paymentChipGroup.run {
                setOnCheckedStateChangeListener { group, checkedIds ->
                    when (checkedChipId) {
                        // 해당 chipId를 통해서 long 타입 전환
                        R.id.paymentNaver -> chipSelect = 1
                        R.id.paymentKakao -> chipSelect = 2
                        R.id.paymentCard -> chipSelect = 3
                        R.id.paymentAccount -> chipSelect = 4
                        R.id.paymentMoblie -> chipSelect = 5
                        R.id.paymentCash -> chipSelect = 6
                    }
                }
            }

        }

        return fragmentPaymentBinding.root
    }

    // viewPager2에 붙여줄 recyclerAdapter
    inner class ItemRecyclerAdapter: RecyclerView.Adapter<ItemRecyclerAdapter.ItemViewHolder>() {
        inner class ItemViewHolder(paymentItemRowBinding: PaymentItemRowBinding): RecyclerView.ViewHolder(paymentItemRowBinding.root) {
            val paymentItemImage: ImageView
            val paymentItemName: TextView
            val paymentItemPrice: TextView
            val paymentItemQuantity: TextView
            val paymentItemColor: TextView
            val paymentItemSize: TextView

            init {
                paymentItemImage = paymentItemRowBinding.paymentItemImage
                paymentItemName = paymentItemRowBinding.paymentItemName
                paymentItemPrice = paymentItemRowBinding.paymentItemPrice
                paymentItemQuantity = paymentItemRowBinding.paymentItemQuantity
                paymentItemColor = paymentItemRowBinding.paymentItemColor
                paymentItemSize = paymentItemRowBinding.paymentItemSize
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
            holder.paymentItemName.text = paymentViewModel.itemList.value?.get(position)?.name.toString()
            holder.paymentItemPrice.text = "가격 : ${paymentViewModel.itemList.value?.get(position)?.price.toString()}"

            holder.paymentItemQuantity.text = "수량 : ${paymentViewModel.itemList.value?.get(position)?.quantity.toString()}"

            // option -> color, size로 분할
            holder.paymentItemColor.text = "색상 : ${paymentViewModel.itemList.value?.get(position)?.color}"
            holder.paymentItemSize.text = "사이즈 : ${paymentViewModel.itemList.value?.get(position)?.size}"
            PaymentRepository.getItemImage(holder.paymentItemImage, paymentViewModel.itemList.value?.get(position)?.mainImage)
        }
    }
}

