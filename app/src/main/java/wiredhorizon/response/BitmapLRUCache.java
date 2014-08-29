package wiredhorizon.response;

/**
 * Created by chriszhu on 7/9/14.
 */

import com.android.volley.toolbox.ImageLoader.ImageCache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class BitmapLRUCache extends LruCache<String, Bitmap> implements ImageCache {

    public BitmapLRUCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }


    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }


    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }

}