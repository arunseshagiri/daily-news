package com.arunkumar.dailynews

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.arunkumar.dailynews.utils.ARTICLE_URL
import kotlinx.android.synthetic.main.activity_article_web_view.*

class ArticleWebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_web_view)

        val bundle = intent.extras
        val webSettings = webview.settings
        webSettings.javaScriptEnabled = true
        webview.webViewClient = WebViewClient()
        webview.loadUrl(bundle?.getString(ARTICLE_URL))
    }
}
