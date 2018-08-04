package com.wurbo.ghostcatcher;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    public static final String TAG = MainThread.class.getSimpleName();
    private final static int MAX_FPS = 60; // desired fps
    private final static int MAX_FRAME_SKIPS = 0; // maximum number of frames to
                                                  // be skipped
    private final static int FRAME_PERIOD = 1000 / MAX_FPS; // the frame period

    private boolean running;

    public void setRunning(boolean running) {
        this.running = running;
    }

    private SurfaceHolder surfaceHolder;
    private GameSurfaceView gameSurfaceView;

    public MainThread(SurfaceHolder surfaceHolder,
            GameSurfaceView gameSurfaceView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameSurfaceView = gameSurfaceView;
    }

    @Override
    public void run() {
        Log.d(TAG, "Starting game loop");
        long beginTime; // the time when the cycle begun
        long timeDiff; // the time it took for the cycle to execute
        int sleepTime; // ms to sleep (<0 if we're behind)
        int framesSkipped; // number of frames being skipped
        sleepTime = 0;

        while (running) {
            beginTime = SystemClock.uptimeMillis();
            framesSkipped = 0;
            synchronized (surfaceHolder) {
                Canvas canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas();
                    gameSurfaceView.update();
                    gameSurfaceView.render(canvas);
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
            timeDiff = SystemClock.uptimeMillis() - beginTime;
            sleepTime = (int) (FRAME_PERIOD - timeDiff);
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    //
                }
            }
            while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
                // catch up - update w/o render
                gameSurfaceView.update();
                sleepTime += FRAME_PERIOD;
                framesSkipped++;
            }
        }
    }
}