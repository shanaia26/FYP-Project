package com.example.fyp_project.Paint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.example.fyp_project.Common.Common;
import com.example.fyp_project.R;

import java.util.ArrayList;

public class PaintViewFill extends View {

    public static int BRUSH_SIZE = 10;
    private int strokeWidth;

    private Bitmap bitmap;
    private Bitmap bitmapSrc;
    private Canvas canvas;
    private Path path;
    private Paint bitmapPaint;
    private Paint paint;

    private ArrayList<FingerPath> paths = new ArrayList<>();

    private float positionX;
    private float positionY;
    private float refX;
    private float refY;
    private ScaleGestureDetector scaleDetector;
    private float scaleFactor = 1.0f;
    private final static float minZoon = 1.0f;
    private final static float maxZoom = 5.0f;

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor += detector.getScaleFactor();
            scaleFactor = Math.max(scaleFactor, Math.min(scaleFactor, maxZoom));
            invalidate();
            return true;
        }
    }

    public PaintViewFill(Context context, AttributeSet attrs) {
        super(context, attrs);
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        path = new Path();
        bitmapPaint = new Paint(Paint.DITHER_FLAG);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Common.COLOUR_SELECTED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
//        if (bitmap == null) {
        bitmapSrc = BitmapFactory.decodeResource(getResources(), R.drawable.design_background)
                .copy(Bitmap.Config.ARGB_8888, true);
        bitmap = Bitmap.createScaledBitmap(bitmapSrc, w, h, false);
        canvas = new Canvas(bitmap);
//            for (int i = 0; i < bitmap.getWidth(); i++) {
//                for (int j = 0; j < bitmap.getHeight(); j++) {
//                    int alpha = 255 - brightness(bitmap.getPixel(i, j));
//                    if (alpha < 300) {
//                        bitmap.setPixel(i, j, Color.WHITE);
//                    } else {
//                        bitmap.setPixel(i, j, Color.BLACK);
//                    }
//                }
//            }
//        }
//            if (defaultBitmap == null) {
//                defaultBitmap = Bitmap.createBitmap(bitmap);
//            }
//        }
    }


    private int brightness(int colour) {
        return (colour >> 16) & 0xff;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        canvas.translate(positionX, positionY);
        canvas.scale(scaleFactor, scaleFactor);
        canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
        canvas.restore();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                refX = event.getX();
                refY = event.getY();
                paint((int)((refX - positionX)/scaleFactor), (int)((refY - positionY)/scaleFactor));
                //invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                float fX = event.getX();
                float fY = event.getY();

                positionX += fX - refX;
                positionY += fY - refY;
                refX = fX;
                refY = fY;
                invalidate();
                break;
        }
        return true;
    }

    private void paint(int x, int y) {
        int targetColour = bitmap.getPixel(x, y);
        if (targetColour != Color.BLACK) {
            FloodFill.floodFill(bitmap, new Point(x, y), targetColour, Common.COLOUR_SELECTED);
            invalidate();
        }
    }

    public void clear() {
        bitmapSrc = BitmapFactory.decodeResource(getResources(), R.drawable.design_background)
                .copy(Bitmap.Config.ARGB_8888, true);
        bitmap = Bitmap.createScaledBitmap(bitmapSrc, (int) (bitmap.getWidth() * 1), (int) (bitmap.getHeight() * 1), false);
        canvas = new Canvas(bitmap);
        invalidate();
        System.gc();

    }

    public void undo() {
        // check whether the List is empty or not
        // if empty, the remove method will return an error
        if (paths.size() != 0) {
            paths.remove(paths.size() - 1);
            invalidate();
        }
    }
//
//    public Bitmap getBitmap() {
//        this.setDrawingCacheEnabled(true);
//        this.buildDrawingCache();
//        Bitmap bmp = Bitmap.createBitmap(this.getDrawingCache());
//        this.setDrawingCacheEnabled(false);
//
//        return bmp;
//    }

    //Sets the colour as the colour user chose
    public void setPathColor(int colour) {
        paint.setColor(colour);
    }

}
