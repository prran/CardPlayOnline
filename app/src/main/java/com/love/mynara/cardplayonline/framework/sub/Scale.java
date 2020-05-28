package com.love.mynara.cardplayonline.framework.sub;

import android.annotation.SuppressLint;
import android.graphics.Point;

public class Scale {

    public final static boolean X = true, Y = false;

    private Point scr;
    private Point compareSize;

    public Scale(Point Scr, Object NullSpace)
    {
        scr = Scr;
    }

    public Scale(Point Scr) {
        this.scr = Scr;
        this.compareSize = new Point(768, 1280);
    }

    public void setScr(int width, int height) {
        this.scr.x = width;
        this.scr.y = height;
    }

    @SuppressLint({"DefaultLocale"})
    public void setTestDevice(String name) {
        switch(name.hashCode()) {
            case -1048794091:
                if(!name.equals("nexus4")) {
                    ;
                }
                break;
            case 3246:
                if(name.equals("g5")) {
                    this.compareSize.x = 1440;
                    this.compareSize.y = 2560;
                    return;
                }
        }

        this.compareSize.x = 768;
        this.compareSize.y = 1280;
    }

    public void onNavigationSize() {
        this.compareSize.y -= 96;
    }

    public Point point(Point point) {
        point.x = point.x * this.scr.x / this.compareSize.x;
        point.y = point.y * this.scr.y / this.compareSize.y;
        return point;
    }

    public float x(float line) {
        return line * (float)this.scr.x / (float)this.compareSize.x;
    }

    public float y(float line) {
        return line * (float)this.scr.y / (float)this.compareSize.y;
    }

    public float scaleByPercent(boolean Static_XorY, int Max_100)
    {
        if(Static_XorY)
            return scr.x * Max_100/100;
        else
            return scr.y * Max_100/100;
    }
}
