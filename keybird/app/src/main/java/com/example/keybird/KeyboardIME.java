package com.example.keybird;

import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;

public class KeyboardIME extends InputMethodService {
    private MyKeyboardView mInputView;

    @Override
    public void onCreate(){
        super.onCreate();

    }
    @Override
    public View onCreateInputView(){

        //mInputView = new MyKeyboardView(this, null);
        mInputView = (MyKeyboardView) getLayoutInflater().inflate(R.layout.input, null);
        return mInputView;
        //Log.d("height", Integer.toString(mInputView.getMeasuredHeight()));
        //mInputView.setOnTouchListener(new MyTouchListener(mInputView, getCurrentInputConnection()));

        //return mInputView;
    }

/*
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI;
    }*/
}
