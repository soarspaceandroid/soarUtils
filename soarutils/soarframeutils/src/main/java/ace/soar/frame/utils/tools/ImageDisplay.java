package ace.soar.frame.utils.tools;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;


/**
 * Auth：yujunyao
 * Since: 2016/3/9 15:38
 * Email：yujunyao@yonglibao.com
 */
public class ImageDisplay {

    public static void imageViewDisplay(ImageView imageView, String imageUrl) {
        ImageLoader.getInstance().displayImage(imageUrl, imageView);
    }

    public static void imageViewDisplay(ImageView imageView, String imageUrl, DisplayImageOptions options) {
        ImageLoader.getInstance().displayImage(imageUrl, imageView, options);
    }

    public static void imageViewDisplay(ImageView imageView, String imageUrl, ImageLoadingListener loadingListener) {
        ImageLoader.getInstance().displayImage(imageUrl, imageView, loadingListener);
    }

    public static void imageViewDisplay(ImageView imageView, String imageUrl, DisplayImageOptions options, ImageLoadingListener loadingListener) {
        ImageLoader.getInstance().displayImage(imageUrl, imageView, options, loadingListener);
    }

    public static void imageViewDisplay(ImageView imageView, String imageUrl, DisplayImageOptions options, ImageLoadingListener loadingListener, ImageLoadingProgressListener loadingProgressListener) {
        ImageLoader.getInstance().displayImage(imageUrl, imageView, options, loadingListener, loadingProgressListener);
    }

    /**
     * 初始化ImageLoader
     */
    private void initImageLoaderConfig(Context cotext) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(cotext)
                .threadPriority(Thread.NORM_PRIORITY - 2)//设置线程优先级
                .threadPoolSize(4)//线程池内加载的数量,推荐范围1-5内。
                .denyCacheImageMultipleSizesInMemory()//当同一个Uri获取不同大小的图片缓存到内存中时只缓存一个。不设置的话默认会缓存多个不同大小的图片
                .memoryCacheExtraOptions(480, 800)//内存缓存文件的最大长度
                .memoryCache(new LruMemoryCache(10 * 1024 * 1024))//内存缓存方式,这里可以换成自己的内存缓存实现。(推荐LruMemoryCache,道理自己懂的)
                .memoryCacheSize(10 * 1024 * 1024)//内存缓存的最大值
                .diskCache(new UnlimitedDiskCache(FileUtil.createImageCache(cotext)))//可以自定义缓存路径
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//对保存的URL进行加密保存
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(cotext, 5 * 1000, 30 * 1000))//设置连接时间5s,超时时间30s
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }

}
