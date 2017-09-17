package com.carlos.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by carlos on 14/07/2017.
 */
public class SampleTwoViewGroup extends SampleOneViewGroup {

    private static final float ANNULUS_WIDTH = 2;
    private Paint mBigPaint;
    private RectF mArcRectF;

    public SampleTwoViewGroup(Context context) {
        super(context);
    }

    public SampleTwoViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SampleTwoViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, @Nullable AttributeSet attrs) {
        super.init(context, attrs);
        // big paint.
        mBigPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBigPaint.setStrokeWidth(dip2px(ANNULUS_WIDTH));
        mBigPaint.setColor(getResources().getColor(R.color.primaryBlue));
        mBigPaint.setStyle(Paint.Style.STROKE);
        mArcRectF = new RectF();
    }

    @Override
    protected void drawBigIndicator(Canvas canvas, RectF rectF, @Nullable Drawable bigDrawable) {
        float diameter = Math.min(rectF.width(), rectF.height());
        float centerX = rectF.centerX();
        float centerY = rectF.centerY();
        mArcRectF.set(centerX - diameter / 2, centerY - diameter / 2, centerX + diameter / 2, centerY + diameter / 2);
        canvas.drawArc(mArcRectF, 0, 360, false, mBigPaint);
    }

}
