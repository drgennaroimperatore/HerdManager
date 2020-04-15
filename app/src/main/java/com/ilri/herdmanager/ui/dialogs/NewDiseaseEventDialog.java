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

import java.util.List;

public class NewDiseaseEventDialog extends DialogFragment {

    Context mContext;
    List<String> mDiseases;

    public NewDiseaseEventDialog(Context context, List<String> diseases)
    {
        mContext = context;
        mDiseases = diseases;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View convertView = inflater.inflate(R.layout.dialog_new_health_event_disease, null);






        return convertView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner diseaseSpinner = view.findViewById(R.id.health_event_disease_spinner);

      //  String[] dummyDiseases = {"Disease 1", "Disease 2", "Disease 3"};
        ArrayAdapter<String> signSpinnerAdapter = new ArrayAdapter(mContext,R.layout.health_event_spinner_item, mDiseases);
        diseaseSpinner.setAdapter(signSpinnerAdapter);





    }
}
