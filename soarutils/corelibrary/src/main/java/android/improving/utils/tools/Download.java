package android.improving.utils.tools;

import android.improving.utils.tools.downupload.OnLoadListener;
import android.improving.utils.tools.downupload.ProgressHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Auth：yujunyao
 * Since: 2016/3/18 14:14
 * Email：yujunyao@yonglibao.com
 */
public class Download {

    /**
     * 下载文件
     * @param url
     * @param savePath
     * @param saveFileName
     * @param loadListener 如果不需要进度，实现方法中不要写
     */
    public static void downloadFile(String url, final String savePath, final String saveFileName, final OnLoadListener loadListener) {
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        ProgressHelper.addProgressResponseListener(okHttpClient, loadListener).newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                loadListener.loadFail();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(savePath, saveFileName);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    loadListener.loadSuccess(file.getAbsolutePath());
                } catch (Exception e) {
                    loadListener.loadFail();
                } finally {
                    if(is != null) {
                        is.close();
                    }
                    if(fos != null) {
                        fos.close();
                    }
                }
            }
        });
    }

}
