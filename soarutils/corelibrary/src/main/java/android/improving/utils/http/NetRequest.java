package android.improving.utils.http;

import android.improving.utils.secretkey.SubmitHelper;
import android.improving.utils.tools.LogPrint;
import android.text.TextUtils;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Auth：yujunyao
 * Since: 2016/3/2 16:54
 * Email：yujunyao@yonglibao.com
 */
public class NetRequest<T> extends Request<T> {

    private Response.Listener<T> successListener;
    private Map<String, String> bodyParams = new HashMap<>();
    private Map<String, String> headerParams;
    private static final String POST_TAG = "i";
    private Class mClass;

    public NetRequest(int method, String url,Map<String, String> bodyParams, Object object, Response.ErrorListener errorListener, Response.Listener<T> successListener) {
        super(method, url, errorListener);
        setShouldCache(false);
        this.successListener = successListener;
        this.mClass = object.getClass();
        this.bodyParams.put(POST_TAG, getRequestParam(bodyParams));
    }

//    public NetRequest(int method, String url,Map<String, String> bodyParams, Map<String, String> headerParams, Response.ErrorListener errorListener, Response.Listener<T> successListener) {
//        super(method, url, errorListener);
//        setShouldCache(false);
//        this.successListener = successListener;
//        this.bodyParams = bodyParams;
//        this.headerParams = headerParams;
//    }

    /**请求参数转化为json，再加密*/
    private static String getRequestParam(Map<String, String> params){
        String base64EncodeData = "";
        try{
            Gson gson = CreateGsonBuilder.createBuilder().create();
            String content = gson.toJson(params);
            LogPrint.iLogPrint("NetRequest", "Request AFTER Decryption--->" + content);
            byte[] encodeContent = SubmitHelper.encryptMode(content
                    .getBytes("UTF-8"));
            base64EncodeData = Base64.encodeToString(encodeContent, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return base64EncodeData;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return bodyParams;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/x-www-form-urlencoded");
        return headerMap;
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        return new DefaultRetryPolicy(15000, 0, 1f);
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String data = new String(response.data,  HttpHeaderParser.parseCharset(response.headers));
            LogPrint.iLogPrint("data--->", data);
            if(!TextUtils.isEmpty(data)) {
                byte[] decodeData = Base64.decode(data, Base64.DEFAULT);
                String json = new String(SubmitHelper.decryptMode(decodeData));
                LogPrint.iLogPrint("NetRequest", "Request AFTER Decryption--->" + json);

                Gson gson = CreateGsonBuilder.createBuilder().create();
                Object responseObject = gson.fromJson(json, mClass);
                return Response.success(responseObject, HttpHeaderParser.parseCacheHeaders(response));
            }else {
                return Response.error(new ParseError());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        if(successListener != null) {
            successListener.onResponse(response);
        }
    }

}
