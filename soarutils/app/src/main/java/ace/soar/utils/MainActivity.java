package ace.soar.utils;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ace.soar.frame.utils.db.DBUtils;
import ace.soar.frame.utils.machine.BaseStatesMachine;
import ace.soar.frame.utils.machine.UIUpdateInter;
import ace.soar.frame.utils.net.HttpUtils;
import ace.soar.frame.utils.net.RequestListener;
import ace.soar.frame.utils.observer.DataChangeListener;
import ace.soar.frame.utils.observer.DataObserver;
import ace.soar.utils.model.FoodBean;
import ace.soar.utils.model.User;

public class MainActivity extends AppCompatActivity implements RequestListener<FoodBean>{

        private final static int STATE_1 = 1;
        private final static int STATE_2 = 2;
        private final static int STATE_3 = 3;
        private final static int STATE_4 = 4;


    private TextView state1,state2,state3,state4,state5 ,state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        state1 = (TextView)findViewById(R.id.state1);
        state2 = (TextView)findViewById(R.id.state2);
        state3 = (TextView)findViewById(R.id.state3);
        state4 = (TextView)findViewById(R.id.state4);
        state5 = (TextView)findViewById(R.id.state5);
        state = (TextView)findViewById(R.id.state);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final BaseStatesMachine machine = new BaseStatesMachine("test");
        List<BaseStatesMachine.BaseState> list = new ArrayList<>();
        list.add(machine.new BaseState(STATE_1));
        list.add(machine.new BaseState(STATE_2));
        list.add(machine.new BaseState(STATE_3));
        list.add(machine.new BaseState(STATE_4));
        machine.setStatesData(list);
        machine.setUIUpdateInter(new UIUpdateInter() {
            @Override
            public void updateUIByStatus(final int states) {
                Log.e("soar", "get  cureent state === " + machine.getLocalCurrentState());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (states) {
                            case STATE_1:
                                state.setText("state ---- " + STATE_1);
                                break;
                            case STATE_2:
                                state.setText("state ---- " + STATE_2);
                                break;
                            case STATE_3:
                                state.setText("state ---- " + STATE_3);
                                break;
                            case STATE_4:
                                state.setText("state ---- " + STATE_4);
                                break;
                        }


                    }
                });

            }
        });
        machine.start();


        state1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                machine.changeToState(STATE_1);
            }
        });

        state2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                machine.changeToState(STATE_2);
            }
        });

        state3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                machine.changeToState(STATE_3);
            }
        });

        state4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                machine.changeToState(STATE_4);
            }
        });

        state5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                machine.changeToState(STATE_3);
            }
        });


        //test observer
        DataObserver observer = new DataObserver(new DataChangeListener<User>(){
            @Override
            public void dataChanged(User user) {
                Log.e("soar" , "data chagne "+user.getName() +"   "+user.getAge());
            }
        });
        User user = new User(observer ,10 , "test");
        user.setAge(11);
        user.setName("soar");


        Map<String , Object> map = new HashMap<>();
        map.put("name", "黄豆猪脚汤");
        HttpUtils.request(map,HttpUtils.REQUEST_GET ,this , FoodBean.class);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void requestSuccess(FoodBean foodBean) {

        try {
            DBUtils.getDbManager().saveOrUpdate(foodBean.tngou);
        }catch (Exception e){

        }
        Log.e("soar" , "get reso--- "+foodBean.tngou.get(0).img);

        try {
            Thread.sleep(10000);
        }catch (Exception e){

        }

        try {
            List<FoodBean.TngouBean> list = DBUtils.getDbManager().selector(FoodBean.TngouBean.class).findAll();
            Log.e("soar" ,  " list --- "+list.size()+"    "+list.get(1).img);
        }catch (Exception e){

        }


    }

    @Override
    public void requestFail(int code, String msg, String result) {
        Log.e("soar" , "get eroor--- "+msg);
    }
}
