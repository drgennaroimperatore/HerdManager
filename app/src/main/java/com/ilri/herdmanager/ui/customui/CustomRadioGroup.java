package com.ilri.herdmanager.ui.customui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.ilri.herdmanager.R;

public class CustomRadioGroup extends ConstraintLayout {

    private static OnCustomRadioButtonListener onClickListener;

    public CustomRadioGroup(Context context) {
        super(context);
    }

    public CustomRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static void setOnClickListener(OnCustomRadioButtonListener onClickListener) {
        CustomRadioGroup.onClickListener = onClickListener;
    }

    @Override
    public void addView(View c, int index, ViewGroup.LayoutParams params) {
        final View child = c;

        if (child instanceof BaseRadioButton) {
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseRadioButton selectedButton = (BaseRadioButton) child;

                    setAllButtonsToUnselectedState();
                    setSelectedButtonToSelectedState(selectedButton);
                    initOnClickListener(selectedButton);

                }
            } );
        }

        super.addView(child, index, params);
    }

    private void setAllButtonsToUnselectedState() {
        ConstraintLayout container = this;

        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);

            if (child instanceof BaseRadioButton) {
                BaseRadioButton containerView = (BaseRadioButton) child;
                setButtonToUnselectedState(containerView);
            }
        }
    }

    private void setButtonToUnselectedState(BaseRadioButton containerView) {
        float viewWithFilter = 0.5f;

        containerView.setAlpha(viewWithFilter);
        containerView.setBackground(getResources()
                .getDrawable(R.drawable.farmer_radio_button_selected_state));
    }

    private void setSelectedButtonToSelectedState(BaseRadioButton selectedButton) {
        float viewWithoutFilter = 1f;

        selectedButton.setAlpha(viewWithoutFilter);
        selectedButton.setBackground(getResources()
                .getDrawable(R.drawable.farmer_radio_button_unselected_state));
    }

    private void initOnClickListener(View selectedButton) {
        if(onClickListener != null) {
            onClickListener.onClick(selectedButton);
        }
    }



}
