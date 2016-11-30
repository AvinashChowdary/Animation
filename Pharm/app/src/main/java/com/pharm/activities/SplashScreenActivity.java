package com.pharm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.eftimoff.androidplayer.Player;
import com.eftimoff.androidplayer.actions.property.PropertyAction;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.pharm.R;
import com.pharm.helper.Fonts;
import com.pharm.helper.Numerics;
import com.pharm.helper.Preferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Avinash
 */
public class SplashScreenActivity extends AppCompatActivity {

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.splash);
        callbackManager = CallbackManager.Factory.create();
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
                                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                                SplashScreenActivity.this.finish();
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

        findViewById(R.id.btn_skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences.getIns().setFb(false);
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                SplashScreenActivity.this.finish();
            }
        });
        setFonts();
        delayedAnimation();

    }

    private void setFonts() {
        Fonts.getInstance(SplashScreenActivity.this).setLightFont(findViewById(R.id.about));
        Fonts.getInstance(SplashScreenActivity.this).setLightFont(findViewById(R.id.btn_skip));
    }


    private void delayedAnimation() {
        Runnable splashRunnable = new Runnable() {
            @Override
            public void run() {
                if (Preferences.getIns().getFB()) {
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    SplashScreenActivity.this.finish();
                } else {
                    showAnimation();
                }
            }
        };
        new Handler().postDelayed(splashRunnable, Numerics.THREE_THOUSAND);
    }


    private void showAnimation() {

        findViewById(R.id.about).setVisibility(View.VISIBLE);
        findViewById(R.id.ll).setVisibility(View.VISIBLE);

        PropertyAction text = PropertyAction.newPropertyAction(findViewById(R.id.about)).
                scaleX(0).
                scaleY(0).
                duration(Numerics.FIVE_HUNDRED).
                interpolator(new AccelerateDecelerateInterpolator()).
                build();

        PropertyAction skip = PropertyAction.newPropertyAction(findViewById(R.id.ll)).
                scaleX(0).
                scaleY(0).
                duration(Numerics.FIVE_HUNDRED).
                interpolator(new AccelerateDecelerateInterpolator()).
                build();

        Player.init().
                animate(text).
                then().
                animate(skip).
                play();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }
}