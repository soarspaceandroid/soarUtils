package android.improving.utils.tools;

import android.improving.utils.http.CreateGsonBuilder;
import android.improving.utils.secretkey.SubmitHelper;
import android.improving.utils.storage.file.FileManager;
import android.improving.utils.tools.downupload.OnLoadListener;
import android.improving.utils.tools.downupload.ProgressHelper;
import android.util.Base64;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Auth：yujunyao
 * Since: 2016/3/9 15:57
 * Email：yujunyao@yonglibao.com
 */
public class Upload {
    private static final String POST_TAG = "i";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    /**
     * @param loadUrl 图片上传地址
     * @param contentParams 上传的表单内容
     * @param fileParams 第二个String为图片绝对路径
     * @param loadListener 结果回调
     */
    public static void uploadFile(String loadUrl, Map<String, String> contentParams, Map<String, String> fileParams, final OnLoadListener loadListener) {
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        builder.addFormDataPart(POST_TAG, getRequestParam(contentParams));
        for (Map.Entry<String, String> entry : fileParams.entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getKey(), RequestBody.create(MEDIA_TYPE_PNG, FileManager.getAbsoluteFile(entry.getValue())));
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(loadUrl)//地址
                .post(ProgressHelper.addProgressRequestListener(requestBody, loadListener))//如需上传进度显示可以这样
//                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                loadListener.loadFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                call.cancel();
                loadListener.loadSuccess(getResponseParam(response.body().toString()));
            }
        });
    }

    public static void uploadFile(String loadUrl, Map<String, String> contentParams, String photoKey, byte[] byteArray, final OnLoadListener loadListener) {
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        builder.addFormDataPart(POST_TAG, getRequestParam(contentParams));
        builder.addFormDataPart(photoKey, photoKey, RequestBody.create(MEDIA_TYPE_PNG, byteArray));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(loadUrl)//地址
                .post(ProgressHelper.addProgressRequestListener(requestBody, loadListener))//如需上传进度显示可以这样
//                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                loadListener.loadFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                call.cancel();
                loadListener.loadSuccess(getResponseParam(response.body().toString()));
            }
        });
    }

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

    /**返回参数解密*/
    private static String getResponseParam(String backResult) {
        byte[] decodeData = Base64.decode(backResult, Base64.DEFAULT);
        String json = new String(SubmitHelper.decryptMode(decodeData));
        return json;
    }

}
