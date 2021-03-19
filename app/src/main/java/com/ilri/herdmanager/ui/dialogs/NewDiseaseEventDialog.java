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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;
import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.ADDB;
import com.ilri.herdmanager.database.entities.ADDBDAO;
import com.ilri.herdmanager.database.entities.DiseasesForHealthEvent;
import com.ilri.herdmanager.ui.main.AddHeardHealthEventFragment;

import java.util.List;

public class NewDiseaseEventDialog extends DialogFragment {

    Context mContext;
    List<String> mDiseases;
    EditText mEditTextAffectedBabies, mEditTextAffectedYoung, mEditTextAffectedOld;
    Button mButtonAddDiseaseToHealthEvent;
    AddHeardHealthEventFragment mFragment;
    ADDBDAO addbdao = null;
    Integer nAffectedBabies, nAffectedYoung, nAffectedOld;
    Integer mPositionToEdit;
    boolean isEditing = false;


    public NewDiseaseEventDialog(Context context, List<String> diseases, AddHeardHealthEventFragment fragment)
    {
        mContext = context;
        mDiseases = diseases;
        mFragment = fragment;
        addbdao = ADDB.getInstance(context).getADDBDAO();
        nAffectedBabies = null;
        nAffectedYoung= null;
        nAffectedOld = null;
        mPositionToEdit = null;

    }

    public NewDiseaseEventDialog(Context context, List<String> diseases,
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

      final  Spinner diseaseSpinner = view.findViewById(R.id.health_event_disease_spinner);

      if(isEditing) {
          diseaseSpinner.setVisibility(View.GONE);
          TextView header = view.findViewById(R.id.dialog_new_health_event_disease_dialog_title_textview);
          header.setText("Edit " + mFragment.getDiseaseName(mPositionToEdit));
      }

       final CoordinatorLayout cl = view.findViewById(R.id.dialog_disease_parent);
        mEditTextAffectedBabies = view.findViewById(R.id.editText_disease_health_event_baby);
        mEditTextAffectedBabies.setHint("0");
        mEditTextAffectedYoung = view.findViewById(R.id.editText_disease_health_event_young);
        mEditTextAffectedYoung.setHint("0");
        mEditTextAffectedOld = view.findViewById(R.id.editText_disease_health_event_old);
        mEditTextAffectedOld.setHint("0");

        if(nAffectedOld!=null && nAffectedYoung!=null && nAffectedOld!=null) // if we get here
        {
            mEditTextAffectedOld.setText(String.valueOf(nAffectedOld));
            mEditTextAffectedYoung.setText(String.valueOf(nAffectedYoung));
            mEditTextAffectedBabies.setText(String.valueOf(nAffectedBabies));
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

                DiseasesForHealthEvent dhe = new DiseasesForHealthEvent();
                String diseaseName = diseaseSpinner.getSelectedItem().toString();
                int diseaseID = addbdao.getDiseaseIDFromName(diseaseName).get(0);

                if(mEditTextAffectedOld.getText().toString().isEmpty())
                    mEditTextAffectedOld.setText("0");
                if(mEditTextAffectedYoung.getText().toString().isEmpty())
                    mEditTextAffectedYoung.setText("0");
                if(mEditTextAffectedBabies.getText().toString().isEmpty())
                    mEditTextAffectedBabies.setText("0");

                int nAffectedBabies = Integer.valueOf( mEditTextAffectedBabies.getText().toString());
                int nAffectedYoung =Integer.valueOf( mEditTextAffectedYoung.getText().toString());
                int nAffectedOld = Integer.valueOf( mEditTextAffectedOld.getText().toString());

                if(nAffectedBabies==0 && nAffectedYoung==0 && nAffectedOld==0)
                {
                    Snackbar mySnackbar = Snackbar.make(cl, "No Animal was Affected", Snackbar.LENGTH_LONG);
                    mySnackbar.getView().setBackgroundColor(R.color.black);
                    mySnackbar.show();
                }
                else {

                    dhe.diseaseID = diseaseID;
                    dhe.numberOfAffectedBabies = nAffectedBabies;
                    dhe.numberOfAffectedYoung = nAffectedYoung;
                    dhe.numberOfAffectedOld = nAffectedOld;

                    if (isEditing) {

                        mFragment.editDisease(mPositionToEdit, nAffectedBabies,nAffectedYoung,nAffectedOld);
                        dismiss();

                    } else {

                        if (mFragment.addDiseaseToList(dhe)) {
                            Snackbar mySnackbar = Snackbar.make(cl, "This disease was already inserted", Snackbar.LENGTH_LONG);
                            mySnackbar.getView().setBackgroundColor(R.color.black);
                            mySnackbar.show();
                        } else {
                            mFragment.expandList(0);
                            dismiss();
                        }
                    }
                }

            }
        });

      //  String[] dummyDiseases = {"Disease 1", "Disease 2", "Disease 3"};
        ArrayAdapter<String> signSpinnerAdapter = new ArrayAdapter(mContext,R.layout.health_event_spinner_item, mDiseases);
        diseaseSpinner.setAdapter(signSpinnerAdapter);



    }
}
