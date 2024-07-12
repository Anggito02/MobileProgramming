package com.example.platenumberrecognition;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OverlayView extends View {

    private static final int LABEL_PADDING = 8;

    private Paint textPaint;
    private Paint textBackgroundPaint;
    private Paint boxPaint;
    private Rect bounds = new Rect();

    private List<BoundingBox> boundingBoxList = new ArrayList<>();

    public OverlayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.textPaint = new Paint();
        this.textPaint.setTextSize(48f);
        this.textPaint.setStyle(Paint.Style.FILL);
        this.textPaint.setColor(Color.WHITE);

        this.textBackgroundPaint = new Paint();
        this.textBackgroundPaint.setTextSize(48f);
        this.textBackgroundPaint.setStyle(Paint.Style.FILL);
        this.textBackgroundPaint.setColor(Color.BLACK);

        this.boxPaint = new Paint();
        this.boxPaint.setStrokeWidth(4);
        this.boxPaint.setColor(Color.RED);
        this.boxPaint.setStyle(Paint.Style.STROKE);
    }

    public void setBoundingBox(List<BoundingBox> boundingBoxList) {
        synchronized (this) {
            this.boundingBoxList = boundingBoxList;
            this.invalidate();
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);

        for (BoundingBox box: this.boundingBoxList) {

            int x1 = (int) (box.getX1() * (float)this.getWidth());
            int x2 = (int) (box.getX2() * (float)this.getWidth());
            int y1 = (int) (box.getY1() * (float)this.getHeight());
            int y2 = (int) (box.getY2() * (float)this.getHeight());

            canvas.drawRect(x1, y1, x2, y2, this.boxPaint);
            String label = box.getLabel();

            textBackgroundPaint.getTextBounds(label, 0, label.length(), this.bounds);
            int textWidth = this.bounds.width();
            int textHeight = this.bounds.height();

            canvas.drawRect(
                    x1, y1,
                    x1 + textWidth + (LABEL_PADDING * 2) , y1 + textHeight + (LABEL_PADDING * 2),
                    textBackgroundPaint
            );

            canvas.drawText(label, x1, y1 + bounds.height() + LABEL_PADDING, textPaint);
        }
    }
}
