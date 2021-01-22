package com.example.arcns.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.arcns.glide.grogress.GlideProgressExtensionKt;
import com.arcns.glide.grogress.GlideProgressListener;
import com.bumptech.glide.Glide;

public class JavaUse {
    public void use(Context context, ImageView imageView) {
        GlideProgressExtensionKt.loadWithProgress(
                Glide.with(context), // Glide RequestManager或RequestBuilder
                context, // 上下文
                "", // 图片网络地址
                new GlideProgressListener<Drawable>() {
                    @Override
                    public void onProgress(long current, long total, float percent) {

                    }
                }, // 进度监听，可为空
                null, // 生命周期感知，生命周期结束时自动解除监听，可为空
                null, // 进度条，可为空
                null // 进度文本(x%)，可为空
        ).into(imageView);
    }
}
