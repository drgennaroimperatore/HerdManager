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

public class ExisitingFarmerDialog extends Dialog {

    Context mContext;

    public ExisitingFarmerDialog(Context context)
    {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.dialog_existing_farmer);
    }


}
