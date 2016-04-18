package ace.soar.frame.utils.observer;

import java.util.Observable;

/**
 * Created by gaofei on 2016/4/18.
 */
public class DataObserable extends Observable {

    public DataObserable(DataObserver observer) {
        this.addObserver(observer);
    }

}
