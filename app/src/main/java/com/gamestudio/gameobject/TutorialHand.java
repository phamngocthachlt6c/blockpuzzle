package com.gamestudio.gameobject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.gamestudio.common.ResourceManager;

public class TutorialHand {
    private static final long TIME_TO_MOVE_TARGET = 1000;

    private float homeX, homeY, currentX, currentY;
    private float targetX, targetY;
    private float w, h;
    private Rect rect;
    private float speedX, speedY;
    private Bitmap tutorialHand;

    public TutorialHand() {
        rect = new Rect();
        tutorialHand = ResourceManager.getInstance().getBitmap("tutorial_hand");
    }

    public float getHomeX() {
        return homeX;
    }

    public void setHomeX(float homeX) {
        this.homeX = homeX;
        rect.left = (int) homeX;
        rect.right = rect.left + (int) w;
        currentX = homeX;
    }

    public float getHomeY() {
        return homeY;
    }

    public void setHomeY(float homeY) {
        currentY = homeY;
        this.homeY = homeY;
        rect.top = (int) homeY;
        rect.bottom = rect.top + (int) h;
    }

    public float getTargetX() {
        return targetX;
    }

    public void setTargetX(float targetX) {
        this.targetX = targetX;
        speedX = (targetX - homeX) / TIME_TO_MOVE_TARGET;
    }

    public float getTargetY() {
        return targetY;
    }

    public void setTargetY(float targetY) {
        this.targetY = targetY;
        speedY = (targetY - homeY) / TIME_TO_MOVE_TARGET;
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

    public void setCurrentX(float currentX) {
        this.currentX = currentX;
        rect.left = (int) currentX;
        rect.right = rect.left + (int) w;
    }

    public void setCurrentY(float currentY) {
        this.currentY = currentY;
        rect.top = (int) currentY;
        rect.bottom = rect.top + (int) h;
    }

    public void update(long deltaTime) {
        setCurrentX(currentX + (deltaTime * speedX));
        setCurrentY(currentY + (deltaTime * speedY));
        if ((currentX > targetX && speedX > 0) || (currentX < targetX && speedX < 0) || (currentY > targetY && speedY > 0) || (currentY < targetY && speedY < 0)) {
            setCurrentX(homeX);
            setCurrentY(homeY);
        }
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(tutorialHand, null, rect, paint);
    }
}
