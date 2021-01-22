package com.arcns.glide.grogress

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.io.InputStream
import kotlin.collections.HashMap


/**
 * Glide进度更新核心
 */
class GlideProgressCore {
    private val mainHandler: Handler = Handler(Looper.getMainLooper())

    companion object {
        // Headers Glide Progress Key
        val GLIDE_PROGRESS_KEY = "GLIDE_PROGRESS_KEY"

        // 进度事件
        private val progressListeners: MutableMap<String, GlideProgressListener<*>> = HashMap()

        // 进度值
        private val progressValues: MutableMap<String, Long> = HashMap()

        // 是否完成Glide Progress OkHttp Client替换
        private var hasInit = false

        /**
         * 初始化进度更新器
         */
        fun init(context: Context) {
            if (hasInit) return
            hasInit = true
            replaceGlideProgressOkHttpClient(context)
        }

        /**
         * 替换进度更新OkHttpClient
         */
        fun replaceGlideProgressOkHttpClient(context: Context) {
            Glide.get(context).registry.replace(
                GlideUrl::class.java,
                InputStream::class.java,
                OkHttpUrlLoader.Factory(
                    OkHttpClient.Builder()
                        .addNetworkInterceptor(Interceptor { chain ->
                            val response = chain.proceed(chain.request())
                            val key = response.request.headers[GLIDE_PROGRESS_KEY]
                                ?: response.request.url.toString()
                            response.newBuilder()
                                .body(GlideProgressResponseBody(key, response.body!!))
                                .build()
                        })
                        .build()
                )
            )
        }

        fun getGlideUrl(networkUrl: String, key: String): GlideUrl = GlideUrl(
            networkUrl,
            LazyHeaders.Builder().apply {
                if (key != networkUrl) addHeader(GLIDE_PROGRESS_KEY, key)
            }.build()
        )

        fun getGlideUrl(glideUrl: GlideUrl, key: String): GlideUrl = GlideUrl(
            glideUrl.toString(),
            LazyHeaders.Builder().apply {
                glideUrl.headers.forEach {
                    addHeader(it.key, it.value)
                }
                if (key != glideUrl.toString()) addHeader(GLIDE_PROGRESS_KEY, key)
            }.build()
        )


        /**
         * 不再接收更新回调
         */
        fun forgetProgressUpdate(key: String) {
            progressListeners.remove(key)
            progressValues.remove(key)
        }

        /**
         * 接收更新回调
         */
        fun expectProgressUpdate(
            key: String,
            listener: GlideProgressListener<*>,
            lifecycleOwner: LifecycleOwner? = null,
        ) {
            progressListeners[key] = listener
            lifecycleOwner?.lifecycle?.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestroy() {
                    forgetProgressUpdate(key)
                    lifecycleOwner?.lifecycle?.removeObserver(this)

                }
            })
        }
    }

    /**
     * 检查是否需要进行派发进度更新
     *
     */
    private fun needsDispatchProgressUpdate(
        key: String,
        current: Long,
        total: Long,
        granularity: Float
    ): Boolean {
        if (granularity == 0f || current == 0L || total == current) {
            return true
        }
        val percent = 100f * current / total
        val currentProgress = (percent / granularity).toLong()
        val lastProgress = progressValues[key]
        return if (lastProgress == null || currentProgress != lastProgress) {
            progressValues[key] = currentProgress
            true
        } else {
            false
        }
    }

    /**
     * 派发进度更新
     */
    fun dispatchProgressUpdate(key: String, bytesRead: Long, contentLength: Long) {
        val listener = progressListeners[key] ?: return
        if (contentLength <= bytesRead) {
            forgetProgressUpdate(key)
        }
        if (needsDispatchProgressUpdate(
                key,
                bytesRead,
                contentLength,
                listener.getGranularityPercentages()
            )
        ) {
            mainHandler.post {
                listener.onProgress(
                    bytesRead,
                    contentLength,
                    100f * bytesRead / contentLength
                )
            }
        }
    }
}
