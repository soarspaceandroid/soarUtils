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
import java.util.List;

import ace.soar.frame.utils.machine.BaseStatesMachine;
import ace.soar.frame.utils.machine.UIUpdateInter;

public class MainActivity extends AppCompatActivity {

    private TextView tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tip = (TextView)findViewById(R.id.tip);
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
        list.add(machine.new BaseState(1));
        list.add(machine.new BaseState(2));
        list.add(machine.new BaseState(3));
        list.add(machine.new BaseState(4));
        machine.setStatesData(list);
        machine.setUIUpdateInter(new UIUpdateInter() {
            @Override
            public void updateUIByStatus(int state) {
                switch (state) {
                    case 1:

                        Log.e("soar", "state -- -1");
                        break;
                    case 2:
                        Log.e("soar", "state -- -2");
                        break;
                    case 3:
                        Log.e("soar", "state -- -3");
                        break;
                    case 4:
                        Log.e("soar", "state -- -4");
                        break;
                }
            }
        });
        machine.start();


        tip.postDelayed(new Runnable() {
            @Override
            public void run() {
                machine.changeToState(1);
            }
        }, 5000);

        tip.postDelayed(new Runnable() {
            @Override
            public void run() {
                machine.changeToState(2);
            }
        },10000);

        tip.postDelayed(new Runnable() {
            @Override
            public void run() {
                machine.changeToState(3);
            }
        },15000);

        tip.postDelayed(new Runnable() {
            @Override
            public void run() {
                machine.changeToState(4);
            }
        },20000);


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
}
