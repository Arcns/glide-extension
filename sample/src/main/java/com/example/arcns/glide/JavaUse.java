package com.example.arcns.glide;

import android.content.Context;
import android.widget.ImageView;

import com.arcns.glide.grogress.GlideProgressExtensionKt;
import com.bumptech.glide.Glide;

public class JavaUse {
    public void use(Context context, ImageView imageView) {
        GlideProgressExtensionKt.loadWithProgress(
                Glide.with(context),
                context,
                "",
                null,
                null,
                null,
                null
        ).into(imageView);
    }
}
