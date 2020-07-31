package com.thachpham.blockpuzzlerockstone.gameobject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.thachpham.blockpuzzlerockstone.common.ResourceManager;

public class GamePlayHeader {
    private float x, y, w, h;
    private Bitmap background;
    private Rect rect;
    private PointLabel currentPointLabel;

    public GamePlayHeader() {
        background = ResourceManager.getInstance().getBitmap("header_background");
        rect = new Rect();
        // Point
        currentPointLabel = new PointLabel(ResourceManager.getInstance().getBitmap("stone"));
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        rect.left = (int) x;
        rect.right = rect.left + (int) w;
        currentPointLabel.setX(x);
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        rect.top = (int) y;
        rect.bottom = rect.top + (int) h;
        currentPointLabel.setY(y);
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
        rect.right = rect.left + (int) w;
        currentPointLabel.setW(w * 0.3f);
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
        rect.bottom = rect.top + (int) h;
        currentPointLabel.setH(h * 0.5f);
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(background, null, rect, paint);
        currentPointLabel.draw(canvas);
    }

    public void update(long deltaTime) {
        currentPointLabel.update(deltaTime);
    }

    public void setCurrentPointLabel(int point) {
        currentPointLabel.setPoint(point);
        currentPointLabel.resetAnim();
    }

    public void startUpPoint(int newPoint) {
        currentPointLabel.startPointUp(newPoint);
    }
}
