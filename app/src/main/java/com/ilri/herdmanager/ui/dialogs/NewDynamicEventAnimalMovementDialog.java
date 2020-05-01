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

public class NewDynamicEventAnimalMovementDialog extends DialogFragment {

    Button mEditAnimalMovementButton;
    EditText mAnmialsBoughtBabiesET, mAnimalsBoughtYoungET, mAnimalsBoughtOldET;
    EditText mAnimalsSoldBabiesET, mAnimalsSoldYoungET, mAnimalsSoldOldET;
    EditText mAnimalsLostBabiesET, mAnimalsLostYoungET, mAnimalsLostOldET;
    DynamicEventExpandableListAdapter mAdapter;
    AnimalMovementsForDynamicEvent animalMovementEvent = new AnimalMovementsForDynamicEvent();

    public NewDynamicEventAnimalMovementDialog(Context context, DynamicEventExpandableListAdapter adapter) {

        mAdapter = adapter;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_new_dynamic_event_animal_movement,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mEditAnimalMovementButton = view.findViewById(R.id.button_dialog_dynamic_event_animal_movement_edit_event);

        AnimalMovementsForDynamicEvent animalMovementsForDynamicEvent = mAdapter.getAnimalMovements();
       int soldBabies = animalMovementEvent.soldBabies;
       int soldYoung = animalMovementEvent.soldYoung;
       int soldOld = animalMovementEvent.soldOld;

       int boughtBabies = animalMovementEvent.boughtBabies;
       int boughtYoung = animalMovementEvent.boughtYoung;
       int boughtOld = animalMovementEvent.boughtOld;

       int lostBabies = animalMovementEvent.lostBabies;
       int lostYoung = animalMovementEvent.lostYoung;
       int lostOld = animalMovementEvent.lostOld;


        mAnmialsBoughtBabiesET =view.findViewById(R.id.editText_add_dynamic_event_babies_animals_bought);

        if(boughtBabies>0)
            mAnmialsBoughtBabiesET.setText(String.valueOf(boughtBabies));

        mAnimalsBoughtYoungET =view.findViewById(R.id.editText_add_dynamic_event_young_animals_bought);

        if(boughtYoung>0)
            mAnimalsBoughtYoungET.setText(String.valueOf(boughtYoung));

        mAnimalsBoughtOldET = view.findViewById(R.id.editText_add_dynamic_event_old_animals_bought);

        if(boughtOld>0)
            mAnimalsBoughtOldET.setText(String.valueOf(boughtOld));

        mAnimalsSoldBabiesET = view.findViewById(R.id.editText_add_dynamic_event_babies_animals_sold);
        mAnimalsSoldYoungET = view.findViewById(R.id.editText_add_dynamic_event_young_animals_sold);
        mAnimalsSoldOldET =  view.findViewById(R.id.editText_add_dynamic_event_old_animals_sold);

        mAnimalsLostBabiesET = view.findViewById(R.id.editText_add_dynamic_event_babies_animals_lost);
        mAnimalsLostYoungET = view.findViewById(R.id.editText_add_dynamic_event_young_animals_lost);
        mAnimalsLostOldET = view.findViewById(R.id.editText_add_dynamic_event_old_animals_lost);






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

                dismiss();



            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}
