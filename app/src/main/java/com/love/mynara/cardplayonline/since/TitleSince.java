package com.love.mynara.cardplayonline.since;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.SurfaceHolder;

import com.love.mynara.cardplayonline.engine.TitleIntro;
import com.love.mynara.cardplayonline.framework.DrawBase;
import com.love.mynara.cardplayonline.framework.FrameBase;
import com.love.mynara.cardplayonline.framework.LoadBase;
import com.love.mynara.cardplayonline.framework.sub.Calculations;

public class TitleSince extends TitleIntro implements Runnable{

    private final static int FPS = 80;
    private final SurfaceHolder holder;
    private final TitleSince titleSince;
    private final Bitmap background;
    private final Calculations calcul;
    private final Point scr;

    private final LoadBase load;
    private final DrawBase draw;
    //private FrameBase frame;

    private PointF CMDistance;//CardMove
    private PointF CMFrameUnit;
    private PointF CMFrameProgress;
    private float CMDistanceX;
    private float CMDistanceY;
    private float CMPlayTime;
    private float CMRotateProgress;
    private float CMRotateUnit;

    private Point cardLocation;
    private float cardRotate;

    public TitleSince(LoadBase loadBase,FrameBase frameBase)
    {
        super(loadBase,frameBase,FPS);
        loadBase.getCashToBitmap("MainBackground.jpg");
        background = (Bitmap) loadBase.get(LoadBase.IMAGE,"MainBackground.jpg");
        titleSince = this;
        load = loadBase;
        draw = new DrawBase(loadBase, null);
        //frame = frameBase;
        holder = (SurfaceHolder) loadBase.getObject();
        scr = loadBase.getScreenSize();
        calcul = new Calculations(FPS,scr);
        prepare();
        CMFrameProgress = new PointF();
        CMDistance = new PointF(calcul.getScrSize(Calculations.X,CMDistanceX),calcul.getScrSize(Calculations.X,CMDistanceY));
        CMFrameUnit = new PointF(calcul.getFrameMoveUnit(CMPlayTime,CMDistance.x),calcul.getFrameMoveUnit(CMPlayTime,CMDistance.y));
        CMRotateUnit = calcul.getFrameMoveUnit(CMPlayTime,cardRotate);
    }

    @Override
    public void run()
    {
        Canvas canvas = holder.lockCanvas();
        draw.setCanvas(canvas);
        synchronized (holder)
        {
            start(canvas);
            drawCardAnima();
        }

        if(canvas != null)
            holder.unlockCanvasAndPost(canvas);

    }
    //--------------------------------------------------------------------------------------------------------------------------------user method

    //--------------------------------------------------------------------------------------------------------------------------------------private

    private void drawCardAnima()
    {
        draw.rotateImage("card.png", cardLocation.x - CMFrameProgress.x, cardLocation.y + CMFrameProgress.y, -CMRotateProgress);
        draw.image("card.png", cardLocation.x, cardLocation.y);
        draw.rotateImage("card.png", cardLocation.x + CMFrameProgress.x, cardLocation.y + CMFrameProgress.y, CMRotateProgress);

        if(CMDistance.x>CMFrameProgress.x)
        {
            CMFrameProgress.x += CMFrameUnit.x;
            CMFrameProgress.y += CMFrameUnit.y;
            CMRotateProgress += CMRotateUnit;
        }
    }

    private void prepare()
    {
        //-----------------------------------------------------각종 수치 설정을 하는 구간입니다.

        titleSince.setBackground(background);
        titleSince.setTitle("title.png",80,16);
        titleSince.setResizeTitleAnima(150,90,0.125f);
        titleSince.setTitleFlashAnima(0.5f,4f,true);
        titleSince.done();

        draw.saveScaleImage("card.png",25);//이미지파일의 크기를 조정

        CMDistanceX =8f;//화면단위100분율(X축 기준)
        CMDistanceY =2f;//화면단위100분율(X축 기준)
        CMPlayTime = 0.17f;
        cardRotate = 20f;

        final Point cardSize = draw.getSize("card.png");
        cardLocation = new Point((int)calcul.getScrSize(Calculations.X,50)-cardSize.x/2,(int)calcul.getScrSize(Calculations.Y,50)-cardSize.y/2);
        //=====================================================
    }
}
