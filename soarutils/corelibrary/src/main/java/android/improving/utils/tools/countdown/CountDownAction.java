package android.improving.utils.tools.countdown;

/**
 * Auth：yujunyao
 * Since: 16/3/31 下午7:52
 * Email：yujunyao@yonglibao.com
 */
public class CountDownAction {

    public TimeCount mTimeCount;

    public CountDownAction(long millisInFuture, long countDownInterval, CountDownListener listener) {
        mTimeCount = new TimeCount(millisInFuture, countDownInterval, listener);
    }

    public void onStart() {
        if(mTimeCount != null) {
            mTimeCount.start();
        }
    }

    public void onCancel() {
        if(mTimeCount != null) {
            mTimeCount.cancel();
            mTimeCount = null;
        }
    }


}
