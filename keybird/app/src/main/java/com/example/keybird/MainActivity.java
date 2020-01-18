package com.example.keybird;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MyKeyboardView kb = findViewById(R.id.kb);
        final TextView display = findViewById(R.id.disp);

        //kb.setOnTouchListener(new MyTouchListener(kb),);
    }
/*
    @Override
    protected void onDraw(Canvas canvas) {
        View kb = findViewById(R.id.kb);
        canvas.drawArc(kb, 22.5F, 67.5F, true, new Paint());
        canvas.drawArc(kb, 112.5F, 157.5F, true, new Paint());

    }*/


}

