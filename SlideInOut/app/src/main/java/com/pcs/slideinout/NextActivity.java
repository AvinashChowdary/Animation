package com.pcs.slideinout;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by pcs20 on 4/2/15.
 */
public class NextActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_screen);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
