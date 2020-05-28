package com.love.mynara.cardplayonline.framework;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

public class InputBase {
    private Activity act;
    private MotionEvent touch;
    private InputMethodManager keyBoardSet;
    private InputBase.KeyListener keyListener;

    @SuppressLint("WrongConstant")
    public InputBase(Activity activity, MotionEvent e) {
        this.act = activity;
        this.keyBoardSet = (InputMethodManager)this.act.getSystemService("input_method");
        this.touch = e;
    }

    public InputBase(MotionEvent e) {
        this.touch = e;
    }

    public void updateMotionEvent(MotionEvent e)
    {touch = e;}


    public void Keybroad(boolean Show_Hide) {
        if(this.act != null) {
            if(Show_Hide) {
                this.keyBoardSet.toggleSoftInput(1, 0);
                Log.i("Log", "keyBoard is set!");
            } else if(!Show_Hide) {
                this.keyBoardSet.toggleSoftInput(0, 1);
            }
        }

    }

    public void setKeyListener(InputBase.KeyListener e) {
        this.keyListener = e;
    }

    public InputBase.KeyListener getKeyListener() {
        return this.keyListener;
    }

    public InputMethodManager getKeyBoard() {
        return this.keyBoardSet;
    }

    public boolean isState(int Static_state) {
        return this.touch.getActionMasked() == Static_state;
    }

    public int getActionMasked()
    {
        return touch.getActionMasked();
    }

    public ArrayList<float[]> getAllPoint() {
        ArrayList points = new ArrayList();

        for(int count = this.touch.getPointerCount(); count != 0; --count) {
            points.add(new float[]{this.touch.getX(count), this.touch.getY(count)});
        }

        return points;
    }

    public Point getPoint() {
        Point point = new Point((int)this.touch.getX(), (int)this.touch.getY());
        return point;
    }

    public interface KeyListener {
        void setKeyMethode(short var1, KeyEvent var2);
    }

    public interface touchListener {
        int applyTouchMethod(MotionEvent var1);
    }
}
