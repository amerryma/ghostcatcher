package com.wurbo.ghostcatcher;

import java.util.Random;

import android.graphics.Paint;
import android.graphics.RectF;

class Ghost extends RectF {
    enum GhostType {
        WHITE, RED, BLACK, GOLD, GREEN, BLUE
    }

    private float x = 0;
    private float y = 0;
    private float size = 0;
    private float offset = 0;
    private boolean flipped = false;
    private float speed = 0;
    private Paint paint;
    private GhostType type;
    private float widthRatio = 1;
    private float heightRatio = 1;
    Random rand;

    public Ghost(float size, int screenWidth, int screenHeight) {
        rand = new Random();
        this.widthRatio = (float) (screenWidth / 720.0);
        this.heightRatio = (float) (screenHeight / 1080.0);
        setSize(size);
        paint = new Paint();
        paint.setARGB(0, 0, 0, 0);
        setType(GhostType.WHITE);
    }

    public void setType(GhostType type) {
        this.type = type;
        switch (type) {
        case WHITE:
            paint.setARGB(0, 0, 0, 0);
            setSize(50);
            break;
        case RED:
            paint.setARGB(100, 255, 0, 0);
            setSize(50);
            break;
        case BLACK:
            paint.setARGB(100, 0, 0, 0);
            setSize(80);
            break;
        case GOLD:
            paint.setARGB(100, 255, 204, 0);
            setSize(150);
            break;
        case GREEN:
            paint.setARGB(100, 0, 255, 0);
            setSize(40);
            break;
        case BLUE:
            paint.setARGB(100, 0, 0, 255);
            setSize(65);
            break;
        default:
            break;
        }
    }

    public float getSize() {
        return size;
    }

    public float getUnscaledSize() {
        return size / widthRatio;
    }

    public void setSize(float size) {
        this.size = size * widthRatio; // Scale to 720 screen size
        offset = this.size / 2;
        set(0, 0, size, size);
    }

    public float getOffset() {
        return offset;
    }

    public void setX(float x) {
        this.x = x;
        recalc();
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        recalc();
    }

    public float getX() {
        return x;
    }

    public void setPos(float x, float y) {
        this.y = y;
        this.x = x;
        recalc();
    }

    public void recalc() {
        if (flipped) {
            set(x - offset, y + size, x + size - offset, y);
        } else {
            set(x - offset, y, x + size - offset, y + size);
        }
    }

    public void moveX(float f) {
        // TODO Auto-generated method stub
        x += f;
        recalc();
    }

    public void moveY(float f) {
        // TODO Auto-generated method stub
        y += f;
        recalc();
    }

    public void setFlipped(boolean bool) {
        flipped = bool;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void random(int width, int height) {
        if (randRange(0, 3) == 1) {
            setType(GhostType.RED);
            if (randRange(0, 200) == 1) {
                setType(GhostType.BLACK);
            }
        } else {
            setType(GhostType.WHITE);
            if (randRange(0, 75) == 1) {
                setType(GhostType.GREEN);
                if (randRange(0, 1) == 1) {
                    setType(GhostType.BLUE);
                }
                if (randRange(0, 1) == 1) {
                    setType(GhostType.GOLD);
                }
            }
        }
        setPos(randRange(0, height - (int) size), randRange(-width, 0));// -50;
                                                                        // //Might
                                                                        // try
                                                                        // randRange(-50,
                                                                        // 0);
        setSpeed((float) (randRange(280, 560) / 1000.0));
    }

    public int randRange(int min, int max) {
        int randomNum = rand.nextInt(max - min + 1) + min;
        return randomNum;
    }

    public void setShield(boolean on) {
        if (on) {
            paint.setARGB(100, 0, 255, 0);
        } else {
            paint.setARGB(0, 0, 0, 0);
        }
    }

    public Paint getPaint() {
        return paint;
    }

    public GhostType getType() {
        return type;
    }
}