package com.love.mynara.cardplayonline.framework.sub;

import android.graphics.Point;

public class Calculations {

    public final static byte X = 0;
    public final static byte Y = 1;

    private final float FPS;
    private final Point scr;

    public Calculations(int FPS, Point screenSize)
    {
        this.FPS = FPS;
        scr = screenSize;
    }

    public float getScrSize(byte XorY, float percent)
    {
        float screen;
        if(XorY == X)
            screen = scr.x;
        else if(XorY == Y)
            screen = scr.y;
        else
            screen = 0;
        return percent/100*screen;
    }

    public float getFrameMoveUnit(float forSec, float moveDistance)
    {
        return moveDistance/(forSec*FPS);
    }
}
