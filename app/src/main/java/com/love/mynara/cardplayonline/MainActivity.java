package com.love.mynara.cardplayonline;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    final private Handler UIWorks = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        surfaceView = findViewById(R.id.viewout);
    }

    public View getScreen()
    {return  surfaceView;}

    public void FristPaly()
    {
        UIWorks.post(new Runnable() {
            @Override
            public void run() {
                final Resources res = getApplicationContext().getResources();
                XmlResourceParser parser = res.getLayout(R.layout.imageview);
                final LayoutInflater inflater = getLayoutInflater();
                final ConstraintLayout layout = findViewById(R.id.layout);

                View view = inflater.inflate(parser,layout,false);
                layout.removeView(view);
                parser.close();

            }
        });
    }
}
