package ace.soar.frame.utils.net;

/**
 * Created by gaofei on 2016/5/3.
 */
public interface RequestListener<T> {

    public void requestSuccess(T t);

    public void requestFail(int code , String msg , String result);
}
