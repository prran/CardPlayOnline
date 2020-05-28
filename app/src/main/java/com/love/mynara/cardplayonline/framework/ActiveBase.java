package com.love.mynara.cardplayonline.framework;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;

import com.love.mynara.cardplayonline.framework.sub.*;

import java.util.HashMap;

public class ActiveBase {
    private HashMap<String, float[]> dotStorage = new HashMap();
    private HashMap<String, Point> dotSizeStorage = new HashMap();
    private HashMap<String, float[]> boxStorage = new HashMap();
    private HashMap<String, Point[]> boxDetectStorage = new HashMap();
    private Bitmap detectSpace;
    private int nullColor = -16777216;
    public static final int x = 0;
    public static final int y = 1;
    public static final int strX = 0;
    public static final int strY = 1;
    public static final int finX = 2;
    public static final int finY = 3;

    @SuppressLint({"UseSparseArrays"})
    public ActiveBase() {
    }

    public void setPoint(float x, float y, String id) {
        this.dotStorage.put(id, new float[]{x, y});
    }

    public void setBox(float strX, float strY, float finX, float finY, String id) {
        float[] box = new float[]{strX, strY, finX, finY};
        this.boxStorage.put(id, box);
    }

    public void setDetectColor(int red, int blue, int green) {
        this.nullColor = Color.argb(255, red, green, blue);
    }

    public void setBitmap(Bitmap bitmap) {
        this.detectSpace = bitmap;
    }

    public void updatePoint(float addX, float addY, String pointId) {
        float[] point = (float[])this.dotStorage.get(pointId);
        point[0] += addX;
        point[1] += addY;
        this.dotStorage.put(pointId, point);
    }

    public void updateBox(float addX, float addY, String boxId)
    {
        float[] box = this.boxStorage.get(boxId);
        Point[] detect = boxDetectStorage.get(boxId);

        if(box == null)
        {
            Log.e("ActiveBase","box is not defined");
            return;
        }

        box[0] += addX;
        box[1] += addY;
        box[2] += addX;
        box[3] += addY;
        boxStorage.put(boxId,box);

        if(detect != null)
        {
            for(int i= 0 ; i<4; i++)
            {
                detect[i].x += addX;
                detect[i].y += addY;
            }
            boxDetectStorage.put(boxId,detect);
        }
    }

    public void setOneXorY(float Xor_Y, String pointId) {
        float[] point = (float[])this.dotStorage.get(pointId);
        if(Xor_Y > 0.0F) {
            point[0] = Xor_Y;
        } else {
            if(Xor_Y >= 0.0F) {
                return;
            }

            point[1] = 0.0F - Xor_Y;
        }

    }

    public boolean PtPDetect(String pointId1,String pointId2)
    {
        try{
            float[] dotA = dotStorage.get(pointId1);
            Point sizeA = dotSizeStorage.get(pointId1);
            float[] dotB = dotStorage.get(pointId2);
            Point sizeB = dotSizeStorage.get(pointId2);

            return Math.abs(dotA[0]-dotB[0]) <= sizeA.x+sizeB.x && Math.abs(dotA[1]-dotB[1]) <= sizeA.y+sizeB.y;
        }
        catch (NullPointerException e)
        {
            Log.e("PtPDetect","you must setPtPSize(String pointId, Point pointCenterLocation)");
            return false;
        }
    }

    public boolean PtClickDetect(String pointId1,Point click)
    {
        try{
            float[] point = dotStorage.get(pointId1);
            Point size = dotSizeStorage.get(pointId1);

            return Math.abs(point[0]-click.x) <= size.x && Math.abs(point[1]-click.y) <= size.y;
        }
        catch (NullPointerException e)
        {
            Log.e("PtPDetect","you must setPtPSize(String pointId, Point pointCenterLocation)");
            return false;
        }
    }

    public void setPtPSize(String pointId, Point pointCenterLocation)
    {
        dotSizeStorage.put(pointId, pointCenterLocation);
    }

    public boolean lineDetectX(String pointId, int X)
    {
        float point = 0;
        try {
            point = dotStorage.get(pointId)[0];

            if(point <X)
                return point >= X;
            else if(point >X)
                return point <= X;
        }
        catch (NullPointerException e)
        {
                Log.e("ActiveBase","lineDetect:point is not defined");
                return false;

        }

        return true;
    }

    public boolean lineDetect(int point, int X)
    {
            if(point <X)
                return point >= X;
            else if(point >= X)
                return point <= X;

        return true;
    }

    public boolean boxDetect(String pointId, String boxId) {
        float[] point = (float[])this.dotStorage.get(pointId);
        float[] box = (float[])this.boxStorage.get(boxId);
        return point != null && box != null && box[0] <= point[0] && point[0] <= box[2] && box[1] <= point[1] && point[1] <= box[3];
    }
    public boolean boxDetect(String boxId1, String boxId2, Object nullSpace) {
        Point[] point = boxDetectStorage.get(boxId1);
        float[] box = (float[])this.boxStorage.get(boxId2);
        try{
            return box[0] <= point[0].x && point[0].x <= box[2] && box[1] <= point[0].y && point[0].y <= box[3]||
                    box[0] <= point[1].x && point[1].x <= box[2] && box[1] <= point[1].y && point[1].y <= box[3]||
                    box[0] <= point[2].x && point[2].x <= box[2] && box[1] <= point[2].y && point[2].y <= box[3]||
                    box[0] <= point[3].x && point[3].x <= box[2] && box[1] <= point[3].y && point[3].y <= box[3];
        }
        catch (NullPointerException e)
        {
            float[] resource = this.boxStorage.get(boxId1);

            if(resource == null || box == null)
            {
                Log.e("ActiveBase","box is not defined");
                return false;
            }

            Point left = new Point((int)resource[0],(int)resource[3]/2);
            Point right = new Point((int)resource[2],(int)resource[3]/2);
            Point top = new Point((int)resource[2]/2,(int)resource[1]);
            Point bottom = new Point((int)resource[2]/2,(int)resource[3]);

            Point[] points = new Point[]{left,right,top,bottom};

            boxDetectStorage.put(boxId1,points);

            return  box[0] <= points[0].x && points[0].x <= box[2] && box[1] <= points[0].y && points[0].y <= box[3]||
                    box[0] <= points[1].x && points[1].x <= box[2] && box[1] <= points[1].y && points[1].y <= box[3]||
                    box[0] <= points[2].x && points[2].x <= box[2] && box[1] <= points[2].y && points[2].y <= box[3]||
                    box[0] <= points[3].x && points[3].x <= box[2] && box[1] <= points[3].y && points[3].y <= box[3];
        }

    }//PtPDetect 사용을 권고함

    public boolean boxDetect(String pointId, String boxId, int asembleX, int asembleY) {
        float[] point = (float[])this.dotStorage.get(pointId);
        float[] box = (float[])this.boxStorage.get(boxId);
        return point != null && box != null && box[0] <= point[0] + (float)asembleX && point[0] + (float)asembleX <= box[2] && box[1] <= point[1] + (float)asembleY && point[1] + (float)asembleY <= box[3];
    }

    public boolean boxDetect(float x, float y, String boxId) {
        float[] box = (float[])this.boxStorage.get(boxId);
        return box != null && box[0] <= x && x <= box[2] && box[1] <= y && y <= box[3];
    }

    public float[] getPoint(String pointId) {
        return (float[])this.dotStorage.get(pointId);
    }

    public float[] getBox(String boxId) {
        return (float[])this.boxStorage.get(boxId);
    }

    public Area getBox(String boxId, int notUse) {
        float[] box = (float[])this.boxStorage.get(boxId);
        return new Area(box[0], box[1], box[2], box[3]);
    }

    public boolean colorDetect(Bitmap bitmap, float x, float y, int color) {
        int pixelColor = bitmap.getPixel((int)x, (int)y);
        return color == pixelColor;
    }

    public boolean colorDetect(Bitmap bitmap, float x, float y) {
        int pixelColor = bitmap.getPixel((int)x, (int)y);
        return this.nullColor == pixelColor;
    }

    public boolean colorDetect(Bitmap bitmap, String pointId, int color) {
        float[] point = (float[])this.dotStorage.get(pointId);
        int pixelColor = bitmap.getPixel((int)point[0], (int)point[1]);
        return color == pixelColor;
    }

    public boolean colorDetect(Bitmap bitmap, String pointId) {
        float[] point = (float[])this.dotStorage.get(pointId);
        int pixelColor = bitmap.getPixel((int)point[0], (int)point[1]);
        return this.nullColor == pixelColor;
    }

    public boolean colorDetect(Bitmap bitmap, String pointId, int asembleX, int asembleY) {
        float[] point = (float[])this.dotStorage.get(pointId);
        int pixelColor = bitmap.getPixel((int)point[0] + asembleX, (int)point[1] + asembleY);
        return this.nullColor == pixelColor;
    }

    public boolean colorDetect(float x, float y, int color) {
        if(this.detectSpace == null) {
            return false;
        } else {
            int pixelColor = this.detectSpace.getPixel((int)x, (int)y);
            return color == pixelColor;
        }
    }

    public boolean colorDetect(float x, float y) {
        if(this.detectSpace == null) {
            return false;
        } else {
            int pixelColor = this.detectSpace.getPixel((int)x, (int)y);
            return this.nullColor == pixelColor;
        }
    }

    public boolean colorDetect(String pointId, int color) {
        if(this.detectSpace == null) {
            return false;
        } else {
            float[] point = (float[])this.dotStorage.get(pointId);
            int pixelColor = this.detectSpace.getPixel((int)point[0], (int)point[1]);
            return color == pixelColor;
        }
    }

    public boolean colorDetect(String pointId) {
        if(this.detectSpace == null) {
            return false;
        } else {
            float[] point = (float[])this.dotStorage.get(pointId);
            int pixelColor = this.detectSpace.getPixel((int)point[0], (int)point[1]);
            return this.nullColor == pixelColor;
        }
    }

    public boolean colorDetect(String pointId, int asembleX, int asembleY) {
        if(this.detectSpace == null) {
            return false;
        } else {
            float[] point = (float[])this.dotStorage.get(pointId);
            int pixelColor = this.detectSpace.getPixel((int)point[0] + asembleX, (int)point[1] + asembleY);
            return this.nullColor == pixelColor;
        }
    }
}