package ace.soar.frame.utils.machine;

import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gaofei on 2016/4/15.
 */
public class BaseStatesMachine extends StateMachine{

    private List<BaseState> mStateList = new ArrayList<>();
    private Map<Integer , Integer> mStateFlag = new HashMap<>();
    private State mDefaultState;

    private UIUpdateInter mInter;

    public BaseStatesMachine(String name) {
        super(name);
    }


    /**
     * set states data
     * @param stateList
     */
    public void setStatesData(List<BaseState> stateList){
        this. mStateList =stateList;
        mDefaultState = mStateList.get(0);
        Log.e("soar" , "set date'");
        for (int x = 0 ;x < mStateList.size() ; x++){
            if(x == 0){
                addState(mStateList.get(x) , null);
            }else{
                addState(mStateList.get(x) , mDefaultState);
            }

            mStateFlag.put(x, mStateList.get(x).getFlag());
        }
        Log.e("soar" , "set date111");
        setInitialState(mDefaultState);
    }


    /**
     * set update ui interface
     * @param inter
     */
    public void setUIUpdateInter(UIUpdateInter inter){
        this.mInter = inter;
    }

    /**
     * update UI when you change to the state
     * @param state
     */
    private void notifyUI(int state){
        Log.e("soar" ,"notifay -- "+state);
        mInter.updateUIByStatus(state);
    }

    /**
     * change to the state what you want
     * @param flag
     */
    public void changeToState(int flag){
        for (int x = 0 ; x < mStateFlag.size() ; x++){
            if(mStateFlag.get(x) == flag){
                sendMessage(x);
                break;
            }
        }

    }

    /**
     * Base state
     */
    public class BaseState extends State{
        private int flag;

        public BaseState(int flag) {
            this.flag = flag;
        }

        public int getFlag(){
            return flag;
        }

        @Override
        public void enter() {
            //进入该状态更新UI
            notifyUI(flag);
            super.enter();
        }

        @Override
        public boolean processMessage(Message msg) {
            Log.e("soar" , "process --- "+msg.what);
            transitionTo(mStateList.get(msg.what));
            return super.processMessage(msg);
        }
    }

}
