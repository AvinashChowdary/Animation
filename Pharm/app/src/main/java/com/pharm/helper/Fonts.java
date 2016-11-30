package com.pharm.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by Avinash
 */
public class Fonts {

    private static final String TAG = Fonts.class.getSimpleName();

    private static Fonts fonts;
    private Typeface medium;
    private Typeface regular;
    private Typeface light;

    private Fonts(Context context) {
        light = Typeface.createFromAsset(context.getAssets(), "roboto_thin.ttf");

    }

    public static Fonts getInstance(Context context) {
        if (fonts == null) {
            fonts = new Fonts(context);
        }
        return fonts;
    }

    /**
     * Sets the medium font.
     * @param view the view
     */
    public void setMediumFont(View view) {
        setFont(view, medium, Typeface.NORMAL);
    }

    /**
     * Sets the regular font.
     * @param view the view
     */
    public void setRegularFont(View view) {
        setFont(view, regular, Typeface.NORMAL);
    }

    public void setLightFont(View view) {
        setFont(view, light, Typeface.NORMAL);
    }

    private void setFont(View view, Typeface typeFace, int type) {
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(typeFace, type);
        } else if (view instanceof EditText) {
            ((EditText) view).setTypeface(typeFace, type);
        } else if (view instanceof Button) {
            ((Button) view).setTypeface(typeFace, type);
        } else if (view instanceof RadioButton) {
            ((RadioButton) view).setTypeface(typeFace, type);
        } else {
            Log.i(TAG, "Handled only for TextView, EditText and Buttons");
        }
    }




}
