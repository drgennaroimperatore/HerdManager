package com.ilri.herdmanager.ui.dialogs;

import android.app.Dialog;
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
import com.ilri.herdmanager.database.entities.HealthIntervention;
import com.ilri.herdmanager.database.entities.HerdDao;

import java.util.ArrayList;
import java.util.List;

public class HealthInterventionDialog extends DialogFragment
{
    Context mContext;
    HerdDao mDao;


    public HealthInterventionDialog(Context context, HerdDao dao)
    {
      mContext = context;
      mDao = dao;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.dialog_health_event_intervention, null);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       List<String> mHealthInterventionNames = mDao.getHealthInterventionNames();
        Spinner healthInterventionSpinner = view.findViewById(R.id.health_intervention_dialog_intervention_spinner);

        ArrayAdapter<String> healthInterventionSpinnerAdapter = new ArrayAdapter(mContext,R.layout.health_event_spinner_item, mHealthInterventionNames);
        healthInterventionSpinner.setAdapter(healthInterventionSpinnerAdapter);


    }
}
