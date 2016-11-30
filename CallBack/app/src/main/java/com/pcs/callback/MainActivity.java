package com.pcs.callback;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity implements SubClass.MyCallbackClass {

    private TextView resultTxt;

    private SubClass mySubClass;

    public static Boolean fromCallBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button callBackBtn = (Button) findViewById(R.id.call_back_btn);
        resultTxt = (TextView) findViewById(R.id.result);

        mySubClass = new SubClass();

        mySubClass.registerCallback(this);

        callBackBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mySubClass.doSomething();
            }
        });


    }

    @Override
    public void callbackReturn() {
        if(fromCallBack) {
            resultTxt.setText("Call Back True"+"\n\n\n"+"From Call Back");
        }
        else {
            resultTxt.setText("Call Back False"+"\n\n\n"+"Not From Call Back");
        }
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
