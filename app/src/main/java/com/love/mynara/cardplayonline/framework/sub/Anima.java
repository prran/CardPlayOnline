package com.love.mynara.cardplayonline.framework.sub;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.love.mynara.cardplayonline.framework.*;

public class Anima {
    private Bitmap image;
    private int[] info;
    private static final int ELEMENT = 0;
    private static final int WIDTH = 6;
    public float playTime;

    public Anima(Bitmap image, int[] inof) {
        this.image = image;
        this.info = inof;
        this.playTime = -1.0F;
    }

    public Bitmap play(FrameBaseV1 frame) {
        int turn = frame.getTurn(this.info, this.playTime);
        Bitmap CurSine = Bitmap.createBitmap(this.image, this.info[6] * turn, 0, this.info[6], this.image.getHeight());
        return CurSine;
    }

    public void stop() {
    }

    public void clear() {
    }

    public void changeSize(int width, int height) {
        Canvas cvs = new Canvas();
        Bitmap result = Bitmap.createBitmap(width * this.info[0], height, (Config)null);
        cvs.setBitmap(result);

        for(int count = 0; count < this.info[0]; ++count) {
            Bitmap bitmap = Bitmap.createBitmap(this.image, this.info[6] * count, 0, this.info[6], this.image.getHeight());
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            cvs.drawBitmap(bitmap, (float)(width * count), 0.0F, (Paint)null);
        }

        this.info[6] = width;
        this.image = result;
    }

    public Point getSize() {
        return new Point(this.info[6], this.image.getHeight());
    }
}
