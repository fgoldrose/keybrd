package com.example.keybird;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Path;
import java.util.HashMap;
import java.util.Map;


public class KeyboardView extends View {

    Paint mPaint, otherPaint, outerPaint, mTextPaint;
    Rect mRect;
    RectF kb;
    int mSquareColor;
    float outerpercent;
    float innerpercent;
    int highlighted;
    int mode;

    public KeyboardView(Context context){
        super(context);
        init(null);
    }
    public KeyboardView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        init(attrs);
    }
    public KeyboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRect = new Rect();
        kb = new RectF();
        highlighted=-1;
        mode = 0;

        if(set == null){
            return;
        }
        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.KeyboardView);
        mSquareColor = ta.getColor(R.styleable.KeyboardView_square_color, Color.GREEN);
        mPaint.setColor(mSquareColor);
        outerpercent = ta.getFloat(R.styleable.KeyboardView_outer_percent, 1);
        innerpercent = ta.getFloat(R.styleable.KeyboardView_inner_percent, 1/2);
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float outerradius = outerpercent * getHeight()/2;
        float innerradius = innerpercent * outerradius;
        float centerx = getWidth() /2;
        float centery = getHeight() /2;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GRAY);
        RectF kb = new RectF();
        kb.left = centerx - outerradius;
        kb.right = centerx + outerradius;
        kb.top = centery - outerradius;
        kb.bottom = centery + outerradius;

        Paint segpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        segpaint.setColor(Color.LTGRAY);
        Paint centpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centpaint.setColor(Color.WHITE);
        Paint hlpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if(mode == 0){
            hlpaint.setColor(Color.BLUE);
        }
       else{
           hlpaint.setColor(Color.RED);
        }

        canvas.drawOval(kb, paint);
        canvas.drawArc(kb, 22.5F, 45F, true, segpaint);
        canvas.drawArc(kb, 112.5F, 45F, true, segpaint);
        canvas.drawArc(kb, 202.5F, 45F, true, segpaint);
        canvas.drawArc(kb, 292.5F, 45F, true, segpaint);
        if (highlighted >0) {
            float startangle = (float) 112.5 - 45 * highlighted;
            canvas.drawArc(kb, startangle, 45F, true, hlpaint);
        }
        if(highlighted == 0){
            canvas.drawCircle(centerx, centery, innerradius, hlpaint);
        }
        else{
            canvas.drawCircle(centerx, centery, innerradius, centpaint);
        }

        Paint textpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textpaint.setColor(Color.BLACK);
        textpaint.setTextSize(60);
        float textrad = (innerradius + outerradius) / 2;
        float angle = 1 / (float) Math.sqrt(2);
        canvas.drawText(Character.toString(charCode(highlighted, 1)), centerx, centery + textrad, textpaint);
        canvas.drawText(Character.toString(charCode(highlighted, 2)), centerx + angle * textrad, centery + angle * textrad, textpaint);
        canvas.drawText(Character.toString(charCode(highlighted, 3)), centerx + textrad, centery, textpaint);
        canvas.drawText(Character.toString(charCode(highlighted, 4)), centerx + angle * textrad, centery - angle * textrad, textpaint);
        canvas.drawText(Character.toString(charCode(highlighted, 5)), centerx, centery - textrad, textpaint);
        canvas.drawText(Character.toString(charCode(highlighted, 6)), centerx - angle * textrad, centery - angle * textrad, textpaint);
        canvas.drawText(Character.toString(charCode(highlighted, 7)), centerx - textrad, centery, textpaint);
        canvas.drawText(Character.toString(charCode(highlighted, 8)), centerx - angle * textrad, centery + angle * textrad, textpaint);
    }


    public char charCode(int from, int to) {
        int code;
        if (from < to) {
            code = mode * 100 + from * 10 + to;
        } else {
            code = mode * 100 + to * 10 + from;
        }
        switch(code){
            case 0:
                return ' ';
            case 1:
                return '.';
            case 2:
                return '?';
            case 8:
                return ',';
            case 7:
                return '!';

            case 13:
                return 'y';
            case 24:
                return 'w';
            case 34:
                return 'q';
            case 16:
                return 'r';
            case 28:
                return 'z';
            case 46:
                return 'd';
            case 57:
                return 's';
            case 12:
                return 'a';
            case 78:
                return 'k';
            case 35:
                return 'u';
            case 15:
                return 'o';
            case 47:
                return 'v';
            case 18:
                return 'p';
            case 68:
                return 'x';
            case 36:
                return 'l';
            case 56:
                return 'i';
            case 27:
                return 'h';
            case 17:
                return 'c';
            case 25:
                return 't';
            case 14:
                return 'j';
            case 58:
                return 'm';
            case 26:
                return 'n';
            case 37:
                return 'b';
            case 23:
                return 'g';
            case 67:
                return 'e';
            case 45:
                return 'f';

            /*
            case 12:
                return 'a';
            case 13:
                return 'b';
            case 14:
                return 'c';
            case 15:
                return 'd';
            case 16:
                return 'e';
            case 17:
                return 'f';
            case 18:
                return 'g';
            case 23:
                return 'h';
            case 24:
                return 'i';
            case 25:
                return 'j';
            case 26:
                return 'k';
            case 27:
                return 'l';
            case 28:
                return 'm';
            case 34:
                return 'n';
            case 35:
                return 'o';
            case 36:
                return 'p';
            case 37:
                return 'q';
            case 38:
                return 'r';
            case 45:
                return 's';
            case 46:
                return 't';
            case 47:
                return 'u';
            case 48:
                return 'v';
            case 56:
                return 'w';
            case 57:
                return 'x';
            case 58:
                return 'y';
            case 67:
                return 'z';
            case 68:
                return '\0';
            case 78:
                return '\0';
             */

            default:
                return '\0';
        }
    }
}

