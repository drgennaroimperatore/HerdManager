package com.ilri.herdmanager.ui.customui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ilri.herdmanager.R;

public class FarmerRadioButton extends BaseRadioButton {
   private TextView mTitleTextView;

    public FarmerRadioButton(Context context, AttributeSet attrs, int layoutResId, int[] styleable) {
        super(context, attrs, R.layout.custom_farmer_radio_button, R.styleable.FarmerRadioButton);
    }

    public FarmerRadioButton(Context context, AttributeSet attrs, int defStyleAttr, int layoutResId, int[] styleable) {
        super(context, attrs, defStyleAttr, R.layout.custom_farmer_radio_button, R.styleable.FarmerRadioButton);
    }

    @Override
    protected void bindViews() {
     mTitleTextView = findViewById(R.id.radio_button_farmerName);

    }

    @Override
    protected void initAttributes() {



    }

    @Override
    protected void populateViews() {

    }
}
