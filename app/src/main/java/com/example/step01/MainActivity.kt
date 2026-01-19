package com.example.step01 // 👈 본인 패키지 이름 확인!

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var myWebView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myWebView = findViewById(R.id.myWebView) // 👈 XML 방식인 경우 (아니면 수정 필요)
        // 만약 XML 안 쓰는 방식이면: myWebView = WebView(this); setContentView(myWebView)

        myWebView.settings.javaScriptEnabled = true
        myWebView.webViewClient = WebViewClient()

        // 1. 처음 앱 켤 때 확인
        checkUrl(intent)

        // (만약 URL 없이 왔으면 기본 페이지 로딩)
        if (intent.getStringExtra("url") == null) {
            myWebView.loadUrl("https://mockny.dothome.co.kr/")
        }

        // 뒤로가기 설정
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (myWebView.canGoBack()) {
                    myWebView.goBack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        checkUrl(intent)
    }


    private fun checkUrl(intent: Intent?) {
        val pushUrl = intent?.getStringExtra("url")
        if (pushUrl != null) {
            myWebView.loadUrl(pushUrl)
        }
    }
}