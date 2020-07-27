package com.example.keybird;

import android.inputmethodservice.InputMethodService;
import android.support.v4.view.KeyEventDispatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;


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

    public void onEnter(){


        int actionId = getCurrentInputEditorInfo().actionId;

        switch (actionId) {
            case EditorInfo.IME_ACTION_SEARCH:
                sendDefaultEditorAction(true);
                break;
            case EditorInfo.IME_ACTION_GO:
                sendDefaultEditorAction(true);
                break;
            case EditorInfo.IME_ACTION_SEND:
                sendDefaultEditorAction(true);
                break;
            default:
                getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
        }


    }
}
