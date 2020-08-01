package com.gamestudio.gameobject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.gamestudio.common.MeasureUtils;

public class PointLabel {
    private static final int STATE_IDLE = 0;
    private static final int STATE_UP_POINT = 1;

    private static final long TOTAL_TIME_UP_POINT = 500;
    private float x, y, w, h;
    private Bitmap background;
    private Rect rect;
    private int point;
    private int newPointToUp;
    private Paint paint;

    private long startTimeUpPoint;
    private int state = STATE_IDLE;
    private long upPointEachTime;

    public PointLabel(Bitmap background) {
        rect = new Rect();
        this.background = background;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getPoint() {
        return point;
    }

    public void resetAnim() {
        state = STATE_IDLE;
    }

    public void startPointUp(int pointUp) {
        startTimeUpPoint = System.currentTimeMillis();
        newPointToUp = point + pointUp;
        state = STATE_UP_POINT;
        upPointEachTime = TOTAL_TIME_UP_POINT / pointUp;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        rect.left = (int) x;
        rect.right = rect.left + (int) w;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        rect.top = (int) y;
        rect.bottom = rect.top + (int) h;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
        rect.right = rect.left + (int) w;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
        rect.bottom = rect.top + (int) h;
        MeasureUtils.setTextSizeForHeight(paint, h * 0.6f, "9999");
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, null, rect, paint);
        paint.setColor(Color.YELLOW);
        canvas.drawText(String.valueOf(point), x + w / 2 - paint.measureText(String.valueOf(point)) / 2, y + h * 0.75f, paint);
    }

    public void update(long deltaTime) {
        if (state == STATE_UP_POINT) {
            if (System.currentTimeMillis() - startTimeUpPoint > upPointEachTime) {
                point += 1;
                startTimeUpPoint = System.currentTimeMillis();
                if (point > newPointToUp) {
                    point = newPointToUp;
                    state = STATE_IDLE;
                }
            }
        }
    }
}
