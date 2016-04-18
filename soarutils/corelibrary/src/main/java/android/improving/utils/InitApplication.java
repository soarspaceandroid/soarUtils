package android.improving.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.improving.utils.storage.file.FileManager;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.util.List;

/**
 * Auth：yujunyao
 * Since: 2016/3/8 16:58
 * Email：yujunyao@yonglibao.com
 */
public class InitApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        String processName = getProcessName(this, android.os.Process.myPid());
        if (processName != null) {
            if (processName.equals("com.huolicai.android")) {
                onCreated();
            }
        }

    }

    public void onCreated(){
        Init.Init(this);
        initImageLoaderConfig();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Init.destory();

        ImageLoader.getInstance().clearMemoryCache();
    }

    /** 返回进程的名称 */
    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    /**
     * 初始化ImageLoader
     */
    private void initImageLoaderConfig() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)//设置线程优先级
                .threadPoolSize(4)//线程池内加载的数量,推荐范围1-5内。
                .denyCacheImageMultipleSizesInMemory()//当同一个Uri获取不同大小的图片缓存到内存中时只缓存一个。不设置的话默认会缓存多个不同大小的图片
                .memoryCacheExtraOptions(480, 800)//内存缓存文件的最大长度
                .memoryCache(new LruMemoryCache(10 * 1024 * 1024))//内存缓存方式,这里可以换成自己的内存缓存实现。(推荐LruMemoryCache,道理自己懂的)
                .memoryCacheSize(10 * 1024 * 1024)//内存缓存的最大值
                .diskCache(new UnlimitedDiskCache(FileManager.createImageCache(this)))//可以自定义缓存路径
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//对保存的URL进行加密保存
                .defaultDisplayImageOptions(setImageOptionsConfig())
                .imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000))//设置连接时间5s,超时时间30s
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }


    /**
     * 配置图片加载时候的配置,在实际开发中可以对这些参数进行一次封装。
     */
    private DisplayImageOptions setImageOptionsConfig(){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)//设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)//设置图片Uri为null或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.ic_launcher)//设置图片加载/解码过程中错误时显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)//是否考虑JPEG图像的旋转,翻转
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置和复位
                .displayer(new SimpleBitmapDisplayer())//不设置的时候是默认的
//                .displayer(new RoundedBitmapDisplayer(30))//是否为圆角,弧度是多少
                        //displayer()还可以设置渐入动画
                .build();
        return options;
    }

}
