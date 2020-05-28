package com.love.mynara.cardplayonline.framework.sub;

import android.graphics.Point;

public class Area {
    public float width;
    public float height;
    public float x;
    public float y;

    public Area(float Width, float Height, float X, float Y) {
        this.width = Width;
        this.height = Height;
        this.x = X;
        this.y = Y;
    }

    public Area() {
    }

    public Area(float Width, float Height) {
        this.width = Width;
        this.height = Height;
    }

    public void location(float X, float Y) {
        this.x = X;
        this.y = Y;
    }

    public void size(float Width, float Height) {
        this.width = Width;
        this.height = Height;
    }

    public Point getLocationPoint() {
        return new Point((int)this.x, (int)this.y);
    }

    public Point getSizePoint() {
        return new Point((int)this.width, (int)this.height);
    }
}
