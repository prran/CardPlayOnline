package com.love.mynara.cardplayonline.framework.sub;

import android.app.Activity;
import com.love.mynara.cardplayonline.framework.sub.Linker;

public class RoofThread extends Thread {
    private Linker support;
    public Activity activity;
    public boolean isRun;

    public RoofThread(Runnable render) {
        super(render);
        this.support = (Linker)render;
        this.isRun = true;
    }

    public void run() {
        while(this.isRun) {
            super.run();
        }

    }

    public Linker getLinker() {
        return this.support;
    }

    public RoofThread stateChange(Runnable run, boolean start) {
        this.isRun = false;
        RoofThread thread = new RoofThread(run);
        if(start) {
            thread.start();
        }

        return thread;
    }
}
