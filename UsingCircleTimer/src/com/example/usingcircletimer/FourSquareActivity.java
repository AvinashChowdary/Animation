package com.example.usingcircletimer;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foursquare.FoursquareApp;
import com.example.foursquare.FoursquareApp.FsqAuthListener;
import com.example.foursquare.FoursquareSession;
import com.example.foursquare.FsqVenue;
import com.example.foursquare.NearbyAdapter;
import com.example.foursquare.People;

public class FourSquareActivity extends Activity {
	private FoursquareApp mFsqApp;
	private ListView mListView;
	private FoursquareSession mSession;
	private NearbyAdapter mAdapter;
	private ArrayList<FsqVenue> mNearbyList;
	private ArrayList<People> names;
	private ProgressDialog mProgress;
	private EditText searchEdt;
	private Button connectBtn;

	public static final String CLIENT_ID = "SKJDWN1QCCYPUAXH5GZDIMFZKE3LXZ53MU4BY4LP5YRSC21S";
	public static final String CLIENT_SECRET = "RJYIIGPEUFKN1KXU3EDA5RLSLCT3UH0WAOTBLVWVJGOPJDNG";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.four_square);

		final TextView nameTv = (TextView) findViewById(R.id.tv_name);
		connectBtn = (Button) findViewById(R.id.b_connect);
		final EditText latitudeEt = (EditText) findViewById(R.id.et_latitude);
		final EditText longitudeEt = (EditText) findViewById(R.id.et_longitude);
		Button goBtn = (Button) findViewById(R.id.b_go);
		mListView = (ListView) findViewById(R.id.lv_places);

		mFsqApp = new FoursquareApp(this, CLIENT_ID, CLIENT_SECRET);
		if(mFsqApp.hasAccessToken()){
			connectBtn.setText("Logout of Fsq");
			nameTv.setText("Connected as " + mFsqApp.getUserName());
		}else{
			connectBtn.setText("Login to Fsq");
		}
		mAdapter = new NearbyAdapter(this);
		mNearbyList = new ArrayList<FsqVenue>();
		names = new ArrayList<People>();
		mProgress = new ProgressDialog(this);

		mProgress.setMessage("Loading data ...");

		FsqAuthListener listener = new FsqAuthListener() {
			@Override
			public void onSuccess() {
				Toast.makeText(FourSquareActivity.this,
						"Connected as " + mFsqApp.getUserName(),
						Toast.LENGTH_SHORT).show();
				nameTv.setText("Connected as " + mFsqApp.getUserName());
			}

			@Override
			public void onFail(String error) {
				Toast.makeText(FourSquareActivity.this, error,
						Toast.LENGTH_SHORT).show();
			}
		};

		mFsqApp.setListener(listener);
		mSession = new FoursquareSession(this);
		// get access token and user name from foursquare
		connectBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mFsqApp.hasAccessToken()){
					mSession.resetAccessToken();
					connectBtn.setText("Login to Fsq");
					
				}else{
					mFsqApp.authorize();
				}
			}
		});

		// use access token to get nearby places
		goBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String latitude = latitudeEt.getText().toString();
				String longitude = longitudeEt.getText().toString();

				if (latitude.equals("") || longitude.equals("")) {
					Toast.makeText(FourSquareActivity.this,
							"Latitude or longitude is empty",
							Toast.LENGTH_SHORT).show();
					return;
				}

				double lat = Double.valueOf(latitude);
				double lon = Double.valueOf(longitude);

				loadNearbyPlaces(lat, lon);
			}
		});
		searchEdt = (EditText) findViewById(R.id.search);
		Button searchBtn = (Button) findViewById(R.id.searcb_btn);
		searchBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String data = searchEdt.getText().toString();
				if (data.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Enter data to search", Toast.LENGTH_LONG).show();
				} else {
					if (data.matches("[0-9]+") && data.length() == 10) {
						searchPeople(data, true);
					} else {
						searchPeople(data, false);
					}
				}
			}
		});
	}

	private void searchPeople(final String data, final boolean isNumber) {
		mProgress.show();
		new Thread() {
			@Override
			public void run() {
				try {
					names = mFsqApp.getPeople(data, isNumber);
				} catch (Exception e) {
					e.printStackTrace();
				}
				mHandler.sendMessage(mHandler.obtainMessage(5));
			}
		}.start();

	}

	private void loadNearbyPlaces(final double latitude, final double longitude) {
		mProgress.show();

		new Thread() {
			@Override
			public void run() {
				int what = 0;

				try {

					mNearbyList = mFsqApp.getNearby(latitude, longitude);
				} catch (Exception e) {
					what = 1;
					e.printStackTrace();
				}

				mHandler.sendMessage(mHandler.obtainMessage(what));
			}
		}.start();
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mProgress.dismiss();

			if (msg.what == 0) {
				if (mNearbyList.size() == 0) {
					Toast.makeText(FourSquareActivity.this,
							"No nearby places available", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				mAdapter.setData(mNearbyList);
				mListView.setAdapter(mAdapter);
			}
			if (msg.what == 5) {
				PeopleAdapter adapter = new PeopleAdapter(
						getApplicationContext(), names);
				mListView.setAdapter(adapter);
			} else {
				Toast.makeText(FourSquareActivity.this,
						"Failed to load nearby places", Toast.LENGTH_SHORT)
						.show();
			}
		}
	};
}
