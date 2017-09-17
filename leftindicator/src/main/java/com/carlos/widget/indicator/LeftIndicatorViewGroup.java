package com.carlos.widget.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by carlos on 03/08/2017.
 */

public class LeftIndicatorViewGroup extends LinearLayout {

    private static final float HALF_BIG_HEIGHT = 5;
    private static final float HALF_SMALL_HEIGHT = 2;
    private static final float BIG_AND_SMALL_OFFSET = 2;
    private static final float SMALL_DISTANCE = 2.5f;

    private List<PointF> mBigIndicatorPoints;
    private List<PointF> mSmallIndicatorPoints;

    private float mLeftAreaWidth;
    private float mHalfBigHeight;
    private float mHalfSmallHeight;
    private float mSmallDistance;
    private float mBigAndSmallOffset;

    private Drawable mSmallDrawable;
    private Drawable mBigDrawable;
    private RectF mIndicatorRectF;

    private boolean mFirstMeasure = true;

    public LeftIndicatorViewGroup(Context context) {
        super(context);
        init(context, null);
    }

    public LeftIndicatorViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LeftIndicatorViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    protected void init(Context context, @Nullable AttributeSet attrs) {
        setOrientation(VERTICAL);
        setWillNotDraw(false);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LeftIndicatorViewGroup);
        mLeftAreaWidth = a.getDimension(R.styleable.LeftIndicatorViewGroup_left_area_width, 0);
        mSmallDistance = a.getDimension(R.styleable.LeftIndicatorViewGroup_small_distance, dip2px(SMALL_DISTANCE));
        mBigAndSmallOffset = a.getDimension(R.styleable.LeftIndicatorViewGroup_big_and_small_offset, dip2px(BIG_AND_SMALL_OFFSET));

        mBigDrawable = a.getDrawable(R.styleable.LeftIndicatorViewGroup_big_src);
        if (mBigDrawable == null) {
            mHalfBigHeight = a.getDimension(R.styleable.LeftIndicatorViewGroup_half_big_height, dip2px(HALF_BIG_HEIGHT));
        } else {
            mHalfBigHeight = mBigDrawable.getIntrinsicHeight() / 2;
        }

        mSmallDrawable = a.getDrawable(R.styleable.LeftIndicatorViewGroup_small_src);
        if (mSmallDrawable == null) {
            mHalfSmallHeight = a.getDimension(R.styleable.LeftIndicatorViewGroup_half_small_height, dip2px(HALF_SMALL_HEIGHT));
        } else {
            mHalfSmallHeight = mSmallDrawable.getIntrinsicHeight() / 2;
        }
        // recycle.
        a.recycle();
        mBigIndicatorPoints = new ArrayList<>();
        mSmallIndicatorPoints = new ArrayList<>();
        mIndicatorRectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mFirstMeasure) {
            mFirstMeasure = false;
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                if (view == null) {
                    continue;
                }
                ViewGroup.LayoutParams params = view.getLayoutParams();
                if (params instanceof MarginLayoutParams) {
                    ((MarginLayoutParams) params).leftMargin += mLeftAreaWidth;
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (PointF pointF : mBigIndicatorPoints) {
            mIndicatorRectF.set(pointF.x - mLeftAreaWidth / 2, pointF.y - mHalfBigHeight,
                    pointF.x + mLeftAreaWidth / 2, pointF.y + mHalfBigHeight);
            drawBigIndicator(canvas, mIndicatorRectF, mBigDrawable);
        }

        for (PointF pointF : mSmallIndicatorPoints) {
            mIndicatorRectF.set(pointF.x - mLeftAreaWidth / 2, pointF.y - mHalfSmallHeight,
                    pointF.x + mLeftAreaWidth / 2, pointF.y + mHalfSmallHeight);
            drawSmallIndicator(canvas, mIndicatorRectF, mSmallDrawable);
        }

    }

    private float getIndicatorXPointF() {
        return mLeftAreaWidth / 2;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // clear all points info.
        mBigIndicatorPoints.clear();
        mSmallIndicatorPoints.clear();
        int childCount = getChildCount();
        float height = 0;
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view == null || view.getVisibility() == GONE) {
                continue;
            }
            float childHeight = view.getMeasuredHeight();
            height += childHeight;
            // cal big points info.
            if (view instanceof ViewGroup) {
                mBigIndicatorPoints.add(new PointF(getIndicatorXPointF(), height - childHeight / 2));
            }
        }

        int size = mBigIndicatorPoints.size();
        if (size == 0) {
            return;
        }

        float bigStart = mBigIndicatorPoints.get(0).y;
        for (int i = 1; i < size; i++) {
            float current = mBigIndicatorPoints.get(i).y;
            // generate small points.
            generateSmallPoints(bigStart, current);
            bigStart = current;
        }
    }

    /**
     * genearate samll points.
     *
     * @param bigStart prev big circle position.
     * @param current  current big circle position.
     */
    private void generateSmallPoints(float bigStart, float current) {
        // location start.
        float smallStart = bigStart + mHalfBigHeight;
        // location end.
        float smallEnd = current - mHalfBigHeight;
        float smallHeight = mHalfSmallHeight * 2 + mSmallDistance * 2;
        // if has space.
        if (smallEnd - smallStart < smallHeight) {
            return;
        }
        // half of top points.
        float halfTop = (bigStart + current) / 2 - mHalfSmallHeight - mSmallDistance;
        while (halfTop - smallStart >= smallHeight) {
            mSmallIndicatorPoints.add(new PointF(getIndicatorXPointF(), halfTop - smallHeight / 2));
            halfTop -= smallHeight;
        }
        // does need extra.
        if (halfTop - smallStart >= smallHeight - mSmallDistance + mBigAndSmallOffset) {
            mSmallIndicatorPoints.add(new PointF(getIndicatorXPointF(), halfTop - smallHeight / 2));
        }

        // center.
        mSmallIndicatorPoints.add(new PointF(getIndicatorXPointF(), (bigStart + current) / 2));
        // half bottom points.
        float halfBottom = (bigStart + current) / 2 + mHalfSmallHeight + mSmallDistance;
        while (halfBottom + smallHeight <= smallEnd) {
            mSmallIndicatorPoints.add(new PointF(getIndicatorXPointF(), halfBottom + smallHeight / 2));
            halfBottom += smallHeight;
        }
        // does need extra.
        if (halfBottom + smallHeight - mSmallDistance + mBigAndSmallOffset <= smallEnd) {
            mSmallIndicatorPoints.add(new PointF(getIndicatorXPointF(), halfBottom + smallHeight / 2));
        }
    }

    /**
     *
     *
     * @param canvas
     * @param smallIndicatorPoints
     * @param halfSmallHeight
     * @param smallDrawable
     */
    /**
     * draw small indicator.
     *
     * @param canvas        canvas.
     * @param rectF         points.
     * @param smallDrawable drawable.
     */
    protected void drawSmallIndicator(Canvas canvas, RectF rectF, @Nullable Drawable smallDrawable) {
        if (smallDrawable != null) {
            int drawableWidth = smallDrawable.getIntrinsicWidth();
            int drawableHeight = smallDrawable.getIntrinsicHeight();
            float centerX = rectF.centerX();
            float centerY = rectF.centerY();
            smallDrawable.setBounds((int) (centerX - drawableWidth / 2), (int) (centerY - drawableHeight / 2),
                    (int) (centerX + drawableWidth / 2), (int) (centerY + drawableHeight / 2));
            smallDrawable.draw(canvas);
        }
    }

    /**
     * draw big indicator.
     *
     * @param canvas      canvas.
     * @param rectF       points.
     * @param bigDrawable drawable.
     */
    protected void drawBigIndicator(Canvas canvas, RectF rectF, @Nullable Drawable bigDrawable) {
        if (bigDrawable != null) {
            int drawableWidth = bigDrawable.getIntrinsicWidth();
            int drawableHeight = bigDrawable.getIntrinsicHeight();
            float centerX = rectF.centerX();
            float centerY = rectF.centerY();
            bigDrawable.setBounds((int) (centerX - drawableWidth / 2), (int) (centerY - drawableHeight / 2),
                    (int) (centerX + drawableWidth / 2), (int) (centerY + drawableHeight / 2));
            bigDrawable.draw(canvas);
        }
    }
}
