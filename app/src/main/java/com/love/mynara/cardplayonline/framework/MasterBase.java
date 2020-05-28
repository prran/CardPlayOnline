package com.love.mynara.cardplayonline.framework;

import android.graphics.Canvas;
import android.graphics.Paint;

public class MasterBase {
    public MasterBase() {
    }

    public void showFPS(Canvas canvas, FrameBaseV1 frame) {
        float skip = (float)(Math.floor((double)(frame.getSkipValue() * 10.0F)) / 10.0D);
        Paint inSideColor = new Paint();
        inSideColor.setColor(0XFF000000);
        inSideColor.setTextSize(30.0F);
        Paint outSideColor = new Paint();
        outSideColor.setColor(-1);
        outSideColor.setTextSize(35.0F);
        canvas.drawText("FPS: " + frame.getRealCountFrame() + "  Skiped: " + skip, 0.0F, 35.0F, outSideColor);
        canvas.drawText("FPS: " + frame.getRealCountFrame() + "  Skiped: " + skip, 0.0F, 30.0F, inSideColor);
    }

    public void showPoint(String id, Canvas canvas, ActiveBase active) {
        float[] point = active.getPoint(id);
        Paint showPaint = new Paint();
        showPaint.setColor(0XFFFF0000);
        canvas.drawRect(point[0], point[1], point[0] + 5.0F, point[1] + 5.0F, showPaint);
    }
}
