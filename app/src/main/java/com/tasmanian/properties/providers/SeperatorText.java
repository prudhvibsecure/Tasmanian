package com.tasmanian.properties.providers;

/**
 * Created by w7u on 12/26/2016.
 */


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Simple version used in the article:
 * https://medium.com/@ali.muzaffar/creating-a-custom-textview-as-a-section-header-73cb8e194917
 */
public class SeperatorText extends TextView {


    public SeperatorText(Context context) {
        super(context);
    }

    public SeperatorText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SeperatorText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SeperatorText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // we use the default paint,
        // our line will be the same color as the text.
        Paint paint = getPaint();
        //start at the vertical center of the textview
        int top = (getHeight() + getPaddingTop() - getPaddingBottom()) / 2;
        //start at the left margin
        int left = getPaddingLeft();
        //we draw all the way to the right
        int right = getWidth() - getPaddingRight();
        //we want the line to be 2 pixel thick
        int bottom = top + 2;

        int horizontalCenter = (getWidth() + getPaddingLeft() - getPaddingRight()) / 2;

        int textWidth = (int) paint.measureText(getText().toString());
        int halfTextWidth = textWidth / 2;

        int padding = 16;

        int gravity = getGravity();

        if ((gravity & Gravity.LEFT) == Gravity.LEFT) {
            canvas.drawRect(left + textWidth + padding, //left
                    top,
                    right,
                    bottom,
                    paint);
        } else if ((gravity & Gravity.RIGHT) == Gravity.RIGHT) {
            canvas.drawRect(left,
                    top,
                    right - textWidth - padding, //right
                    bottom,
                    paint);
        } else {
            canvas.drawRect(left,
                    top,
                    horizontalCenter - halfTextWidth - padding, //right
                    bottom,
                    paint);
            canvas.drawRect(horizontalCenter + halfTextWidth + padding, //left
                    top,
                    right,
                    bottom,
                    paint);
        }
    }
}