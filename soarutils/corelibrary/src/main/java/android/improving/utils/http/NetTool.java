package android.improving.utils.http;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import okhttp3.OkHttpClient;

/**
 * Auth：yujunyao
 * Since: 2016/3/1 17:55
 * Email：yujunyao@yonglibao.com
 */
public class NetTool {

    private static NetTool instance;
    private Context mContext;
    private static RequestQueue mRequestQueue;

    public NetTool(Context context) {
        this.mContext = context;
        init();
    }

    public static NetTool getInstance(Context context) {
        if(instance == null) {
            instance = new NetTool(context);
        }
        return instance;
    }

    private void init() {
        if(mRequestQueue != null) {
            mRequestQueue.stop();
        }
        mRequestQueue = Volley.newRequestQueue(mContext, new OkHttp3Stack(new OkHttpClient()));
    }

    public void destory() {
        if(mRequestQueue != null) {
            mRequestQueue.stop();
            mRequestQueue = null;
        }
    }

    public void addRequest(Request request) {
        if(mRequestQueue != null) {
            mRequestQueue.add(request);
        }
    }

    public void addRequest(Request request, Object tag) {
        if(mRequestQueue != null) {
            request.setTag(tag);
            mRequestQueue.add(request);
        }
    }

    public void cancelRequest(Object tag) {
        if(mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
