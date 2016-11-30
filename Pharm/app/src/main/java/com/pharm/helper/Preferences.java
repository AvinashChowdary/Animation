package com.pharm.helper;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.pharm.activities.App;

/**
 * @author Avinash
 * Class to save Preferences
 */
public class Preferences {
    private static Preferences pref;
    private static SharedPreferences prefObj;
    private static Editor prefsEditor;

    public static Preferences getIns() {
        if (pref == null) {
            pref = new Preferences();
            prefObj = PreferenceManager.getDefaultSharedPreferences(App
                    .get());
            prefsEditor = prefObj.edit();
        }
        return pref;
    }

    public SharedPreferences getSharedPref() {
        if (prefObj == null) {
            prefObj = PreferenceManager.getDefaultSharedPreferences(App
                    .get());
        }
        return prefObj;
    }

    public Editor getEditor() {
        if (prefsEditor == null) {
            prefsEditor = getSharedPref().edit();
        }
        return prefsEditor;
    }


    /**
     * Store first name.
     *
     * @param firstName the first name
     */
    public void storeFirstName(String firstName) {
        getEditor().putString(Constants.FIRST_NAME, firstName).commit();
    }

    /**
     * Gets the first name.
     *
     * @return the first name if not null, otherwise null
     */
    public String getFirstName() {
        return getSharedPref().getString(Constants.FIRST_NAME, "");
    }


    public boolean getFB() {
        return getSharedPref().getBoolean(Constants.FACEBOOK, false);
    }

    public void setFb(boolean bool) {
        getEditor().putBoolean(Constants.FACEBOOK, bool).commit();
    }

    public void setLocation(String location) {
        getEditor().putString(Constants.LOCATION, location).commit();
    }

    public String getLocation() {
        return getSharedPref().getString(Constants.LOCATION, "");
    }

    public void setUserName(String name) {
        getEditor().putString(Constants.USER_NAME, name).commit();
    }

    public String getUserName() {
        return getSharedPref().getString(Constants.USER_NAME, "");
    }

    public void setUserFBId(String fbid) {
        getEditor().putString(Constants.FBID, fbid).commit();
    }

    public String getUserFBId() {
        return getSharedPref().getString(Constants.FBID, "");
    }

    public void setUserEmail(String email) {
        getEditor().putString(Constants.EMAIL, email).commit();
    }

    public String getUserEmail() {
        return getSharedPref().getString(Constants.EMAIL, "");
    }

    public void setUserGender(String gender) {
        getEditor().putString(Constants.GENDER, gender).commit();
    }

    public String getUserGender() {
        return getSharedPref().getString(Constants.GENDER, "");
    }

    public void setUserBirthday(String birthday) {
        getEditor().putString(Constants.BIRTHDAY, birthday).commit();
    }

    public String getUserBirthday() {
        return getSharedPref().getString(Constants.BIRTHDAY, "");
    }

    public void setUserProfilePicture(String picture) {
        getEditor().putString(Constants.PROFILE_PICTURE, picture).commit();
    }

    public String getUserProfilePicture() {
        return getSharedPref().getString(Constants.PROFILE_PICTURE, "");
    }

    public void setUserNumber(String number) {
        getEditor().putString(Constants.PHONE_NUMBER, number).commit();
    }

    public String getUserNumber() {
        return getSharedPref().getString(Constants.PHONE_NUMBER, "");
    }
}
