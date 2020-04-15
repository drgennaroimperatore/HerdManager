package com.ilri.herdmanager.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.ADDB;
import com.ilri.herdmanager.database.entities.Signs;

import java.util.List;


public class NewSignEventDialog extends DialogFragment {

    Context mContext;
    List<Signs> mSigns = null;

    public NewSignEventDialog(Context context, List<Signs> signs)
    {
        mContext=context;
        mSigns = signs;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.dialog_new_health_event_sign, null);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner signSpinner = view.findViewById(R.id.health_event_sign_spinner);



        // String[] dummySigns = {"Sign 1", "Sign 2", "Sign 3"};
        ArrayAdapter<String> signSpinnerAdapter = new ArrayAdapter(mContext,R.layout.health_event_spinner_item, mSigns);
        signSpinner.setAdapter(signSpinnerAdapter);
    }
}
