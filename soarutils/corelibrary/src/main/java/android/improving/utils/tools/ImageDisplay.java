package android.improving.utils.tools;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
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

}
