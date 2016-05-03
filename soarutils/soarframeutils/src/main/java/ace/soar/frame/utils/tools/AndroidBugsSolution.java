package ace.soar.frame.utils.tools;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

/**
 * Auth：yujunyao
 * Since: 2016/4/14 10:29
 * Email：yujunyao@yonglibao.com
 * 解决values-v19以上style中添加<item name="android:windowTranslucentStatus">true</item>配置时，导致软键盘弹起关闭监听失效的bug
 * 需要使用此类的Activity如果继承的是BaseActivity(使用Appbar)就会导致下面已知问题
 * 1.键盘弹出的时候 Appbar 将会被 Rect代替 , Appbar消失 ,使用Appbar的Activity如果监听键盘弹出消失 不建议使用该方法
 */
public class AndroidBugsSolution {

    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.

    /**
     * 关于 全屏Activity adjustResize属性无效解决方案
     * 在Activity setContentView 之后调用
     *
     * @param activity
     * @param listener 软键盘显示隐藏监听, 可以为空
     */
    public static void assistActivity(Activity activity, OnKeyboardListener listener) {
        boolean subOffset = false;
        if (Build.VERSION.SDK_INT < 18) {
            subOffset = true;
        }
        new AndroidBugsSolution(activity, subOffset, listener);
    }

    private boolean mSubOffset;
    private int mCurrHeight;
    private Rect mRootRect;
    private View mContentView;
    private LayoutParams mLayoutParams;
    private OnKeyboardListener mListener;

    /**
     *
     * @param activity
     * @param isSubOffset 是否需要偏移
     * @param listener 软键盘监听 监听隐藏，显示
     */
    private AndroidBugsSolution(final Activity activity, boolean isSubOffset, OnKeyboardListener listener) {
        this.mSubOffset = isSubOffset;
        this.mListener = listener;
        this.mContentView = ((FrameLayout) activity.findViewById(android.R.id.content)).getChildAt(0);
        this.mLayoutParams = (LayoutParams) mContentView.getLayoutParams();
        this.mContentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                possiblyResizeChildOfContent();
            }
        });
        activity.getWindow().getDecorView().getRootView().getWindowVisibleDisplayFrame(mRootRect = new Rect());
    }

    private void possiblyResizeChildOfContent() {
        Rect r = new Rect();
        mContentView.getWindowVisibleDisplayFrame(r);
        int usableHeightNow = r.height();
        // Log.i("III", "usableHeightNow " + usableHeightNow + ", r " + r.toString());
        if (usableHeightNow != mCurrHeight) {
            int usableHeightSansKeyboard = r.bottom;
            if(mSubOffset) {
                usableHeightSansKeyboard -= r.top;
            }
            // android.util.Log.i("III", "usableHeightSansKeyboard " + usableHeightSansKeyboard + ", r " + r.toString());
            // keyboard probably just became hidden
            mLayoutParams.height = usableHeightSansKeyboard;
            if (mListener != null) {
                if (mRootRect.bottom != r.bottom) {
                    mListener.onKeyboardChanged(SHOW_KEYBOARD);
                } else {
                    mListener.onKeyboardChanged(HIDE_KEYBOARD);
                }
            }

            mContentView.requestLayout();
            mCurrHeight = usableHeightNow;
        }
    }

    public static final int SHOW_KEYBOARD = 1;
    public static final int HIDE_KEYBOARD = 2;

    public interface OnKeyboardListener {
        void onKeyboardChanged(int state);
    }

}
