package com.gamestudio.gameobject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.gamestudio.common.Constant;

public class MovedBlock {
    private float x, y, w, h;
    private Rect rect;
    private Bitmap bitmapBlock;
    private boolean enableAlpha;

    public MovedBlock(float x, float y, float w, float h, Bitmap bitmap) {
        rect = new Rect((int) x, (int) y, (int) (x + w), (int) (y + h));
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        bitmapBlock = bitmap;
    }

    public void setX(float x) {
        this.x = x;
        rect.left = (int) x;
        rect.right = (int) (x + w);
    }

    public void setY(float y) {
        this.y = y;
        rect.top = (int) y;
        rect.bottom = (int) (y + h);
    }

    public void setSize(float size) {
        w = size;
        h = size;
    }

    public void setEnableAlpha(boolean enableAlpha) {
        this.enableAlpha = enableAlpha;
    }

    public void draw(Canvas canvas, Paint paint) {
        if (enableAlpha) {
            paint.setAlpha(Constant.ALPHA_OBJECT);
        }
        canvas.drawBitmap(bitmapBlock, null, rect, paint);
        paint.setAlpha(Constant.MAX_ALPHA);
    }
}
