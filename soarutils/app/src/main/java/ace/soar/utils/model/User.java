package ace.soar.utils.model;

import ace.soar.frame.utils.model.BaseModel;
import ace.soar.frame.utils.observer.DataObserver;

/**
 * Created by gaofei on 2016/4/18.
 */
public class User extends BaseModel{
    public int age ;
    public String name;


    public User(DataObserver observer, int age, String name) {
        super(observer);
        this.age = age;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setChanged();
        notifyObservers(this);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
        setChanged();
        notifyObservers(this);
    }

}
