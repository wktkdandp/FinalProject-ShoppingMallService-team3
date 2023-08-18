package com.petpal.swimmer_seller

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.firebase.FirebaseApp
import com.petpal.swimmer_seller.data.model.Address
import com.petpal.swimmer_seller.data.model.Seller
import com.petpal.swimmer_seller.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    lateinit var activityMainBinding: ActivityMainBinding
    lateinit var loginSeller: Seller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        FirebaseApp.initializeApp(this)

        // 판매자 더미 데이터
        val address = Address(1, 12345, "서울특별시 종로구 종로3길 17 D타워, 16-17층", "멋쟁이사자처럼 본사", "010-1234-5678")
        loginSeller = Seller(
            1L, "264-88-01106", "김멋사",
            "멋쟁이사자처럼", "멋진 수영복을 제공합니다.", address,
            "010-1234-5678", "신한은행", "123-456-789012",
            "김사자", "010-1234-5678", "contact@likelion.net"
        )

        setContentView(activityMainBinding.root)
    }

    // 입력 요소에 포커스를 주는 메서드
    fun showSoftInput(view: View){
        view.requestFocus()

        val inputMethodManger = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        thread {
            SystemClock.sleep(200)
            inputMethodManger.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}