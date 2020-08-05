package com.example.rotakey;

import android.inputmethodservice.InputMethodService;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;


public class KeyboardIME extends InputMethodService implements MyKeyboardView.MyListener {

    @Override
    public View onCreateInputView(){
        MyKeyboard keyboard = new MyKeyboard();
        keyboard.setupCenter();
        keyboard.setupNormal();
        keyboard.setupSymbols();

        View layout = getLayoutInflater().inflate(R.layout.input, null);
        MyKeyboardView kbv = layout.findViewById(R.id.kbv);
        kbv.setListener(this);
        kbv.setKeyboard(keyboard);
        return layout;
    }

    public void onText(String text){
        getCurrentInputConnection().commitText(text, 1);
    }
    public void onBackspace(){
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
    }

    public void onEnter(){

        int actionId = getCurrentInputEditorInfo().imeOptions & EditorInfo.IME_MASK_ACTION;

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
                getCurrentInputConnection().sendKeyEvent(
                        new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
        }


    }
}
