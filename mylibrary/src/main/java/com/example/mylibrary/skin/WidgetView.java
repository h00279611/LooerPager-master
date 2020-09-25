package com.example.mylibrary.skin;

import android.util.AttributeSet;
import android.view.View;

public class WidgetView {

    private View mView;
    private AttributeSet attrs;

    public WidgetView(View view, AttributeSet attrs) {
        this.mView = view;
        this.attrs = attrs;
    }

    public void changeSkinView() {

        // 符合TextView的控件，我就换字体
        changeTypeface(mView);

    }

    private void changeTypeface(View view) {
    }
}
