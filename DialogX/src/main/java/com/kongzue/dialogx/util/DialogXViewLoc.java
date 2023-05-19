package com.kongzue.dialogx.util;

public class DialogXViewLoc {
    private float x;
    private float y;
    private float w;
    private float h;

    public float getX() {
        return x;
    }

    public DialogXViewLoc setX(float x) {
        this.x = x;
        return this;
    }

    public float getY() {
        return y;
    }

    public DialogXViewLoc setY(float y) {
        this.y = y;
        return this;
    }

    public float getW() {
        return w;
    }

    public DialogXViewLoc setW(float w) {
        this.w = w;
        return this;
    }

    public float getH() {
        return h;
    }

    public DialogXViewLoc setH(float h) {
        this.h = h;
        return this;
    }

    public boolean isSameLoc(int[] loc){
        if (loc.length == 2) {
            return x == loc[0] && y == loc[1];
        }
        if (loc.length == 4) {
            return x == loc[0] && y == loc[1] && w == loc[2] && h == loc[3];
        }
        return false;
    }

    public void set(int[] loc) {
        if (loc.length == 2) {
            x = loc[0];
            y = loc[1];
        }
        if (loc.length == 4) {
            x = loc[0];
            y = loc[1];
            w = loc[2];
            h = loc[3];
        }
    }
}
