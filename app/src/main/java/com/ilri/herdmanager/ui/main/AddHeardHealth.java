package com.ilri.herdmanager.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.ilri.herdmanager.HealthEventExpandableListAdapter;
import com.ilri.herdmanager.R;

import java.util.ArrayList;

public class AddHeardHealth extends Fragment {

    private AddHeardHealthViewModel mViewModel;
    private ExpandableListView mHealthEventExpandableListView;

    public static AddHeardHealth newInstance() {
        return new AddHeardHealth();
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

        mHealthEventExpandableListView = view.findViewById(R.id.health_event_exapandableListView);
        ArrayList<HealthEvent> healthEvents = new ArrayList<>();
        //healthEvents.add( new HealthEvent());

        HealthEventExpandableListAdapter adapter = new HealthEventExpandableListAdapter(getContext(), healthEvents);
        mHealthEventExpandableListView.setAdapter(adapter);

    }
}
