package com.example.keybird;

import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;

public class KeyboardIME extends InputMethodService {
    private MyKeyboardView mInputView;

    @Override
    public View onCreateInputView(){
        View layout = getLayoutInflater().inflate(R.layout.input, null);
        mInputView = layout.findViewById(R.id.kbv);
        mInputView.setOnTouchListener(new MyTouchListener(mInputView, getCurrentInputConnection()));
        return layout;
    }
}
