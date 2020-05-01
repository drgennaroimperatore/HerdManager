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
import android.widget.TextView;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.adapters.ProductivityEventExpandableListAdapter;
import com.ilri.herdmanager.classes.ProductivityEventContainer;
import com.ilri.herdmanager.database.entities.BirthsForProductivityEvent;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.database.entities.HerdVisit;
import com.ilri.herdmanager.database.entities.MilkProductionForProductivityEvent;
import com.ilri.herdmanager.database.entities.ProductivityEvent;
import com.ilri.herdmanager.ui.dialogs.NewDynamicEventAnimalMovementDialog;
import com.ilri.herdmanager.ui.dialogs.NewProductivityEventBirthsDialog;
import com.ilri.herdmanager.ui.dialogs.NewProductivityEventMilkProductionDialog;

public class AddHerdProductivityFragment extends Fragment {

    private AddHerdProductivityViewModel mViewModel;
    private ExpandableListView mExpandableListView;
    private ProductivityEventExpandableListAdapter mAdapter;

    public static AddHerdProductivityFragment newInstance() {
        return new AddHerdProductivityFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_herd_productivity_fragment, container, false);
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
        }


        mExpandableListView = view.findViewById(R.id.productivity_event_expandable_list_view);
        mAdapter = new ProductivityEventExpandableListAdapter(getContext());
        mExpandableListView.setAdapter(mAdapter);
        mExpandableListView.expandGroup(0);
        mExpandableListView.expandGroup(1);

        Button editMilkProdutionButton = view.findViewById(R.id.button_add_herd_productivity_milk_production);
        Button editBirthsButton = view.findViewById(R.id.button_add_herd_productivity_births);

        editMilkProdutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dialogFragment = new NewProductivityEventMilkProductionDialog(getContext(), mAdapter);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                dialogFragment.show(ft, "dialog");
            }
        });

        editBirthsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BirthsForProductivityEvent bpe = mAdapter.getBirths();
                int nBirths = bpe.nOfBirths;
                int nGestating = bpe.nOfGestatingAnimals;
                DialogFragment dialogFragment = new NewProductivityEventBirthsDialog(getContext(), mAdapter,nBirths,nGestating);
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


            editBirthsButton.setVisibility(View.INVISIBLE);
            editMilkProdutionButton.setVisibility(View.INVISIBLE);

            ProductivityEvent pv = HerdDatabase.getInstance(getContext()).getHerdDao().getProductivityEventForVisit(herdVisitID).get(0);
            BirthsForProductivityEvent bpe = HerdDatabase.getInstance(getContext()).getHerdDao().getBirthsForProductivityEvent(pv.ID).get(0);
            MilkProductionForProductivityEvent mpe = HerdDatabase.getInstance(getContext()).getHerdDao().getMilkProductionForProductivityEvent(pv.ID).get(0);
            mAdapter.setReadOnly(mpe,bpe);




        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddHerdProductivityViewModel.class);


        // TODO: Use the ViewModel
        setRetainInstance(true);
    }

    public ProductivityEventContainer getProductivityEventForHelthVisit()
    {
        ProductivityEventContainer pce = new ProductivityEventContainer();
        pce.birthsForProductivityEvent = mAdapter.getBirths();
        pce.milkProductionForProductivityEvent = mAdapter.getMilkProduction();
        return pce;
    }
}
