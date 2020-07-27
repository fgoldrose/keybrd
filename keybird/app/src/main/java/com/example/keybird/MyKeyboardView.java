package com.example.keybird;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyKeyboardView extends View {
    public MyKeyboardView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
    }
    public MyKeyboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }

    // size of
    float outerpercent = (float) 0.7;
    float innerpercent = (float) 0.5;
    float buttonpercent = (float) 0.2;

    int highlighted = -1;
    int mode = 0;
    private int start = -1;
    double longpresstime = 500;

    private MyListener listener = null;

    public interface MyListener {
        void onKey(String text);
        void onBackspace();
    }


    public void setListener(MyListener listener){
        this.listener = listener;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float outerradius = outerpercent * getHeight()/2;
        float innerradius = innerpercent * outerradius;
        float buttonradius = buttonpercent * getHeight()/2;
        float centerx = getWidth() /2;
        float centery = getHeight() /2;
        int seg = getSegment(event.getX(), event.getY(),
                centerx, centery, outerradius, innerradius, buttonradius);
        if(event.getAction() == MotionEvent.ACTION_UP){
            if(start == 0 && seg == 0){
                // Center button

                if(event.getEventTime() - event.getDownTime() >= longpresstime){
                    // Trigger mode change
                    if(mode == 0){
                        mode = 1;
                    }
                    else if(mode == 1){
                        mode = 0;
                    }
                }
                else {
                    if(listener != null)
                        listener.onKey(" ");
                }
            }
            start = -1;
            this.highlighted = start;
            this.invalidate();
        }
        else {
            if (start == -1) {
                // First button touched
                start = seg;
            }
            else if (start != seg && seg != 0 && seg != -1) {
                // Second button touched

                if(start == 0 && seg == 7 && listener != null){
                    listener.onBackspace();
                }
                else if (listener != null){
                    listener.onKey(charCode(start, seg));
                }
                start = seg;
            }

            this.highlighted = start;
            this.invalidate();
        }
        return true;
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
        float textHeight = textpaint.descent() - textpaint.ascent();
        float textOffset = (textHeight / 2) - textpaint.descent();


        canvas.drawText(charCode(highlighted, 1), x1, y1 + textOffset, textpaint);
        canvas.drawText(charCode(highlighted, 2), x2, y2 + textOffset, textpaint);
        canvas.drawText(charCode(highlighted, 3), x3, y3 + textOffset, textpaint);
        canvas.drawText(charCode(highlighted, 4), x4, y4 + textOffset, textpaint);
        canvas.drawText(charCode(highlighted, 5), x5, y5 + textOffset, textpaint);
        canvas.drawText(charCode(highlighted, 6), x6, y6 + textOffset, textpaint);
        canvas.drawText(charCode(highlighted, 7), x7, y7 + textOffset, textpaint);
        canvas.drawText(charCode(highlighted, 8), x8, y8 + textOffset, textpaint);
    }

    public String charCode(int from, int to) {
        int code;
        if (from < to) {
            code = mode * 100 + from * 10 + to;
        } else {
            code = mode * 100 + to * 10 + from;
        }
        switch(code){
            case 0:
                return " ";
            case 1:
                return ".";
            case 2:
                return "?";
            case 3:
                return "!";
            case 7:
                return "<--";
            case 8:
                return ",";

            case 13:
                return "y";
            case 24:
                return "w";
            case 34:
                return "q";
            case 16:
                return "r";
            case 28:
                return "z";
            case 46:
                return "d";
            case 57:
                return "s";
            case 12:
                return "a";
            case 78:
                return "k";
            case 35:
                return "u";
            case 15:
                return "o";
            case 47:
                return "v";
            case 18:
                return "p";
            case 68:
                return "x";
            case 36:
                return "l";
            case 56:
                return "i";
            case 27:
                return "h";
            case 17:
                return "c";
            case 25:
                return "t";
            case 14:
                return "j";
            case 58:
                return "m";
            case 26:
                return "n";
            case 37:
                return "b";
            case 23:
                return "g";
            case 67:
                return "e";
            case 45:
                return "f";

            default:
                return "";
        }
    }


    private double distance(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2));
    }

    private int getSegment(double xtouch, double ytouch, double centerx, double centery, double outerrad, double innerrad, double buttonrad) {
        double textrad = outerrad;
        double angle = 1 / (float) Math.sqrt(2);
        double x1 = centerx;
        double y1 = centery + textrad;
        double x2 = centerx + angle * textrad;
        double y2 = centery + angle * textrad;
        double x3 = centerx + textrad;
        double y3 = centery;
        double x4 = centerx + angle * textrad;
        double y4 = centery - angle * textrad;
        double x5 = centerx;
        double y5 = centery - textrad;
        double x6 = centerx - angle * textrad;
        double y6 = centery - angle * textrad;
        double x7 = centerx - textrad;
        double y7 = centery;
        double x8 = centerx - angle * textrad;
        double y8 = centery + angle * textrad;
        double cdist = distance(xtouch, ytouch, centerx, centery);
        if (cdist < innerrad) {
            return 0; // inside inner radius of circle
        } else if (distance(xtouch, ytouch, x1, y1) <= buttonrad) {
            return 1;
        } else if (distance(xtouch, ytouch, x2, y2) <= buttonrad) {
            return 2;
        } else if (distance(xtouch, ytouch, x3, y3) <= buttonrad) {
            return 3;
        } else if (distance(xtouch, ytouch, x4, y4) <= buttonrad) {
            return 4;
        } else if (distance(xtouch, ytouch, x5, y5) <= buttonrad) {
            return 5;
        } else if (distance(xtouch, ytouch, x6, y6) <= buttonrad) {
            return 6;
        } else if (distance(xtouch, ytouch, x7, y7) <= buttonrad) {
            return 7;
        } else if (distance(xtouch, ytouch, x8, y8) <= buttonrad) {
            return 8;
        } else {
            return -1;
        }
    }
}

