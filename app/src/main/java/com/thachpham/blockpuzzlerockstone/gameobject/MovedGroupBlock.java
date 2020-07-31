package com.thachpham.blockpuzzlerockstone.gameobject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class MovedGroupBlock {
    private static final float ZOOM_SPEED_PER_MILLIS = 0.32f;// on screen width = 800px
    private static final float CHANGE_Y_SPEED_PER_MILLIS = 2.4f;// on screen width = 800px
    private static final float MOVE_BACK_HOME_TIME = 300;
    private static final float INIT_TIME = 300;

    private static final int STATE_INIT = 0;
    private static final int STATE_IDLE_NORMAL = 1;
    private static final int STATE_ZOOM_OUT = 2;
    private static final int STATE_IDLE_MAX_SIZE = 3;
    private static final int STATE_MOVE_HOME_POSITION = 4;

    private int state = STATE_INIT;

    private float middleX, middleY;
    private float homePosX, homePosY;
    private float moveHomeSpeedX, moveHomeSpeedY, moveHomeSpeedZoom, initSpeedZoom;
    private float childSizeNormal, childSizeMaximum;
    private float childSizeCurrently;
    private MovedBlock movedBlockDrawOnly;
    private long startTimePeriodAnim;
    private float changeYMaxDistance;

    // Row is [] 1
    // Column is [] 2
    private int[][] matrixBlock;

    private float zoomOutedDistance;

    private boolean isPressed;

    private boolean blockAlpha;

    private boolean hidden;

    public MovedGroupBlock() {

    }

    public MovedGroupBlock(float middleX, float middleY, float childSizeNormal, float childSizeMaximum, int[][] matrixBlock, Bitmap bitmapBlock) {
        this.middleX = middleX;
        this.middleY = middleY;
        this.childSizeNormal = childSizeNormal;
        this.childSizeMaximum = childSizeMaximum;
        this.matrixBlock = matrixBlock;
        movedBlockDrawOnly = new MovedBlock(0, 0, childSizeCurrently, childSizeCurrently, bitmapBlock);
    }

    public void setChildSizeNormal(float size) {
        childSizeNormal = size;
        initSpeedZoom = childSizeNormal / INIT_TIME;
    }

    public void setSizeMaximum(float size) {
        childSizeMaximum = size;
        changeYMaxDistance = childSizeMaximum * 3;
    }

    public void startZoomOut() {
        startTimePeriodAnim = System.currentTimeMillis();
        changeState(STATE_ZOOM_OUT);
    }

    public void startMoveHome(float homeX, float homeY) {
        homePosX = homeX;
        homePosY = homeY;
        middleY = middleY - zoomOutedDistance;
        zoomOutedDistance = 0;
        state = STATE_MOVE_HOME_POSITION;
        moveHomeSpeedX = (homePosX - middleX) / MOVE_BACK_HOME_TIME;
        moveHomeSpeedY = (homePosY - middleY) / MOVE_BACK_HOME_TIME;
        moveHomeSpeedZoom = (childSizeNormal - childSizeMaximum) / MOVE_BACK_HOME_TIME;
    }

    public void setMiddleX(float middleX) {
        this.middleX = middleX;
    }

    public float getMiddleX() {
        return middleX;
    }

    public void setMiddleY(float middleY) {
        this.middleY = middleY;
    }

    private void changeState(int state) {
        this.state = state;
    }

    public void update(long deltaTime) {
        if (hidden) {
            return;
        }
        switch (state) {
            case STATE_INIT:
                childSizeCurrently += initSpeedZoom * deltaTime;
                if (childSizeCurrently > childSizeNormal) {
                    childSizeCurrently = childSizeNormal;
                    changeState(STATE_IDLE_NORMAL);
                }
                break;
            case STATE_ZOOM_OUT:
                zoomOutedDistance += CHANGE_Y_SPEED_PER_MILLIS * (System.currentTimeMillis() - startTimePeriodAnim);
                if (zoomOutedDistance > changeYMaxDistance) {
                    zoomOutedDistance = changeYMaxDistance;

                    childSizeCurrently += ZOOM_SPEED_PER_MILLIS * (System.currentTimeMillis() - startTimePeriodAnim);
                    if (childSizeCurrently > childSizeMaximum) {
                        childSizeCurrently = childSizeMaximum;
                        changeState(STATE_IDLE_MAX_SIZE);
                    }
                }
                startTimePeriodAnim = System.currentTimeMillis();
                break;

            case STATE_MOVE_HOME_POSITION:
                childSizeCurrently += moveHomeSpeedZoom * deltaTime;
                middleX += (moveHomeSpeedX * deltaTime);
                middleY += (moveHomeSpeedY * deltaTime);
                if ((moveHomeSpeedX < 0 && middleX < homePosX) || (moveHomeSpeedX > 0 && middleX > homePosX)
                        || (moveHomeSpeedY < 0 && middleY < homePosY) || (moveHomeSpeedY > 0 && middleY > homePosY)) {
                    middleX = homePosX;
                    middleY = homePosY;
                    state = STATE_IDLE_NORMAL;
                    childSizeCurrently = childSizeNormal;
                }
                break;
        }
    }

    public void draw(Canvas canvas, Paint paint) {
        if (hidden) {
            return;
        }
        // Draw cheat
//        paint.setColor(Color.RED);
//        canvas.drawCircle(middleX, middleY, childSizeNormal * 2, paint);

        // Draw object
        movedBlockDrawOnly.setSize(childSizeCurrently);
        movedBlockDrawOnly.setEnableAlpha(blockAlpha);
        float groupBlockWidth = matrixBlock[0].length * childSizeCurrently;
        float groupBlockHeight = matrixBlock.length * childSizeCurrently;
        float left = middleX - groupBlockWidth / 2;
        float top = middleY - groupBlockHeight / 2;

        for (int row = 0; row < matrixBlock.length; row++) {
            for (int col = 0; col < matrixBlock[0].length; col++) {
                if (matrixBlock[row][col] == 1) {
                    movedBlockDrawOnly.setX(left + col * childSizeCurrently);
                    movedBlockDrawOnly.setY(top + row * childSizeCurrently - zoomOutedDistance);
                    movedBlockDrawOnly.draw(canvas, paint);
                }
            }
        }

        // Draw cheat
//        paint.setColor(Color.RED);
//        canvas.drawCircle(getFirstBlockMiddleX(), getFirstBlockMiddleY(), 5, paint);
    }

    public boolean isInArea(float x, float y) {
        return !hidden && x > middleX - childSizeNormal * 2 && x < middleX + childSizeNormal * 2
                && y > middleY - childSizeNormal * 2 && y < middleY + childSizeNormal * 2;
    }

    public int[][] getBlockMatrix() {
        return matrixBlock;
    }

    public float getFirstBlockMiddleX() {
        float groupBlockWidth = matrixBlock[0].length * childSizeMaximum;
        return middleX - groupBlockWidth / 2 + childSizeMaximum / 2;
    }

    public float getFirstBlockMiddleY() {
        float groupBlockHeight = matrixBlock.length * childSizeMaximum;
        return middleY - changeYMaxDistance - groupBlockHeight / 2 + childSizeMaximum / 2;
    }

    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public void resetStatus() {
        state = STATE_INIT;
        zoomOutedDistance = 0;
        childSizeCurrently = 0;
        setEnableBlockAlpha(false);
        setHidden(false);
    }

    public void setEnableBlockAlpha(boolean alpha) {
        blockAlpha = alpha;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isHidden() {
        return hidden;
    }

    public MovedGroupBlock clone() {
        MovedGroupBlock groupBlock = new MovedGroupBlock();
        groupBlock.setMiddleX(middleX);
        groupBlock.setMiddleY(middleY);
        groupBlock.childSizeNormal = childSizeNormal;
        groupBlock.childSizeMaximum = childSizeMaximum;
        groupBlock.hidden = hidden;
        groupBlock.initSpeedZoom = initSpeedZoom;
        groupBlock.state = state;
        groupBlock.movedBlockDrawOnly = movedBlockDrawOnly;
        groupBlock.blockAlpha = blockAlpha;
        groupBlock.zoomOutedDistance = zoomOutedDistance;
        groupBlock.moveHomeSpeedZoom = moveHomeSpeedZoom;
        groupBlock.matrixBlock = matrixBlock;
        groupBlock.homePosX = homePosX;
        groupBlock.homePosY = homePosY;
        groupBlock.childSizeCurrently = childSizeCurrently;
        groupBlock.changeYMaxDistance = changeYMaxDistance;
        return groupBlock;
    }
}