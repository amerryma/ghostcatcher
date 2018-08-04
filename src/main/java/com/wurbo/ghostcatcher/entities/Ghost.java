package com.wurbo.ghostcatcher.entities;

import android.graphics.drawable.PictureDrawable;


public class Ghost extends Entity {
    public enum GhostType {
        WHITE, RED, BLACK, GOLD, GREEN, BLUE
    }

    private GhostType type;

    public Ghost(float size, int screenWidth, int screenHeight, PictureDrawable ghostBase) {
        super(size, screenWidth, screenHeight, ghostBase);
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
                    setType(GhostType.GOLD);
                    if (randRange(0, 1) == 1) {
                        setType(GhostType.BLUE);
                    }
                }
            }
        }
        setPos(randRange(0, (int) (height - getSize())), randRange(-width, 0));
        setSpeed((float) (randRange(560, 840) / 1000.0));
    }

    public GhostType getType() {
        return type;
    }
}