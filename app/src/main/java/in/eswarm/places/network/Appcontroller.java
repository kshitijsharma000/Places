package in.eswarm.places.network;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class Appcontroller extends Application {
    public static final String TAG = Appcontroller.class.getSimpleName();
    private RequestQueue mrequestQueue;
    private ImageLoader mimageLoader;
    private static Appcontroller mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized Appcontroller getmInstance() {
        return mInstance;
    }

    public RequestQueue getrequestQueue() {
        if (mrequestQueue == null) {
            mrequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mrequestQueue;
    }

    public ImageLoader getImageLoader() {
        getrequestQueue();
        if (mimageLoader == null) {
            mimageLoader = new ImageLoader(this.mrequestQueue, new LruBitmapCache());
        }
        return mimageLoader;
    }

    public <T> void addtoRequestqueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getrequestQueue().add(req);
    }

    public <T> void addtoRequestqueue(Request<T> req) {
        req.setTag(TAG);
        getrequestQueue().add(req);
    }

    public void cancelpendingRequest(Object tag) {
        if (mrequestQueue != null) {
            mrequestQueue.cancelAll(tag);
        }
    }
}
