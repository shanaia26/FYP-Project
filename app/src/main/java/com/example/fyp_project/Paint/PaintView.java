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
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.example.fyp_project.Common.Common;
import com.example.fyp_project.R;

import java.util.ArrayList;
import java.util.List;

public class PaintView extends View {

    public static int BRUSH_SIZE = 10;
    private int strokeWidth;

    private Bitmap bitmap;
    private Bitmap bitmapSrc;
    private Canvas canvas;
    private Path path;
    private Paint bitmapPaint;
    private Paint paint;

    private boolean emboss;
    private boolean blur;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
    private List<FingerPath> paths = new ArrayList<>();

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);

        path = new Path();
        bitmapPaint = new Paint(Paint.DITHER_FLAG);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Common.COLOUR_SELECTED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(BRUSH_SIZE);
        paint.setAlpha(0xff); // 0xff=255 in decimal

        mEmboss = new EmbossMaskFilter(new float[]{1, 1, 1}, 0.4f, 6, 3.5f);
        mBlur = new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL);

        strokeWidth = BRUSH_SIZE;

    }

    //Sets the stroke width
    public void setStrokeWidth(int width) {
        strokeWidth = width;
    }

    //Sets the colour as the colour user chose
    public void setPathColor(int colour) {
        paint.setColor(colour);
    }

    // this methods returns the current bitmap
    public Bitmap save() {
        return bitmap;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        Bitmap bitmapSrc = null;
        if (Common.IMAGE_FROM_GALLERY != null) {
            bitmapSrc = Common.IMAGE_FROM_GALLERY;
        } else {
            bitmapSrc = BitmapFactory.decodeResource(getResources(), R.drawable.design_background)
                    .copy(Bitmap.Config.ARGB_8888, true);
        }
        bitmap = Bitmap.createScaledBitmap(bitmapSrc, w, h, false);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        for (FingerPath fp : paths) {
            paint.setMaskFilter(null);
            paint.setStrokeWidth(fp.strokeWidth);
            if (fp.emboss) {
                paint.setMaskFilter(mEmboss);
                paint.setStrokeWidth(fp.strokeWidth);
            } else if (fp.blur) {
                paint.setMaskFilter(mBlur);
                paint.setStrokeWidth(fp.strokeWidth);
            }
            canvas.drawPath(fp.path, paint);
        }

        canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
        canvas.restore();

    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touchStart(float x, float y) {
        path = new Path();
        FingerPath fp = new FingerPath(Common.COLOUR_SELECTED, emboss, blur, strokeWidth, path);
        paths.add(fp);

        path.reset();
        path.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        path.lineTo(mX, mY);
        // commit the path to our offscreen
        canvas.drawPath(path, paint);
        // kill this so we don't double draw
        path.reset();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }
        return true;
    }

    public void normal() {
        emboss = false;
        blur = false;
    }

    public void emboss() {
        emboss = true;
        blur = false;
    }

    public void blur() {
        emboss = false;
        blur = true;
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

}