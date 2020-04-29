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
import com.ilri.herdmanager.database.entities.BirthsForProductivityEvent;
import com.ilri.herdmanager.database.entities.ProductivityEvent;

public class NewProductivityEventBirthsDialog extends DialogFragment {

    Context mContext;
    ProductivityEventExpandableListAdapter mAdapter;

    public NewProductivityEventBirthsDialog(Context context, ProductivityEventExpandableListAdapter adapter) {
        super();
        mContext= context;
        mAdapter= adapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.dialog_productivity_event_births, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final CoordinatorLayout cl = view.findViewById(R.id.dialog_prod_births_parent);
        final EditText gestatingAnimalsET = view.findViewById(R.id.editText_births_productivity_event_gestating_animals);
        final EditText birthsET = view.findViewById(R.id.editText_births_production_productivity_births);



        Button editBirthsButton = view.findViewById(R.id.button_births_productivity_edit_births);
        editBirthsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String gestatingAnimalsStr = gestatingAnimalsET.getText().toString();
                String birthsStr = birthsET.getText().toString();

                if(gestatingAnimalsStr.isEmpty() || birthsStr.isEmpty())
                {
                    Snackbar mySnackbar = Snackbar.make(cl, "Please fill or add 0 to all fields", Snackbar.LENGTH_LONG);
                    mySnackbar.getView().setBackgroundColor(R.color.black);
                    mySnackbar.show();

                }
                else
                {
                    int births = Integer.valueOf(birthsStr);
                    int gestatingAnimals = Integer.valueOf(gestatingAnimalsStr);

                    BirthsForProductivityEvent bpe = new BirthsForProductivityEvent();
                    bpe.nOfBirths = births;
                    bpe.nOfGestatingAnimals = gestatingAnimals;
                    mAdapter.setBirths(bpe);
                    dismiss();
                }

            }
        });



    }
}
