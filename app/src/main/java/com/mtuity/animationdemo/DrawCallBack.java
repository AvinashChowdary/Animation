package com.mtuity.animationdemo;

import android.graphics.Canvas;
import android.graphics.Paint;

public interface DrawCallBack {
    void doDraw(Canvas canvas, Paint paint);

    void reshape(int width, int height);
}