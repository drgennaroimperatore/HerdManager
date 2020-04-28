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

import com.ilri.herdmanager.adapters.DynamicEventExpandableListAdapter;
import com.ilri.herdmanager.ui.dialogs.NewDynamicEventAnimalDeathDialog;
import com.ilri.herdmanager.ui.dialogs.NewDynamicEventAnimalMovementDialog;
import com.ilri.herdmanager.R;

import java.util.ArrayList;
import java.util.List;

public class AddHerdDynamicFragment extends Fragment {

    private AddHerdDynamicViewModel mViewModel;
    private Button mAddEventButton, mAddDeathButton;
    private ExpandableListView mExpandableListView;

    public static AddHerdDynamicFragment newInstance() {
        return new AddHerdDynamicFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_herd_dynamic_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAddEventButton = view.findViewById(R.id.button_add_herd_dynamic);
        mAddDeathButton = view.findViewById(R.id.button_add_herd_death);


        mExpandableListView = view.findViewById(R.id.dynamic_event_expandable_list_view);
       final DynamicEventExpandableListAdapter adapter = new DynamicEventExpandableListAdapter(getContext());
        mExpandableListView.setAdapter(adapter);
        mExpandableListView.expandGroup(0);
        mExpandableListView.expandGroup(1);

        mAddEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new NewDynamicEventAnimalMovementDialog(getContext(), adapter);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                dialogFragment.show(ft, "dialog");

            }
        });

        final AddHerdDynamicFragment fragment = this;

        mAddDeathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> causesOfDeath = new ArrayList<>();
                causesOfDeath.add("Age");
                causesOfDeath.add("Disease");
                causesOfDeath.add("Accident");
                causesOfDeath.add("Unknown");

                DialogFragment dialogFragment = new NewDynamicEventAnimalDeathDialog(getContext(), causesOfDeath, fragment);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddHerdDynamicViewModel.class);
        // TODO: Use the ViewModel
        setRetainInstance(true);
    }

}
