package com.capstone.bloomy.ui.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.capstone.bloomy.data.model.TodayNewsModel
import com.capstone.bloomy.data.model.TopNewsModel
import com.capstone.bloomy.databinding.ActivityNewsDetailBinding

class NewsDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsDetailBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION") val selectedTopNews = intent.getParcelableExtra<TopNewsModel>("selected_top_news")
        @Suppress("DEPRECATION") val selectedTodayNews = intent.getParcelableExtra<TodayNewsModel>("selected_today_news")

        if (selectedTopNews != null) {
            binding.webViewNewsDetail.settings.javaScriptEnabled = true

            binding.webViewNewsDetail.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    binding.progressBarNewsDetail.visibility = View.VISIBLE
                    binding.webViewNewsDetail.visibility = View.INVISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.progressBarNewsDetail.visibility = View.GONE
                    binding.webViewNewsDetail.visibility = View.VISIBLE
                }
            }

            binding.webViewNewsDetail.loadUrl(selectedTopNews.webUrl)
        } else if (selectedTodayNews != null) {
            binding.webViewNewsDetail.settings.javaScriptEnabled = true

            binding.webViewNewsDetail.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    binding.progressBarNewsDetail.visibility = View.VISIBLE
                    binding.webViewNewsDetail.visibility = View.INVISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.progressBarNewsDetail.visibility = View.GONE
                    binding.webViewNewsDetail.visibility = View.VISIBLE
                }
            }

            binding.webViewNewsDetail.loadUrl(selectedTodayNews.webUrl)
        }
    }
}