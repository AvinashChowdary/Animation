package com.pharm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.pharm.R;
import com.pharm.helper.Preferences;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by Avinash
 */
public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private Toolbar toolbar;

    private CollapsingToolbarLayout collapsingToolbarLayout;

    private EditText nameEdt;

    private EditText numberEdt;

    private EditText emailEdt;

    private TextView dobtxt;

    private CallbackManager callbackManager;

    private AccessTokenTracker accessTokenTracker;

    private boolean isResumed = false;

    private ImageView img;

    private AppBarLayout appBarLayout;

    private ImageView save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_settings);
        FacebookSdk.sdkInitialize(getApplicationContext());
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        callbackManager = CallbackManager.Factory.create();
        collapsingToolbarLayout.setTitle(Preferences.getIns().getUserName());
        setSupportActionBar(toolbar);
        save = (ImageView) findViewById(R.id.save_btn);
        save.setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        img = (ImageView) findViewById(R.id.image);
        initView();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_photos", "public_profile", "user_birthday", "user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                String name = "";
                                String id = "";
                                String gender = "";
                                String email = "";
                                String profilePicture = "";
                                String birthday = "";

                                try {
                                    name = object.getString("name");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    id = object.getString("id");
                                    profilePicture = "https://graph.facebook.com/"+id+"/picture?type=large&redirect=true&width=600&height=600";
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    gender = object.getString("gender");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    birthday = object.getString("birthday");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    email = object.getString("email");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (!TextUtils.isEmpty(name)) {
                                    Preferences.getIns().setUserName(name);
                                }
                                if (!TextUtils.isEmpty(id)) {
                                    Preferences.getIns().setUserFBId(id);
                                }
                                if (!TextUtils.isEmpty(email)) {
                                    Preferences.getIns().setUserEmail(email);
                                }
                                if (!TextUtils.isEmpty(gender)) {
                                    Preferences.getIns().setUserGender(gender);
                                }
                                if (!TextUtils.isEmpty(birthday)) {
                                    Preferences.getIns().setUserBirthday(birthday);
                                }
                                if (!TextUtils.isEmpty(profilePicture)) {
                                    Preferences.getIns().setUserProfilePicture(profilePicture);
                                }

                                Preferences.getIns().setFb(true);
                                initView();
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,birthday,picture,email,gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (isResumed) {
                    if (currentAccessToken != null) {
                    } else {
                        Preferences.getIns().setUserName("");
                        Preferences.getIns().setUserFBId("");
                        Preferences.getIns().setUserEmail("");
                        Preferences.getIns().setUserGender("");
                        Preferences.getIns().setUserBirthday("");
                        Preferences.getIns().setUserProfilePicture("");
                        Preferences.getIns().setFb(false);
                        clearUserData();
                    }
                }
            }
        };

    }

    private void clearUserData() {
        nameEdt.setText("");
        numberEdt.setText("");
        emailEdt.setText("");
        dobtxt.setText("");
        img.setImageBitmap(null);
        collapsingToolbarLayout.setTitle("Settings");
        appBarLayout.setExpanded(false);
    }

    private void initView() {
        nameEdt = (EditText) findViewById(R.id.name_edt);
        numberEdt = (EditText) findViewById(R.id.phone_edt);
        emailEdt = (EditText) findViewById(R.id.email_edt);
        dobtxt = (TextView) findViewById(R.id.calendar_txt);

        nameEdt.setText(Preferences.getIns().getUserName());
        numberEdt.setText(Preferences.getIns().getUserNumber());
        emailEdt.setText(Preferences.getIns().getUserEmail());
        dobtxt.setText(Preferences.getIns().getUserBirthday());
        dobtxt.setOnClickListener(this);
        collapsingToolbarLayout.setTitle(Preferences.getIns().getUserName());
        appBarLayout.setExpanded(true);
        Picasso.with(SettingsActivity.this).load(Preferences.getIns().getUserProfilePicture()).into(img);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onResume() {
        super.onResume();
        isResumed = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isResumed = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_btn:

                break;
            case R.id.calendar_txt:
                showDatePicker();
                break;
        }
    }

    private void showDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                SettingsActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        dobtxt.setText(date);
        Preferences.getIns().setUserBirthday(date);
    }
}
