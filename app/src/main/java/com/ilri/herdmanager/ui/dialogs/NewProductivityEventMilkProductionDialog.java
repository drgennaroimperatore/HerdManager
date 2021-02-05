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
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;
import com.ilri.herdmanager.R;
import com.ilri.herdmanager.adapters.ProductivityEventExpandableListAdapter;
import com.ilri.herdmanager.database.entities.HerdVisit;
import com.ilri.herdmanager.database.entities.MilkProductionForProductivityEvent;
import com.ilri.herdmanager.managers.HerdVisitManager;

public class NewProductivityEventMilkProductionDialog extends DialogFragment {

    Context mContext;
    ProductivityEventExpandableListAdapter mAdapter;
    boolean mIsEditingInReadOnly = false;
    int mHerdVisitID =-155;


   public NewProductivityEventMilkProductionDialog(Context context, ProductivityEventExpandableListAdapter adapter,
                                                   int herdVisitID, boolean isEditingInReadOnly)
    {
        mContext = context;
        mAdapter = adapter;
        mHerdVisitID = herdVisitID;
        mIsEditingInReadOnly = isEditingInReadOnly;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.dialog_productivity_event_milk,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MilkProductionForProductivityEvent mpe = mAdapter.getMilkProduction();
        int lactating = mpe.numberOfLactatingAnimals;
        float litres = mpe.litresOfMilkPerDay;

       final EditText lactatingAnimalsET = view.findViewById(R.id.editText_milk_production_productivity_lactating_animals);
       lactatingAnimalsET.setHint("0");
       if(lactating>0)
           lactatingAnimalsET.setText(String.valueOf(lactating));

       final EditText litresPerDayET = view.findViewById(R.id.editText_milk_production_productivity_litres_day);
       litresPerDayET.setHint("0");
        if (litres>0)
            litresPerDayET.setText(String.valueOf(litres));

            final CoordinatorLayout cl = view.findViewById(R.id.dialog_prod_milk_parent);

        Button editMilkButton = view.findViewById(R.id.button_milk_productivity_edit_milk_production);
        editMilkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String lactatingAnimalsStr = lactatingAnimalsET.getText().toString();
               String litresPerDayStr = litresPerDayET.getText().toString();

               if(lactatingAnimalsStr.isEmpty() || litresPerDayStr.isEmpty())
               {
                   Snackbar mySnackbar = Snackbar.make(cl, "Provide Value or 0 for all fields", Snackbar.LENGTH_LONG);
                   mySnackbar.getView().setBackgroundColor(R.color.black);
                   mySnackbar.show();
               }
               else
               {
                   int lactatingAnimals = Integer.valueOf(lactatingAnimalsStr);
                   float litresPerDay = Float.parseFloat(litresPerDayStr);

                   MilkProductionForProductivityEvent mpe = new MilkProductionForProductivityEvent();
                   mpe.litresOfMilkPerDay= litresPerDay;
                   mpe.numberOfLactatingAnimals= lactatingAnimals;
                   mAdapter.setMilkProduction(mpe);

                   if(mIsEditingInReadOnly)
                       HerdVisitManager.getInstance().editMilkProductionForExistingProductivityEvent(getContext(),mpe, mHerdVisitID);

                   dismiss();

               }


            }
        });
    }
}
