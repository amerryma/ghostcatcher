package com.wurbo.ghostcatcher.entities;

import android.graphics.drawable.PictureDrawable;


public class Player extends Entity {
    private float minSize = 50;
    private float maxSize = 2000;
    private long shieldTime = 10000;
    private long freeMovementTime = 10000;
    private int score = 0;

    public Player(float size, int screenWidth, int screenHeight, PictureDrawable playerBase) {
        super(size, screenWidth, screenHeight, playerBase);
        paint.setARGB(0, 0, 0, 0);
    }
    
    /**
     * Update the players game logic with delta. 
     * 
     * @param delta The time that passed since the last update.
     */
    public void update(long delta) {
        int alpha = 0;
        int red = 0;
        int green = 0;
        int blue = 0;
        if (shieldTime > 0) {
            shieldTime -= delta;
            alpha = 100;
            green = 255;
        } else {
            shieldTime = 0;
        }
        if (freeMovementTime > 0) {
            freeMovementTime -= delta;
            alpha = 100;
            blue = 255;
        } else {
            freeMovementTime = 0;
        }
        paint.setARGB(alpha, red, green, blue);
    }

    /**
     * Returns the minimum size that the player can be in pixels.
     * 
     * @return
     */
    public float getMinSize() {
        return minSize;
    }

    /**
     * Sets the minimum size that the player can be in pixels.
     * 
     * @param minSize
     */
    public void setMinSize(float minSize) {
        this.minSize = minSize;
    }

    /**
     * Returns the max size that the player can be in pixels.
     * 
     * @return
     */
    public float getMaxSize() {
        return maxSize;
    }
    
    /**
     * Sets the minimum size that the player can be in pixels.
     * 
     * @param maxSize
     */
    public void setMaxSize(float maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * Returns true if the shield of the player is current active.
     * 
     * @return
     */
    public boolean isShieldOn() {
        return shieldTime > 0;
    }

    /**
     * Returns the amount of time remaining of the shield in milliseconds.
     * 
     * @return
     */
    public long getShieldTime() {
        return shieldTime;
    }

    /**
     * Sets the amount of time remaining of the shield in milliseconds.
     * 
     * @param shieldTime Milliseconds
     */
    public void setShieldTime(int shieldTime) {
        this.shieldTime = shieldTime;
    }
    
    /**
     * Increases the amount of time remaining of the shield by shieldTime.
     * 
     * @param shieldTime
     */
    public void increaseShieldTime(long shieldTime) {
        this.shieldTime += shieldTime;
    }

    /**
     * Returns true if the free movement of the player is current active.
     *
     * @return
     */
    public boolean isFreeMoving() {
        return freeMovementTime > 0;
    }

    /**
     * Returns the amount of time remaining of the free movement in milliseconds.
     *
     * @return
     */
    public long getFreeMovementTime() {
        return freeMovementTime;
    }

    /**
     * Sets the amount of time remaining of the free movement in milliseconds.
     *
     * @param freeMovementTime Milliseconds
     */
    public void setFreeMovementTime(long freeMovementTime) {
        this.freeMovementTime = freeMovementTime;
    }

    /**
     * Increases the amount of time remaining of the free movement by freeMovementTime.
     *
     * @param freeMovementTime
     */
    public void increaseFreeMovementTime(long freeMovementTime) {
        this.freeMovementTime += freeMovementTime;
    }

    /**
     * Returns the score of the player.
     * 
     * @return
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the score of the player.
     * 
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Visually updates the players paint with the shield paint.
     * 
     * @param on
     */
    public void setShield(boolean on) {
        if (on) {
            paint.setARGB(100, 0, 255, 0);
        } else {
            paint.setARGB(0, 0, 0, 0);
        }
    }
    

}
