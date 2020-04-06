package com.ilri.herdmanager.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.Herd;
import com.ilri.herdmanager.database.entities.HerdDao;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.ui.dialogs.NewCaseConfirmationDialog;
import com.ilri.herdmanager.ui.dialogs.NewFarmerDialog;
import com.ilri.herdmanager.ui.main.AddHerdVisitActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Date;

public class NewCaseActivity extends AppCompatActivity {

    Button mUseNewFarmer, mUseExistingFarmer;
    RadioGroup mSpeciesRadioGroup;
    TextView mHerdSizeTextView, mHerdSizeDateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_case);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUseNewFarmer = findViewById(R.id.add_new_farmer_button);
        mSpeciesRadioGroup = findViewById(R.id.species_radioGroup);
        mHerdSizeTextView = findViewById(R.id.new_herd_activity_textview_new_herd_size);
        mHerdSizeTextView.setText("0");
        mHerdSizeDateTextView = findViewById(R.id.new_herd_activity_textview_date_of_insertion);
        //mHerdSizeDateTextView.setText();




        mUseNewFarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewFarmerDialog dialog = new NewFarmerDialog(NewCaseActivity.this);
                dialog.show();


        }
        });

        mUseExistingFarmer= findViewById(R.id.use_existing_farmer_button);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Context context = this;
        final NewCaseActivity a = this;



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

               int selectedID = mSpeciesRadioGroup.getCheckedRadioButtonId();
                RadioButton selectedButton = findViewById(selectedID);
                String species = selectedButton.getText().toString();
                int herdSize = Integer.parseInt(mHerdSizeTextView.getText().toString());
                Date dateOfInsertion = new Date();
                NewCaseConfirmationDialog confirmationDialog = new NewCaseConfirmationDialog(context,a, species,herdSize,dateOfInsertion,"fname","sname");
                confirmationDialog.show();



            }
        });
    }

    public void goToEvents()
    {
        Intent goToAddHerdVisitActivity = new Intent(getApplicationContext(), AddHerdVisitActivity.class);
        startActivity(goToAddHerdVisitActivity);



    }
}
