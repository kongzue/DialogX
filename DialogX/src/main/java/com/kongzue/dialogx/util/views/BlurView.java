package com.kongzue.dialogx.util.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.renderscript.Allocation;
import androidx.renderscript.Element;
import androidx.renderscript.RenderScript;
import androidx.renderscript.ScriptIntrinsicBlur;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.BaseDialog;

public class BlurView extends View {
    private float mDownsampleFactor = 4;
    private int mOverlayColor = Color.WHITE;
    private float mBlurRadius = 35;
    
    private float mRadius = 0;
    private Path mBoundPath = null;
    
    private boolean mDirty;
    private Bitmap mBitmapToBlur, mBlurredBitmap;
    private Canvas mBlurringCanvas;
    private RenderScript mRenderScript;
    private ScriptIntrinsicBlur mBlurScript;
    private Allocation mBlurInput, mBlurOutput;
    private boolean mIsRendering;
    private final Rect mRectSrc = new Rect(), mRectDst = new Rect();
    // mDecorView should be the root view of the activity (even if you are on a different window like a dialog)
    private View mDecorView;
    // If the view is on different root view (usually means we are on a PopupWindow),
    // we need to manually call invalidate() in onPreDraw(), otherwise we will not be able to see the changes
    private boolean mDifferentRoot;
    private static int RENDERING_COUNT;
    
    private Paint mPaint;
    private RectF mRectF;
    
    private Bitmap mRoundBitmap;
    private Canvas mTmpCanvas;
    
    public BlurView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        init(context, attrs);
    }
    
    public BlurView(Context context) {
        super(context);
        
        init(context, null);
    }
    
    public BlurView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        init(context, attrs);
    }
    
    public BlurView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        
        init(context, attrs);
    }
    
    private boolean isInit = false;
    
    private void init(Context context, AttributeSet attrs) {
        if (!isInit) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RealtimeBlurView);
            mBlurRadius = a.getDimension(
                    R.styleable.RealtimeBlurView_realtimeBlurRadius,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, context.getResources().getDisplayMetrics())
            );
            mDownsampleFactor = a.getFloat(R.styleable.RealtimeBlurView_realtimeDownsampleFactor, 4);
            mOverlayColor = a.getColor(R.styleable.RealtimeBlurView_realtimeOverlayColor, 0x00ffffff);
            
            //ready rounded corner
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mRectF = new RectF();
            
            mRadius = a.getDimension(R.styleable.RealtimeBlurView_radius, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, context.getResources().getDisplayMetrics()));
            a.recycle();
            
            isInit = true;
        }
    }
    
    public void setBlurRadius(float radius) {
        if (mBlurRadius != radius) {
            mBlurRadius = radius;
            mDirty = true;
            invalidate();
        }
    }
    
    public void setRadiusPx(float r) {
        if (mRadius != r) {
            mRadius = r;
            mDirty = true;
            invalidate();
        }
    }
    
    public void setDownsampleFactor(float factor) {
        if (factor <= 0) {
            throw new IllegalArgumentException("Downsample factor must be greater than 0.");
        }
        
        if (mDownsampleFactor != factor) {
            mDownsampleFactor = factor;
            mDirty = true; // may also change blur radius
            releaseBitmap();
            invalidate();
        }
    }
    
    public void setOverlayColor(int color) {
        if (mOverlayColor != color) {
            mOverlayColor = color;
            invalidate();
        }
    }
    
    private void releaseBitmap() {
        if (mBlurInput != null) {
            mBlurInput.destroy();
            mBlurInput = null;
        }
        if (mBlurOutput != null) {
            mBlurOutput.destroy();
            mBlurOutput = null;
        }
        if (mBitmapToBlur != null) {
            mBitmapToBlur.recycle();
            mBitmapToBlur = null;
        }
        if (mBlurredBitmap != null) {
            mBlurredBitmap.recycle();
            mBlurredBitmap = null;
        }
    }
    
    private void releaseScript() {
        if (mRenderScript != null) {
            mRenderScript.destroy();
            mRenderScript = null;
        }
        if (mBlurScript != null) {
            mBlurScript.destroy();
            mBlurScript = null;
        }
    }
    
    protected void release() {
        log("release");
        releaseBitmap();
        releaseScript();
    }
    
    protected boolean prepare() {
        if (mBlurRadius == 0) {
            release();
            return false;
        }
        
        float downsampleFactor = mDownsampleFactor;
        
        if (mDirty || mRenderScript == null) {
            if (supportRenderScript && useBlur) {
                if (mRenderScript == null) {
                    try {
                        mRenderScript = RenderScript.create(getContext());
                        mBlurScript = ScriptIntrinsicBlur.create(mRenderScript, Element.U8_4(mRenderScript));
                        
                    } catch (Exception e) {
                        if (isDebug()) {
                            e.printStackTrace();
                        }
                    }
                }
                
                mDirty = false;
                float radius = mBlurRadius / downsampleFactor;
                if (radius > 25) {
                    downsampleFactor = downsampleFactor * radius / 25;
                    radius = 25;
                }
                if (mBlurScript != null) mBlurScript.setRadius(radius);
            }
        }
        
        final int width = getWidth();
        final int height = getHeight();
        
        int scaledWidth = Math.max(1, (int) (width / downsampleFactor));
        int scaledHeight = Math.max(1, (int) (height / downsampleFactor));
        
        if (mBlurringCanvas == null || mBlurredBitmap == null || mBlurredBitmap.getWidth() != scaledWidth || mBlurredBitmap.getHeight() != scaledHeight) {
            releaseBitmap();
            
            boolean r = false;
            try {
                mBitmapToBlur = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
                if (mBitmapToBlur == null) {
                    return false;
                }
                mBlurringCanvas = new Canvas(mBitmapToBlur);
                
                if (!supportRenderScript || !useBlur) {
                    return true;
                }
                
                mBlurInput = Allocation.createFromBitmap(mRenderScript, mBitmapToBlur,
                        Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT
                );
                mBlurOutput = Allocation.createTyped(mRenderScript, mBlurInput.getType());
                
                mBlurredBitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
                if (mBlurredBitmap == null) {
                    return false;
                }
                
                r = true;
            } catch (Exception e) {
                if (isDebug()) e.printStackTrace();
            } finally {
                if (!r) {
                    releaseBitmap();
                    return false;
                }
            }
        }
        return true;
    }
    
    protected void blur(Bitmap bitmapToBlur, Bitmap blurredBitmap) {
        mBlurInput.copyFrom(bitmapToBlur);
        mBlurScript.setInput(mBlurInput);
        mBlurScript.forEach(mBlurOutput);
        mBlurOutput.copyTo(blurredBitmap);
    }
    
    private final ViewTreeObserver.OnPreDrawListener preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            final int[] locations = new int[2];
            Bitmap oldBmp = mBlurredBitmap;
            View decor = mDecorView;
            if (decor != null && isShown() && prepare()) {
                boolean redrawBitmap = mBlurredBitmap != oldBmp;
                oldBmp = null;
                decor.getLocationOnScreen(locations);
                int x = -locations[0];
                int y = -locations[1];
                
                getLocationOnScreen(locations);
                x += locations[0];
                y += locations[1];
                
                // just erase transparent
                mBitmapToBlur.eraseColor(mOverlayColor & 0xffffff);
                
                int rc = mBlurringCanvas.save();
                mIsRendering = true;
                RENDERING_COUNT++;
                try {
                    mBlurringCanvas.scale(1.f * mBitmapToBlur.getWidth() / getWidth(), 1.f * mBitmapToBlur.getHeight() / getHeight());
                    mBlurringCanvas.translate(-x, -y);
                    if (decor.getBackground() != null) {
                        decor.getBackground().draw(mBlurringCanvas);
                    }
                    decor.draw(mBlurringCanvas);
                } catch (Exception e) {
                    if (isDebug()) e.printStackTrace();
                } finally {
                    mIsRendering = false;
                    RENDERING_COUNT--;
                    mBlurringCanvas.restoreToCount(rc);
                }
                
                blur(mBitmapToBlur, mBlurredBitmap);
                
                if (redrawBitmap || mDifferentRoot) {
                    invalidate();
                }
            }
            
            return true;
        }
    };
    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (BaseDialog.getRootFrameLayout() != null && BaseDialog.getRootFrameLayout().getChildCount() >= 1) {
            mDecorView = BaseDialog.getRootFrameLayout().getChildAt(0);
        }
        if (mDecorView != null) {
            log("mDecorView is ok.");
            mDecorView.getViewTreeObserver().addOnPreDrawListener(preDrawListener);
            mDifferentRoot = mDecorView.getRootView() != getRootView();
            if (mDifferentRoot) {
                mDecorView.postInvalidate();
            }
        } else {
            log("mDecorView is NULL.");
            mDifferentRoot = false;
        }
    }
    
    @Override
    protected void onDetachedFromWindow() {
        if (mDecorView != null) {
            mDecorView.getViewTreeObserver().removeOnPreDrawListener(preDrawListener);
        }
        release();
        super.onDetachedFromWindow();
    }
    
    @Override
    public void draw(Canvas canvas) {
        if (!useBlur) {
            Paint cutPaint = new Paint();
            cutPaint.setAntiAlias(true);
            cutPaint.setColor(mOverlayColor);
            mRectF.right = getWidth();
            mRectF.bottom = getHeight();
            canvas.drawRoundRect(mRectF, mRadius, mRadius, cutPaint);
        } else {
            if (mIsRendering) {
                // Quit here, don't draw views above me
                //throw STOP_EXCEPTION;
            } else if (RENDERING_COUNT > 0) {
                // Doesn't support blurview overlap on another blurview
            } else {
                if (mRadius != 0) {
                    Rect rect = new Rect();
                    getLocalVisibleRect(rect);
                    rect.right = rect.left + getWidth();
                    rect.bottom = rect.top + getHeight();
                    mBoundPath = caculateRoundRectPath(rect);
                    canvas.clipPath(mBoundPath);
                }
                super.draw(canvas);
            }
        }
    }
    
    private Path caculateRoundRectPath(Rect r) {
        Path path = new Path();
        float radius = mRadius;
        float elevation = 0;
        path.addRoundRect(new RectF(r.left + elevation, r.top + elevation, r.right - elevation, r.bottom - elevation), radius, radius, Path.Direction.CW);
        return path;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBlurredBitmap(canvas, mBlurredBitmap, mOverlayColor);
    }
    
    /**
     * Custom draw the blurred bitmap and color to define your own shape
     *
     * @param canvas
     * @param blurredBitmap
     * @param overlayColor
     */
    protected void drawBlurredBitmap(Canvas canvas, Bitmap blurredBitmap, int overlayColor) {
        if (blurredBitmap != null) {
            mRectSrc.right = blurredBitmap.getWidth();
            mRectSrc.bottom = blurredBitmap.getHeight();
            mRectDst.right = getWidth();
            mRectDst.bottom = getHeight();
            canvas.drawBitmap(blurredBitmap, mRectSrc, mRectDst, null);
        }
        canvas.drawColor((supportRenderScript && useBlur) ? overlayColor : removeAlphaColor(overlayColor));
    }
    
    private static boolean supportRenderScript = false;
    private boolean useBlur = true;
    
    public boolean isUseBlur() {
        return useBlur;
    }
    
    public BlurView setUseBlur(boolean useBlur) {
        this.useBlur = useBlur;
        invalidate();
        return this;
    }
    
    private static int removeAlphaColor(@ColorInt int color) {
        int alpha = 255;
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }
    
    static {
        /**
         * 之所以需要启动一个新线程检测RenderScript是否可用的原因是不清楚Android什么时候对loadClass做了变更，
         * 会直接抛出NoClassDefFoundError无法拦截，在主线程检测会导致程序直接闪退，因此改为异步检测。
         * 检测后会给定(boolean)supportRenderScript用于判断时光支持
         */
        new Thread() {
            @Override
            public void run() {
                try {
                    BlurView.class.getClassLoader().loadClass(RenderScript.class.getCanonicalName());
                    supportRenderScript = true;
                } catch (Throwable e) {
                    if (isDebug()) {
                        e.printStackTrace();
                    }
                    supportRenderScript = false;
                }
            }
        }.start();
    }
    
    public static boolean DEBUGMODE = false;
    
    static boolean isDebug() {
        return DEBUGMODE && DialogX.DEBUGMODE;
    }
    
    public static void log(Object o) {
        if (isDebug()) Log.i(">>>", o.toString());
    }
    
    public static void error(Object o) {
        if (isDebug()) Log.e(">>>", o.toString());
    }
}