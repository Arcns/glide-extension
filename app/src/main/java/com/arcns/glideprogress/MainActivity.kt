package com.arcns.glideprogress

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.arcns.glideprogress.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okio.*
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadImage()
        binding.ivTest.setOnClickListener {
            if (binding.pbTest.visibility == View.VISIBLE) return@setOnClickListener
            loadImage()
        }
    }

    private fun loadImage() {
        Glide.with(this)
            .progressNetworkLoad(
                context = this,
                networkUrl = "https://data.1freewallpapers.com/download/surreal-landscape-4k.jpg",
                progressBar = binding.pbTest,
                progressTextView = binding.tvTest,
                lifecycleOwner = this,
                listener = object : GlideProgressListener<Drawable>() {
                    override fun onProgress(current: Long, total: Long, percent: Float) {
                        Log.e("GlideProgress", "current:$current,total:$total,percent:$percent")
                    }
                }
            )
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.ivTest)
    }
}




