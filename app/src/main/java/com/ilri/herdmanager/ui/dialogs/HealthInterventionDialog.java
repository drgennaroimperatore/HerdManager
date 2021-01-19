package com.ilri.herdmanager.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.ADDB;
import com.ilri.herdmanager.database.entities.ADDBDAO;
import com.ilri.herdmanager.database.entities.HealthIntervention;
import com.ilri.herdmanager.database.entities.Herd;
import com.ilri.herdmanager.database.entities.HerdDao;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.database.entities.VaccinesForSpecies;

import java.util.ArrayList;
import java.util.List;

public class HealthInterventionDialog extends DialogFragment
{
    Context mContext;
    String mSpecies;
    ADDBDAO mADDBDAO;
    HerdDao mHerdDAO;


    public HealthInterventionDialog(Context context, int herdID)
    {
      mContext = context;

      HerdDao herdDao = HerdDatabase.getInstance(context).getHerdDao();
        ADDBDAO addbdao = ADDB.getInstance(context).getADDBDAO();
        mADDBDAO = addbdao; mHerdDAO = herdDao;

        Herd herd = herdDao.getHerdByID(herdID).get(0);
        switch(addbdao.getAnimalNameFromID(herd.speciesID).get(0))
        {
            case " CATTLE":
                mSpecies= "CATTLE";
                break;
            case " CAMEL":
                mSpecies="CAMEL";
                break;
            case " SHEEP":
                mSpecies="SHEEP";
            case " GOAT":
                mSpecies="GOAT";
                break;
        }
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

       List<String> mHealthInterventionNames = mHerdDAO.getHealthInterventionNames();
        Spinner healthInterventionSpinner = view.findViewById(R.id.health_intervention_dialog_intervention_spinner);

        ArrayAdapter<String> healthInterventionSpinnerAdapter = new ArrayAdapter(mContext,R.layout.health_event_spinner_item, mHealthInterventionNames);
        healthInterventionSpinner.setAdapter(healthInterventionSpinnerAdapter);

        final Spinner vaccinationSpinners = view.findViewById(R.id.dialog_health_intervention_vaccination_spinner);

  //      List<VaccinesForSpecies> test = mHerdDAO.getAllVaccinesForSpecies();

        List<String> vaccinationForSpecies = mHerdDAO.getVaccineNameForSpecies(mSpecies);
        ArrayAdapter<String> vaccinationSpinnerAdapter = new ArrayAdapter(mContext,R.layout.health_event_spinner_item, vaccinationForSpecies);
        vaccinationSpinners.setAdapter(vaccinationSpinnerAdapter);

        healthInterventionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    vaccinationSpinners.setVisibility(View.VISIBLE);
                }
                else
                {
                    vaccinationSpinners.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
