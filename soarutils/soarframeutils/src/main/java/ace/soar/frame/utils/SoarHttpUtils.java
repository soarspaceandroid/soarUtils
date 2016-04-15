package ace.soar.frame.utils;

import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by gaofei on 2016/2/17.
 */
public class SoarHttpUtils {

    public static void requestHttp(){
        /**
         * 自定义实体参数类请参考:
         * 请求注解 {@link org.xutils.http.annotation.HttpRequest}
         * 请求注解处理模板接口 {@link org.xutils.http.app.ParamsBuilder}
         *
         * 需要自定义类型作为callback的泛型时, 参考:
         * 响应注解 {@link org.xutils.http.annotation.HttpResponse}
         * 响应注解处理模板接口 {@link org.xutils.http.app.ResponseParser}
         *
         * 示例: 查看 org.xutils.sample.http 包里的代码
         */
        RequestParams params = new RequestParams();
        params.setCacheMaxAge(1000 * 60);  // set cache
//        params.setSslSocetFactory(); // 设置ssl
//        params.addQueryStringParameter("wd", "xUtils");
        // 有上传文件时使用multipart表单, 否则上传原始文件流.
        // params.setMultipart(true);
        // 上传文件方式 1
        // params.uploadFile = new File("/sdcard/test.txt");
        // 上传文件方式 2
        // params.addBodyParameter("uploadFile", new File("/sdcard/test.txt"));
        Callback.Cancelable cancelable = x.http().get(params,
                /**
                 * 1. callback的泛型:
                 * callback参数默认支持的泛型类型参见{@link org.xutils.http.loader.LoaderFactory},
                 * 例如: 指定泛型为File则可实现文件下载, 使用params.setSaveFilePath(path)指定文件保存的全路径.
                 * 默认支持断点续传(采用了文件锁和尾端校验续传文件的一致性).
                 * 其他常用类型可以自己在LoaderFactory中注册,
                 * 也可以使用{@link org.xutils.http.annotation.HttpResponse}
                 * 将注解HttpResponse加到自定义返回值类型上, 实现自定义ResponseParser接口来统一转换.
                 * 如果返回值是json形式, 那么利用第三方的json工具将十分容易定义自己的ResponseParser.
                 * 如示例代码{@link org.xutils.sample.http.BaiduResponse}, 可直接使用BaiduResponse作为
                 * callback的泛型.
                 *
                 * 2. callback的组合:
                 * 可以用基类或接口组合个种类的Callback, 见{@link org.xutils.common.Callback}.
                 * 例如:
                 * a. 组合使用CacheCallback将使请求检测缓存或将结果存入缓存(仅GET请求生效).
                 * b. 组合使用PrepareCallback的prepare方法将为callback提供一次后台执行耗时任务的机会,
                 * 然后将结果给onCache或onSuccess.
                 * c. 组合使用ProgressCallback将提供进度回调.
                 * ...(可参考{@link org.xutils.image.ImageLoader}
                 * 或 示例代码中的 {@link org.xutils.sample.download.DownloadCallback})
                 *
                 * 3. 请求过程拦截或记录日志: 参考 {@link org.xutils.http.app.RequestTracker}
                 *
                 * 4. 请求Header获取: 参考 {@link org.xutils.http.app.RequestInterceptListener}
                 *
                 * 5. 其他(线程池, 超时, 重定向, 重试, 代理等): 参考 {@link org.xutils.http.RequestParams}
                 *
                 **/
                new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        //Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                        if (ex instanceof HttpException) { // 网络错误
                            HttpException httpEx = (HttpException) ex;
                            int responseCode = httpEx.getCode();
                            String responseMsg = httpEx.getMessage();
                            String errorResult = httpEx.getResult();
                            // ...
                        } else { // 其他错误
                            // ...
                        }
                        Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFinished() {

                    }
                });

        // cancelable.cancel(); // 取消请求
    }
}
