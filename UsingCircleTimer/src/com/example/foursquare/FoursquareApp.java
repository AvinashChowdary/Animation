package com.example.foursquare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.foursquare.FoursquareDialog.FsqDialogListener;


public class FoursquareApp {
	private FoursquareSession mSession;
	private FoursquareDialog mDialog;
	private FsqAuthListener mListener;
	private ProgressDialog mProgress;
	private String mTokenUrl;
	private String mAccessToken;

	/**
	 * Callback url, as set in 'Manage OAuth Costumers' page
	 * (https://developer.foursquare.com/)
	 */
	public static final String CALLBACK_URL = "https://developer.foursquare.com/";
	private static final String AUTH_URL = "https://foursquare.com/oauth2/authenticate?response_type=code";
	private static final String TOKEN_URL = "https://foursquare.com/oauth2/access_token?grant_type=authorization_code";
	private static final String API_URL = "https://api.foursquare.com/v2";

	private static final String TAG = "FoursquareApi";

	public FoursquareApp(Context context, String clientId, String clientSecret) {
		mSession = new FoursquareSession(context);

		mAccessToken = mSession.getAccessToken();

		mTokenUrl = TOKEN_URL + "&client_id=" + clientId + "&client_secret="
				+ clientSecret + "&redirect_uri=" + CALLBACK_URL;

		String url = AUTH_URL + "&client_id=" + clientId + "&redirect_uri="
				+ CALLBACK_URL;

		FsqDialogListener listener = new FsqDialogListener() {
			@Override
			public void onComplete(String code) {
				getAccessToken(code);
			}

			@Override
			public void onError(String error) {
				mListener.onFail("Authorization failed");
			}
		};

		mDialog = new FoursquareDialog(context, url, listener);
		mProgress = new ProgressDialog(context);

		mProgress.setCancelable(false);
	}

	private void getAccessToken(final String code) {
		mProgress.setMessage("Getting access token ...");
		mProgress.show();

		new Thread() {
			@Override
			public void run() {
				Log.i(TAG, "Getting access token");

				int what = 0;

				try {
					URL url = new URL(mTokenUrl + "&code=" + code);

					Log.i(TAG, "Opening URL " + url.toString());

					HttpURLConnection urlConnection = (HttpURLConnection) url
							.openConnection();

					urlConnection.setRequestMethod("GET");
					urlConnection.setDoInput(true);

					urlConnection.connect();

					JSONObject jsonObj = (JSONObject) new JSONTokener(
							streamToString(urlConnection.getInputStream()))
							.nextValue();
					mAccessToken = jsonObj.getString("access_token");

					Log.i(TAG, "Got access token: " + mAccessToken);
				} catch (Exception ex) {
					what = 1;

					ex.printStackTrace();
				}

				mHandler.sendMessage(mHandler.obtainMessage(what, 1, 0));
			}
		}.start();
	}

	private void fetchUserName() {
		mProgress.setMessage("Finalizing ...");

		new Thread() {
			@Override
			public void run() {
				Log.i(TAG, "Fetching user name");
				int what = 0;

				try {
					String v = timeMilisToString(System.currentTimeMillis());
					URL url = new URL(API_URL + "/users/self?oauth_token="
							+ mAccessToken + "&v=" + v);

					Log.d(TAG, "Opening URL " + url.toString());

					HttpURLConnection urlConnection = (HttpURLConnection) url
							.openConnection();

					urlConnection.setRequestMethod("GET");
					urlConnection.setDoInput(true);
					// urlConnection.setDoOutput(true);

					urlConnection.connect();

					String response = streamToString(urlConnection
							.getInputStream());
					JSONObject jsonObj = (JSONObject) new JSONTokener(response)
							.nextValue();

					JSONObject resp = (JSONObject) jsonObj.get("response");
					JSONObject user = (JSONObject) resp.get("user");

					String firstName = user.getString("firstName");
					String lastName = user.getString("lastName");

					Log.i(TAG, "Got user name: " + firstName + " " + lastName);

					mSession.storeAccessToken(mAccessToken, firstName + " "
							+ lastName);
				} catch (Exception ex) {
					what = 1;

					ex.printStackTrace();
				}

				mHandler.sendMessage(mHandler.obtainMessage(what, 2, 0));
			}
		}.start();
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.arg1 == 1) {
				if (msg.what == 0) {
					fetchUserName();
				} else {
					mProgress.dismiss();

					mListener.onFail("Failed to get access token");
				}
			} else {
				mProgress.dismiss();

				mListener.onSuccess();
			}
		}
	};

	public boolean hasAccessToken() {
		return (mAccessToken == null) ? false : true;
	}

	public void setListener(FsqAuthListener listener) {
		mListener = listener;
	}

	public String getUserName() {
		return mSession.getUsername();
	}

	public void authorize() {
		mDialog.show();
	}

	public ArrayList<FsqVenue> getNearby(double latitude, double longitude)
			throws Exception {
		ArrayList<FsqVenue> venueList = new ArrayList<FsqVenue>();

		try {
			String v = timeMilisToString(System.currentTimeMillis());
			String ll = String.valueOf(latitude) + ","
					+ String.valueOf(longitude);
			URL url = new URL(API_URL + "/venues/search?ll=" + ll
					+ "&oauth_token=" + mAccessToken + "&v=" + v);

			Log.d(TAG, "Opening URL " + url.toString());

			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();

			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			// urlConnection.setDoOutput(true);

			urlConnection.connect();

			String response = streamToString(urlConnection.getInputStream());
			JSONObject jsonObj = (JSONObject) new JSONTokener(response)
					.nextValue();

			JSONArray groups = (JSONArray) jsonObj.getJSONObject("response")
					.getJSONArray("venues");

			int length = groups.length();

			if (length > 0) {
				for (int i = 0; i < length; i++) {
					JSONObject group = (JSONObject) groups.get(i);

					FsqVenue venue = new FsqVenue();

					venue.id = group.getString("id");
					venue.name = group.getString("name");

					JSONObject location = (JSONObject) group
							.getJSONObject("location");

					Location loc = new Location(LocationManager.GPS_PROVIDER);

					loc.setLatitude(Double.valueOf(location.getString("lat")));
					loc.setLongitude(Double.valueOf(location.getString("lng")));

					venue.location = loc;
					if (location.has("address"))
						venue.address = location.getString("address");
					venue.distance = location.getInt("distance");
					venue.herenow = group.getJSONObject("hereNow").getInt(
							"count");
					if (group.has("type"))
						venue.type = group.getString("type");

					venueList.add(venue);
					// }
				}
			}
		} catch (Exception ex) {
			throw ex;
		}

		return venueList;
	}

	private String streamToString(InputStream is) throws IOException {
		String str = "";

		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));

				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				reader.close();
			} finally {
				is.close();
			}

			str = sb.toString();
		}

		return str;
	}

	private String timeMilisToString(long milis) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();

		calendar.setTimeInMillis(milis);

		return sd.format(calendar.getTime());
	}

	public interface FsqAuthListener {
		public abstract void onSuccess();

		public abstract void onFail(String error);
	}

	public ArrayList<People> getPeople(String data, boolean isNumber) {
		String searchUrl = null;
		String v = timeMilisToString(System.currentTimeMillis());
		ArrayList<People> peopleList = new ArrayList<People>();

		if (isNumber) {
			searchUrl = "https://api.foursquare.com/v2/users/search?oauth_token="
					+ mAccessToken + "&v=" + v + "&phone=" + data;
		} else {
			searchUrl = "https://api.foursquare.com/v2/users/search?oauth_token="
					+ mAccessToken + "&v=" + v + "&name=" + data;
		}

		try {
			URL url = new URL(searchUrl);
			URLConnection connection;
			try {
				connection = url.openConnection();

				// opening connection
				connection.connect();
				// getting inpust stream and readers
				InputStream inputStream = connection.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(
						inputStream);
				BufferedReader bufferReader = new BufferedReader(
						inputStreamReader);

				StringBuilder stringBuilder = new StringBuilder();
				String line;
				while ((line = bufferReader.readLine()) != null) {
					// updating the progress in progress bar
					stringBuilder.append(line);
				}
				String result = stringBuilder.toString();

				JSONObject jsonObj;
				try {
					jsonObj = (JSONObject) new JSONTokener(result).nextValue();

					JSONArray groups = (JSONArray) jsonObj.getJSONObject(
							"response").getJSONArray("results");

					int length = groups.length();

					if (length > 0) {
						for (int i = 0; i < length; i++) {
							JSONObject group = (JSONObject) groups.get(i);

							People people = new People();
							people.firstName = group.getString("firstName");
							people.lastName = group.getString("lastName");
							people.gender = group.getString("gender");

							peopleList.add(people);

						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		Log.e("List of people", peopleList.toString());
		return peopleList;
	}
}