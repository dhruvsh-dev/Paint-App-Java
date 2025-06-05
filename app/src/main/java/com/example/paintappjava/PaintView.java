package com.example.paintappjava;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class PaintView extends View {

    private Paint currentPaint;
    private Path currentPath;
    private ArrayList<Paint> paints;
    private ArrayList<Path> paths;
    private Bitmap bitmap;

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        currentPaint = new Paint();
        currentPaint.setColor(Color.BLACK);
        currentPaint.setStyle(Paint.Style.STROKE);
        currentPaint.setStrokeWidth(10f);
        currentPaint.setAntiAlias(true);
        currentPaint.setStrokeCap(Paint.Cap.ROUND);

        currentPath = new Path();
        paths = new ArrayList<>();
        paints = new ArrayList<>();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < paths.size(); i++) {
            canvas.drawPath(paths.get(i), paints.get(i));
        }

        // Draw the current path
        canvas.drawPath(currentPath, currentPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentPath.moveTo(x, y);
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                currentPath.lineTo(x, y);
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                paths.add(currentPath);
                paints.add(new Paint(currentPaint));
                currentPath = new Path();
                invalidate();
                return true;
            default:
                return false;
        }
    }

    public void setColor(int color) {
        currentPaint.setColor(color);
    }

    public void setStrokeWidth(float width) {
        currentPaint.setStrokeWidth(width);
    }

    public void clearCanvas() {
        paths.clear();
        paints.clear();
        currentPath.reset();
        invalidate();
    }

    public Bitmap getBitmap() {
        // Create a bitmap with view size and draw content into it
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        draw(canvas);
        return bitmap;
    }
}
