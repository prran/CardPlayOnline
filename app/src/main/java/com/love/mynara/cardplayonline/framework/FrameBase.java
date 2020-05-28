package com.love.mynara.cardplayonline.framework;


import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class FrameBase
{
    private HashMap<String, Timer> timeStorage = new HashMap();
    private HashMap<String, Frame> frameStorage = new HashMap();

    public void start(String frameName, Runnable doing, int FPS)
    {
        final Frame frame = new Frame(doing,FPS);
        frame.start();
        frameStorage.put(frameName,frame);
    }


    public void stop(String frameName)
    {
        frameStorage.get(frameName).stopCycle();
    }


    public boolean timeSwitch(String applyFrameName, String switchName, float playTime, boolean setRoof)
    {
        boolean result = false;
        Timer timer = null;

        try
        {
            timer = timeStorage.get(switchName);
            result = timer.result;
        }
        catch (NullPointerException e)
        {
            Frame frame = frameStorage.get(applyFrameName);
            Timer newTimer = new Timer(playTime,frame.FPS,setRoof);
            frame.addTimer(newTimer);
            timeStorage.put(switchName, newTimer);
        }

        return result;
    }

    //------------------------------------------------------------------------------------
    //Frame관련
    private class Frame extends Thread
    {
        private long time;
        private double devicePerformance;
        private int FPS;
        private int count;
        private float msCycle;
        private boolean isRun;
        private Runnable run;
        private ArrayList<Timer> timer = new ArrayList<>();

        public Frame(Runnable runnable,int FPS) {super(); this.FPS = FPS; run = runnable;}

        @Override
        public void run()
        {
            prepare();
            while(isRun)
            {
                startCycle();
                try
                {for(Timer tmp:timer)
                    tmp.set();
                }
                catch (NullPointerException e){Log.e("framebaseRun","timerNull");}
                run.run();
            }
        }

        private void prepare()
        {
            msCycle = 1000f/(float)FPS;
            isRun = true;
            time = System.currentTimeMillis();
            count = 0;

            while(System.currentTimeMillis() - time <= msCycle) count++;
            devicePerformance = count;
            count = 0;
        }

        private void startCycle()
        {
                for (time = System.currentTimeMillis();devicePerformance >= count;count++);
                devicePerformance *= (msCycle/(System.currentTimeMillis() - time));
                count = 0;
        }

        private void stopCycle() {isRun = false;}

        private int getFPS() {return FPS;}

        private void addTimer(Timer timer) {this.timer.add(timer);}

    }

    //------------------------------------------------------------------------------------
    //Timer관련

    private class Timer {
        private float leftTime;
        private int count = 0;
        private int countCycle;
        private boolean result;
        private boolean setRoof;

        public Timer(float playTime,int FPS,boolean setRoof) {
            leftTime = (float)FPS*playTime;
            countCycle = (int)Math.floor(leftTime);
            leftTime -= countCycle;
            this.setRoof = setRoof;
        }

        public void set() {
            if (count >= countCycle)
                result = true;
            else
                result = false;
            if (result)
            {
                if (setRoof) {count = 0;}
                else {result = false; return;}
            }
            count++;
        }
    }
}
