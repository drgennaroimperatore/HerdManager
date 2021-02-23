package com.ilri.herdmanager.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.adapters.DynamicEventExpandableListAdapter;
import com.ilri.herdmanager.database.entities.AnimalMovementsForDynamicEvent;
import com.ilri.herdmanager.managers.HerdVisitManager;

public class NewDynamicEventAnimalMovementDialog extends DialogFragment {

    Button mEditAnimalMovementButton;
    EditText mAnmialsBoughtBabiesET, mAnimalsBoughtYoungET, mAnimalsBoughtOldET;
    EditText mAnimalsSoldBabiesET, mAnimalsSoldYoungET, mAnimalsSoldOldET;
    EditText mAnimalsLostBabiesET, mAnimalsLostYoungET, mAnimalsLostOldET;
    DynamicEventExpandableListAdapter mAdapter;
    AnimalMovementsForDynamicEvent animalMovementEvent = new AnimalMovementsForDynamicEvent();
    boolean mIsEditingInReadOnly = false;

    public NewDynamicEventAnimalMovementDialog(Context context,
                                               DynamicEventExpandableListAdapter adapter,
                                               boolean isEditingInReadOnly) {

        mAdapter = adapter;
        mIsEditingInReadOnly = isEditingInReadOnly;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_new_dynamic_event_animal_movement,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mEditAnimalMovementButton = view.findViewById(R.id.button_dialog_dynamic_event_animal_movement_edit_event);

        mAnmialsBoughtBabiesET =view.findViewById(R.id.editText_add_dynamic_event_babies_animals_bought);
        mAnimalsBoughtYoungET =view.findViewById(R.id.editText_add_dynamic_event_young_animals_bought);
        mAnimalsBoughtOldET = view.findViewById(R.id.editText_add_dynamic_event_old_animals_bought);
        mAnimalsSoldBabiesET = view.findViewById(R.id.editText_add_dynamic_event_babies_animals_sold);
        mAnimalsSoldYoungET = view.findViewById(R.id.editText_add_dynamic_event_young_animals_sold);
        mAnimalsSoldOldET =  view.findViewById(R.id.editText_add_dynamic_event_old_animals_sold);
        mAnimalsLostBabiesET = view.findViewById(R.id.editText_add_dynamic_event_babies_animals_lost);
        mAnimalsLostYoungET = view.findViewById(R.id.editText_add_dynamic_event_young_animals_lost);
        mAnimalsLostOldET = view.findViewById(R.id.editText_add_dynamic_event_old_animals_lost);

        final AnimalMovementsForDynamicEvent animalMovementsForDynamicEvent = mAdapter.getAnimalMovements();
        int soldBabies = animalMovementsForDynamicEvent.soldBabies;
        int soldYoung = animalMovementsForDynamicEvent.soldYoung;
        int soldOld = animalMovementsForDynamicEvent.soldOld;

        int boughtBabies = animalMovementsForDynamicEvent.boughtBabies;
        int boughtYoung = animalMovementsForDynamicEvent.boughtYoung;
        int boughtOld = animalMovementsForDynamicEvent.boughtOld;

        int lostBabies = animalMovementsForDynamicEvent.lostBabies;
        int lostYoung = animalMovementsForDynamicEvent.lostYoung;
        int lostOld = animalMovementsForDynamicEvent.lostOld;

        if(boughtBabies>0)
            mAnmialsBoughtBabiesET.setText(String.valueOf(boughtBabies));
        if(boughtYoung>0)
            mAnimalsBoughtYoungET.setText(String.valueOf(boughtYoung));
        if(boughtOld>0)
            mAnimalsBoughtOldET.setText(String.valueOf(boughtOld));
        if(soldBabies>0)
            mAnimalsSoldBabiesET.setText(String.valueOf(soldBabies));
        if(soldYoung>0)
            mAnimalsSoldYoungET.setText(String.valueOf(soldYoung));
        if(soldOld>0)
            mAnimalsSoldOldET.setText(String.valueOf(soldOld));
        if(lostBabies>0)
            mAnimalsLostBabiesET.setText(String.valueOf(lostBabies));
        if(lostYoung>0)
            mAnimalsLostYoungET.setText(String.valueOf(lostYoung));
        if(lostOld>0)
            mAnimalsLostOldET.setText(String.valueOf(lostOld));


        mEditAnimalMovementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if( mAnmialsBoughtBabiesET.getText().toString().isEmpty())
                    mAnmialsBoughtBabiesET.setText("0");
                if (mAnimalsBoughtYoungET.getText().toString().isEmpty())
                    mAnimalsBoughtYoungET.setText("0");
                if(mAnimalsBoughtOldET.getText().toString().isEmpty())
                    mAnimalsBoughtOldET.setText("0");

                if( mAnimalsSoldBabiesET.getText().toString().isEmpty())
                    mAnimalsSoldBabiesET.setText("0");
                if (mAnimalsSoldYoungET.getText().toString().isEmpty())
                    mAnimalsSoldYoungET.setText("0");
                if(mAnimalsSoldOldET.getText().toString().isEmpty())
                    mAnimalsSoldOldET.setText("0");

                if( mAnimalsLostBabiesET.getText().toString().isEmpty())
                    mAnimalsLostBabiesET.setText("0");
                if (mAnimalsLostYoungET.getText().toString().isEmpty())
                    mAnimalsLostYoungET.setText("0");
                if(mAnimalsLostOldET.getText().toString().isEmpty())
                    mAnimalsLostOldET.setText("0");


                animalMovementEvent.boughtBabies = Integer.valueOf( mAnmialsBoughtBabiesET.getText().toString());
                animalMovementEvent.boughtYoung = Integer.valueOf( mAnimalsBoughtYoungET.getText().toString());
                animalMovementEvent.boughtOld = Integer.valueOf( mAnimalsBoughtOldET.getText().toString());

                animalMovementEvent.soldBabies = Integer.valueOf( mAnimalsSoldBabiesET.getText().toString());
                animalMovementEvent.soldYoung = Integer.valueOf( mAnimalsSoldYoungET.getText().toString());
                animalMovementEvent.soldOld = Integer.valueOf( mAnimalsSoldOldET.getText().toString());

                animalMovementEvent.lostBabies = Integer.valueOf( mAnimalsLostBabiesET.getText().toString());
                animalMovementEvent.lostYoung = Integer.valueOf( mAnimalsLostYoungET.getText().toString());
                animalMovementEvent.lostOld = Integer.valueOf( mAnimalsLostOldET.getText().toString());


                mAdapter.editAnimalMovements(animalMovementEvent);

                if(mIsEditingInReadOnly) {
                    animalMovementEvent.ID = animalMovementsForDynamicEvent.ID;
                    animalMovementEvent.serverID = animalMovementsForDynamicEvent.serverID;
                    animalMovementEvent.dynamicEventID = animalMovementsForDynamicEvent.dynamicEventID;
                    animalMovementEvent.syncStatus = animalMovementsForDynamicEvent.syncStatus;
                    HerdVisitManager.getInstance().editAnimalMovementsForExistingDynamicEvent
                            (getContext(), animalMovementEvent);
                }

                dismiss();



            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}
