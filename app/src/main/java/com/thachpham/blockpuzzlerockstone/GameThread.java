package com.thachpham.blockpuzzlerockstone;

import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

    private boolean running;
    private GameSurfaceView gameSurface;
    private SurfaceHolder surfaceHolder;

    private boolean pauseGameBySurfaceViewDestroy;

    public GameThread(GameSurfaceView gameSurface) {
        this.gameSurface = gameSurface;
    }

    public void setPauseGameBySurfaceViewDestroy(boolean pauseGameBySurfaceViewDestroy) {
        this.pauseGameBySurfaceViewDestroy = pauseGameBySurfaceViewDestroy;
    }

    @Override
    public void run() {
        long startTime = System.nanoTime();
        long previousTimeDraw = System.nanoTime();
        long previousTimeUpdate = System.currentTimeMillis();
        long period = 2 * 1000000;
        long periodDraw = (1000 / 60) * 1000000;

        while (running) {
            if (!pauseGameBySurfaceViewDestroy) {
                Canvas canvas = null;
                long now = System.nanoTime();
                this.gameSurface.update(System.currentTimeMillis() - previousTimeUpdate);
                previousTimeUpdate = System.currentTimeMillis();
                if (now - previousTimeDraw >= periodDraw) {
                    previousTimeDraw = now;
                    if (surfaceHolder != null) {
                        try {
                            // Get Canvas from Holder and lock it.
                            canvas = this.surfaceHolder.lockCanvas();

                            // Synchronized
                            synchronized (canvas) {
                                this.gameSurface.draw(canvas);
                            }
                        } catch (Exception e) {
                            // Do nothing.
                        } finally {
                            if (canvas != null) {
                                // Unlock Canvas.
                                this.surfaceHolder.unlockCanvasAndPost(canvas);
                            }
                        }
                    }
//                Log.d("aaa", "run: time to draw = " + (System.nanoTime() - previousTimeDraw)/1000000);
                }
                // Interval to redraw game
                // (Change nanoseconds to milliseconds)
                long waitTime = period - (now - startTime);
                if (waitTime < period) {
                    waitTime = period; // Millisecond.
                }
                try {
                    // Sleep.
                    sleep(waitTime / 1000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startTime = System.nanoTime();
            } else {
                // fix bug sleep time after click ads
                startTime = System.nanoTime();
                // keep update deltaTime is right
                previousTimeUpdate = System.currentTimeMillis();
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
//        surfaceHolder.setFormat(PixelFormat.RGBA_8888);
    }

    public void destroyThread() {
        running = false;
    }
}
