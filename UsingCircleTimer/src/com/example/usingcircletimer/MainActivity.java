package com.example.usingcircletimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends Activity {

	private ProgressBar mProgressBar;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mProgressBar = (ProgressBar) findViewById(R.id.circle_progress_bar);
		Button btn = (Button) findViewById(R.id.btn);
		Button fsqBtn = (Button) findViewById(R.id.fsq_btn);
		fsqBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, FourSquareActivity.class);
				startActivity(intent);
			}
		});
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					int i = 0;
					int progressStatus = 0;

					public void run() {
						while (progressStatus < 100) {
							progressStatus += 5;
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

							// Update the progress bar
							handler.post(new Runnable() {
								public void run() {
										mProgressBar
												.setProgress(progressStatus);
									i++;
								}
							});
						}
					}
				}).start();
			}
		});
	}
}
