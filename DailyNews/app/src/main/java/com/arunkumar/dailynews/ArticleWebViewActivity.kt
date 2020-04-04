package com.arunkumar.dailynews

import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.arunkumar.dailynews.utils.ARTICLE_URL
import com.arunkumar.dailynews.utils.hideProgressUI
import com.arunkumar.dailynews.utils.showProgressUI
import kotlinx.android.synthetic.main.activity_article_web_view.*

class ArticleWebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_web_view)

        val bundle = intent.extras
        val webSettings = webview.settings

        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        webSettings.setSupportZoom(true)
        webSettings.defaultTextEncodingName = "utf-8"

        webview.webChromeClient = WebChromeClient()

        webview.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                showProgressUI(iv_progress)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                hideProgressUI(iv_progress)
            }
        }
        webview.loadUrl(bundle?.getString(ARTICLE_URL))
    }
}
