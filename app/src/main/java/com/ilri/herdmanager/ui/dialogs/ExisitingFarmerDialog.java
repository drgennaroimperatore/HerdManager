package com.ilri.herdmanager.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.Farmer;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.ui.customui.CustomRadioGroup;
import com.ilri.herdmanager.ui.customui.FarmerRadioButton;

import java.util.List;

public class ExisitingFarmerDialog extends Dialog {

    Context mContext;
    CustomRadioGroup mFarmerRadioGroup;

    public ExisitingFarmerDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_existing_farmer);

        mFarmerRadioGroup = findViewById(R.id.existing_farmer_RadioGroup);

        List<Farmer> farmers = HerdDatabase.getInstance(mContext).getHerdDao().getAllFarmers();

        for (Farmer f :farmers)
        {
            FarmerRadioButton rb = new FarmerRadioButton(mContext);
            rb.setFarmerInformation(f);

            mFarmerRadioGroup.addView(rb);

        }
    }
}
