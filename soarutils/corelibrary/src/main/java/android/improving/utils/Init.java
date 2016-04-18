package android.improving.utils;

import android.app.Application;
import android.improving.utils.http.NetTool;
import android.improving.utils.storage.sp.SharePreferenceUtil;
import android.text.TextUtils;

import com.android.volley.Request;
import com.baidu.android.common.util.CommonParam;

/**
 * Auth：yujunyao
 * Since: 2016/3/8 16:52
 * Email：yujunyao@yonglibao.com
 */
public class Init {

    public static Application application;

    public static NetTool netTool;

    public static SharePreferenceUtil spUtil;

    public static String deviceToken;

    public static void Init(Application application) {
        if(Init.application == null) {
            synchronized (Init.class) {
                if(Init.application == null) {
                    Init.application = application;

                    netTool = NetTool.getInstance(application);

                    spUtil = new SharePreferenceUtil(application);

                    deviceToken = CommonParam.getCUID(application);
                }
            }
        }
    }

    public static Application getApplication() {
        return application;
    }

    public static void addRequest(Request request) {
        netTool.addRequest(request);
    }

    public static void addRequest(Request request, Object tag) {
        netTool.addRequest(request, tag);
    }

    public static void cancelRequest(Object tag) {
        netTool.cancelRequest(tag);
    }

    public static void destory() {
        netTool.destory();
    }

    public static String getDeviceToken() {
        if(TextUtils.isEmpty(deviceToken)) {
            deviceToken = CommonParam.getCUID(application);
        }
        return deviceToken;
    }

}
