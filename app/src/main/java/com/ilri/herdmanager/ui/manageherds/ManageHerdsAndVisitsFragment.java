package com.ilri.herdmanager.ui.manageherds;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.adapters.HerdsAndVisitsListExpandableListAdapter;

public class ManageHerdsAndVisitsFragment extends Fragment {

    private ManageHerdsAndVisitsViewModel manageHerdsAndVisitsViewModel;
    private ExpandableListView mManageHerdsAndVisitsExpandableListView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        manageHerdsAndVisitsViewModel =
                ViewModelProviders.of(this).get(ManageHerdsAndVisitsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_manage_herds_and_visits, container, false);
        //final TextView textView = root.findViewById(R.id.text_dashboard);
       /* manageHerdsAndVisitsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mManageHerdsAndVisitsExpandableListView = view.findViewById(R.id.herds_and_visits_expandableListView);
        HerdsAndVisitsListExpandableListAdapter adapter = new HerdsAndVisitsListExpandableListAdapter(getContext());
        mManageHerdsAndVisitsExpandableListView.setAdapter(adapter);
    }
}