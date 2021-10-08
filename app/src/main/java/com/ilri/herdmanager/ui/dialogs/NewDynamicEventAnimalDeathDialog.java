package com.ilri.herdmanager.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;
import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.DeathsForDynamicEvent;
import com.ilri.herdmanager.managers.HerdVisitManager;
import com.ilri.herdmanager.ui.main.AddHerdDynamicFragment;

import java.util.List;

public class NewDynamicEventAnimalDeathDialog extends DialogFragment {

   private List<String> mCausesOfDeath;
   private AddHerdDynamicFragment mFragment;
   private Context mContext;
   private Integer nDeadBabies, nDeadYoung, nDeadOld;
   private Integer mPositionToEdit;
   private boolean isEditing = false;
   private boolean mIsEditingInReadOnly = false;
   private int mHerdVisitID = -155;
   private DeathsForDynamicEvent mDdeToEdit;

    public NewDynamicEventAnimalDeathDialog(Context context, List<String> causesOfDeath,
                                            AddHerdDynamicFragment f,
                                            boolean isEditingInReadOnly, int herdVisitID)
    {
        mContext = context;
        mCausesOfDeath = causesOfDeath;
        mFragment = f;
        nDeadBabies= null;
        nDeadYoung= null;
        nDeadOld = null;
        mIsEditingInReadOnly = isEditingInReadOnly;
        mHerdVisitID = herdVisitID;
    }

    public NewDynamicEventAnimalDeathDialog(Context context,List<String> causesOfDeath,
                                            int pos, DeathsForDynamicEvent dde,
                                            AddHerdDynamicFragment f, boolean isEditingInReadOnly,
                                            int herdVisitID )
    {
        mContext = context;
        mCausesOfDeath = causesOfDeath;
        mFragment = f;
        nDeadBabies = dde.deadBabies;
        nDeadYoung = dde.deadYoung;
        nDeadOld = dde.deadOld;
        mPositionToEdit= pos;
        isEditing=true;
        mIsEditingInReadOnly = isEditingInReadOnly;
        mHerdVisitID = herdVisitID;
        mDdeToEdit = dde;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.dialog_new_dynamic_event_animal_death,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final Spinner causesOfDeathSpinner = view.findViewById(R.id.dynamic_event_death_spinner);
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(mContext,R.layout.health_event_spinner_item,mCausesOfDeath);
        causesOfDeathSpinner.setAdapter(spinnerAdapter);

        final EditText deadBabiesET, deadYoungET, deadOldET;

        deadBabiesET = view.findViewById(R.id.editText_death_dynamic_event_baby);
        deadYoungET = view.findViewById(R.id.editText_death_dynamic_event_young);
        deadOldET = view.findViewById(R.id.editText_death_dynamic_event_old);

        deadBabiesET.setHint("0");
        deadYoungET.setHint("0");
        deadOldET.setHint("0");

        if(nDeadBabies!=null && nDeadYoung!=null && nDeadOld!=null)
        {
            deadBabiesET.setText(String.valueOf(nDeadBabies));
            deadYoungET.setText(String.valueOf(nDeadYoung));
            deadOldET.setText(String.valueOf(nDeadOld));
        }

        Button addDeathButton = view.findViewById(R.id.button_death_dynamic_add_death);

        if(isEditing) {
            causesOfDeathSpinner.setVisibility(View.INVISIBLE);
            addDeathButton.setText("Edit Death");

            TextView heading = view.findViewById(R.id.dialog_new_dynamic_event_animal_death_title_textView);
            heading.setText("Edit "+ mFragment.getDeathCauseName(mPositionToEdit));

            Button deleteDeathButton = view.findViewById(R.id.button_death_dynamic_delete_death);
            deleteDeathButton.setVisibility(View.VISIBLE);
            deleteDeathButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragment.deleteDeath(mPositionToEdit);
                    dismiss();
                }
            });
        }


        addDeathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* */

                if(deadBabiesET.getText().toString().isEmpty())
                    deadBabiesET.setText("0");
                if(deadYoungET.getText().toString().isEmpty())
                    deadYoungET.setText("0");
                if(deadOldET.getText().toString().isEmpty())
                    deadOldET.setText("0");

                int nAffectedBabies = Integer.valueOf(deadBabiesET.getText().toString());
                int nAffectedYoung = Integer.valueOf(deadYoungET.getText().toString());
                int nAffectedOld = Integer.valueOf(deadOldET.getText().toString());



                if(nAffectedBabies ==0 && nAffectedYoung ==0 && nAffectedOld ==0)
                {
                    Toast.makeText(mContext,"Please Fill a Field", Toast.LENGTH_LONG).show();
                }
                else
                {
                    DeathsForDynamicEvent dde = new DeathsForDynamicEvent();

                    dde.causeOfDeath = causesOfDeathSpinner.getSelectedItem().toString();
                    dde.deadBabies = nAffectedBabies;
                    dde.deadYoung = nAffectedYoung;
                    dde.deadOld = nAffectedOld;

                    if(isEditing) {
                        mFragment.editDeath(mPositionToEdit,nAffectedBabies,nAffectedYoung,nAffectedOld);
                        if(mIsEditingInReadOnly) {
                            dde.ID = mDdeToEdit.ID;
                            dde.dynamicEventID = mDdeToEdit.dynamicEventID;
                            dde.serverID = mDdeToEdit.serverID;
                            dde.syncStatus = mDdeToEdit.syncStatus;
                            HerdVisitManager.getInstance().editDeathForExistingDynamicEvent(getContext(), dde);
                        }
                        dismiss();
                    }
                    else {

                        if (mFragment.addDeath(dde)) {
                            Toast.makeText(mContext,"Cause of death was already inserted", Toast.LENGTH_LONG).show();
                        } else {
                            if(mIsEditingInReadOnly) {
                                dde.ID = HerdVisitManager.getInstance().addDeathToExistingDynamicEvent(getContext(), dde, mHerdVisitID);

                            }
                            dismiss();
                        }
                    }


                }
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}
