package com.kongzue.dialogx.util.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class RoundView extends RelativeLayout {
    
    private float mRadius = 0;
    private Path mBoundPath = null;
    
    public RoundView(Context context) {
        this(context, null);
    }
    
    public RoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public RoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        mRadius = 50;
    }
    
    public void setRadius(float radius) {
        if (mRadius == radius)
            return;
        this.mRadius = radius;
        postInvalidate();
    }
    
    public float getRadius() {
        return mRadius;
    }
    
    public void draw(Canvas canvas) {
        Rect rect = new Rect();
        getLocalVisibleRect(rect);
        mBoundPath = caculateRoundRectPath(rect);
        canvas.clipPath(mBoundPath);
        super.draw(canvas);
    }
    
    private Path caculateRoundRectPath(Rect r) {
        Path path = new Path();
        float radius = getRadius();
        float elevation = 0;
        path.addRoundRect(new RectF(r.left + elevation, r.top + elevation, r.right - elevation, r.bottom - elevation), radius, radius, Path.Direction.CW);
        return path;
    }
}