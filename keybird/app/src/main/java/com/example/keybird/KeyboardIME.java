package com.example.keybird;

import android.inputmethodservice.InputMethodService;
import android.view.View;


public class KeyboardIME extends InputMethodService implements MyKeyboardView.MyListener {

    @Override
    public View onCreateInputView(){
        View layout = getLayoutInflater().inflate(R.layout.input, null);
        MyKeyboardView kb = layout.findViewById(R.id.kbv);
        kb.setListener(this);
        return layout;
    }

    public void onKey(String text){
        getCurrentInputConnection().commitText(text, 1);
    }
    public void onBackspace(){
        getCurrentInputConnection().deleteSurroundingText(1,0);
    }
}
