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
import android.widget.ExpandableListView;

import com.ilri.herdmanager.adapters.HealthEventExpandableListAdapter;
import com.ilri.herdmanager.database.entities.HealthEvent;
import com.ilri.herdmanager.ui.dialogs.NewDiseaseEventDialog;
import com.ilri.herdmanager.ui.dialogs.NewSignEventDialog;
import com.ilri.herdmanager.R;

import java.util.ArrayList;

public class AddHeardHealthEventFragment extends Fragment {

    private AddHeardHealthViewModel mViewModel;
    private ExpandableListView mHealthEventExpandableListView;
    private Button mShowAddSignButton, mShowAddDiseaseButton;

    public static AddHeardHealthEventFragment newInstance() {
        return new AddHeardHealthEventFragment();
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
        mHealthEventExpandableListView.expandGroup(0);
        mHealthEventExpandableListView.expandGroup(1);

        mShowAddDiseaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dialogFragment = new NewDiseaseEventDialog(getContext());
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


                DialogFragment dialogFragment = new NewSignEventDialog(getContext());
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
}