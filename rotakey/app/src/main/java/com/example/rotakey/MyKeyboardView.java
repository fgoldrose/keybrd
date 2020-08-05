package com.example.rotakey;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Path;
import com.example.rotakey.MyKeyboard.Key;


public class MyKeyboardView extends View {
    public MyKeyboardView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
    }
    public MyKeyboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }

    // size of
    float outerpercent = (float) 0.8;
    float innerpercent = (float) 0.7;


    private Key start;

    private MyKeyboard keyboard;

    private MyListener listener = null;

    public interface MyListener {
        //void onCode(int code);
        void onText(String text);
        void onBackspace();
        void onEnter();
    }

    public void setListener(MyListener listener){
        this.listener = listener;
    }

    public void setKeyboard(MyKeyboard k){
        this.keyboard = k;
    }

    private double distance(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2));
    }

    private MyKeyboard.Key getKey(float ex, float ey, float centerx, float centery, float outerradius, float innerradius){
        int position = getSegment(ex, ey, centerx, centery, outerradius, innerradius);
        if(position != -1){
            return this.keyboard.getKey(position);
        }
        else{
            return null;
        }
    }

    private void handleAction(MyKeyboard.Action a){
        if (a == null){
            return;
        }

        switch(a.action){
            case MyKeyboard.DELETE:
                listener.onBackspace();
                break;
            case MyKeyboard.ENTER:
                listener.onEnter();
                break;
            case MyKeyboard.INPUT:
                if (a.text != null) {
                    if(keyboard.mode == MyKeyboard.SHIFTED) {
                        listener.onText(a.text.toUpperCase());
                        if(!keyboard.capslock){
                            keyboard.mode = MyKeyboard.NORMAL;
                        }
                    }
                    else{
                        listener.onText(a.text);
                    }
                }
                else {
                    //listener.onCode(a.arg);
                }
                break;
            case MyKeyboard.MODE_CHANGE:
                keyboard.mode = a.arg;
                break;
            case MyKeyboard.CAPSLOCK:
                if(keyboard.capslock){
                    keyboard.capslock = false;
                    keyboard.mode = MyKeyboard.NORMAL;
                }
                else{
                    keyboard.mode = MyKeyboard.SHIFTED; // Should already be true
                    keyboard.capslock = true;
                }
                break;
            default:
                ;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float outerradius = outerpercent * getHeight() / 2;
        float innerradius = innerpercent * outerradius;
        float centerx = getWidth() / 2;
        float centery = getHeight() / 2;
        int index = event.getActionIndex();
        int oppindex = index == 0 ? 1 : 0;
        float ex = event.getX(index);
        float ey = event.getY(index);

        MyKeyboard.Action a;
        MyKeyboard.Key k = getKey(ex, ey, centerx, centery, outerradius, innerradius);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_UP:
                // second finger up, start goes to where other finger is
                keyboard.secondkey = null;
                start = getKey(event.getX(oppindex), event.getY(oppindex), centerx, centery, outerradius, innerradius);
                break;
            case MotionEvent.ACTION_UP:
                // last finger up
                if (start != null && start == k && start.clickaction != null) {
                    handleAction(start.clickaction);
                }
                else if(start != null && k != null && start.position != 0 && k.position != 0 && Math.abs(start.position - k.position) == 1
                    && distance(centerx, centery, ex, ey) < (outerradius + innerradius) * 1/2){
                    // would normally not count because neighbors but should if we lift finger on it
                    handleAction(keyboard.getAction(start, k));
                }
                start = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //second finger added
                a = keyboard.getAction(start, k);
                if (a != null) {
                    handleAction(a);
                }
                keyboard.setSecondkey(start);
                start = k;
                break;
            case MotionEvent.ACTION_DOWN:
                // one finger down
                start = k;
                break;
            case MotionEvent.ACTION_MOVE:
                // one finger move.
                if (event.getPointerCount() > 1) {
                    return true;
                }

                if (start == null) {
                    start = k;
                } else if(k != null && k.position != 0) {
                    if (start.position != 0 && k.position != 0 && Math.abs(start.position - k.position) == 1
                            && distance(centerx, centery, ex, ey) < (outerradius + innerradius) * 1/2) {
                        //dont count inner half of key on neighboring keys.
                        return true;
                    }
                    a = keyboard.getAction(start, k);
                    if (a != null) {
                        handleAction(a);
                    }
                    start = k;
                }
                break;
            default:
                return false;

        }

        keyboard.setCurkey(start);
        this.invalidate();
        return true;
    }

    @Override
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        final float outerradius = outerpercent * getHeight() / 2;
        final float innerradius = innerpercent * outerradius;
        final float centerx = getWidth() / 2;
        final float centery = getHeight() / 2;

        for (int i = 0; i < 9; i++) {
            Key k = keyboard.getKey(i);
            float cx = centerx + k.xoffset * (outerradius + innerradius) / 2;
            float cy = centery + k.yoffset * (outerradius + innerradius) / 2;

            Paint paint = new Paint();
            if (keyboard.curkey == k || keyboard.secondkey == k) {
                paint.setColor(getResources().getColor(R.color.colorHighlight));
            } else {
                paint.setColor(getResources().getColor(R.color.colorBG));
            }

            if (k.position == 0) {
                canvas.drawCircle(cx, cy, innerradius, paint);
            } else {
                RectF outer_rect = new RectF(centerx - outerradius, centery - outerradius, centerx + outerradius, centery + outerradius);
                RectF inner_rect = new RectF(centerx - innerradius, centery - innerradius, centerx + innerradius, centery + innerradius);

                Paint outlinepaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                outlinepaint.setStyle(Paint.Style.STROKE);
                outlinepaint.setStrokeWidth(3);

                Path path = new Path();
                path.arcTo(outer_rect, -45 * k.position + (float) 112.5, 45);
                path.arcTo(inner_rect, -45 * k.position + 45 + (float) 112.5, -45);
                path.close();


                canvas.drawPath(path, paint);
                canvas.drawPath(path, outlinepaint);


                Paint textpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                textpaint.setColor(Color.BLACK);
                textpaint.setTextSize(60);
                textpaint.setTextAlign(Paint.Align.CENTER);
                float textHeight = textpaint.descent() - textpaint.ascent();
                float textOffset = (textHeight / 2) - textpaint.descent();

                canvas.drawText(k.label, cx, cy + textOffset, textpaint);
            }
        }
    }

    private int getSegment(double xtouch, double ytouch, double xcenter, double ycenter, double outerrad, double innerrad) {
        double dist = distance(xtouch, ytouch, xcenter, ycenter);
        if (dist < innerrad) {
            return 0; // inside inner radius of circle
        } else {//if (dist <= outerrad){
            double y = ytouch - ycenter;
            double x = xtouch - xcenter;
            double pf = (Math.sqrt(2) - 1) * x; //positive flat
            double ps = (Math.sqrt(2) + 1) * x; //positive steep
            double nf = -pf; //negative flat
            double ns = -ps; //negative steep
            if (y >= ps && y >= ns) {
                return 1;
            } else if (y >= pf && y <= ps) {
                return 2;
            } else if (y >= nf && y <= pf) {
                return 3;
            } else if (y >= ns && y <= nf) {
                return 4;
            } else if (y <= ns && y <= ps) {
                return 5;
            } else if (y >= ps && y <= pf) {
                return 6;
            } else if (y >= pf && y <= nf) {
                return 7;
            } else if (y >= nf && y <= ns) {
                return 8;
            }
            else {
                return -2; // should never return
            }
        }
        /*else {
            return -1; //outside outer radius of circle
        }*/
    }

}

