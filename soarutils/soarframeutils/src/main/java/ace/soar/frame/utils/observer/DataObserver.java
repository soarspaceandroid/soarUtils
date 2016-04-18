package ace.soar.frame.utils.observer;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by gaofei on 2016/4/18.
 */
public class DataObserver implements Observer{


    private DataChangeListener dataChangeListener;

    public DataObserver(DataChangeListener dataChangeListener) {
        this.dataChangeListener = dataChangeListener;
    }

    @Override
    public void update(Observable observable, Object data) {
        if(dataChangeListener!=null) {
            dataChangeListener.dataChanged(data);
        }
    }
}
