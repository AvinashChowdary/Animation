package com.pcs.slidingactivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText srcEdt = (EditText) findViewById(R.id.src_city);
        EditText destEdt = (EditText) findViewById(R.id.src_city);
        srcEdt.setOnClickListener(this);
        destEdt.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.src_city:
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, 0);
                break;
            case R.id.dest_city:
                Intent mIntent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(mIntent);
                overridePendingTransition(R.anim.right_in, 0);
                break;

        }
    }
}
