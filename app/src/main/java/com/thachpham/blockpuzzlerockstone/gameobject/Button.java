package com.thachpham.blockpuzzlerockstone.gameobject;

import android.graphics.Rect;

public class Button {
    private float x, y, w, h;
    protected boolean isPressed;
    protected Rect rect;

    public Button() {
        rect = new Rect();
    }

    public void setIspressed(boolean isPressed) {
        this.isPressed = isPressed;
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
    }

    public boolean isInArea(float x, float y) {
        return x > this.x && x < this.x + w && y > this.y && y < this.y + h;
    }

}
