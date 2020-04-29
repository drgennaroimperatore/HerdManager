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
import com.ilri.herdmanager.database.entities.MilkProductionForProductivityEvent;

public class NewProductivityEventMilkProductionDialog extends DialogFragment {

    Context mContext;
    ProductivityEventExpandableListAdapter mAdapter;

   public NewProductivityEventMilkProductionDialog(Context context, ProductivityEventExpandableListAdapter adapter)
    {
        mContext = context;
        mAdapter = adapter;
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

       final EditText lactatingAnimalsET = view.findViewById(R.id.editText_milk_production_productivity_lactating_animals);
       lactatingAnimalsET.setHint("0");
       final EditText litresPerDayET = view.findViewById(R.id.editText_milk_production_productivity_litres_day);
       litresPerDayET.setHint("0");
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
                   int litresPerDay = Integer.valueOf(litresPerDayStr);

                   MilkProductionForProductivityEvent mpe = new MilkProductionForProductivityEvent();
                   mpe.litresOfMilkPerDay= litresPerDay;
                   mpe.numberOfLactatingAnimals= lactatingAnimals;
                   mAdapter.setMilkProduction(mpe);

                   dismiss();

               }


            }
        });
    }
}
