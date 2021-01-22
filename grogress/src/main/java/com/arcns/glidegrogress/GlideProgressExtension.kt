package com.arcns.glidegrogress

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.util.*

/**
 * 开始进度更新加载
 */
fun RequestManager.loadWithProgress(
    context: Context, // 上下文
    networkUrl: String, // 网络链接
    listener: GlideProgressListener<Drawable>? = null, // 加载进度回调
    lifecycleOwner: LifecycleOwner? = null, // 生命周期
    progressBar: ProgressBar? = null, // 进度条控件
    progressTextView: TextView? = null, // 进度文本控件(x%)
    progressKey: String? = UUID.randomUUID()
        .toString() // 当前进度key，默认使用uuid并缓存到请求的headers中，为空时使用url作为key（可能会有url重复的问题
): RequestBuilder<Drawable> =
    asDrawable().loadWithProgress(
        context,
        networkUrl,
        listener,
        lifecycleOwner,
        progressBar,
        progressTextView,
        progressKey
    )

/**
 * 开始进度更新加载
 */
fun RequestManager.loadWithProgress(
    context: Context,// 上下文
    glideUrl: GlideUrl,// 网络链接
    listener: GlideProgressListener<Drawable>? = null, // 加载进度回调
    lifecycleOwner: LifecycleOwner? = null, // 生命周期
    progressBar: ProgressBar? = null,// 进度条控件
    progressTextView: TextView? = null,// 进度文本控件(x%)
    progressKey: String? = UUID.randomUUID()
        .toString()// 当前进度key，默认使用uuid并缓存到请求的headers中，为空时使用url作为key（可能会有url重复的问题
): RequestBuilder<Drawable> =
    asDrawable().loadWithProgress(
        context,
        glideUrl,
        listener,
        lifecycleOwner,
        progressBar,
        progressTextView,
        progressKey
    )


/**
 * 开始进度更新加载
 */
fun <T> RequestBuilder<T>.loadWithProgress(
    context: Context, // 上下文
    networkUrl: String, // 网络链接
    listener: GlideProgressListener<T>? = null, // 加载进度回调
    lifecycleOwner: LifecycleOwner? = null, // 生命周期
    progressBar: ProgressBar? = null, // 进度条控件
    progressTextView: TextView? = null, // 进度文本控件(x%)
    progressKey: String? = UUID.randomUUID()
        .toString() // 当前进度key，默认使用uuid并缓存到请求的headers中，为空时使用url作为key（可能会有url重复的问题
): RequestBuilder<T> = loadWithProgress(
    context,
    GlideUrl(networkUrl),
    listener,
    lifecycleOwner,
    progressBar,
    progressTextView,
    progressKey
)


/**
 * 开始进度更新加载
 */
fun <T> RequestBuilder<T>.loadWithProgress(
    context: Context,// 上下文
    glideUrl: GlideUrl,// 网络链接
    listener: GlideProgressListener<T>? = null, // 加载进度回调
    lifecycleOwner: LifecycleOwner? = null, // 生命周期
    progressBar: ProgressBar? = null,// 进度条控件
    progressTextView: TextView? = null,// 进度文本控件(x%)
    progressKey: String? = UUID.randomUUID()
        .toString()// 当前进度key，默认使用uuid并缓存到请求的headers中，为空时使用url作为key（可能会有url重复的问题
): RequestBuilder<T> {
    // 初始化核心
    com.arcns.glidegrogress.GlideProgressCore.init(context)
    // 获取当前进度key
    val key = progressKey ?: glideUrl.toString()
    // 初始化控件
    progressBar?.apply {
        visibility = View.VISIBLE
        isIndeterminate = false
        max = 100
        progress = 0
    }
    progressTextView?.apply {
        visibility = View.VISIBLE
        text = "0%"
    }
    // 注册更新回调
    com.arcns.glidegrogress.GlideProgressCore.expectProgressUpdate(
        key,
        object : GlideProgressListener<T>() {
            override fun getGranularityPercentages(): Float =
                listener?.getGranularityPercentages() ?: super.getGranularityPercentages()

            override fun onProgress(current: Long, total: Long, percent: Float) {
                // 更新控件
                progressBar?.progress = percent.toInt()
                progressTextView?.text = "${percent.toInt()}%"
                // 通知自定义回调
                listener?.onProgress(current, total, percent)
            }
        },
        lifecycleOwner
    )
    // 返回RequestBuilder
    return load(
        com.arcns.glidegrogress.GlideProgressCore.Companion.getGlideUrl(
            glideUrl,
            key
        )
    ).addListener(object :
        RequestListener<T> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<T>?,
            isFirstResource: Boolean
        ): Boolean {
            // 更新控件
            progressBar?.visibility = View.GONE
            progressTextView?.visibility = View.GONE
            // 注销更新回调
            com.arcns.glidegrogress.GlideProgressCore.forgetProgressUpdate(key)
            // 通知自定义回调
            listener?.onFailed(e, model, target, isFirstResource)
            return false
        }

        override fun onResourceReady(
            resource: T?,
            model: Any?,
            target: Target<T>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            // 更新控件
            progressBar?.visibility = View.GONE
            progressTextView?.visibility = View.GONE
            // 注销更新回调
            com.arcns.glidegrogress.GlideProgressCore.forgetProgressUpdate(key)
            // 通知自定义回调
            listener?.onSuccess(resource, model, target, dataSource, isFirstResource)
            return false
        }
    })
}