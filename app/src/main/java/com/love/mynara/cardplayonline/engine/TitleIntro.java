package com.love.mynara.cardplayonline.engine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.Log;

import com.love.mynara.cardplayonline.framework.DrawBase;
import com.love.mynara.cardplayonline.framework.FrameBase;
import com.love.mynara.cardplayonline.framework.LoadBase;

import java.util.ArrayList;

public class TitleIntro {

    private final static String DynamicBackground = "@Background";
    private final static String DynamicTitle = "@Title";

    private ArrayList<Runnable> calculations;
    private String BGImage;
    private Canvas canvas;
    private Bitmap flashBitmap;
    private Paint maskPaint;
    private Paint flashPaint;
    private Point scr;
    private float keepYLocaUnit;
    private int i;
    private int FPS;

    private String titleImage;
    private Point titleSize;
    private Point titleStrPnt;
    private float titleY;

    private float[] TRSizes;//TR = TitleResize
    private float TRFrameUnit;
    private float TRChangeSize;

    private float TFFrameUnit;//TF = TitleFlash
    private float TFWaitTime;
    private float TFLocation;
    private boolean TFisRoof;

    private LoadBase load;
    private FrameBase frame;
    private DrawBase draw;

    protected TitleIntro(LoadBase loadBase,FrameBase frameBase, int FPS)
    {
        this.FPS = FPS;
        calculations = new ArrayList<>();
        load = loadBase;
        frame = frameBase;
        draw = new DrawBase(load,null);
        load.load("TitleIntro");
        scr = load.getScreenSize();
        flashBitmap = Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_8888);
    }

    protected void start(Canvas canvas)
    {
        draw.setCanvas(canvas);
        for(Runnable r : calculations) {r.run();}
        draw.image(BGImage,0,0);
        draw.centerScaleImagX(titleImage,titleY - keepYLocaUnit,TRSizes[0]);
        draw.image(flashBitmap,titleStrPnt.x,titleStrPnt.y);
    }

    /*public void setBackground(final String filename)
    {
        try
        {
            new ExtensionMissException().check(filename);
            new CannotFoundFileException().check(filename);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }
        BGImage = filename;
        draw.saveScaleImage(filename,100,100);
    }*/

    public void setBackground(final Bitmap bitmap)
    {
        try
        {
            if(bitmap == null)
                throw new NullPointerException();
            load.set(LoadBase.IMAGE,DynamicBackground,bitmap);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }
        BGImage = DynamicBackground;
        draw.saveScaleImage(DynamicBackground,100,100);
    }

    public void setTitle(final String filename, final int percentSizeByDeviceX, final float percentPositionByDeviceY)
    {
        titleY = percentPositionByDeviceY/100f*scr.y;
        try
        {
            new OverLimitException(100).check(percentPositionByDeviceY);
            new ExtensionMissException().check(filename);
            new CannotFoundFileException().check(filename);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }
        titleImage = filename;
        draw.saveScaleImage(titleImage,percentSizeByDeviceX);
        titleSize = draw.getSize(titleImage);
    }

    //setTitle 비트맵으로 추가기능은 보류

    public void setResizeTitleAnima(final float startSizePercent,final float middleSizePercent,final float playTime)
    {
        TRSizes = new float[]{startSizePercent/100f, middleSizePercent/100f, 1.0f};
        TRFrameUnit = FPS * playTime /2f;
    }

    public void setTitleFlashAnima(final float playtime, final float waitTime, final boolean isRoof)
    {
        TFFrameUnit = titleSize.x/(playtime * FPS);
        TFWaitTime = waitTime;
        TFisRoof = isRoof;
        flashPaint = new Paint();
        flashPaint.setAntiAlias(true);
        maskPaint = new Paint();
        canvas = new Canvas();
    }

    public void done()
    {
        if(BGImage == null)
            setDefaultBG();
        if(titleImage == null)
            setDefaultTitle();

        keepYLocaUnit = (float)titleSize.y/2f;

        if(TRSizes != null)
            setTRCalculattion();
        if(TFFrameUnit != 0)
            setTFCalculattion();
    }

    //public LoadBase getLoadBase() {return load;}

    //-------------------------------------------------------------------------------------------private


    private void setDefaultBG()
    {
        Bitmap background = Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(background);
        canvas.drawRect(0,0,1,1,DrawBase.getColor(0xFFFFFFFF));

        BGImage = DynamicBackground;
        load.set(LoadBase.IMAGE,BGImage,background);
        draw.saveScaleImage(BGImage,100,100);
    }

    private void setDefaultTitle()
    {
        int imageCutPoint = 0;

        Bitmap titleText = Bitmap.createBitmap(24,8, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(titleText);
        Paint paint = DrawBase.getColor(0XFF000000);
        paint.setTextSize(8);
        canvas.drawText("Title",0,0,paint);

        for(int x = 24;imageCutPoint == 0; x--) {
            for(int y = 0; y<8; y++){
                if(titleText.getPixel(x,y) == 0XFF000000)
                    imageCutPoint = x+1;
            }
        }

        Bitmap title = Bitmap.createBitmap(titleText,0,0,imageCutPoint,8);
        titleImage = DynamicTitle;
        load.set(LoadBase.IMAGE,titleImage,title);
        draw.saveScaleImage(titleImage,80);
        titleSize = draw.getSize(titleImage);
        titleY = scr.y * (1/5f);
    }

    private void setTRCalculattion()
    {
        calculations.add(new Runnable() {
            @Override
            public void run() {

                if(i == 3)
                    return;

                if(TRSizes[0] != TRSizes[i])
                {
                    TRSizes[0] += TRChangeSize;

                    if(Math.abs(TRSizes[0] - TRSizes[i])<0.01f)
                        TRSizes[0] = TRSizes[i];
                }
                else
                    try{TRChangeSize = (TRSizes[++i] - TRSizes[0])/TRFrameUnit;}
                    catch (ArrayIndexOutOfBoundsException e) {TRChangeSize = 0;}

                keepYLocaUnit = titleSize.y*TRSizes[0]/2;
            }
        });
    }

    private void setTFCalculattion()
    {
        titleStrPnt =  new Point((scr.x-titleSize.x)/2, (int) (titleY - titleSize.y/2));
        TFLocation = scr.x;

        maskPaint.setFilterBitmap(false);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        calculations.add(new Runnable() {
            @Override
            public void run()
            {
                if(i != 3 && TRSizes != null)
                    return;
                if(TFLocation >= -titleSize.x)
                {
                    TFLocation -= TFFrameUnit;
                    flashBitmap = Bitmap.createBitmap(titleSize.x,titleSize.y, Bitmap.Config.ARGB_8888);
                    canvas.setBitmap(flashBitmap);
                    flashPaint.setShader(new LinearGradient(TFLocation, titleSize.y, TFLocation + titleSize.y, 0, new int[]{0x00ffffff, 0xffffffff, 0x00ffffff},null/*float[]*/ , Shader.TileMode.CLAMP));
                    canvas.drawRect(0,0,titleSize.x,titleSize.y,flashPaint);
                    canvas.drawBitmap((Bitmap)load.get(LoadBase.IMAGE,titleImage),0,0,maskPaint);
                }
                else if(TFisRoof)
                {
                    if(frame.timeSwitch("TitleScreen","titleFlashReload",TFWaitTime,true))
                    {TFLocation = scr.x;}
                }
            }
        });
    }

    //=======================================================예외처리

    private class CannotFoundFileException extends Exception
    {
        @Override
        public void printStackTrace() {
            super.printStackTrace();
            Log.e("CannotFoundFile","asset/TitleIntro 디렉토리 내부에서 파일을 발견하지 못했습니다.");
        }

        void check(final String filename) throws CannotFoundFileException
        {
            if(!load.isExist(filename))
                throw this;
        }
    }

    private class ExtensionMissException extends Exception
    {
        @Override
        public void printStackTrace() {
            super.printStackTrace();
            Log.e("ExtensionMiss","파일의 확장자를 입력해주세요 -> image.png");
        }

        void check(final String filename) throws ExtensionMissException
        {
            String[] getExtension = filename.split("\\.");
            try{getExtension[1] = null;}
            catch (Exception e) { throw this; }
        }
    }

    private class OverLimitException extends Exception
    {
        private float number;
        private float limit;

        OverLimitException(final int limit)
        {
            this.limit = limit;
        }

        @Override
        public void printStackTrace() {
            super.printStackTrace();
            Log.e("OverLimitException","한계 수치값을 넘었습니다. 한계값 = '"+limit+"' 확인 값 = '"+number+"'");
            load.activity.finish();
        }

        void check(final float number) throws OverLimitException
        {
            if(limit<number)
            {
                this.number = number;
                throw this;
            }
        }
    }
}
