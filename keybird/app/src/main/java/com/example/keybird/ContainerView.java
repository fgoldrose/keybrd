package com.example.keybird;

import android.inputmethodservice.KeyboardView;
import android.widget.RelativeLayout;
import android.content.Context;
import android.util.AttributeSet;

public class ContainerView extends KeyboardView {
    private final Context mContext;
    private MyKeyboardView kbv;

    public ContainerView(Context context, AttributeSet attrs){
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        kbv = (MyKeyboardView) findViewById(R.id.kb);
    }

}
