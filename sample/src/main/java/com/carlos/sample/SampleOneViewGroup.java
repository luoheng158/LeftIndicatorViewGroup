package com.carlos.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.carlos.widget.indicator.LeftIndicatorViewGroup;

/**
 * Created by carlos on 03/08/2017.
 */

public class SampleOneViewGroup extends LeftIndicatorViewGroup {

    private Paint mSmallPaint;

    public SampleOneViewGroup(Context context) {
        super(context);
    }

    public SampleOneViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SampleOneViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, @Nullable AttributeSet attrs) {
        super.init(context, attrs);
        mSmallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSmallPaint.setColor(getResources().getColor(R.color.whiteGray));
        mSmallPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void drawSmallIndicator(Canvas canvas, RectF rectF, @Nullable Drawable smallDrawable) {
        float diameter = Math.min(rectF.width(), rectF.height());
        canvas.drawCircle(rectF.centerX(), rectF.centerY(), diameter / 2, mSmallPaint);
    }

}
