package com.wurbo.ghostcatcher.entities;

import java.util.Random;

import android.graphics.Paint;
import android.graphics.RectF;

public class Entity extends RectF {

    private float x = 0;
    private float y = 0;
    private float size = 0;
    private float offset = 0;
    private float speed = 0;
    private float widthRatio = 1;
    private float heightRatio = 1;
    private boolean flipped = false;
    protected Random rand;
    protected Paint paint;

    public Entity(float size, int screenWidth, int screenHeight) {
        rand = new Random();
        paint = new Paint();
        //Create ratio for width for target width of 720px
        this.widthRatio = (float) (screenWidth / 720.0);
        this.heightRatio = (float) (screenHeight / 1080.0);
        this.size = size;
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
        x += f;
        recalc();
    }

    public void moveY(float f) {
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

    public int randRange(int min, int max) {
        int randomNum = rand.nextInt(max - min + 1) + min;
        return randomNum;
    }

    public Paint getPaint() {
        return paint;
    }
}
