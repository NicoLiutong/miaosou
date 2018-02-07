package com.example.animation.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.EditText;

import com.example.animation.R;

/**
 * Created by 刘通 on 2017/5/18.
 */

public class LineEditText extends EditText {

    private Paint paint;

    private String hint;

    public LineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPaint();
        init();
    }

    private void setPaint(){
        //设置画笔的属性
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        //可以自定义画笔的颜色
        paint.setColor(ContextCompat.getColor(this.getContext(), R.color.edit_line));
        //设置线宽
        paint.setStrokeWidth((float) 5.0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(2,this.getHeight()-2,this.getWidth()-3,this.getHeight()-3,paint);
    }

    private void init(){
        hint = getHint().toString();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if(focused){
            setHint("");
        }else {
            setHint(hint);
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }
}
