package com.petpal.swimmer_customer.ui.deliverypointmanage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.databinding.FragmentAddressDialogBinding

//배송지 추가를 눌렀을때 나오는 다음 api 웹뷰 처리 프래그먼트





private infix fun Unit.BridgeInterface(unit: Unit) {

}

class AddressDialogFragment : Fragment() {

    lateinit var fragmentAddressDialogBinding: FragmentAddressDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentAddressDialogBinding = FragmentAddressDialogBinding.inflate(layoutInflater)

        setupToolbar()
        handleBackPress()
        fragmentAddressDialogBinding.webViewAddress.settings.javaScriptEnabled = true
        fragmentAddressDialogBinding.webViewAddress.addJavascriptInterface(
            BridgeInterface(),
            "Android"
        )
        //캐시 삭제
        fragmentAddressDialogBinding.webViewAddress.clearCache(true);
        fragmentAddressDialogBinding.webViewAddress.clearHistory();

        fragmentAddressDialogBinding.webViewAddress.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                // 페이지 로딩이 완료된 후Andorid-> JavaScript 함수를 호출
                view?.loadUrl("javascript:sample2_execDaumPostcode();")
            }
        }

        // WebView에 원하는 페이지를 로드
        val mainUrl = "https://swimmer-f0505.web.app"
        fragmentAddressDialogBinding.webViewAddress.loadUrl(mainUrl)

        return fragmentAddressDialogBinding.root
    }
    private fun handleBackPress() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
    private fun setupToolbar() {
        fragmentAddressDialogBinding.toolbarAddressDialog.run {
            title = getString(R.string.delivery_point_search)
            setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener {
                showToast(getString(R.string.delevery_point_cancel))
                findNavController().popBackStack()
            }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    fun navigateWithData(data: String) {
        val splitData = data.split(",")
        val bundle = Bundle()
        //split에 쉼표로 분기하는데 애초에 api에서 넘어온 data는 data.address+처리 코드 // data.zonecode 두개를 쉼표로 나누기
        //위함이었는데 data.address에서 ,가 붙여져서 넘어오면 IndexOutOfBoundsException가 생긴다 그래서
        //splitData의 크기가 3이면 주소에 splitData[0]+splitData[1] 를 해서 오류를 방지
        if (splitData.size >= 3) {
            bundle.putString("address", splitData[0] +" "+ splitData[1].trim())
            bundle.putString("postcode", splitData[2].trim())
        } else if (splitData.size == 2) {
            bundle.putString("address", splitData[0].trim())
            bundle.putString("postcode", splitData[1].trim())
        } else {
            // 데이터 형식이 예상과 다를 경우의 처리
            return
        }
        val action=R.id.action_AddressDialogFragment_to_DetailAddressFragment
        findNavController().navigate(action,bundle)
    }

    inner class BridgeInterface {
        @JavascriptInterface
        fun processDATA(data: String) {

            navigateWithData(data)


        }

    }

}
