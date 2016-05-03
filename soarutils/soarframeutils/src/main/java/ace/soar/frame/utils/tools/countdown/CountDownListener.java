package ace.soar.frame.utils.tools.countdown;

/**
 * Auth：yujunyao
 * Since: 16/3/28 上午11:45
 * Email：yujunyao@yonglibao.com
 */

public interface CountDownListener {

    void onTick(long millisUntilFinished);

    void onFinish();

}
