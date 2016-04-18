package android.improving.utils.tools.countdown;

import android.os.CountDownTimer;

/**
 * Auth：yujunyao
 * Since: 16/3/28 上午11:47
 * Email：yujunyao@yonglibao.com
 */

public class TimeCount extends CountDownTimer {

    private CountDownListener listener = null;

    /**
     *
     * @param millisInFuture 总的时间
     * @param countDownInterval 时间间隔
     * @param listener 回调监听
     */
    public TimeCount(long millisInFuture, long countDownInterval, CountDownListener listener) {
        super(millisInFuture, countDownInterval);
        this.listener = listener;
    }

    @Override
    public void onTick(long l) {
        if(listener != null) {
            listener.onTick(l);
        }
    }

    @Override
    public void onFinish() {
        if(listener != null) {
            listener.onFinish();
        }
    }
}
