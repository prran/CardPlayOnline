package com.love.mynara.cardplayonline.framework;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import com.love.mynara.cardplayonline.framework.sub.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class LoadBase {
    private AssetManager asset;
    private SoundPool sound;
    private InputBase input;
    private PlayBase play;
    private Scale scale;
    private HashMap<String, Bitmap> imageStorage;
    private HashMap<String, Anima> animaStorage;
    private HashMap<String, MediaPlayer> musicStorage;
    private HashMap<String, Integer> soundStorage;
    private HashMap<String, String> textStorage;
    private ArrayList<String> imageName;
    private ArrayList<String> animaName;
    private ArrayList<String> soundName;
    private ArrayList<String> textName;
    private ArrayList<String> afterName;
    private ArrayList<String> getIndex;
    private Canvas canvas;
    private Bitmap background;
    private Bitmap bar;
    private Paint nullBG;
    private Paint nullBar;
    private Paint text;
    private String recentDirectory;
    private String renderDerectory;
    private String effect;
    private String log;
    private int barLength;
    private int barHeight;
    private int switcher;
    private int index;
    private int fileCount;
    private int loadCount;
    private int animaCount;
    private int imageCount;
    private int soundCount;
    private Point scr = new Point();
    private Point barLocation;
    private Point textLocation;
    private boolean newAfter;
    private boolean displayLog;
    private boolean priLoaded;
    private Object ob;
    public Activity activity;
    public static final int LOAD = -3;
    public static final int ID = -2;
    public static final int ALL = -1;
    public static final int IMAGE = 0;
    public static final int ANIMA = 1;
    public static final int SOUND = 2;
    public static final int TEXT = 3;

    public Context conText;

    @SuppressLint({"NewApi", "WrongConstant"})
    public LoadBase(Context context) {
        if(Build.VERSION.SDK_INT < 14)
        ((WindowManager)context.getSystemService("window")).getDefaultDisplay().getSize(this.scr);
        else {
            try {
                Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                Display.class.getMethod("getRealSize", Point.class).invoke(display, scr);
            } catch (IllegalArgumentException e) {
                ((Activity) context).finish();
            } catch (IllegalAccessException e) {
                ((Activity) context).finish();
            } catch (InvocationTargetException e) {
                ((Activity) context).finish();
            } catch (NoSuchMethodException e) {
                ((Activity) context).finish();
            }
        }
        this.asset = context.getAssets();
        conText = context;
        this.activity = (Activity)context;
        this.scale = new Scale(this.scr);
        this.imageStorage = new HashMap();
        this.animaStorage = new HashMap();
        this.musicStorage = new HashMap();
        this.soundStorage = new HashMap();
        textStorage = new HashMap();
        this.imageName = new ArrayList();
        this.animaName = new ArrayList();
        this.soundName = new ArrayList();
        this.afterName = new ArrayList();
        this.getIndex = new ArrayList();
        this.nullBG = new Paint();
        this.nullBG.setColor(-1);
        this.nullBar = new Paint();
        this.nullBar.setColor(-16776961);
        this.text = new Paint();
        this.text.setTextSize(20.0F);
        this.barLocation = new Point();
        this.barLocation.x = -1;
        this.barLocation.y = this.scr.y / 16 * 12;
        this.textLocation = new Point();
        this.textLocation.x = this.scr.x / 8;
        this.textLocation.y = (int)((float)(this.scr.y / 16 * 12) + this.scale.y(5.0F));
        this.recentDirectory = "Undefined";
        this.renderDerectory = "after/image/";
        this.log = "priparing...";
        this.newAfter = true;

        try {
            this.prepare();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public boolean priLoad() {
        try {
            boolean e = false;
            int var3;
            switch(this.switcher) {
                case 0:
                    this.updateLogText("LoadBase", "Load " + (String)this.imageName.get(this.index) + " about Image...");
                    this.image("before/image/" + (String)this.imageName.get(this.index));
                    var3 = this.imageCount;
                    break;
                case 1:
                    this.updateLogText("LoadBase", "Load " + (String)this.animaName.get(this.index) + " about Animation...");
                    this.anima("before/anima/" + (String)this.animaName.get(this.index));
                    var3 = this.animaCount;
                    break;
                case 2:
                    this.updateLogText("LoadBase", "Load " + (String)this.soundName.get(this.index) + " about Sound...");
                    this.sound("before/sound/" + (String)this.soundName.get(this.index));
                    var3 = this.soundCount;
                    break;
                default:
                    this.priLoaded = true;
                    return true;
            }

            ++this.index;
            ++this.loadCount;
            if(this.index == var3) {
                ++this.switcher;
                this.index = 0;
            }

            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public boolean load(String[] files) {
        short obChange = 0;
        int index = 0;
        if(this.newAfter) {
            if(this.imageStorage.get(files[0]) == null && this.animaStorage.get(files[0]) == null && this.soundStorage.get(files[0]) == null && this.musicStorage.get(files[0]) == null) {
                this.newAfter = false;
                this.delete(this.afterName);
                this.afterName = new ArrayList();
                Collections.addAll(this.afterName, files);
                this.loadCount = 0;
                this.fileCount = this.afterName.size();

                Log.d("t","after/sound/" + files[this.index]);
            }
        } else if(this.loadCount == this.fileCount) {
            this.newAfter = true;
            return true;
        }

        while(!this.newAfter && this.loadCount != this.fileCount) {
            try {
                switch(obChange) {
                    case 0:
                        this.image("after/image/" + files[index]);
                        this.updateLogText("Loading", "Load " + (String)this.afterName.get(index) + " about Image...");
                        ++this.loadCount;
                        ++index;
                        break;
                    case 1:
                        this.anima("after/anima/" + files[index]);
                        this.updateLogText("Loading", "Load " + (String)this.afterName.get(index) + " about Anima...");
                        ++this.loadCount;
                        ++index;
                        break;
                    case 2:
                        this.sound("after/sound/" + files[index]);
                        this.updateLogText("Loading", "Load " + (String)this.afterName.get(index) + " about Sound...");
                        ++this.loadCount;
                        ++index;
                        break;
                    default:
                        if(!this.priLoaded) {
                            this.resetPrepare();
                        }

                        return false;
                }
            } catch (Exception var5) {
                ++obChange;
            }
        }

        if(!this.priLoaded) {
            this.resetPrepare();
        }

        return true;
    }

    public boolean pieceLoad(String[] files) {
        short obChange = 0;
        if(this.newAfter) {
            if(this.imageStorage.get(files[0]) == null && this.animaStorage.get(files[0]) == null && this.soundStorage.get(files[0]) == null && this.musicStorage.get(files[0]) == null) {
                this.newAfter = false;
                this.delete(this.afterName);
                this.afterName = new ArrayList();
                Collections.addAll(this.afterName, files);
                this.loadCount = 0;
                this.fileCount = this.afterName.size();
            }
        } else {
            if(this.loadCount == this.fileCount) {
                this.newAfter = true;
                this.index = 0;
                return true;
            }

            if(this.loadCount == 0) {
                return false;
            }
        }

        while(!this.newAfter && this.loadCount != this.fileCount) {
            try {
                switch(obChange) {
                    case 0:
                        this.image("after/image/" + files[this.index]);
                        this.updateLogText("Loading", "Load " + (String)this.afterName.get(this.index) + " about Image...");
                        break;
                    case 1:
                        this.anima("after/anima/" + files[this.index]);
                        this.updateLogText("Loading", "Load " + (String)this.afterName.get(this.index) + " about Anima...");
                        break;
                    case 2:
                        this.sound("after/sound/" + files[this.index]);
                        this.updateLogText("Loading", "Load " + (String)this.afterName.get(this.index) + " about Sound...");
                        break;
                    default:
                        if(!this.priLoaded) {
                            this.resetPrepare();
                        }

                        return false;
                }

                ++this.loadCount;
                ++this.index;
                return true;
            } catch (Exception var4) {
                ++obChange;
            }
        }

        if(!this.priLoaded) {
            this.resetPrepare();
        }

        return true;
    }

    public boolean load(String directory) {
        short obChage = 0;
        int index = 0;
        String path = directory + "/";
        final ArrayList<String> tmp = afterName;
        if(this.newAfter) {
            if(!directory.equals(recentDirectory)) {
                try {
                    newAfter = false;
                    afterName = new ArrayList();
                    fileCount = this.setFileCount(this.afterName, directory);
                    loadCount = 0;
                    updateLogText("LoadBase", "Success to looking for Directory : " + directory);
                    delete(tmp);
                    recentDirectory = directory;
                } catch (Exception var7) {
                    newAfter = true;
                    afterName = tmp;
                    return false;
                }
            }
        } else{
            if(this.loadCount == this.fileCount)
            {
                this.newAfter = true;
                return true;
            }
        }

        while(!this.newAfter && this.loadCount < this.fileCount) {
            try {
                switch(obChage) {
                    case 0:
                        this.anima(path
                                + (String)this.afterName.get(index));
                        updateLogText("Loading", "Load " + this.afterName.get(index) + " about Anima...");
                        break;
                    case 1:
                        this.image(path + (String)this.afterName.get(index));
                        this.updateLogText("Loading", "Load " + this.afterName.get(index) + " about Image...");
                        break;
                    case 2:
                        this.sound(path + (String)this.afterName.get(index));
                        this.updateLogText("Loading", "Load " + this.afterName.get(index) + " about Sound...");
                        break;
                    default:
                        return false;
                }

                ++this.loadCount;
                ++index;
            } catch (Exception var6) {
                ++obChage;
            }
        }

        if(!this.priLoaded) {
            this.resetPrepare();
        }

        return true;
    }

    public boolean pieceLoad(String derectory) {
        short obChage = 0;
        String path = derectory + "/";
        if(this.newAfter) {
            if(derectory != this.recentDirectory) {
                try {
                    this.newAfter = false;
                    this.delete(this.afterName);
                    this.afterName = new ArrayList();
                    this.loadCount = 0;
                    this.fileCount = this.setFileCount(this.afterName, derectory);
                    this.recentDirectory = derectory;
                } catch (Exception var5) {
                    this.newAfter = true;
                    this.recentDirectory = null;
                    this.resetPrepare();
                    return false;
                }
            }
        } else {
            if(this.loadCount == this.fileCount) {
                this.newAfter = true;
                this.index = 0;
                return true;
            }

            if(this.loadCount == 0) {
                return false;
            }
        }

        while(!this.newAfter && this.loadCount != this.fileCount) {
            try {
                switch(obChage) {
                    case 0:
                        this.anima(path + (String)this.afterName.get(this.index));
                        this.updateLogText("Loading", "Load " + (String)this.afterName.get(this.index) + " about Anima...");
                        break;
                    case 1:
                        this.image(path + (String)this.afterName.get(this.index));
                        this.updateLogText("Loading", "Load " + (String)this.afterName.get(this.index) + " about Image...");
                        break;
                    case 2:
                        this.sound(path + (String)this.afterName.get(this.index));
                        this.updateLogText("Loading", "Load " + (String)this.afterName.get(this.index) + " about Sound...");
                        break;
                    default:
                        if(!this.priLoaded) {
                            this.resetPrepare();
                        }

                        return false;
                }

                ++this.loadCount;
                ++this.index;
                return true;
            } catch (Exception var6) {
                ++obChage;
            }
        }

        if(!this.priLoaded) {
            this.resetPrepare();
        }

        return true;
    }

    public boolean isExist(String fileName)
    {
        String[] fileIdentity = fileName.split("\\.");

        switch (fileIdentity[1])
        {
            case "jpg":
            case "png":
            case "jpeg":

                if(imageStorage.get(fileName) == null) return false;
                else return true;

                default:
                Log.w("preparing Method","이미지 외의 파일은 아직 판별할 수 없습니다.");
                return false;
        }
    }

    public boolean textLoad(String fileName)
    {
        String str;

        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(asset.open(fileName)));
            for(int i = 0;(str = reader.readLine()) != null;i++)
            {

                textStorage.put(new Integer(i).toString(), str);

                Log.i("textLoad", "Key =" + i +" str is" + str);
            }
        }
        catch(IOException e)
        {
            return false;
        }

        return true;
    }

    public Object get(int fileInfo, String fileName) {
        switch(fileInfo) {
            case -2:
                return this.soundStorage.get(fileName);
            case -1:
            default:
                return null;
            case 0:
                return this.imageStorage.get(fileName);
            case 1:
                return this.animaStorage.get(fileName);
            case 2:
                return this.musicStorage.get(fileName);
            case 3:
                return this.textStorage.get(fileName);
        }
    }

    public void set(int fileInfo, String fileName, Object ob) {
        switch(fileInfo) {
            case -2:
                this.soundStorage.put(fileName, (Integer)ob);
            case -1:
            default:
                return;
            case 0:
                this.imageStorage.put(fileName, (Bitmap)ob);
                return;
            case 1:
                this.animaStorage.put(fileName, (Anima)ob);
                return;
            case 2:
                this.musicStorage.put(fileName, (MediaPlayer)ob);
        }
    }

    public int getFileCount(int fileInfo) {
        switch(fileInfo) {
            case -3:
                return this.loadCount;
            case -2:
            default:
                return 0;
            case -1:
                return this.fileCount;
            case 0:
                return this.imageCount;
            case 1:
                return this.animaCount;
            case 2:
                return this.soundCount;
        }
    }

    public SoundPool getSoundPool() {
        return this.sound;
    }

    public void setInputBase(Context context, MotionEvent e) {
        if(this.input == null) {
            this.input = new InputBase((Activity)context, e);
        }

    }

    public InputBase getInputBase() {
        return this.input;
    }

    public void setScreenSize(int Width, int Height) {
        this.scr.x = Width;
        this.scr.y = Height;
        this.scale.setScr(Width, Height);
    }

    public Point getScreenSize() {
        return this.scr;
    }

    public void basicRender(SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas((Rect)null);
        synchronized(holder) {
            canvas.drawRect(0.0F, 0.0F, (float)this.scr.x, (float)this.scr.y, this.nullBG);
            canvas.drawRect((float)(this.scr.x / 8), (float)(this.scr.y / 16 * 12), (float)(this.scr.x / 8 + this.scr.x / (8 * this.fileCount) * 6 * this.loadCount), (float)(this.scr.y / 16 * 14), this.nullBar);
        }

        if(canvas != null) {
            holder.unlockCanvasAndPost(canvas);
        }

    }

    public void saveObject(Object object)
    {
        ob = object;
    }

    public Object getObject()
    {
        return ob;
    }

    public void saveBitmapToJPGCache(Bitmap bitmap, String fileName)
    {
        final File filePath = new File(new ContextWrapper(conText).getDir("image",Context.MODE_PRIVATE),fileName);
        FileOutputStream stream = null;
        try
        {
            stream = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        }
        catch(Exception e){}
        finally {
            try{stream.close();}
            catch(Exception e){}
        }
    }
    public void getCashToBitmap(String fileName)
    {
        final File file = new File(new ContextWrapper(conText).getDir("image",Context.MODE_PRIVATE),fileName);
        try{imageStorage.put(fileName, BitmapFactory.decodeStream(new FileInputStream(file)));}
        catch(Exception e){}
    }
    public boolean isExistCach(String fileName)
    {
        return new File(new ContextWrapper(conText).getDir("image",Context.MODE_PRIVATE).toString()+"/"+fileName).exists();
    }



    //-----------------------------------------------------------------------로딩화면 관련 함수들

    public void render(SurfaceHolder holder) {
        this.canvas = holder.lockCanvas((Rect)null);
        boolean width = false;
        float barCenterSet = 0.0F;
        Bitmap barState = null;
        if(this.bar != null) {
            int width1 = this.bar.getWidth() * this.loadCount / this.fileCount;
            barCenterSet = (float)((this.scr.x - this.bar.getWidth()) / 2);
            if(width1 == 0) {
                width1 = 1;
            }

            barState = Bitmap.createBitmap(this.bar, 0, 0, width1, this.bar.getHeight());
        }

        synchronized(holder) {
            if(this.background != null) {
                this.canvas.drawBitmap(this.background, 0.0F, 0.0F, (Paint)null);
            } else {
                this.canvas.drawRect(0.0F, 0.0F, (float)this.scr.x, (float)this.scr.y, this.nullBG);
            }

            if(this.bar != null) {
                if(this.barLocation.x == -1) {
                    this.canvas.drawBitmap(barState, barCenterSet, (float)this.barLocation.y, (Paint)null);
                } else {
                    this.canvas.drawBitmap(barState, (float)this.barLocation.x, (float)this.barLocation.y, (Paint)null);
                }
            } else {
                this.canvas.drawRect((float)(this.scr.x / 8), (float)(this.scr.y / 16 * 12), (float)(this.scr.x / 8 + this.scr.x / (8 * this.fileCount) * 6 * this.loadCount), (float)(this.scr.y / 16 * 14), this.nullBar);
            }

            if(this.displayLog) {
                this.canvas.drawText(this.log, (float)this.textLocation.x, (float)this.textLocation.y, this.text);
            }
        }

        if(this.canvas != null) {
            holder.unlockCanvasAndPost(this.canvas);
        }

        if(this.loadCount == this.fileCount) {
            this.clearRenderSound();
            this.log = "Complete!";
        }

    }

    public void render(SurfaceHolder holder, LoadBase.Support support) {
        Canvas canvas = holder.lockCanvas((Rect)null);
        boolean width = false;
        float barCenterSet = 0.0F;
        Bitmap barState = null;
        if(this.bar != null) {
            int width1 = this.bar.getWidth() * this.loadCount / this.fileCount;
            barCenterSet = (float)((this.scr.x - this.bar.getWidth()) / 2);
            if(width1 == 0) {
                width1 = 1;
            }

            barState = Bitmap.createBitmap(this.bar, 0, 0, width1, this.bar.getHeight());
        }

        synchronized(holder) {
            if(this.background != null) {
                canvas.drawBitmap(this.background, 0.0F, 0.0F, (Paint)null);
            } else {
                canvas.drawRect(0.0F, 0.0F, (float)this.scr.x, (float)this.scr.y, this.nullBG);
            }

            support.drawBgScreen(canvas);
            if(this.bar != null) {
                if(this.barLocation.x == -1) {
                    canvas.drawBitmap(barState, barCenterSet, (float)(this.scr.y / 16 * 12), (Paint)null);
                } else {
                    canvas.drawBitmap(barState, (float)this.barLocation.y, (float)this.barLocation.y, (Paint)null);
                }
            } else {
                canvas.drawRect((float)(this.scr.x / 8), (float)(this.scr.y / 16 * 12), (float)(this.scr.x / 8 + this.scr.x / (8 * this.fileCount) * 6 * this.loadCount), (float)(this.scr.y / 16 * 14), this.nullBar);
            }

            if(this.displayLog) {
                canvas.drawText(this.log, (float)this.textLocation.x, (float)this.textLocation.y, this.text);
            }

            support.additionalDrawing(canvas);
        }

        if(canvas != null) {
            holder.unlockCanvasAndPost(canvas);
        }

        if(this.loadCount == this.fileCount) {
            this.clearRenderSound();
            this.log = "Complete!";
        }

    }

    public void setDerectory(String derectory) {
        this.renderDerectory = derectory;
    }

    public void setBG(String fileName) {
        try {
            this.image(this.renderDerectory + fileName);
            this.background = Bitmap.createScaledBitmap((Bitmap)this.get(0, fileName), this.scr.x, this.scr.y, true);
            this.imageStorage.remove(fileName);
        } catch (Exception var3) {
            Log.e("FAIL", "File not found");
        }
    }

    public void setBar(String fileName) {
        try {
            this.image(this.renderDerectory + fileName);
            this.bar = (Bitmap)this.get(0, fileName);
            this.imageStorage.remove(fileName);
        } catch (Exception var3) {
            Log.e("FAIL", "File not found");
        }
    }

    public void setInfo(boolean onOff) {
        this.displayLog = onOff;
    }

    public void setInfoSize(int size) {
        this.text.setTextSize(this.scale.x((float)size));
    }

    public void setBarLocation(int length, int height, int y) {
        try {
            this.barLength = (int)this.scale.x((float)length);
            this.barHeight = (int)this.scale.y((float)height);
            if(this.bar != null) {
                this.bar = Bitmap.createScaledBitmap(this.bar, this.barLength, this.barHeight, false);
                this.barLocation.y = (int)this.scale.y((float)y);
            } else {
                throw new Exception();
            }
        } catch (Exception var5) {
            var5.printStackTrace();
            Log.e("Error", "Bar is not Defined");
        }
    }

    public void setInfoLocation(int y) {
        this.textLocation.y = (int)this.scale.y((float)y);
    }

    public void freeLayoutBar(int length, int height, int x, int y) {
        try {
            this.barLength = (int)this.scale.x((float)length);
            this.barHeight = (int)this.scale.y((float)height);
            if(this.bar != null) {
                this.bar = Bitmap.createScaledBitmap(this.bar, this.barLength, this.barHeight, false);
                this.barLocation.x = (int)this.scale.x((float)x);
                this.barLocation.y = (int)this.scale.y((float)y);
            } else {
                throw new Exception();
            }
        } catch (Exception var6) {
            Log.e("Error", "Bar is not Defined");
        }
    }

    public void freeLayoutInfo(int x, int y) {
        this.textLocation.x = (int)this.scale.x((float)x);
        this.textLocation.y = (int)this.scale.y((float)y);
    }

    public void setMusic(PlayBase Play, String fileName) {
        try {
            this.play = Play;
            if(this.renderDerectory == "after/image/") {
                this.sound("after/sound/" + fileName);
            } else {
                this.sound(this.renderDerectory + fileName);
            }

            this.play.sound(fileName);
        } catch (Exception var4) {
            var4.printStackTrace();
            Log.e("FAIL", "File not found");
        }
    }

    public void setLoadCompleteSound(PlayBase Play, String fileName) {
        try {
            this.play = Play;
            this.effect = fileName;
            if(this.renderDerectory == "after/image/") {
                this.sound("after/sound/" + fileName);
            } else {
                this.sound(this.renderDerectory + fileName);
            }

        } catch (Exception var4) {
            Log.e("FAIL", "File not found");
        }
    }

    public void resetPrepare() {
        this.loadCount = 0;
        this.fileCount = this.imageCount + this.animaCount + this.soundCount;
        this.index = 0;
    }

    public boolean isComplete() {
        return this.fileCount >= this.loadCount - 1 && this.fileCount != 0;
    }

    private void image(String derectory) throws Exception {
        BitmapDrawable getImage = (BitmapDrawable) Drawable.createFromStream(this.asset.open(derectory), derectory);
        Bitmap image = getImage.getBitmap();
        Log.i("Log", "Get File...");
        String[] filename = derectory.split("/");
        Collections.addAll(this.getIndex, filename);
        int index = this.getIndex.size() - 1;
        this.getIndex.clear();
        this.imageStorage.put(filename[index], image);
        Log.i("Log", "Save to Stroage...");
    }

    private void anima(String derectory) throws Exception {
        BitmapDrawable getImage = (BitmapDrawable) Drawable.createFromStream(this.asset.open(derectory), derectory);
        Bitmap image = getImage.getBitmap();
        Log.i("Log", "Get File...");
        int totalWidth = image.getWidth();
        String matchingFileName = (new Integer(image.getHeight())).toString();
        BitmapDrawable getMatchingFile = (BitmapDrawable) Drawable.createFromStream(this.asset.open("sub/" + matchingFileName + ".png"), matchingFileName);
        Log.i("Log", "Get Matched File...");
        int width = getMatchingFile.getBitmap().getWidth();
        int[] info = new int[]{totalWidth / width, totalWidth % width, 0, 0, 0, 0, width};
        Anima anima = new Anima(image, info);
        String[] filename = derectory.split("/");
        Collections.addAll(this.getIndex, filename);
        int index = this.getIndex.size() - 1;
        this.getIndex.clear();
        this.animaStorage.put(filename[index], anima);
        Log.i("Log", "Save to Storage...");
        if(this.animaStorage.get(filename[index]) != null) {
            ;
        }
    }

    private void sound(String derectory) throws Exception {
        AssetFileDescriptor getSound = this.asset.openFd(derectory);
        Log.i("Log", "Get File...");
        long fileSize = getSound.getLength();
        String[] filename = derectory.split("/");
        Collections.addAll(this.getIndex, filename);
        int index = this.getIndex.size() - 1;
        this.getIndex.clear();
        if(fileSize >= 1048576L) {
            MediaPlayer id = new MediaPlayer();
            id.setDataSource(getSound.getFileDescriptor(), getSound.getStartOffset(), fileSize);
            Log.i("Log", "Change Object " + filename[index] + " to MediaPlayer...");
            this.musicStorage.put(filename[index], id);
            Log.i("Log", "Save to Storage...");
        } else {
            int id1 = this.sound.load(getSound, 8);
            Log.i("Log", "Load to SoundPool with " + filename[index]);
            this.soundStorage.put(filename[index], Integer.valueOf(id1));
            Log.i("Log", "Save SoundID to Storage...");
        }

        getSound.close();
    }

    private void delete(ArrayList<String> deleteMent) {
        int obChange = 0;
        int index = 0;
        if(deleteMent != null) {
            while(true) {
                String fileName;
                try {
                    fileName = (String)deleteMent.get(index);
                } catch (IndexOutOfBoundsException var7) {
                    return;
                }

                Object getNull;
                switch(obChange) {
                    case 0:
                        getNull = this.imageStorage.remove(fileName);
                        break;
                    case 1:
                        getNull = this.animaStorage.remove(fileName);
                        break;
                    case 2:
                        getNull = this.soundStorage.remove(fileName);
                        break;
                    case 3:
                        getNull = this.musicStorage.remove(fileName);
                        break;
                    default:
                        this.updateLogText("ERROR", "Load not found.");
                        return;
                }

                if(getNull == null) {
                    ++obChange;
                } else {
                    ++index;
                }
            }
        }
    }

    private void prepare() throws Exception {
        this.imageCount = this.setFileCount(this.imageName, "before/image");
        this.animaCount = this.setFileCount(this.animaName, "before/anima");
        this.soundCount = this.setFileCount(this.soundName, "before/sound");
        this.fileCount = this.imageCount + this.animaCount + this.soundCount;
        this.updateLogText("Pripare", "Get Amount of Files...");
        this.sound = new SoundPool(this.soundCount, 3, 0);
        this.updateLogText("Pripare", "Create SoundPool...");
    }

    private int setFileCount(ArrayList<String> nameStorage, String derectory) throws Exception {
        Collections.addAll(nameStorage, this.asset.list(derectory));
        return nameStorage.size();
    }

    private void updateLogText(String tag, String msg) {
        this.log = msg;
        Log.i(tag, msg);
    }

    private void clearRenderSound() {
        if(this.play != null && this.effect != null) {
            this.play.sound(this.effect);
            this.soundStorage.remove(this.effect);
            this.effect = null;
        }

    }

    public interface Support {
        void drawBgScreen(Canvas var1);

        void additionalDrawing(Canvas var1);
    }
}
