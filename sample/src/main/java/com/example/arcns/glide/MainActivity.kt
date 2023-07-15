package com.example.arcns.glide

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.arcns.glide.grogress.loadWithProgress
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.arcns.glide.databinding.ActivityMainBinding
import java.util.*


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
            .loadWithProgress(
                context = this, // 上下文
                networkUrl = "https://data.1freewallpapers.com/download/surreal-landscape-4k.jpg", // 图片网络地址
                progressBar = binding.pbTest,// 进度条，可为空
                progressTextView = binding.tvTest, // 进度文本(x%)，可为空
                lifecycleOwner = this, // 生命周期感知，生命周期结束时自动解除监听，可为空
                listener = object : com.arcns.glide.grogress.GlideProgressListener<Drawable>() {
                    override fun onProgress(current: Long, total: Long, percent: Float) {
                        Log.e("GlideProgress", "current:$current,total:$total,percent:$percent")
                    }
                } // 进度监听，可为空
            )
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.ivTest)
    }
}




