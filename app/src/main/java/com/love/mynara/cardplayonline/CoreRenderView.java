package com.love.mynara.cardplayonline;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.love.mynara.cardplayonline.framework.FrameBase;
import com.love.mynara.cardplayonline.framework.LoadBase;
import com.love.mynara.cardplayonline.since.TitleSince;

public final class CoreRenderView extends SurfaceView implements SurfaceHolder.Callback
{
    /*private static final String WIFI_STATE = "WIFE";
    private static final String MOBILE_STATE = "MOBILE";
    private static final String NONE_STATE = "NONE";*/

    private LoadBase load;
    private FrameBase frame;

    public CoreRenderView(Context context, AttributeSet attrs)
    {
        super(context,attrs);
        getHolder().addCallback(this);
        creator();//각종 리소스를 초기화 합니다.
    }

    public void surfaceCreated(SurfaceHolder holder) {
        frame.start("TitleScreen",new TitleSince(load,frame),80);
    }
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    public void surfaceDestroyed(SurfaceHolder holder) {}

    private void creator()
    {
        frame = new FrameBase();
        load = new LoadBase(getContext());
        load.saveObject(getHolder());
        if(!load.isExistCach("MainBackground.jpg"))
            ((MainActivity)getContext()).FristPaly();
    }


}
