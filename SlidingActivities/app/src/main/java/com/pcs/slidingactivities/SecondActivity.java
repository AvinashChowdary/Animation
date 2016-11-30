package com.pcs.slidingactivities;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by pcs20 on 27/1/15.
 */
public class SecondActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_cities);
    }

    @Override
    public void onBackPressed()
    {

        super.onBackPressed();
        overridePendingTransition  (0, R.anim.right_out);
    }
}
