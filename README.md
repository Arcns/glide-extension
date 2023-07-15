# glide-extension [![](https://www.jitpack.io/v/com.gitee.arcns/glide-extension.svg)](https://www.jitpack.io/#com.gitee.arcns/glide-extension)
[English](README.md) | [中文](README-CN.md)

#### Introduction
Glide extension project
- progress：Glide loading progress monitoring callback
- sample：Use Cases




#### Integration tutorial

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



#### Use tutorial
- Kotlin
```
Glide.with(this)
            .loadWithProgress(
                context = this,// Context
                networkUrl = "https://data.1freewallpapers.com/download/surreal-landscape-4k.jpg", // Picture network address
                progressBar = binding.pbTest, // Progress bar, can be empty
                progressTextView = binding.tvTest,// Progress text view(x%), can be empty
                lifecycleOwner = this,// Life cycle awareness, automatically release monitoring at the end of the life cycle, can be empty
                listener = object : com.arcns.glide.grogress.GlideProgressListener<Drawable>() {
                    override fun onProgress(current: Long, total: Long, percent: Float) {
                        Log.e("GlideProgress", "current:$current,total:$total,percent:$percent")
                    }
                } // Progress monitoring callback, can be empty
            )
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.ivTest)
```
- Java
```
 GlideProgressExtensionKt.loadWithProgress(
                Glide.with(context), // Glide RequestManager或RequestBuilder
                context, // Context
                "", // Picture network address
                new GlideProgressListener<Drawable>() {
                    @Override
                    public void onProgress(long current, long total, float percent) {

                    }
                }, // Progress monitoring callback, can be empty
                null, // Life cycle awareness, automatically release monitoring at the end of the life cycle, can be empty
                null, //  Progress bar, can be empty
                null // Progress text view(x%), can be empty
        ).into(imageView);
```
