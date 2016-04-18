package android.improving.utils.views.circleprogressbutton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.improving.utils.R;
import android.improving.utils.tools.countdown.TimeCount;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by gaofei on 2016/4/1.
 */
public class CircularCountDownButton extends LinearLayout {

    private TextView mCountText;
    private CircularProgressButton mCircularProgressButton;
    private ViewCountDownListener mCountDownListener ;
    private TimeCount countDown;
    private int mCountTime = 0;
    private boolean isDoneStartAnimation = false;
    private boolean isDoneFinishAnimation = false;

    private boolean mIsTranstionX = false;

    public CircularCountDownButton(Context context) {
        super(context);
    }

    public CircularCountDownButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CircularCountDownButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.circular_countdown_button, this);
        mCountText = (TextView) view.findViewById(R.id.text_count);
        mCircularProgressButton = (CircularProgressButton) view.findViewById(R.id.circularButton);
        mCircularProgressButton.setIndeterminateProgressMode(false);
        mCircularProgressButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCircularProgressButton.getProgress() != 0) {
                    return;
                }
                mCountText.setVisibility(View.VISIBLE);
                countDown = new TimeCount(mCountTime * 1000, 1000, new android.improving.utils.tools.countdown.CountDownListener() {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int second = (int) Math.ceil(millisUntilFinished / 1000d);
                        int progress = Math.round((float) 100 / mCountTime * second - (float) 100 / mCountTime);
                        mCircularProgressButton.setProgress(progress);
                        animatGone(second);
                        if (!isDoneStartAnimation && second == mCountTime-1) {
                            animatTrans();
                            isDoneStartAnimation = true;
                        }
                        if (!isDoneFinishAnimation && second == 2) {
                            animatTransRever();
                            isDoneFinishAnimation = true;
                        }
                    }

                    @Override
                    public void onFinish() {
                        mCountText.setVisibility(View.INVISIBLE);
                        mCircularProgressButton.setProgress(0);
                        mCountDownListener.finish();
                        mCountText.setText(mCountTime + "");
                        isDoneStartAnimation = false;
                        isDoneFinishAnimation = false;
                    }
                });
                countDown.start();
                mCountDownListener.start();
            }
        });
    }


    /**
     * 拦截到此view 处理事件
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return onTouchEvent(ev);
    }


    /**
     * strat count down  60s is countDownTime = 60  not 60000
     * @param countDownTime
     */
    public  void startCountDown(int countDownTime){
        this.mCountTime = countDownTime;
        mCircularProgressButton.performClick();
    }

    /**
     * set idle text display
     * @param text
     */
    public void setIdleText(String text){
        mCircularProgressButton.setIdleText(text);
    }
    /**
     * set complement text display
     * @param text
     */
    public void setCompleteText(String text){
        mCircularProgressButton.setCompleteText(text);
    }

    /**
     * set error text display
     * @param text
     */
    public void setErrorText(String text){
        mCircularProgressButton.setErrorText(text);
    }

    /**
     * cancel count down
     */
    public void cancel(){
        if(countDown != null){
            countDown.cancel();
        }
        mCircularProgressButton.setProgress(0);
        mCountText.setText("0");
        mCountText.setVisibility(View.INVISIBLE);
    }

    /**
     * set count text size
     * @param sizeSource
     */
    public void setCountTextSize(int sizeSource){
        mCountText.setText(sizeSource);
    }

    /**
     * set text color
     * @param color
     */
    public  void setCountTextColor(int color){
        mCountText.setTextColor(color);
    }


    /**
     * set count down listener
     * @param listener
     */
    public void setCountDownListener(ViewCountDownListener listener ) {
        this.mCountDownListener = listener;
    }


    /**
     * gone animation
     * @param time
     */
    private void animatGone(final int time){
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animatorTrans = ObjectAnimator.ofFloat(mCountText , "translationY" , 0 ,getHeight()/2 - getResources().getDimension(R.dimen.count_down_edge));
        ObjectAnimator animatorApha = ObjectAnimator.ofFloat(mCountText , "alpha" , 1.0f ,0f);
        set.setDuration(300);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mCountText.setText((time - 1) + "");
                animatVisible();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCountText.setText((time - 1) + "");
                animatVisible();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.setInterpolator(new DecelerateInterpolator());
        set.playTogether(animatorTrans , animatorApha);
        set.start();
    }

    /**
     * visible animation
     */
    private void animatVisible(){
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animatorTrans = ObjectAnimator.ofFloat(mCountText , "translationY" , -getHeight()/2+getResources().getDimension(R.dimen.count_down_edge) ,0);
        ObjectAnimator animatorApha = ObjectAnimator.ofFloat(mCountText , "alpha" , 0f,1.0f);
        set.setDuration(300);
        set.setInterpolator(new BounceInterpolator());
        set.playTogether(animatorTrans, animatorApha);
        set.start();
    }

    public void setInditorColor(int colorResouce){
        mCircularProgressButton.setIndicator(colorResouce);
    }

    /**
     * 倒计时开始 和结束监听
     */
   public interface ViewCountDownListener{
        void start();
        void finish();
    }

    private void animatTrans(){
        if(!mIsTranstionX){
            return;
        }
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator rotate = ObjectAnimator.ofFloat(mCircularProgressButton , "rotation" , 0f , 360f);
        ObjectAnimator transx = ObjectAnimator.ofFloat(this , "translationX" , mCircularProgressButton.getLeft() , mCircularProgressButton.getLeft()+mCircularProgressButton.getWidth()/3);
        set.playTogether(rotate, transx);
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(1000);
        set.start();
    }

    private void animatTransRever(){
        if(!mIsTranstionX){
            return;
        }
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator rotate = ObjectAnimator.ofFloat(mCircularProgressButton , "rotation" , 360f , 0f);
        ObjectAnimator transx = ObjectAnimator.ofFloat(this , "translationX" , mCircularProgressButton.getLeft()+mCircularProgressButton.getWidth()/3 , mCircularProgressButton.getLeft());
        set.playTogether(rotate , transx);
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(1000);
        set.start();
    }

    /**
     * 移位动画
     * @param isTranstionX
     */
    public void setIsTranstionX(boolean isTranstionX){
        this.mIsTranstionX = isTranstionX;
    }
}


