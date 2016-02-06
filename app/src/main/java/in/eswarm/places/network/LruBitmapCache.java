package in.eswarm.places.network;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;


public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

    /**
     *  maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */

    public static int getDefaultLruCachesize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int Cachesize = maxMemory / 8;
        System.out.println("Cache size : " + Cachesize);
        return Cachesize;
    }

    public LruBitmapCache(int sizeinKB) {
        super(sizeinKB);
    }

    public LruBitmapCache() {
        super(getDefaultLruCachesize());
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

}
