package com.mtuity.animationdemo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

public class DemoGameThread extends Thread {

    private boolean mRun = false;

    public enum AnimateState {
        asReady, asRunning, asPause;
    }

    private SurfaceHolder surfaceHolder;
    private AnimateState state;
    private Paint paint;
    private DrawCallBack mCallBack;

    DemoGameThread(int width, int height, SurfaceHolder surfaceHolder, DrawCallBack callBack) {
        this.surfaceHolder = surfaceHolder;
        mCallBack = callBack;
        paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
    }

    public void doStart() {
        synchronized (surfaceHolder) {
            setState(AnimateState.asRunning);
        }
    }

    public void pause() {
        synchronized (surfaceHolder) {
            if (state == AnimateState.asRunning)
                setState(AnimateState.asPause);
        }
    }

    public void unPause() {
        setState(AnimateState.asRunning);
    }

    @Override
    public void run() {
        while (mRun) {
            Canvas c = null;
            try {
                c = surfaceHolder.lockCanvas(null);

                synchronized (surfaceHolder) {
                    if (state == AnimateState.asRunning)
                        doDraw(c);
                }
            } finally {
                if (c != null) {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

    public void setRunning(boolean b) {
        mRun = b;
    }

    public void setState(AnimateState state) {
        synchronized (surfaceHolder) {
            this.state = state;
        }
    }

    public void doDraw(Canvas canvas) {
        mCallBack.doDraw(canvas, paint);
    }

    public void setSurfaceSize(int width, int height) {
        synchronized (surfaceHolder) {
            mCallBack.reshape(width, height);
        }
    }

}
