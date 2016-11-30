package com.mtuity.animationdemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class DemoSurfaceView extends SurfaceView implements SurfaceHolder.Callback, DrawCallBack {

    private DemoGameThread thread;
    private long lastTime;
    private List<Ball> balls;

    public DemoSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public DemoSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DemoSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        balls = new ArrayList<>();
        createBalls();
//        setLayerType(LAYER_TYPE_SOFTWARE, null);
        lastTime = System.currentTimeMillis();
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        getHolder().addCallback(this);
        thread = new DemoGameThread(getWidth(), getHeight(), holder, this);
        setFocusable(true);

    }

    public void createBalls() {
        balls.clear();
        int total = getWidth() * getHeight() / (75 * 75);
        for (int i = 0; i < total; i++) {
            balls.add(new Ball(Math.random() * getWidth(), Math.random() * getHeight(), Math.random() * 2 - 1, Math.random() * 2 - 1));
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        thread.setSurfaceSize(width, height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        createBalls();
        thread.setRunning(true);
        thread.doStart();
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);

        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public void doDraw(Canvas canvas, Paint paint) {
        update(canvas);
        drawMethod(canvas, paint);
    }

    private void drawMethod(Canvas canvas, Paint paint1) {
        if (canvas == null) {
            return;
        }
        paint1.setColor(Color.parseColor("#020c13"));
        canvas.drawPaint(paint1);
        Paint pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Path path = new Path();
        for (int index = 0; index < balls.size(); index++) {
            Ball ball = balls.get(index);
            ball.draw(canvas);
            path.reset();
            drawSticks(canvas, pathPaint, path, index, ball);
        }
    }

    private void drawSticks(Canvas canvas, Paint pathPaint, Path path, int index, Ball ball) {
        for (int index2 = balls.size() - 1; index2 > index; index2 += -1) {
            Ball ball2 = balls.get(index2);
            double dist = Math.hypot(ball.x - ball2.x, ball.y - ball2.y);
            if (dist < 110) {
                pathPaint.setColor(Color.parseColor("#1297e2"));
                double aphaPerc = (dist > 100 ? .8 : dist / 120);
                int value = 255 - (int) (255 * aphaPerc);
                pathPaint.setAlpha(value);
                pathPaint.setStyle(Paint.Style.STROKE);
                pathPaint.setStrokeWidth(1);
                path.moveTo((float) (0.5 + ball.x), (float) (0.5 + ball.y));
                path.lineTo((float) (0.5 + ball2.x), (float) (0.5 + ball2.y));
                canvas.drawPath(path, pathPaint);
            }
        }
    }

    @Override
    public void reshape(int width, int height) {

    }

    public void update(Canvas canvas) {
        long diff = System.currentTimeMillis() - lastTime;
        for (int frame = 0; frame * 16.6667 < diff; frame++) {
            for (int index = 0; index < balls.size(); index++) {
                balls.get(index).update(canvas);
            }
        }
        lastTime = System.currentTimeMillis();
    }

    private class Velocity {
        double x, y;
    }

    private class Ball {
        double x, y;
        Velocity vel = new Velocity();

        Ball(double startX, double startY, double startVelX, double startVelY) {
            this.x = startX > 0 ? startX : Math.random() * getWidth();
            this.y = startY > 0 ? startY : Math.random() * getHeight();
            vel.x = startVelX > 0 ? startVelX : Math.random() * 2 - 1;
            vel.y = startVelY > 0 ? startVelY : Math.random() * 2 - 1;
        }

        public void update(Canvas canvas) {
            if (canvas == null) {
                return;
            }
            if (this.x > canvas.getWidth() + 50 || this.x < -50) {
                this.vel.x = -this.vel.x;
            }
            if (this.y > canvas.getHeight() + 50 || this.y < -50) {
                this.vel.y = -this.vel.y;
            }
            this.x += this.vel.x;
            this.y += this.vel.y;
        }

        //
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public void draw(Canvas canvas) {
            Paint paint = new Paint();
            paint.setAlpha((int) (255 * 0.4));
            paint.setColor(Color.parseColor("#1292da"));
            paint.setStrokeWidth(5);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle((float) (1.5 + this.x), (float) (1.5 + this.y), 4, paint);
        }

    }


}
