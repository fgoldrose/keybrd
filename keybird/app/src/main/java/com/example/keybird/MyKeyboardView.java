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

    int highlighted = -1;
    int mode = 0;
    private int start = -1;
    private int tentative = -1;
    double backspacetime = 200;
    long lastBack = -1;

    private MyListener listener = null;

    public interface MyListener {
        void onKey(String text);
        void onBackspace();
        void onEnter();
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
        int index = event.getActionIndex();
        int oppindex = index == 0 ? 1 : 0;
        float ex = event.getX(index);
        float ey = event.getY(index);


        int seg = getSegment(ex, ey, centerx, centery, outerradius, innerradius);

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

    @Override
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        final float outerradius = outerpercent * getHeight()/2;
        final float innerradius = innerpercent * outerradius;
        final float buttonradius = buttonpercent * getHeight()/2;
        final float centerx = getWidth() /2;
        final float centery = getHeight() /2;



        class Key {
            int position;
            float cx;
            float cy;
            

            Key(int p, float cx, float cy){
                this.position = p;
                this.cx = cx;
                this.cy = cy;
            }

            public void drawKey(){

                float rad = buttonradius;
                if (position == 0){
                    rad = innerradius;
                }
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setStyle(Paint.Style.STROKE);

                canvas.drawCircle(cx, cy, rad, paint);

                if(highlighted == position) {
                    Paint hlpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    hlpaint.setStyle(Paint.Style.STROKE);

                    canvas.drawCircle(cx, cy, rad + 10, hlpaint);
                }

                if(position != 0) {

                    Paint textpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    textpaint.setColor(Color.BLACK);
                    textpaint.setTextSize(60);
                    textpaint.setTextAlign(Paint.Align.CENTER);
                    float textHeight = textpaint.descent() - textpaint.ascent();
                    float textOffset = (textHeight / 2) - textpaint.descent();

                    canvas.drawText(charCode(highlighted, position), cx, cy + textOffset, textpaint);
                }

            }

            void drawSegment(){
                if(position == 0){
                    Paint paint = new Paint();

                    if(highlighted == position) {
                        paint.setColor(Color.GRAY);
                    }
                    else{
                        paint.setColor(Color.WHITE);
                    }
                    canvas.drawCircle(cx, cy, innerradius, paint);
                }
                if(position != 0) {
                    RectF outer_rect = new RectF(centerx-outerradius, centery-outerradius, centerx+outerradius, centery+outerradius);
                    RectF inner_rect = new RectF(centerx-innerradius, centery-innerradius, centerx+innerradius, centery+innerradius);

                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(3);

                    Path path = new Path();
                    path.arcTo(outer_rect, -45*position + (float) 112.5, 45);
                    path.arcTo(inner_rect, -45*position + 45 + (float) 112.5, -45);
                    path.close();

                    Paint fill = new Paint();
                    if(highlighted == position) {

                        fill.setColor(Color.GRAY);

                    }
                    else{
                        fill.setColor(Color.WHITE);
                    }
                    canvas.drawPath(path, fill);

                    canvas.drawPath(path, paint);


                    Paint textpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    textpaint.setColor(Color.BLACK);
                    textpaint.setTextSize(60);
                    textpaint.setTextAlign(Paint.Align.CENTER);
                    float textHeight = textpaint.descent() - textpaint.ascent();
                    float textOffset = (textHeight / 2) - textpaint.descent();

                    canvas.drawText(charCode(highlighted, position), cx, cy + textOffset, textpaint);
                }
            }

        }
        float textrad = (outerradius + innerradius)/2;
        float angle = 1 / (float) Math.sqrt(2);
/*


        new Key(0, centerx, centery).drawKey();
        new Key(1, centerx, centery + textrad).drawKey();
        new Key(2,centerx + angle * textrad, centery + angle * textrad).drawKey();
        new Key(3,centerx + textrad, centery).drawKey();
        new Key(4,centerx + angle * textrad, centery - angle * textrad).drawKey();
        new Key(5,centerx, centery - textrad).drawKey();
        new Key(6,centerx - angle * textrad, centery - angle * textrad).drawKey();
        new Key(7,centerx - textrad, centery).drawKey();
        new Key(8,centerx - angle * textrad, centery + angle * textrad).drawKey();
*/



        new Key(0, centerx, centery).drawSegment();
        new Key(1, centerx, centery + textrad).drawSegment();
        new Key(2,centerx + angle * textrad, centery + angle * textrad).drawSegment();
        new Key(3,centerx + textrad, centery).drawSegment();
        new Key(4,centerx + angle * textrad, centery - angle * textrad).drawSegment();
        new Key(5,centerx, centery - textrad).drawSegment();
        new Key(6,centerx - angle * textrad, centery - angle * textrad).drawSegment();
        new Key(7,centerx - textrad, centery).drawSegment();
        new Key(8,centerx - angle * textrad, centery + angle * textrad).drawSegment();

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
                return "Enter";
            case 5:
                return "CAPS";
            case 6:
                return "!";
            case 7:
                return "<--";
            case 8:
                return ",";
            case 101:
                return ".";
            case 102:
                return "?";
            case 103:
                return "Enter";
            case 105:
                return "lower";
            case 106:
                return "!";
            case 107:
                return "<--";
            case 108:
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
            //CAPS
            case 113:
                return "Y";
            case 124:
                return "W";
            case 134:
                return "Q";
            case 116:
                return "R";
            case 128:
                return "Z";
            case 146:
                return "D";
            case 157:
                return "S";
            case 112:
                return "A";
            case 178:
                return "K";
            case 135:
                return "U";
            case 115:
                return "O";
            case 147:
                return "V";
            case 118:
                return "P";
            case 168:
                return "X";
            case 136:
                return "L";
            case 156:
                return "I";
            case 127:
                return "H";
            case 117:
                return "C";
            case 125:
                return "T";
            case 114:
                return "J";
            case 158:
                return "M";
            case 126:
                return "N";
            case 137:
                return "B";
            case 123:
                return "G";
            case 167:
                return "E";
            case 145:
                return "F";

            default:
                return "";
        }
    }


    private double distance(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2));
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
}

