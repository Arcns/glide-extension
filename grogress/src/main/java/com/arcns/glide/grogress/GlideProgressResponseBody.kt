package com.arcns.glide.grogress

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

class GlideProgressResponseBody internal constructor(
    private val progressKey: String,
    private val responseBody: ResponseBody
) : ResponseBody() {
    private val progressCore = GlideProgressCore();
    private var bufferedSource: BufferedSource? = null


    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = source(responseBody.source()).buffer()
        }
        return bufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                val fullLength = responseBody.contentLength()
                if (bytesRead == -1L) {
                    totalBytesRead = fullLength
                } else {
                    totalBytesRead += bytesRead
                }
                progressCore.dispatchProgressUpdate(progressKey, totalBytesRead, fullLength)
                return bytesRead
            }
        }
    }


}