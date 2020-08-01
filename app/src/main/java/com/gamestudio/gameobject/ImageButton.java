package com.gamestudio.gameobject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class ImageButton extends Button {
    private Bitmap bitmapIdle, bitmapPressed;

    public ImageButton(Bitmap bitmapIdle, Bitmap bitmapPressed) {
        super();
        this.bitmapIdle = bitmapIdle;
        this.bitmapPressed = bitmapPressed;
    }

    public void draw(Canvas canvas, Paint paint) {
        if (isPressed) {
            canvas.drawBitmap(bitmapPressed, null, rect, paint);
        } else {
            canvas.drawBitmap(bitmapIdle, null, rect, paint);
        }
    }
}
