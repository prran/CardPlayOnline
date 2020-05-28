package com.love.mynara.cardplayonline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import com.love.mynara.cardplayonline.framework.LoadBase;

public class abstractImageView extends android.support.v7.widget.AppCompatImageView {

    private Bitmap bitmap;
    private LoadBase load;

    public abstractImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        load = new LoadBase(context);

        bitmap = Bitmap.createBitmap(load.getScreenSize().x,load.getScreenSize().y, Bitmap.Config.ARGB_8888);
        this.draw(new Canvas(bitmap));

    }

    @Override
    public void onDraw(Canvas cvs)
    {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        final int rectSize = load.getScreenSize().x/5;

        for (int j = 0; j * rectSize <= load.getScreenSize().y; j++) {
            for (int i = 1; i * rectSize <= load.getScreenSize().x; i += 2) {
                paint.setShader(new LinearGradient((i - 1) * rectSize, (j + 1) * rectSize, i * rectSize, j * rectSize, 0xff504444, 0xff827676, Shader.TileMode.CLAMP));
                cvs.drawRect((i - 1) * rectSize, j * rectSize, i * rectSize, (j + 1) * rectSize, paint);
                paint.setShader(new LinearGradient(i * rectSize, (j + 1) * rectSize, (i + 1) * rectSize, j * rectSize, 0xffEDD9D9, Color.WHITE, Shader.TileMode.CLAMP));
                cvs.drawRect(i * rectSize, j * rectSize, (i + 1) * rectSize, (j + 1) * rectSize, paint);
            }
            j++;
            for (int i = 1; i * rectSize <= load.getScreenSize().x; i += 2) {
                paint.setShader(new LinearGradient((i - 1) * rectSize, (j + 1) * rectSize, i * rectSize, j * rectSize, 0xffEDD9D9, Color.WHITE, Shader.TileMode.CLAMP));
                cvs.drawRect((i - 1) * rectSize, j * rectSize, i * rectSize, (j + 1) * rectSize, paint);
                paint.setShader(new LinearGradient(i * rectSize, (j + 1) * rectSize, (i + 1) * rectSize, j * rectSize, 0xff504444, 0xff827676, Shader.TileMode.CLAMP));
                cvs.drawRect(i * rectSize, j * rectSize, (i + 1) * rectSize, (j + 1) * rectSize, paint);
            }
        }

        load.saveBitmapToJPGCache(bitmap,"MainBackground.jpg");
    }
}
