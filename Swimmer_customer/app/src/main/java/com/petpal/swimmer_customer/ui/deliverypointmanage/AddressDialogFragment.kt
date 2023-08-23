package com.petpal.swimmer_customer.ui.deliverypointmanage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.databinding.FragmentAddressDialogBinding

private infix fun Unit.BridgeInterface(unit: Unit) {

}

class AddressDialogFragment : Fragment() {

    lateinit var fragmentAddressDialogBinding: FragmentAddressDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentAddressDialogBinding= FragmentAddressDialogBinding.inflate(layoutInflater)

        fragmentAddressDialogBinding.webViewAddress.settings.javaScriptEnabled=true
        fragmentAddressDialogBinding.webViewAddress.addJavascriptInterface(BridgeInterface(),"Android")
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
    fun navigateWithData(data: String) {
        val splitData = data.split(",")
        Log.d("data12345",data)

        val bundle=Bundle()

        bundle.putString("address", data)
        //bundle.putString("postcode", postcode)

        findNavController().navigate(R.id.DeliveryPointManageFragment, bundle)
    }
    inner class BridgeInterface{
        @JavascriptInterface
        fun processDATA(data:String){

            navigateWithData(data)


        }

    }

}

