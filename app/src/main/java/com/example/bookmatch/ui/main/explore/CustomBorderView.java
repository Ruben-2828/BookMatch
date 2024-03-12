package com.example.bookmatch.ui.main.explore;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.bookmatch.R;


public class CustomBorderView extends View {
    private Paint paint;
    private RectF rect;
    private float sweepAngle;
    private final int strokeThickness = 30;

    public CustomBorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.custom_border_view,
                0, 0);

        try {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getContext().getTheme();
            theme.resolveAttribute(com.google.android.material.R.attr.backgroundColor, typedValue, true);
            int defaultColor = typedValue.data;
            int strokeColor = a.getColor(R.styleable.custom_border_view_strokeColor, defaultColor);
            init(strokeColor);
        } finally {
            a.recycle();
        }
    }

    private void init(int strokeColor) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeThickness); // Border width
        paint.setColor(strokeColor); // Use custom color
        rect = new RectF();
        sweepAngle = 0;
    }

    public void updateBorderProgress(float progress) {
        sweepAngle = 360 * progress; // Full circle for 1.0 progress
        invalidate(); // Trigger a redraw
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        int size = Math.min(getWidth(), getHeight());

        rect.set(strokeThickness, strokeThickness, size - strokeThickness, size - strokeThickness);

        float cornerRadius = 75; // This is the corner radius of the rounded square

        // Create a rounded rectangle path
        Path path = new Path();
        path.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CW);

        // Measure the path and calculate the segment to draw
        PathMeasure pathMeasure = new PathMeasure(path, false);
        float length = pathMeasure.getLength();
        float start = length * 5 / 16; // Start at the top center (quarter way through the path)
        float end =  start + (length * (sweepAngle / 360f));
        Path destination = new Path();

        // Extract the segment from the path
        if (sweepAngle <= 90) {
            // If the angle is small, use a simple segment
            pathMeasure.getSegment(start, end, destination, true);
        } else {
            // For larger angles, use a loop to handle wrapping around the path
            while (end > length) {
                pathMeasure.getSegment(start, length, destination, true);
                end -= length;
                start = 0;
            }
            pathMeasure.getSegment(start, end, destination, true);
        }

        // Draw the path segment
        canvas.drawPath(destination, paint);
    }
}
