package ace.soar.frame.utils.net;


import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;
import java.util.Set;

/**
 * Created by gaofei on 2016/2/17.
 */
public class HttpUtils {

    public final static int REQUEST_GET = 0;
    public final static int REQUEST_POST = 1;


    private static RequestParams params ;

    public static void setUrl(String url){
        if(params == null){
            params = new RequestParams(url);
            params.setCacheMaxAge(1000*60); // cache
        }
    }


    private static <T> void requestGet(Map<String , Object> data , final RequestListener requestListener , final Class<T> tClass){
        addParams(data);
        x.http().request(HttpMethod.GET , params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                requestListener.requestSuccess(gson.fromJson(result , tClass));
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    requestListener.requestFail(responseCode, responseMsg, errorResult);
                } else { // 其他错误
                    requestListener.requestFail(1010, ex.toString(), "error");
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }


    private static <T> void requestPost(Map<String , Object> data ,final RequestListener requestListener ,final Class<T> tClass){
        addParams(data);
        x.http().request(HttpMethod.POST , params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                requestListener.requestSuccess(gson.fromJson(result , tClass));
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    requestListener.requestFail(responseCode, responseMsg, errorResult);
                } else { // 其他错误
                    requestListener.requestFail(1010, "other error", "error");
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }



    public static <T> void request(Map<String , Object> data ,int requestMethod ,RequestListener requestListener ,Class<T> tClass ){
        switch (requestMethod){
            case REQUEST_GET:
                requestGet(data,requestListener , tClass);
                break;
            case REQUEST_POST:
                requestPost(data,requestListener , tClass);
                break;
        }
    }

    private static void addParams(Map<String , Object> data){
        Set<String> keys = data.keySet();
        for (String key : keys){
            params.addParameter(key , data.get(key));
        }
    }
}
