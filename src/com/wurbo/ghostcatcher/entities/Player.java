package com.wurbo.ghostcatcher.entities;

import android.graphics.drawable.PictureDrawable;


public class Player extends Entity {
    private float minSize = 50;
    private float maxSize = 2000;
    private long shieldTime = 10000;
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
        if (shieldTime > 0) {
            shieldTime -= delta;
            paint.setARGB(100, 0, 255, 0);
        } else {
            shieldTime = 0;
            paint.setARGB(0, 0, 0, 0);
        }
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
