package com.love.mynara.cardplayonline.framework;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import com.love.mynara.cardplayonline.framework.sub.*;

public class DrawBase {
    private LoadBase data;
    private Canvas canvas;
    private Paint paint;
    private Paint paintTmp;
    private FrameBaseV1 frame;
    private Paint textPaint;
    private float textSizeY;
    private float textSizeX;

    public DrawBase(LoadBase data, FrameBaseV1 frame) {
        this.data = data;
        this.frame = frame;
        this.canvas = null;
        this.paint = new Paint();
        textPaint = new Paint();
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void setPaint(Paint paint) { if(paintTmp == null) paintTmp = this.paint ; this.paint = paint; }

    public void removePaint() {this.paint = paintTmp;}

    public void saveScaleImage(String fileName, int PluX_MinY_Max100) {
        Point scr = data.getScreenSize();
        Bitmap image = (Bitmap)data.get(0, fileName);
        if(image == null) {
            return;
        }

        int w = image.getWidth();
        int h = image.getHeight();

        if(PluX_MinY_Max100 > 0)
        {
            float newW = scr.x * PluX_MinY_Max100/100;
            float rate = newW/w;
            w = (int) newW;
            h *= rate;
        }
        else if(PluX_MinY_Max100 < 0)
        {
            float newH = scr.y * -PluX_MinY_Max100/100;
            float rate = newH/h;
            h = (int) newH;
            w *= rate;
        }

        Bitmap result = Bitmap.createScaledBitmap(image, w, h, true);
        this.data.set(0, fileName, result);
    }
    public void saveScaleImage(String fileName, int RateX100 , int RateY100) {
        Point scr = data.getScreenSize();
        Bitmap image = (Bitmap)data.get(0, fileName);
        if(image == null) {
            return;
        }

        float newW = scr.x * RateX100/100;
        int w = (int) newW;

        float newH = scr.y * RateY100/100;
        int h = (int) newH;


        Bitmap result = Bitmap.createScaledBitmap(image, w, h, true);
        this.data.set(0, fileName, result);
    }

    public void backGroundCut(String fileName, Point scr)
    {
        Bitmap image = (Bitmap)data.get(0, fileName);

        int str = scr.x;

        if(image.getWidth() > scr.x)
            str = (image.getWidth() - scr.x) / 2;
        else
            str = (scr.x - image.getWidth()) / 2;

        data.set(0, fileName, Bitmap.createBitmap(image, str, image.getHeight() - scr.y, scr.x, scr.y));

    }

    public void image(String fileName, int x, int y) {
        if(this.canvas != null) {
            Bitmap image = (Bitmap)this.data.get(0, fileName);
            if(image == null) {
                Log.i("DrawBase","can't found file about : " + fileName);
                return;
            }

            this.canvas.drawBitmap(image, (float)x, (float)y, this.paint);
        }
        else{
            Log.i("DrawBase","You Must have to designate canvas by setCanvas();");
        }

    }

    public void image(String fileName, int x, int y, Paint paint) {
        if(this.canvas != null) {
            Bitmap image = (Bitmap)this.data.get(0, fileName);
            if(image == null) {
                Log.i("DrawBase","can't found file about : " + fileName);
                return;
            }

            this.canvas.drawBitmap(image, (float)x, (float)y, paint);
        }
        else{
            Log.i("DrawBase","You Must have to designate canvas by setCanvas();");
        }

    }

    public void image(String fileName, int x, int y, int width, int height) {
        if(this.canvas != null) {
            Bitmap image = (Bitmap)this.data.get(0, fileName);
            if(image == null) {
                return;
            }

            int w = width;
            int h = height;
            if(width <= 0) {
                w = image.getWidth();
            }

            if(height <= 0) {
                h = image.getHeight();
            }

            Bitmap result = Bitmap.createScaledBitmap(image, w, h, true);
            this.canvas.drawBitmap(result, (float)x, (float)y, this.paint);
        }

    }

    public void image(Bitmap bitmap, int x, int y) {
        if(this.canvas != null) {
            this.canvas.drawBitmap(bitmap, (float)x, (float)y, this.paint);
        }
        else{
            Log.i("DrawBase","You Must have to designate canvas by setCanvas();");
        }

    }

    public void rotateImage(String fileName, float x, float y, float align)
    {
        if(this.canvas != null) {
            Bitmap image = (Bitmap)this.data.get(0, fileName);
            if(image == null) {
                Log.i("DrawBase","can't found file about : " + fileName);
                return;
            }

            Matrix matrix = new Matrix();
            matrix.postRotate(align);
            Bitmap rotatedBitmap = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);

            this.canvas.drawBitmap(rotatedBitmap, x + (image.getWidth() - rotatedBitmap.getWidth())/2, y + (image.getHeight() -rotatedBitmap.getHeight())/2, this.paint);
        }
        else{
            Log.i("DrawBase","You Must have to designate canvas by setCanvas();");
        }
    }

    public void centerImagX(String fileName, float y)
    {
        if(this.canvas != null) {
            Bitmap image = (Bitmap)this.data.get(0, fileName);
            if(image == null) {
                return;
            }

            final float x = data.getScreenSize().x/2 - image.getWidth()/2;

            this.canvas.drawBitmap(image, (float)x, y, this.paint);
        }

    }

    public void centerScaleImagX(String fileName, float y, float rateBy1)
    {
        if(this.canvas != null) {
            Bitmap image = (Bitmap)this.data.get(0, fileName);
            if(image == null) {
                return;
            }

            int w = image.getWidth();
            int h = image.getHeight();

            if(rateBy1 > 0) { w *= rateBy1; h *= rateBy1;}

            Bitmap result = Bitmap.createScaledBitmap(image, w, h, true);

            final float x = data.getScreenSize().x/2 - w/2;

            this.canvas.drawBitmap(result, (float)x, y, this.paint);
        }

    }

    public void centerScaleImagX(String fileName, float y, float rateBy1, Paint paint)
    {
        if(this.canvas != null) {
            Bitmap image = (Bitmap)this.data.get(0, fileName);
            if(image == null) {
                return;
            }

            int w = image.getWidth();
            int h = image.getHeight();

            if(rateBy1 > 0) { w *= rateBy1; h *= rateBy1;}

            Bitmap result = Bitmap.createScaledBitmap(image, w, h, true);

            final float x = data.getScreenSize().x/2 - w/2;

            this.canvas.drawBitmap(result, (float)x, y, paint);
        }

    }

    public void scaleImage(String fileName, int x, int y, float rate) {
        if(this.canvas != null) {
            Bitmap image = (Bitmap)this.data.get(0, fileName);
            if(image == null) {
                return;
            }

            int w = image.getWidth();
            int h = image.getHeight();

            if(rate > 0) { w *= rate; h *= rate;}

            Bitmap result = Bitmap.createScaledBitmap(image, w, h, true);
            this.canvas.drawBitmap(result, (float)x, (float)y, paint);
        }

    }

    public void scaleImage(String fileName, int x, int y, float rate, int alpha) {
        if(this.canvas != null) {
            Bitmap image = (Bitmap)this.data.get(0, fileName);
            if(image == null) {
                return;
            }

            int w = image.getWidth();
            int h = image.getHeight();

            if(rate > 0) { w *= rate; h *= rate;}

            Paint paint = new Paint();
            paint.setAlpha(alpha);

            Bitmap result = Bitmap.createScaledBitmap(image, w, h, true);
            this.canvas.drawBitmap(result, (float)x, (float)y, paint);
        }

    }

    public Point getSize(String fileName)
    {
        Bitmap image = (Bitmap)this.data.get(0, fileName);
        if(image == null) {
            Log.e("DrawBase/getSize","fileLoadError");
            return null;
        }

        return new Point(image.getWidth(),image.getHeight());
    }

    public void setScaleAnima(String fileName, Scale scale) {
        Anima anima = (Anima)this.data.get(1, fileName);
        anima.changeSize((int)scale.x((float)anima.getSize().x), (int)scale.y((float)anima.getSize().y));
        this.data.set(1, fileName, anima);
    }

    public void ChangeAnimaSize(String fileName, int width, int height) {
        Anima anima = (Anima)this.data.get(1, fileName);
        anima.changeSize(width, height);
        this.data.set(1, fileName, anima);
    }

    public void anima(String fileName, int x, int y, float playTime) {
        if(this.canvas != null && this.frame != null) {
            Anima anima = (Anima)this.data.get(1, fileName);
            if(anima == null) {
                return;
            }

            anima.playTime = playTime;
            this.canvas.drawBitmap(anima.play(this.frame), (float)x, (float)y, this.paint);
        }

    }

    public void textPaint(int color, float size)
    {
        textPaint.setColor(color);
        textPaint.setTextSize(size);
        textSizeY = size;
        textSizeX = size/3*2;
    }

    public void textSenterX(String text, int persentY, Point scr)
    {
       /* String[] spliter = text.split(" ");
        int space = spliter.length - 1;
        int spaceLen = ((space / 20) * 5);*/
        float wordLen = textSizeX * (text.length() /*- space + spaceLen*/);
        int x = (int)((scr.x - wordLen)/2);
        if(x < 0)
            x = 0;
        int y = scr.y * persentY / 100 - (int)textSizeX;
        canvas.drawText(text,x ,y ,textPaint);
    }//Test..

    public static final Paint getColor(int Color) {
        final Paint color = new Paint();
        color.setColor(Color);
        return color;
    }
}
