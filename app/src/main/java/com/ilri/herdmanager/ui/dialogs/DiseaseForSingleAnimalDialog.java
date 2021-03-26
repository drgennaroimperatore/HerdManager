package com.ilri.herdmanager.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;
import com.ilri.herdmanager.R;
import com.ilri.herdmanager.adapters.DiseaseForSingleAnimalSignsDialogAdapter;
import com.ilri.herdmanager.database.entities.ADDB;
import com.ilri.herdmanager.database.entities.ADDBDAO;
import com.ilri.herdmanager.database.entities.DiseasesForHealthEvent;
import com.ilri.herdmanager.database.entities.SignsForDiseasesForHealthEvent;
import com.ilri.herdmanager.ui.main.AddHeardHealthEventFragment;

import java.util.ArrayList;
import java.util.List;

public class DiseaseForSingleAnimalDialog extends DialogFragment {

    Context mContext;
    List<SignsForDiseasesForHealthEvent> mSignsForDiseaseForHealthEvent;

    Button mButtonAddDiseaseToHealthEvent;
    AddHeardHealthEventFragment mFragment;
    ADDBDAO addbdao = null;

    Integer mPositionToEdit;
    boolean isEditing = false;


    public DiseaseForSingleAnimalDialog(Context context,
                                        List<SignsForDiseasesForHealthEvent> signsForDiseasesForHealthEvents,
                                        AddHeardHealthEventFragment fragment)
    {
        mContext = context;
        mFragment = fragment;
        mSignsForDiseaseForHealthEvent = signsForDiseasesForHealthEvents;
        addbdao = ADDB.getInstance(context).getADDBDAO();
       /* nAffectedBabies = null;
        nAffectedYoung= null;
        nAffectedOld = null;*/
        mPositionToEdit = null;

    }

   /* public DiseaseForSingleAnimalDialog(Context context, List<String> diseases,
                                        int pos,
                                        int affectedBabies,
                                        int affectedYoung,
                                        int affectedOld,
                                        AddHeardHealthEventFragment fragment)
    {
        mContext = context;
        mDiseases = diseases;
        mFragment = fragment;
        addbdao = ADDB.getInstance(context).getADDBDAO();
        nAffectedBabies = affectedBabies;
        nAffectedYoung= affectedYoung;
        nAffectedOld = affectedOld;
        isEditing = true;
        mPositionToEdit = pos;
    }*/



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

        ListView signsListView = view.findViewById(R.id.dialog_disease_list_of_signs);
        DiseaseForSingleAnimalSignsDialogAdapter adapter = new DiseaseForSingleAnimalSignsDialogAdapter(mContext,0,
                (ArrayList<SignsForDiseasesForHealthEvent>) mSignsForDiseaseForHealthEvent);
        signsListView.setAdapter(adapter);

      if(isEditing) {



      }



        mButtonAddDiseaseToHealthEvent = view.findViewById(R.id.button_disease_health_add_disease);

        if(isEditing)
        {
            mButtonAddDiseaseToHealthEvent.setText("Edit Dis");

            Button deleteDiseaseButton = view.findViewById(R.id.button_disease_health_delete_disease);
            deleteDiseaseButton.setVisibility(View.VISIBLE);

            deleteDiseaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragment.deleteDisease(mPositionToEdit);
                    dismiss();
                }
            });

        }

        mButtonAddDiseaseToHealthEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (isEditing) {

                       dismiss();

                    }
                    else { }

            }
        });

      //  String[] dummyDiseases = {"Disease 1", "Disease 2", "Disease 3"};

    }
}
