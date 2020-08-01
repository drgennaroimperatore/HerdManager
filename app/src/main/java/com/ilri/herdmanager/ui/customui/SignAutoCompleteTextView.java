package com.ilri.herdmanager.ui.customui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

@SuppressLint("AppCompatCustomView")
public class SignAutoCompleteTextView extends AutoCompleteTextView {
    public SignAutoCompleteTextView(Context context) {
        super(context);
    }

    public SignAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SignAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        if(focused && getFilter()!=null)
        {
            performFiltering(getText(),0);
        }
    }
}
