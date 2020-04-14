package com.ilri.herdmanager.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.DialogFragment;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.Farmer;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.ui.NewCaseActivity;
import com.ilri.herdmanager.ui.customui.CustomRadioGroup;
import com.ilri.herdmanager.ui.customui.FarmerRadioButton;

import java.util.List;

public class ExisitingFarmerDialog extends Dialog {

    Context mContext;
    CustomRadioGroup mFarmerRadioGroup;
    Button mConfirmSelectionButton;
    NewCaseActivity mActivity;

    public ExisitingFarmerDialog(Context context, NewCaseActivity nca) {
        super(context);
        mContext = context;
        mActivity = nca;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_existing_farmer);

        mFarmerRadioGroup = findViewById(R.id.existing_farmer_RadioGroup);
        mFarmerRadioGroup.setOrientation(LinearLayout.VERTICAL);


        List<Farmer> farmers = HerdDatabase.getInstance(mContext).getHerdDao().getAllFarmers();

        mConfirmSelectionButton = findViewById(R.id.button_existing_farmer_select_farmer);
        mConfirmSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Farmer selectedFarmer = mFarmerRadioGroup.getSelectedFarmer();
                mActivity.assignFarmer(selectedFarmer);
                dismiss();
            }
        });

        FarmerRadioButton prevButton = null;
        int i =0;
        for (Farmer f :farmers)
        {
            FarmerRadioButton rb = new FarmerRadioButton(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT );
            lp.setMargins(10,5,10,5);
            rb.setFarmerInformation(f);
            rb.setId(View.generateViewId());
            //rb.setLayoutParams(lp);
            mFarmerRadioGroup.addView(rb,i,lp);

/*
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.centerHorizontally(rb.getId(), mFarmerRadioGroup.getId());
            if(prevButton ==null)
            {
               constraintSet.connect(rb.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,10);


            }
            else
            {

                constraintSet.connect(rb.getId(), ConstraintSet.TOP, prevButton.getId(), ConstraintSet.BOTTOM,5);

            }

            constraintSet.connect(rb.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START,10);
            constraintSet.connect(rb.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END,10);

            constraintSet.applyTo(mFarmerRadioGroup);


            prevButton = rb;
           */



i++;
        }

        mFarmerRadioGroup.initialise();
    }
}
