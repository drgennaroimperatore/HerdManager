package com.ilri.herdmanager.ui.customui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.Farmer;

public class CustomRadioGroup extends LinearLayout{

    private static OnCustomRadioButtonListener onClickListener;

    FarmerRadioButton m_SelectedButton = null;

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

    public void initialise()

    {
        setAllButtonsToUnselectedState();
        setFirstButtonToSelectedState();
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
                    m_SelectedButton = (FarmerRadioButton) selectedButton;

                }
            } );
        }

        super.addView(child, index, params);
    }

    private void setFirstButtonToSelectedState()
    {
        LinearLayout container = this;
        View child = container.getChildAt(0);
        if(child instanceof BaseRadioButton)
        {
            setSelectedButtonToSelectedState((BaseRadioButton) child);
            m_SelectedButton = (FarmerRadioButton) child;
        }
    }

    private void setAllButtonsToUnselectedState() {
        LinearLayout container = this;

        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);

            if (child instanceof BaseRadioButton) {
                BaseRadioButton containerView = (BaseRadioButton) child;
                setButtonToUnselectedState((BaseRadioButton) child);
            }
        }
    }

    private void setButtonToUnselectedState(BaseRadioButton containerView) {
        float viewWithFilter = 0.5f;

        containerView.setAlpha(viewWithFilter);
        containerView.setBackground(getResources()
                .getDrawable(R.drawable.farmer_radio_button_unselected_state));
    }

    private void setSelectedButtonToSelectedState(BaseRadioButton selectedButton) {
        float viewWithoutFilter = 1f;

        selectedButton.setAlpha(viewWithoutFilter);
        selectedButton.setBackground(getResources()
                .getDrawable(R.drawable.farmer_radio_button_selected_state));
    }

    private void initOnClickListener(View selectedButton) {
        if(onClickListener != null) {
            onClickListener.onClick(selectedButton);
        }
    }

    public Farmer getSelectedFarmer()
    {
        return m_SelectedButton.getAssociatedFarmer();
    }



}
