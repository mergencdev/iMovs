package com.mergenc.imovs.view.ui.homepage

import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mergenc.imovs.databinding.ActivityHomepageBinding


class HomepageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val homepageUrl: String? = intent.getStringExtra("homepageUrl")
        Log.d("HomepageActivity", "homepageUrl: $homepageUrl")

        if (homepageUrl != null) {
            binding.webViewHomepage.loadUrl(homepageUrl)
        }

        val webSettings: WebSettings = binding.webViewHomepage.getSettings()
        webSettings.javaScriptEnabled = true

        binding.webViewHomepage.setWebViewClient(WebViewClient())
    }

    override fun onBackPressed() {
        if (binding.webViewHomepage.canGoBack())
            binding.webViewHomepage.goBack()
        else
            super.onBackPressed()
    }
}