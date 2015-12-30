package com.almaghrib.mobile.util.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.widget.Button;

import com.almaghrib.mobile.R;

public class AVLoadingIndicatorButton extends Button {


    //indicators
    public static final int BallPulse = 0;


    @IntDef(flag = true,
            value = {
                    BallPulse
            })
    public @interface Indicator {
    }

    //Sizes (with defaults in DP)
    public static final int DEFAULT_SIZE = 45;

    //attrs
    int mIndicatorId;
    int mIndicatorColor;
    boolean mShowIndicator;

    Paint mPaint;

    BaseIndicatorController mIndicatorController;

    private boolean mHasAnimation;


    public AVLoadingIndicatorButton(Context context) {
        super(context);
        init(null, 0);
    }

    public AVLoadingIndicatorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public AVLoadingIndicatorButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AVLoadingIndicatorButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AVLoadingIndicatorButton);
        mIndicatorId = a.getInt(R.styleable.AVLoadingIndicatorButton_indicator, BallPulse);
        mIndicatorColor = a.getColor(R.styleable.AVLoadingIndicatorButton_indicator_color, Color.WHITE);
        mShowIndicator = a.getBoolean(R.styleable.AVLoadingIndicatorButton_show_indicator, false);
        a.recycle();

        mPaint = new Paint();
        mPaint.setColor(mIndicatorColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        if (mShowIndicator) {
            applyIndicator();
        }
    }

    @Override
    public void setVisibility(int v) {
        if (getVisibility() != v) {
            super.setVisibility(v);
            if (v == GONE || v == INVISIBLE) {
                mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.END);
            } else {
                mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.START);
            }
        }
    }

    public void setShowIndicator(boolean showIndicator) {
        if (mShowIndicator != showIndicator) {
            mShowIndicator = showIndicator;
            if (showIndicator) {
                mHasAnimation = false;
                applyIndicator();
                applyAnimation();
                invalidate();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mShowIndicator) {
            drawIndicator(canvas);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        applyAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mShowIndicator) {
            mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.CANCEL);
        }
    }

    private void applyIndicator() {
        switch (mIndicatorId) {
            case BallPulse:
                mIndicatorController = new BallPulseIndicator();
                break;
        }
        mIndicatorController.setTarget(this);
    }

    private void drawIndicator(Canvas canvas) {
        if (mShowIndicator) {
            mIndicatorController.draw(canvas, mPaint);
        }
    }

    private void applyAnimation() {
        if (!mHasAnimation && mShowIndicator) {
            mHasAnimation = true;
            mIndicatorController.initAnimation();
        }
    }

}