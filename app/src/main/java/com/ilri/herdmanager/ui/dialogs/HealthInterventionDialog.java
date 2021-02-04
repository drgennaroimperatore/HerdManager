package com.ilri.herdmanager.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.ADDB;
import com.ilri.herdmanager.database.entities.ADDBDAO;
import com.ilri.herdmanager.database.entities.HealthIntervention;
import com.ilri.herdmanager.database.entities.HealthInterventionForHealthEvent;
import com.ilri.herdmanager.database.entities.Herd;
import com.ilri.herdmanager.database.entities.HerdDao;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.database.entities.VaccinesForSpecies;
import com.ilri.herdmanager.managers.HerdVisitManager;
import com.ilri.herdmanager.ui.main.AddHeardHealthEventFragment;

import java.util.ArrayList;
import java.util.List;

public class HealthInterventionDialog extends DialogFragment
{
    Context mContext;
    String mSpecies;
    ADDBDAO mADDBDAO;
    HerdDao mHerdDAO;
    AddHeardHealthEventFragment mFragment;
    boolean mIsEdting = false;
    boolean mIsEditingInReadOnly = false;
    int mPositionToEdit =0;
    int mHerdVisitID = -155;
    HealthInterventionForHealthEvent mHealthInterventionToBeEdited = null;


    public HealthInterventionDialog(Context context, int herdID, AddHeardHealthEventFragment fragment, int herdVisitID, boolean editingInReadOnly)
    {
      mContext = context;
      mFragment = fragment;


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

        mHerdVisitID = herdVisitID;
        mIsEditingInReadOnly = editingInReadOnly;


    }



    public HealthInterventionDialog(Context context, int herdID,
                                    AddHeardHealthEventFragment fragment, HealthInterventionForHealthEvent healthIntervention,
                                    int pos, boolean editingInReadOnly) {
        mContext = context;
        mFragment = fragment;

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
        mIsEdting = true;
        mIsEditingInReadOnly = editingInReadOnly;
       // mHerdVisitID = herdVisitID;
       mPositionToEdit =pos;
                mHealthInterventionToBeEdited = healthIntervention;
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
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       List<String> mHealthInterventionNames = mHerdDAO.getHealthInterventionNames();
       final Spinner healthInterventionSpinner = view.findViewById(R.id.health_intervention_dialog_intervention_spinner);
       if(mIsEdting)
           healthInterventionSpinner.setVisibility(View.VISIBLE);

        ArrayAdapter<String> healthInterventionSpinnerAdapter = new ArrayAdapter(mContext,R.layout.health_event_spinner_item, mHealthInterventionNames);
        healthInterventionSpinner.setAdapter(healthInterventionSpinnerAdapter);
        final int healthInterventionCount = healthInterventionSpinnerAdapter.getCount();

        final Spinner vaccinationSpinners = view.findViewById(R.id.dialog_health_intervention_vaccination_spinner);

  //      List<VaccinesForSpecies> test = mHerdDAO.getAllVaccinesForSpecies();

        List<String> vaccinationForSpecies = mHerdDAO.getVaccineNameForSpecies(mSpecies);
        ArrayAdapter<String> vaccinationSpinnerAdapter = new ArrayAdapter(mContext,R.layout.health_event_spinner_item, vaccinationForSpecies);
        vaccinationSpinners.setAdapter(vaccinationSpinnerAdapter);

       final LinearLayout comentsSection = view.findViewById(R.id.dialog_health_intervention_comments_section);

        healthInterventionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                comentsSection.setVisibility(View.GONE);
                if(position==0)
                {
                    vaccinationSpinners.setVisibility(View.VISIBLE);
                }
                else
                {
                    if(position == healthInterventionCount-1)
                        comentsSection.setVisibility(View.VISIBLE);
                    vaccinationSpinners.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final TextView nAffectedBabiesTV = view.findViewById(R.id.dialog_health_intervention_nbabiesAffected_editText);
       final TextView nAffectedYoungTV = view.findViewById(R.id.dialog_health_intervention_nYoungffected_editText);
      final  TextView nAffectedOldTV = view.findViewById(R.id.dialog_health_intervention_nOldAffected_editText);

      if(mIsEdting && mHealthInterventionToBeEdited!=null)
      {

          nAffectedBabiesTV.setText(String.valueOf(mHealthInterventionToBeEdited.nBabies));
          nAffectedYoungTV.setText(String.valueOf(mHealthInterventionToBeEdited.nYoung));
          nAffectedOldTV.setText(String.valueOf(mHealthInterventionToBeEdited.nOld));

          Button deleteIntervention = view.findViewById(R.id.dialog_health_intervention_delete_button);
          deleteIntervention.setVisibility(View.VISIBLE);

          deleteIntervention.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                 HealthInterventionForHealthEvent h= mFragment.deleteHealthIntervention(mPositionToEdit);
                 if(mIsEditingInReadOnly)
                     HerdVisitManager.getInstance().deleteHealthInterventionForHealthEventForExistingVisit(getContext(),h);

                  dismiss();
              }
          });
      }


        Button submitButton = view.findViewById(R.id.dialog_health_intervention_submit_button);
        if(mIsEdting) {
            submitButton.setText("Edit Health Intervention");
            if(mIsEditingInReadOnly)
            healthInterventionSpinner.setVisibility(View.GONE);
            vaccinationSpinners.setVisibility(View.GONE);
        }
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String affectedBabiesStr =  nAffectedBabiesTV.getText().toString();
                if(affectedBabiesStr.isEmpty())
                    affectedBabiesStr="0";
                String affectedYoungStr =  nAffectedYoungTV.getText().toString();
                if(affectedYoungStr.isEmpty())
                    affectedYoungStr="0";
                String affectedOldStr =  nAffectedOldTV.getText().toString();
                if(affectedOldStr.isEmpty())
                    affectedOldStr="0";

                int nAffectedBabies = Integer.valueOf(affectedBabiesStr);
                int nAffectedYoung = Integer.valueOf(affectedYoungStr);
                int nAffectedOld = Integer.valueOf(affectedOldStr);

                if(nAffectedBabies ==0 && nAffectedYoung ==0 && nAffectedOld ==0)
                {
                    Toast.makeText(mContext,"Please Fill a Field", Toast.LENGTH_LONG).show();
                    return;
                }


                HealthInterventionForHealthEvent healthInterventionForHealthEvent = new HealthInterventionForHealthEvent();
                healthInterventionForHealthEvent.nBabies = nAffectedBabies;
                healthInterventionForHealthEvent.nYoung = nAffectedYoung;
                healthInterventionForHealthEvent.nOld = nAffectedOld;
                String interventionName = healthInterventionSpinner.getSelectedItem().toString();
                healthInterventionForHealthEvent.healthInterventionID = mHerdDAO.getHealthInterventionIDFromName(interventionName);
                if(vaccinationSpinners.getVisibility()== View.VISIBLE)
                    healthInterventionForHealthEvent.vaccinationName = vaccinationSpinners.getSelectedItem().toString();
                if(comentsSection.getVisibility() == View.VISIBLE)
                {
                    EditText commentsET = view.findViewById(R.id.dialog_health_intervention_comments_editText);
                    if(commentsET.getText().toString().isEmpty())
                    {
                        Toast.makeText(mContext,"Please Add a comment", Toast.LENGTH_LONG).show();
                        return;
                    }
                    healthInterventionForHealthEvent.comments = commentsET.getText().toString();
                }

               if(!mIsEdting) {
                   mFragment.addHealthIntervention(healthInterventionForHealthEvent);
                   if(mIsEditingInReadOnly)
                       HerdVisitManager.getInstance().addHealthInterventionToExistingVisit(getContext(),healthInterventionForHealthEvent,mHerdVisitID);
               }
               else {
                 HealthInterventionForHealthEvent editee=  mFragment.editHealthInterventionForHealthEvent(mPositionToEdit, nAffectedBabies, nAffectedYoung, nAffectedOld);
                   if(mIsEditingInReadOnly)
                       HerdVisitManager.getInstance().editHealthInterventionForExistingVisit(getContext(),editee);
               }
                dismiss();
            }
        });

    }
}
