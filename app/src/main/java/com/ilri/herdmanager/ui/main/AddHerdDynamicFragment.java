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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.ilri.herdmanager.adapters.DynamicEventExpandableListAdapter;
import com.ilri.herdmanager.classes.DynamicEventContainer;
import com.ilri.herdmanager.database.entities.AnimalMovementsForDynamicEvent;
import com.ilri.herdmanager.database.entities.DeathsForDynamicEvent;
import com.ilri.herdmanager.database.entities.DynamicEvent;
import com.ilri.herdmanager.database.entities.HealthEvent;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.ui.dialogs.NewDynamicEventAnimalDeathDialog;
import com.ilri.herdmanager.ui.dialogs.NewDynamicEventAnimalMovementDialog;
import com.ilri.herdmanager.R;

import java.util.ArrayList;
import java.util.List;

public class AddHerdDynamicFragment extends Fragment {

    private AddHerdDynamicViewModel mViewModel;
    private Button mAddEventButton, mAddDeathButton;
    private ExpandableListView mExpandableListView;
     DynamicEventExpandableListAdapter mAdapter = null;
    private boolean mEditableInReadOnly;
    private int mHerdVisitID = -155;


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

        Bundle args = getArguments();
        boolean isReadOnly= false;
        int herdVisitID = -145;
        if(args!=null) {
            herdVisitID = args.getInt("herdVisitID", -145);
            boolean isRO = args.getBoolean("isReadOnly", false);
            isReadOnly = ((isRO) && (herdVisitID!=-145));
            mHerdVisitID = herdVisitID;

        }

        final int hvID = herdVisitID;
        mAddEventButton = view.findViewById(R.id.button_add_herd_dynamic);
        mAddDeathButton = view.findViewById(R.id.button_add_herd_death);


        mExpandableListView = view.findViewById(R.id.dynamic_event_expandable_list_view);
        mAdapter = new DynamicEventExpandableListAdapter(getContext());
        mExpandableListView.setAdapter(mAdapter);
        mExpandableListView.expandGroup(0);
        mExpandableListView.expandGroup(1);

        mAddEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new NewDynamicEventAnimalMovementDialog(getContext(), mAdapter, mEditableInReadOnly);
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

                DialogFragment dialogFragment = new NewDynamicEventAnimalDeathDialog(getContext(), causesOfDeath, fragment, mEditableInReadOnly, hvID);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                dialogFragment.show(ft, "dialog");

            }
        });

        if(isReadOnly)
        {

            mAddDeathButton.setVisibility(View.INVISIBLE);
            mAddEventButton.setVisibility(View.INVISIBLE);

            DynamicEvent de = HerdDatabase.getInstance(getContext()).getHerdDao().getDynamicEventForVisit(herdVisitID).get(0);
            AnimalMovementsForDynamicEvent movements = HerdDatabase.getInstance(getContext()).getHerdDao().getAnimalMovementsForDynamicEvent(de.ID).get(0);
            List<DeathsForDynamicEvent> deaths = HerdDatabase.getInstance(getContext()).getHerdDao().getDeathsForDynamicEvent(de.ID);
            mAdapter.setReadOnlyData(movements,deaths);
        }
        else
        {
            mExpandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                        int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                        int childPosition = ExpandableListView.getPackedPositionChild(id);

                        if (groupPosition == 1) // deaths
                        {
                            DeathsForDynamicEvent dde = mAdapter.getDeathForDynamicEvent(childPosition);
                            List<String> causesOfDeath = new ArrayList<>();
                            causesOfDeath.add("Age");
                            causesOfDeath.add("Disease");
                            causesOfDeath.add("Accident");
                            causesOfDeath.add("Unknown");

                            DialogFragment dialogFragment = new NewDynamicEventAnimalDeathDialog(getContext(), causesOfDeath, childPosition,
                                  dde, fragment, mEditableInReadOnly, hvID);
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                            if (prev != null) {
                                ft.remove(prev);
                            }
                            ft.addToBackStack(null);

                            dialogFragment.show(ft, "dialog");
                        }


                        return true;
                    }

                    return false;
                }
            });
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddHerdDynamicViewModel.class);
        // TODO: Use the ViewModel
        setRetainInstance(true);
    }

    public boolean addDeath(DeathsForDynamicEvent dde)
    {
        return mAdapter.addDeath(dde);

    }

    public void editDeath(int pos, int b, int y, int o)
    {
        mAdapter.editDeath(pos, b,y,o);
    }

    public void deleteDeath(int pos)
    {
        mAdapter.deleteDeath(pos);
    }

    public DeathsForDynamicEvent getDeath(int pos)
    {
       return mAdapter.getDeathForDynamicEvent(pos);
    }

    public String getDeathCauseName(int pos)
    {
       return mAdapter.getDeathForDynamicEvent(pos).causeOfDeath;
    }

    public DynamicEventContainer getDynamicEventForHealthVisit()
    {
        DynamicEventContainer dce = new DynamicEventContainer();
        dce.mDeaths = mAdapter.getDeathsForDynamicEvent();
        dce.mMovements = mAdapter.getAnimalMovements();
        return dce;
    }


    public void setEditableInReadOnly(boolean editable)
    {
        mEditableInReadOnly = editable;

        if(mEditableInReadOnly)
        {
         mAddEventButton.setVisibility(View.VISIBLE);
         mAddDeathButton.setVisibility(View.VISIBLE);

         final AddHerdDynamicFragment fragment = this;

            mExpandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                        int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                        int childPosition = ExpandableListView.getPackedPositionChild(id);

                        if (groupPosition == 1) // deaths
                        {
                            DeathsForDynamicEvent dde = mAdapter.getDeathForDynamicEvent(childPosition);
                            List<String> causesOfDeath = new ArrayList<>();
                            causesOfDeath.add("Age");
                            causesOfDeath.add("Disease");
                            causesOfDeath.add("Accident");
                            causesOfDeath.add("Unknown");

                            DialogFragment dialogFragment = new NewDynamicEventAnimalDeathDialog(getContext(), causesOfDeath, childPosition,
                                   dde, fragment, mEditableInReadOnly, mHerdVisitID);
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                            if (prev != null) {
                                ft.remove(prev);
                            }
                            ft.addToBackStack(null);

                            dialogFragment.show(ft, "dialog");
                        }



                        return true;
                    }

                    return false;
                }
            });

        }
        else
        {
            mAddEventButton.setVisibility(View.GONE);
            mAddDeathButton.setVisibility(View.GONE);

        }
    }
}
