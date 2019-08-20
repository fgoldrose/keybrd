package com.example.keybird;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.graphics.Canvas;
import android.graphics.Paint;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final KeyboardView kb = findViewById(R.id.kb);
        final TextView display = findViewById(R.id.disp);

        kb.setOnTouchListener(new MyTouchListener(kb, display));
    }
/*
    @Override
    protected void onDraw(Canvas canvas) {
        View kb = findViewById(R.id.kb);
        canvas.drawArc(kb, 22.5F, 67.5F, true, new Paint());
        canvas.drawArc(kb, 112.5F, 157.5F, true, new Paint());

    }*/


}

