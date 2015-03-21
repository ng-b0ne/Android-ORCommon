package me.b0ne.android.orcommon;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by b0ne on 14/01/06.
 */
public class BitmapCache implements ImageLoader.ImageCache {

    /** cache size(Bitmap数) */
    // sample: 4 * 1024 * 1024; = 4MiB
    private static final int CACHE_SIZE_MAX = 10 * 1024 * 1024;

    private LruCache<String, Bitmap> mCache;

    public BitmapCache() {
        // 最大メモリ量
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 最大メモリの1/8を使うように
        int cacheMaxSize = maxMemory / 8;

        mCache = new LruCache<String, Bitmap>(cacheMaxSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        Bitmap oldBitmap = mCache.put(url, bitmap);
        if (oldBitmap != null) {
            if (!oldBitmap.isRecycled()) {
                oldBitmap.recycle();
            }
            oldBitmap = null;
        }
    }
}