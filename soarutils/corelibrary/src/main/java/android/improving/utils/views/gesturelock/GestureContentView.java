package android.improving.utils.views.gesturelock;

import android.content.Context;
import android.improving.utils.R;
import android.improving.utils.tools.ScreenUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 手势密码容器类
 * Auth：yujunyao
 * Since: 16/3/31 下午2:04
 * Email：yujunyao@yonglibao.com
 */
public class GestureContentView extends ViewGroup {

    //*/ 手势密码点的状态
    public static final int POINT_STATE_NORMAL = 0; // 正常状态

    public static final int POINT_STATE_SELECTED = 1; // 按下状态

    public static final int POINT_STATE_WRONG = 2; // 错误状态

    private final static int BASE_NUM = 6;

    private int[] screenDispaly;

    /**
     * 每个点区域的宽度
     */
    private int blockWidth;
    /**
     * 声明一个集合用来封装坐标集合
     */
    private List<GesturePoint> list;
    private Context context;
    private boolean isVerify;
    private GestureDrawline gestureDrawline;


    public GestureContentView(Context context) {
        super(context);
        this.context = context;
    }

    public GestureContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public GestureContentView(Context context, AttributeSet attrs, int defStype) {
        super(context, attrs, defStype);
        this.context = context;
    }


    /**
     * 包含9个ImageView的容器，初始化
     * @param isVerify 是否为校验手势密码
     * @param passWord 用户传入密码
     * @param callBack 手势绘制完毕的回调
     */
    public void init(boolean isVerify, String passWord, GestureDrawline.GestureCallBack callBack) {
        screenDispaly = ScreenUtils.getScreenDispaly(context);
        blockWidth = screenDispaly[0]/3;
        this.list = new ArrayList<>();
        this.isVerify = isVerify;
        // 添加9个图标
        addChild();
        // 初始化一个可以画线的view
        gestureDrawline = new GestureDrawline(context);
        gestureDrawline.init(list, isVerify, passWord, callBack);
    }

    private void addChild(){
        for (int i = 0; i < 9; i++) {
            ImageView image = new ImageView(context);
            image.setBackgroundResource(R.mipmap.gesture_node_normal);
            this.addView(image);
            invalidate();
            // 第几行
            int row = i / 3;
            // 第几列
            int col = i % 3;
            // 定义点的每个属性
            int leftX = col * blockWidth + blockWidth / BASE_NUM;
            int topY = row * blockWidth + blockWidth / BASE_NUM;
            int rightX = col * blockWidth + blockWidth - blockWidth / BASE_NUM;
            int bottomY = row * blockWidth + blockWidth - blockWidth / BASE_NUM;
            GesturePoint p = new GesturePoint(leftX, rightX, topY, bottomY, image,i+1);
            this.list.add(p);
        }
    }

    public void setParentView(ViewGroup parent){
        // 得到屏幕的宽度
        int width = screenDispaly[0];
        LayoutParams layoutParams = new LayoutParams(width, width);
        this.setLayoutParams(layoutParams);
        gestureDrawline.setLayoutParams(layoutParams);
        parent.addView(gestureDrawline);
        parent.addView(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            //第几行
            int row = i /3 ;
            //第几列
            int col = i % 3;

            int leftX = col * blockWidth + blockWidth / BASE_NUM;
            int topY = row * blockWidth + blockWidth / BASE_NUM;
            int rightX = col * blockWidth + blockWidth - blockWidth / BASE_NUM;
            int bottomY = row * blockWidth + blockWidth - blockWidth / BASE_NUM;

            View v = getChildAt(i);
            v.layout(leftX, topY, rightX, bottomY);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 遍历设置每个子view的大小
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * 保留路径delayTime时间长
     * @param delayTime
     */
    public void clearDrawlineState(long delayTime) {
        gestureDrawline.clearDrawlineState(delayTime);
    }

}
