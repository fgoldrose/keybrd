package com.example.keybird;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.graphics.Canvas;

public class MyTouchListener implements View.OnTouchListener {
    private int start = -1;
    double longpresstime = 500;

    private KeyboardView kb;
    private TextView display;

    public MyTouchListener(KeyboardView kb, TextView display){
        this.kb = kb;
        this.display = display;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        double outerradius = this.kb.outerpercent * this.kb.getHeight()/2;
        double innerradius = this.kb.innerpercent * outerradius;
        double buttonradius = this.kb.buttonpercent * this.kb.getHeight()/2;

        int seg = getSegment(event.getX(), event.getY(),
                 v.getWidth()/2, v.getHeight()/2, outerradius, innerradius, buttonradius);
        if(event.getAction() == MotionEvent.ACTION_UP){
            if(start == 0 && seg == 0){
                if(event.getEventTime() - event.getDownTime() >= longpresstime){
                    if(kb.mode == 0){
                        kb.mode = 1;
                    }
                    else if(kb.mode == 1){
                        kb.mode = 0;
                    }
                }
                else {
                    String displaytext = display.getText() + " ";
                    display.setText(displaytext);
                }
            }
            start = -1;
            kb.highlighted = start;
            kb.invalidate();
        }
        else {
            if (start == -1) {
                start = seg;
            } else if (start != seg && seg != 0 && seg != -1) {
                String add = Character.toString(kb.charCode(start, seg));
                if (add == "") {
                    add = "(" + Integer.toString(start) + Integer.toString(seg) + ")";
                }
                String displaytext = display.getText() + add;
                display.setText(displaytext);
                start = seg;
            }
            kb.highlighted = start;
            kb.invalidate();
        }
        return true;
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

    /*
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
    } */
}

