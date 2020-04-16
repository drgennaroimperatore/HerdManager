package com.ilri.herdmanager.ui.main;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.ilri.herdmanager.adapters.HealthEventExpandableListAdapter;
import com.ilri.herdmanager.database.entities.ADDB;
import com.ilri.herdmanager.database.entities.Diseases;
import com.ilri.herdmanager.database.entities.DiseasesForHealthEvent;
import com.ilri.herdmanager.database.entities.HealthEvent;
import com.ilri.herdmanager.database.entities.Herd;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.database.entities.Signs;
import com.ilri.herdmanager.database.entities.SignsForHealthEvent;
import com.ilri.herdmanager.managers.HerdManager;
import com.ilri.herdmanager.ui.dialogs.NewDiseaseEventDialog;
import com.ilri.herdmanager.ui.dialogs.NewSignEventDialog;
import com.ilri.herdmanager.R;

import java.util.ArrayList;
import java.util.List;

public class AddHeardHealthEventFragment extends Fragment {

    private AddHeardHealthViewModel mViewModel;
    private ExpandableListView mHealthEventExpandableListView;
    private Button mShowAddSignButton, mShowAddDiseaseButton;
    private int mHerdID = -155;
    private HealthEventExpandableListAdapter mAdapter;

    public static AddHeardHealthEventFragment newInstance() {
        return new AddHeardHealthEventFragment();
    }

    public AddHeardHealthEventFragment() {}

    public AddHeardHealthEventFragment(int herdID)
    {
        mHerdID= herdID;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.add_heard_health_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddHeardHealthViewModel.class);
        // TODO: Use the ViewModel

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mShowAddDiseaseButton = view.findViewById(R.id.health_event_show_disease_dialog);
        mShowAddSignButton = view.findViewById(R.id.health_event_show_sign_dialog);
        mHealthEventExpandableListView = view.findViewById(R.id.health_event_exapandableListView);
        ArrayList<HealthEvent> healthEvents = new ArrayList<>();
        //healthEvents.add( new HealthEvent());

        HealthEventExpandableListAdapter adapter = new HealthEventExpandableListAdapter(getContext(), healthEvents);
        mHealthEventExpandableListView.setAdapter(adapter);
        mAdapter = adapter;
        mHealthEventExpandableListView.expandGroup(0);
        mHealthEventExpandableListView.expandGroup(1);

      final  AddHeardHealthEventFragment f = this;

        mShowAddDiseaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Herd h = HerdDatabase.getInstance(getContext()).getHerdDao().getHerdByID(mHerdID).get(0);
                List<Diseases> diseases = ADDB.getInstance(getContext()).getADDBDAO().getAllDiseasesForAninal(h.speciesID);
                List<String> diseaseNames = new ArrayList<>();

                for(Diseases d: diseases)
                    diseaseNames.add(d.Name);

                DialogFragment dialogFragment = new NewDiseaseEventDialog(getContext(),diseaseNames,f );
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                dialogFragment.show(ft, "dialog");

            }
        });

        mShowAddSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Herd h = HerdDatabase.getInstance(getContext()).getHerdDao().getHerdByID(mHerdID).get(0);
                List<Signs> signs=  ADDB.getInstance(getContext()).getADDBDAO().getAllSignsForAnimal(h.speciesID);
                List<String> sNames = new ArrayList<>();

                for(Signs s: signs)
                    sNames.add(s.Name);

                DialogFragment dialogFragment = new NewSignEventDialog(getContext(), sNames);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                dialogFragment.show(ft, "dialog");


            }
        });



    }

    public void addDiseaseToList(DiseasesForHealthEvent dhe)
    {
       mAdapter.addNewDisease(dhe);
    }

    public void addSignToList(SignsForHealthEvent she)
    {
        mAdapter.addNewSign(she);
    }
}
