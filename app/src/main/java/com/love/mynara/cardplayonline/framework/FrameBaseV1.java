package com.love.mynara.cardplayonline.framework;

import java.util.HashMap;

public class FrameBaseV1 {
    private HashMap<String, Float> timeStorage = new HashMap();
    private long ActivedTime;
    private long TimeFloow;
    private int realFPS;
    private int FPS;
    private float getRealFPS;
    private float playTimeSec;
    private float subOneSec;
    private float count;
    private float subCount;
    private float speedControl = 1.0F;
    private byte countType;
    private float accumTime;
    private boolean accumSwitch;
    private static final int ELEMENT = 0;
    private static final int REVERSE = 1;
    private static final int RUNTIME = 2;
    private static final int SUB_ADD = 3;
    private static final int IS_REBACK = 4;
    private static final int IS_LAUNCHED = 5;
    private static final int FALSE = 0;
    private static final int TRUE = 1;
    private static final int UNDEFINED = -1;
    public static final byte OUTONOMY_TYPE = 0;
    public static final byte REALTIME_COMPARE = 1;
    public static final byte NONE_SKIP = 2;

    public FrameBaseV1() {
    }

    public void setCountType(byte staticInt) {
        this.countType = staticInt;
    }

    public long getCount(int FPS) {
        if(this.FPS != FPS) {
            this.ActivedTime = System.currentTimeMillis();
            this.FPS = FPS;
        }

        if(this.subCount == 0.0F) {
            switch(this.countType) {
                case 1:
                    this.TimeFloow = System.currentTimeMillis();
                    this.count = (float)((this.TimeFloow - this.ActivedTime) * (long)FPS / 1000L);
                    ++this.getRealFPS;
                    float secTmp = (float)((this.TimeFloow - this.ActivedTime) / 1000L);
                    if(this.playTimeSec != secTmp) {
                        this.playTimeSec = secTmp;
                        this.realFPS = (int)this.getRealFPS;
                        this.getRealFPS = 0.0F;
                        this.speedControl = (float)(FPS / this.realFPS);
                    }

                    return (long)this.count;
                case 2:
                    if(this.TimeFloow == 0L) {
                        this.TimeFloow = 1L;
                        return 0L;
                    }

                    if(this.TimeFloow == 1L) {
                        this.TimeFloow = System.currentTimeMillis();
                        this.speedControl = (float)(this.TimeFloow - this.ActivedTime * (long)FPS / 1000L);
                    }

                    this.count += this.speedControl;
                    ++this.getRealFPS;
                    return (long)this.count;
                default:
                    ++this.getRealFPS;
                    this.count += this.speedControl;
                    this.TimeFloow = System.currentTimeMillis();
                    if(this.TimeFloow - this.ActivedTime >= 1000L) {
                        this.ActivedTime = this.TimeFloow;
                        this.realFPS = (int)this.getRealFPS;
                        this.speedControl = (float)FPS / (float)this.realFPS;
                        this.getRealFPS = 0.0F;
                    }

                    return (long)this.count;
            }
        } else {
            return 0L;
        }
    }
    public int getFPS() { return FPS; }

    public int getRealCountFrame() {
        return this.realFPS;
    }

    public float getSkipValue() {
        return this.speedControl - 1.0F;
    }

    public void unskipThisDelay() {
        this.getRealFPS = (float)this.FPS / this.speedControl;
    }

    public int getTurn(int[] data, float playTime) {
        int count = (int)this.count;
        if(data[2] == 0) {
            data[2] = count;
        }

        int runTime = count - data[2];
        float countReTouch = (float)data[0] / ((float)this.FPS * playTime);
        switch(data[1]) {
            case 0:
                return (int)(((float)runTime * countReTouch + (float)data[0]) % (float)data[0]);
            default:
                int totalArgu = data[0] * 2 - data[1];
                countReTouch = (float)(totalArgu - data[1]) / ((float)this.FPS * playTime);
                int turn;
                if((float)runTime * countReTouch < (float)totalArgu) {
                    turn = (int)(((float)runTime * countReTouch + (float)totalArgu) % (float)totalArgu);
                } else {
                    turn = (int)(((float)runTime * countReTouch + (float)totalArgu + (float)data[3]) % (float)totalArgu);
                    data[4] = 0;
                }

                if(turn >= data[0]) {
                    data[4] = 1;
                    turn = data[0] - turn % data[0] - 2;
                }

                if(turn == data[1] - 1 && data[5] == 0) {
                    data[5] = 1;
                }

                if(data[5] == 1 && data[4] == 1) {
                    data[3] += data[1];
                    data[5] = -1;
                }

                if(turn != data[1] - 1) {
                    data[5] = 0;
                }

                return turn;
        }
    }

    public boolean timeSwitch(String switchName, float playTime, boolean setRoof) {

        boolean result = false;
        Float str = null;

        try
        {
            str = ((Float)this.timeStorage.get(switchName)).floatValue();
        }
        catch (NullPointerException e)
        {
            this.timeStorage.put(switchName, Float.valueOf(this.count));
            str = count;
        }

        if(this.count - str >= playTime * FPS) {
            result = true;
        }

        if(setRoof && result) {
            this.timeStorage.put(switchName, Float.valueOf(this.count));
        }

        return result;
    }

    public long getSubFrame(boolean on_off) {
        if(on_off) {
            long curFloow = System.currentTimeMillis();
            long oneFrame = curFloow - this.TimeFloow;
            this.ActivedTime += oneFrame;
            this.TimeFloow = curFloow;
            switch(this.countType) {
                case 1:
                    this.speedControl += (float)(oneFrame * (long)this.FPS / 1000L);
                    this.subCount += this.speedControl;
                    ++this.getRealFPS;
                    this.subOneSec += (float)oneFrame;
                    if(this.subOneSec >= 1000.0F) {
                        this.subOneSec = 0.0F;
                        this.realFPS = (int)this.getRealFPS;
                        this.getRealFPS = 0.0F;
                        this.speedControl = (float)(this.FPS / this.realFPS);
                    }

                    return (long)this.subCount;
                default:
                    if(this.subCount == 0.0F) {
                        this.subOneSec = (float) System.currentTimeMillis();
                    }

                    ++this.getRealFPS;
                    this.subCount += this.speedControl;
                    this.TimeFloow = System.currentTimeMillis();
                    if((float)this.TimeFloow - this.subOneSec >= 1000.0F) {
                        this.subOneSec = (float)this.TimeFloow;
                        this.realFPS = (int)this.getRealFPS;
                        this.speedControl = (float)this.FPS / (float)this.realFPS;
                        this.getRealFPS = 0.0F;
                    }

                    return (long)this.subCount;
            }
        } else {
            this.subCount = 0.0F;
            return 0L;
        }
    }

    public void skip(int skipCount) {
        if(this.countType == 2) {
            this.speedControl += (float)skipCount;
        }

    }
}
