package com.ilri.herdmanager.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.classes.DynamicEventContainer;
import com.ilri.herdmanager.classes.HealthEventContainer;
import com.ilri.herdmanager.classes.ProductivityEventContainer;
import com.ilri.herdmanager.database.entities.HerdVisit;
import com.ilri.herdmanager.managers.HerdVisitManager;
import com.ilri.herdmanager.ui.MainActivity;
import com.ilri.herdmanager.ui.dialogs.ConfrimHerdVisitInsertionDialog;
import com.ilri.herdmanager.ui.dialogs.ErrorDialog;
import com.ilri.herdmanager.ui.main.SectionsPagerAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddHerdVisitActivity extends AppCompatActivity {

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int herdID = getIntent().getIntExtra("herdID", -155);
        final boolean isRO = getIntent().getBooleanExtra("isReadOnly",false);
        final Date herdVisitDate = (Date)getIntent().getSerializableExtra("herdVisitDate");
        final int herdVisitID = getIntent().getIntExtra("herdVisitID", -145);
        FragmentManager fm = getSupportFragmentManager();
        Bundle readOnlyArguments = null;

        boolean isReadonly = ((isRO) &&  (herdVisitDate!=null) && (herdVisitID!=-155));

        if(isReadonly)
        {
            readOnlyArguments = new Bundle();
            readOnlyArguments.putBoolean("isReadOnly",true);
            readOnlyArguments.putInt("herdVisitID",herdVisitID);

        }

        setContentView(R.layout.activity_add_herd_visit);
        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, fm, herdID, readOnlyArguments);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        if(isReadonly)
        {
            TextView titleTV = findViewById(R.id.add_herd_visit_title);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String selectedDate = sdf.format(herdVisitDate);
            titleTV.setText("Herd Visit of the "+ selectedDate);
            fab.setVisibility(View.GONE); // hide the button if we are in read only mode
        }

        final AddHerdVisitActivity a = this;


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Snackbar.make(view, "Adding visit", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                HealthEventContainer hce = sectionsPagerAdapter.getHealthEventForVisit();
                ProductivityEventContainer pce = sectionsPagerAdapter.getProductivityEventForVisit();
                DynamicEventContainer dce = sectionsPagerAdapter.getDynamicEventForVisit();

                try {

                    ConfrimHerdVisitInsertionDialog confrimHerdVisitInsertionDialog = new ConfrimHerdVisitInsertionDialog(a,a,herdID,hce,pce,dce);
                    confrimHerdVisitInsertionDialog.show();


                } catch (Exception e) {
                 //   ErrorDialog dialog = new ErrorDialog(getApplicationContext(), e.getMessage());
                  //  dialog.show();
                    Log.e("ILRI",e.getMessage());
                }
            }
        });
    }
}