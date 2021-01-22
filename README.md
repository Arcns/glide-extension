# glide-extension [![](https://www.jitpack.io/v/com.gitee.arcns/glide-extension.svg)](https://www.jitpack.io/#com.gitee.arcns/glide-extension)

#### 介绍
Glide扩展项目
grogress：实现Glide加载进度监听回调
sample：使用案例



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
	 implementation 'com.gitee.arcns:glide-extension:Tag'
}
```



#### 使用说明

```
Glide.with(this)
            .loadWithProgress(
                context = this,
                networkUrl = "https://data.1freewallpapers.com/download/surreal-landscape-4k.jpg",
                progressBar = binding.pbTest, 
                progressTextView = binding.tvTest,
                lifecycleOwner = this,
                listener = object : com.arcns.glide.grogress.GlideProgressListener<Drawable>() {
                    override fun onProgress(current: Long, total: Long, percent: Float) {
                        Log.e("GlideProgress", "current:$current,total:$total,percent:$percent")
                    }
                }
            )
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.ivTest)
```

