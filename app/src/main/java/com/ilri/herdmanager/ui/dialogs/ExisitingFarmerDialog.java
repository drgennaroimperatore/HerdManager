package com.ilri.herdmanager.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
        EditText searchFarmerEditText = findViewById(R.id.dialog_existing_farmer_search_farmer_editText);
        searchFarmerEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                List<Farmer> farmers = HerdDatabase.getInstance(mContext).getHerdDao().getFarmerbyName(s.toString());
                if(s.toString().isEmpty())
                    farmers=HerdDatabase.getInstance(mContext).getHerdDao().getAllFarmers();
                populateRadioGroup(farmers);



            }
        });

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

        populateRadioGroup(farmers);

    }

    private void populateRadioGroup(List<Farmer> farmers)
    {
        int i = 0;
        mFarmerRadioGroup.removeAllViews();
        for (Farmer f : farmers) {
            FarmerRadioButton rb = new FarmerRadioButton(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(10, 5, 10, 5);
            rb.setFarmerInformation(f);
            rb.setId(View.generateViewId());
            //rb.setLayoutParams(lp);
            mFarmerRadioGroup.addView(rb, i, lp);
            i++;
        }
        mFarmerRadioGroup.initialise();

    }

}
