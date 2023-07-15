# glide-extension [![](https://www.jitpack.io/v/com.gitee.arcns/glide-extension.svg)](https://www.jitpack.io/#com.gitee.arcns/glide-extension)
[English](README.md) | [中文](README-CN.md)

#### 介绍
Glide扩展项目
- progress：实现Glide加载进度监听回调
- sample：使用案例




#### 集成教程

```
allprojects {
	repositories {
		...
		maven { url 'https://www.jitpack.io' }
	}
}
```

```
dependencies {
	 implementation 'com.github.bumptech.glide:glide:4.11.0'
	 implementation "com.github.bumptech.glide:okhttp3-integration:4.11.0"
	 implementation 'com.gitee.arcns:glide-extension:Tag'
}
```



#### 使用说明
- Kotlin
```
Glide.with(this)
            .loadWithProgress(
                context = this,// 上下文
                networkUrl = "https://data.1freewallpapers.com/download/surreal-landscape-4k.jpg", // 图片网络地址
                progressBar = binding.pbTest, // 进度条，可为空
                progressTextView = binding.tvTest,// 进度文本(x%)，可为空
                lifecycleOwner = this,// 生命周期感知，生命周期结束时自动解除监听，可为空
                listener = object : com.arcns.glide.grogress.GlideProgressListener<Drawable>() {
                    override fun onProgress(current: Long, total: Long, percent: Float) {
                        Log.e("GlideProgress", "current:$current,total:$total,percent:$percent")
                    }
                } // 进度监听，可为空
            )
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.ivTest)
```
- Java
```
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
```
