package com.ilri.herdmanager.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ilri.herdmanager.R;
import com.ilri.herdmanager.ui.dialogs.NewFarmerDialog;
import com.ilri.herdmanager.ui.main.AddHerdVisitActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

public class NewCaseActivity extends AppCompatActivity {

    Button mUseNewFarmer, mUseExistingFarmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_case);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUseNewFarmer = findViewById(R.id.add_new_farmer_button);

        mUseNewFarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewFarmerDialog dialog = new NewFarmerDialog(NewCaseActivity.this);
                dialog.show();

        }
        });

        mUseExistingFarmer= findViewById(R.id.use_existing_farmer_button);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                Intent goToAddHerdVisitActivity = new Intent(getApplicationContext(), AddHerdVisitActivity.class);
                startActivity(goToAddHerdVisitActivity);
            }
        });
    }
}
