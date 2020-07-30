package com.example.keybird;

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
import com.example.keybird.Keyboard.Key;

import java.util.Dictionary;
import java.util.Hashtable;

public class MyKeyboardView extends View {
    public MyKeyboardView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
    }
    public MyKeyboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }

    // size of
    float outerpercent = (float) 0.7;
    float innerpercent = (float) 0.6;
    float buttonpercent = (float) 0.2;


    private Key start;

    private Keyboard keyboard;

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

    public void setKeyboard(Keyboard k){
        this.keyboard = k;
    }

    private double distance(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2));
    }

    private Keyboard.Key getKey(float ex, float ey, float centerx, float centery, float outerradius, float innerradius){
        int position = getSegment(ex, ey, centerx, centery, outerradius, innerradius);
        if(position != -1){
            return this.keyboard.getKey(position);
        }
        else{
            return null;
        }
    }

    private void handleAction(Keyboard.Action a){
        if (a == null){
            return;
        }

        switch(a.action){
            case Keyboard.DELETE:
                listener.onBackspace();
                break;
            case Keyboard.ENTER:
                listener.onEnter();
                break;
            case Keyboard.INPUT:
                if (a.text != null) {
                    listener.onText(a.text);
                }
                else {
                    //listener.onCode(a.arg);
                }
                break;
            case Keyboard.MODE_CHANGE:
                keyboard.mode = a.arg;
                break;
            default:
                ;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float outerradius = outerpercent * getHeight() / 2;
        float innerradius = innerpercent * outerradius;
        float buttonradius = buttonpercent * getHeight() / 2;
        float centerx = getWidth() / 2;
        float centery = getHeight() / 2;
        int index = event.getActionIndex();
        int oppindex = index == 0 ? 1 : 0;
        float ex = event.getX(index);
        float ey = event.getY(index);

        Keyboard.Action a;
        Keyboard.Key k = getKey(ex, ey, centerx, centery, outerradius, innerradius);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_UP:
                // second finger leaves, start goes to where other finger is
                start = getKey(event.getX(oppindex), event.getY(oppindex), centerx, centery, outerradius, innerradius);
                break;
            case MotionEvent.ACTION_UP:
                if (start != null && start == k && start.clickaction != null) {
                    handleAction(start.clickaction);
                }
                start = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //second finger added
                a = keyboard.getAction(start, k);
                if (a != null) {
                    handleAction(a);
                }
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

        /*

        if(event.getActionMasked() == MotionEvent.ACTION_POINTER_UP){
            if(tentative != -1){
                listener.onKey(charCode(start, tentative));
                tentative = -1;
            }

            start = getSegment(event.getX(oppindex), event.getY(oppindex), centerx, centery, outerradius, innerradius);
        }
        else if(event.getActionMasked() == MotionEvent.ACTION_UP){
            if(tentative != -1){
                listener.onKey(charCode(start, tentative));
                tentative = -1;
            }
            if(start == 0 && seg == 0){
                listener.onKey(" ");
            }
            start = -1;
        }

        else if(event.getPointerCount() == 1 || event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN){
            if (start == -1) {
                // First button touched
                start = seg;
            }
            else if (seg != 0 && seg != -1) {
                if (start != seg) {
                    // Second button touched
                    if (start == 0 && seg == 7) {
                        listener.onBackspace();
                    }
                    else if(start == 0 && seg == 3) {
                        listener.onEnter();
                    }
                    else if (start == 0 && seg == 5) {
                        if (mode == 0) {
                            mode = 1;
                        } else if (mode == 1) {
                            mode = 0;
                        }
                    } else if (tentative != -1) {
                        if(tentative == seg){
                            if(distance(centerx, centery, ex, ey) >= (outerradius + innerradius) * 1/2){
                                listener.onKey(charCode(start, seg));
                                tentative = -1;
                            }
                            else {
                                return true;
                            }
                        }
                        // process existing tentative value
                        if (Math.abs(tentative - seg) == 1) {
                            // likely tentative value was wrong
                            listener.onKey(charCode(start, seg));
                        }
                        else {
                            // tentative was probably correct, both actions should be processed
                            listener.onKey(charCode(start,  tentative));
                            listener.onKey(charCode(tentative,  seg));
                        }
                        tentative = -1;
                    } else {
                        if (start != 0 && Math.abs(start - seg) == 1
                                && distance(centerx, centery, ex, ey) < (outerradius + innerradius) * 1/2) {
                            //Neighboring positions, and within area of ambiguity.
                            // Set tentative key value, but don't process until seeing if the
                            // next segment touched is the next over neighbor.
                            tentative = seg;
                            //System.out.println("TENTATIVE");
                            return true;
                        }

                        listener.onKey(charCode(start, seg));
                    }
                    start = seg;

                }
                else if(tentative != -1){
                    // we have a tentative value and start==seg, so likely tentative was correct
                    listener.onKey(charCode(start,  tentative));
                    listener.onKey(charCode(tentative,  seg));
                    tentative = -1;
                }

            }
        }
        this.highlighted = start;
        this.invalidate();
        return true;
    }
    */

    @Override
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        final float outerradius = outerpercent * getHeight() / 2;
        final float innerradius = innerpercent * outerradius;
        final float buttonradius = buttonpercent * getHeight() / 2;
        final float centerx = getWidth() / 2;
        final float centery = getHeight() / 2;

        for (int i = 0; i < 9; i++) {
            Key k = keyboard.getKey(i);
            float cx = centerx + k.xoffset * (outerradius + innerradius) / 2;
            float cy = centery + k.yoffset * (outerradius + innerradius) / 2;

            Paint paint = new Paint();
            if (keyboard.curkey == k) {
                paint.setColor(Color.GRAY);
            } else {
                paint.setColor(Color.WHITE);
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
/*

        float textrad = (outerradius + innerradius)/2;
        float angle = 1 / (float) Math.sqrt(2);

        new Key(0, centerx, centery).drawKey();
        new Key(1, centerx, centery + textrad).drawKey();
        new Key(2,centerx + angle * textrad, centery + angle * textrad).drawKey();
        new Key(3,centerx + textrad, centery).drawKey();
        new Key(4,centerx + angle * textrad, centery - angle * textrad).drawKey();
        new Key(5,centerx, centery - textrad).drawKey();
        new Key(6,centerx - angle * textrad, centery - angle * textrad).drawKey();
        new Key(7,centerx - textrad, centery).drawKey();
        new Key(8,centerx - angle * textrad, centery + angle * textrad).drawKey();



        new Key(0, centerx, centery).drawSegment();
        new Key(1, centerx, centery + textrad).drawSegment();
        new Key(2,centerx + angle * textrad, centery + angle * textrad).drawSegment();
        new Key(3,centerx + textrad, centery).drawSegment();
        new Key(4,centerx + angle * textrad, centery - angle * textrad).drawSegment();
        new Key(5,centerx, centery - textrad).drawSegment();
        new Key(6,centerx - angle * textrad, centery - angle * textrad).drawSegment();
        new Key(7,centerx - textrad, centery).drawSegment();
        new Key(8,centerx - angle * textrad, centery + angle * textrad).drawSegment();


    }*/

    private int getSegment(double xtouch, double ytouch, double xcenter, double ycenter, double outerrad, double innerrad) {
        double dist = distance(xtouch, ytouch, xcenter, ycenter);
        if (dist < innerrad) {
            return 0; // inside inner radius of circle
        } else if (dist <= outerrad){
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
        else {
            return -1; //outside outer radius of circle
        }
    }

    /*
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
    */

}

