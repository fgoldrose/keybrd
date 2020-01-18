package com.example.keybird;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.inputmethodservice.KeyboardView;
import android.widget.RelativeLayout;


public class MyKeyboardView extends View {

    RectF kb;
    float outerpercent;
    float innerpercent;
    int highlighted;
    int mode;
    float buttonpercent;

    public MyKeyboardView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        init(attrs);
    }
    public MyKeyboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set){
        kb = new RectF();
        highlighted=-1;
        mode = 0;

        if(set == null){
            return;
        }
        innerpercent= (float) 0.5;
        outerpercent= (float)  0.7;
        buttonpercent= (float) 0.2;
        //TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.MyKeyboardView);
        //outerpercent = ta.getFloat(R.styleable.MyKeyboardView_outer_percent, 1);
        //innerpercent = ta.getFloat(R.styleable.MyKeyboardView_inner_percent, 1/2);
        // buttonpercent = ta.getFloat(R.styleable.MyKeyboardView_button_percent, 1/4);
        //ta.recycle();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float outerradius = outerpercent * getHeight()/2;
        float innerradius = innerpercent * outerradius;
        float buttonradius = buttonpercent * getHeight()/2;
        float centerx = getWidth() /2;
        float centery = getHeight() /2;

        float textrad = outerradius;
        float angle = 1 / (float) Math.sqrt(2);
        float x1 = centerx;
        float y1 = centery + textrad;
        float x2 = centerx + angle * textrad;
        float y2 = centery + angle * textrad;
        float x3 = centerx + textrad;
        float y3 = centery;
        float x4 = centerx + angle * textrad;
        float y4 = centery - angle * textrad;
        float x5 = centerx;
        float y5 = centery - textrad;
        float x6 = centerx - angle * textrad;
        float y6 = centery - angle * textrad;
        float x7 = centerx - textrad;
        float y7 = centery;
        float x8 = centerx - angle * textrad;
        float y8 = centery + angle * textrad;

        Paint p1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p1.setStyle(Paint.Style.STROKE);
        Paint p2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p2.setStyle(Paint.Style.STROKE);
        Paint p3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p3.setStyle(Paint.Style.STROKE);
        Paint p4 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p4.setStyle(Paint.Style.STROKE);
        Paint p5 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p5.setStyle(Paint.Style.STROKE);
        Paint p6 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p6.setStyle(Paint.Style.STROKE);
        Paint p7 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p7.setStyle(Paint.Style.STROKE);
        Paint p8 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p8.setStyle(Paint.Style.STROKE);
        Paint pc = new Paint(Paint.ANTI_ALIAS_FLAG);
        pc.setStyle(Paint.Style.STROKE);

        /*
        Paint p1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p1.setColor(Color.GREEN);
        Paint p2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p2.setColor(Color.BLUE);
        Paint p3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p3.setColor(Color.RED);
        Paint p4 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p4.setColor(Color.MAGENTA);
        Paint p5 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p5.setColor(Color.YELLOW);
        Paint p6 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p6.setColor(Color.CYAN);
        Paint p7 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p7.setColor(Color.DKGRAY);
        Paint p8 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p8.setColor(Color.LTGRAY);
        Paint pc = new Paint(Paint.ANTI_ALIAS_FLAG);
        pc.setColor(Color.GRAY);
        */
        canvas.drawCircle(x1, y1, buttonradius, p1);
        canvas.drawCircle(x2, y2, buttonradius, p2);
        canvas.drawCircle(x3, y3, buttonradius, p3);
        canvas.drawCircle(x4, y4, buttonradius, p4);
        canvas.drawCircle(x5, y5, buttonradius, p5);
        canvas.drawCircle(x6, y6, buttonradius, p6);
        canvas.drawCircle(x7, y7, buttonradius, p7);
        canvas.drawCircle(x8, y8, buttonradius, p8);
        canvas.drawCircle(centerx, centery, innerradius, pc);

        Paint hlpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        hlpaint.setStyle(Paint.Style.STROKE);
        float hlgap = 10;
        switch(highlighted){
            case 1:
                canvas.drawCircle(x1, y1, buttonradius + hlgap, hlpaint); break;
            case 2:
                canvas.drawCircle(x2, y2, buttonradius + hlgap, hlpaint); break;
            case 3:
                canvas.drawCircle(x3, y3, buttonradius + hlgap, hlpaint); break;
            case 4:
                canvas.drawCircle(x4, y4, buttonradius + hlgap, hlpaint); break;
            case 5:
                canvas.drawCircle(x5, y5, buttonradius + hlgap, hlpaint); break;
            case 6:
                canvas.drawCircle(x6, y6, buttonradius + hlgap, hlpaint); break;
            case 7:
                canvas.drawCircle(x7, y7, buttonradius + hlgap, hlpaint); break;
            case 8:
                canvas.drawCircle(x8, y8, buttonradius + hlgap, hlpaint); break;
            case 0:
                canvas.drawCircle(centerx, centery, innerradius + hlgap, hlpaint); break;
            default:
                ;
        }

        Paint textpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textpaint.setColor(Color.BLACK);
        textpaint.setTextSize(60);
        textpaint.setTextAlign(Paint.Align.CENTER);

        canvas.drawText(Character.toString(charCode(highlighted, 1)), x1, y1, textpaint);
        canvas.drawText(Character.toString(charCode(highlighted, 2)), x2, y2, textpaint);
        canvas.drawText(Character.toString(charCode(highlighted, 3)), x3, y3, textpaint);
        canvas.drawText(Character.toString(charCode(highlighted, 4)), x4, y4, textpaint);
        canvas.drawText(Character.toString(charCode(highlighted, 5)), x5, y5, textpaint);
        canvas.drawText(Character.toString(charCode(highlighted, 6)), x6, y6, textpaint);
        canvas.drawText(Character.toString(charCode(highlighted, 7)), x7, y7, textpaint);
        canvas.drawText(Character.toString(charCode(highlighted, 8)), x8, y8, textpaint);


        // BELOW is the old code to draw segments!
        /*
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

        canvas.drawText(Character.toString(charCode(highlighted, 1)), centerx, centery + textrad, textpaint);
        canvas.drawText(Character.toString(charCode(highlighted, 2)), centerx + angle * textrad, centery + angle * textrad, textpaint);
        canvas.drawText(Character.toString(charCode(highlighted, 3)), centerx + textrad, centery, textpaint);
        canvas.drawText(Character.toString(charCode(highlighted, 4)), centerx + angle * textrad, centery - angle * textrad, textpaint);
        canvas.drawText(Character.toString(charCode(highlighted, 5)), centerx, centery - textrad, textpaint);
        canvas.drawText(Character.toString(charCode(highlighted, 6)), centerx - angle * textrad, centery - angle * textrad, textpaint);
        canvas.drawText(Character.toString(charCode(highlighted, 7)), centerx - textrad, centery, textpaint);
        canvas.drawText(Character.toString(charCode(highlighted, 8)), centerx - angle * textrad, centery + angle * textrad, textpaint);
        */
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

