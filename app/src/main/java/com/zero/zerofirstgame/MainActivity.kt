package com.zero.zerofirstgame

import android.app.Activity
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.BaseInputConnection
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewAssetLoader.AssetsPathHandler
import androidx.webkit.WebViewClientCompat


class MainActivity : AppCompatActivity() {
    private lateinit var wbv: WebView

    private fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(
            window,
            window.decorView.rootView
        ).show(WindowInsetsCompat.Type.systemBars())
    }
    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView.rootView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Set default nightmode
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        wbv = findViewById(R.id.wbv)
        wbv.setBackgroundColor(Color.TRANSPARENT)
        wbv.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        wbv.settings.allowFileAccess = false
        wbv.settings.allowContentAccess = false
        wbv.settings.javaScriptEnabled = true
        wbv.settings.databaseEnabled = true
        wbv.settings.domStorageEnabled = true
        wbv.settings.loadsImagesAutomatically = true
        wbv.settings.setGeolocationEnabled(true)

        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", AssetsPathHandler(this))
            .build()

        wbv.webViewClient = (object : WebViewClientCompat() {
            @RequiresApi(21)
            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest
            ): WebResourceResponse? {
                return assetLoader.shouldInterceptRequest(request.url)
            }
            // for API < 21
            override fun shouldInterceptRequest(view: WebView, url: String): WebResourceResponse? {
                return assetLoader.shouldInterceptRequest(Uri.parse(url))
            }
        })

        wbv.webChromeClient = object : WebChromeClient() {}
        wbv.loadUrl("https://appassets.androidplatform.net/assets/app/index.html")
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if(hasFocus) {
            hideSystemUI()
        }
    }
}