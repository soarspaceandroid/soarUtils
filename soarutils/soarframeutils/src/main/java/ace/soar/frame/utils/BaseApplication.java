package ace.soar.frame.utils;

import android.app.Application;

import org.xutils.x;

import ace.soar.frame.utils.net.HttpUtils;

/**
 * Created by gaofei on 2016/2/17.
 */
public class BaseApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        // init xutils
        x.Ext.init(this);
        x.Ext.setDebug(true);// 是否输出debug日志

        setUrl();

    }

    public void setUrl(){
        HttpUtils.setUrl("http://www.tngou.net/api/cook/name");
    }
}
