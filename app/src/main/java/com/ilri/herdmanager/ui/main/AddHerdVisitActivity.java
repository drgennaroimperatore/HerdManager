package com.ilri.herdmanager.ui.main;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.classes.DynamicEventContainer;
import com.ilri.herdmanager.classes.HealthEventContainer;
import com.ilri.herdmanager.classes.ProductivityEventContainer;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.database.entities.HerdVisit;
import com.ilri.herdmanager.managers.HerdVisitManager;
import com.ilri.herdmanager.ui.MainActivity;
import com.ilri.herdmanager.ui.dialogs.ConfrimHerdVisitInsertionDialog;
import com.ilri.herdmanager.ui.dialogs.ErrorDialog;
import com.ilri.herdmanager.ui.dialogs.HerdVisitCommentsDialog;
import com.ilri.herdmanager.ui.dialogs.HerdVisitInfoDialog;
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

        final boolean isReadonly = ((isRO) &&  (herdVisitDate!=null) && (herdVisitID!=-155));

        if(isReadonly)
        {
            readOnlyArguments = new Bundle();
            readOnlyArguments.putInt("herdID",herdID);
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
        Switch editModeSwitch = findViewById(R.id.add_herd_visit_edit_switch);

        editModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                sectionsPagerAdapter.setEditableinReadOnly(isChecked);
            }
        });

        if(isReadonly)
        {
            editModeSwitch.setVisibility(View.VISIBLE);
            editModeSwitch.setTextOff("Editing is Locked");
            editModeSwitch.setTextOn("Edting is Enabled");
            TextView titleTV = findViewById(R.id.add_herd_visit_title);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String selectedDate = sdf.format(herdVisitDate);
            titleTV.setText("Herd Visit of the "+ selectedDate);
            //fab.setVisibility(View.GONE); // hide the button if we are in read only mode
        }

        final AddHerdVisitActivity a = this;


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Snackbar.make(view, "Adding visit", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                if (isReadonly)
                {
                    HerdVisit visit = HerdDatabase.getInstance(a).getHerdDao().getHerdVisitByID(herdVisitID).get(0);
                    HerdVisitInfoDialog infoDialog = new HerdVisitInfoDialog(a,visit );
                    infoDialog.show();
                }
                else {
                    final HealthEventContainer hce = sectionsPagerAdapter.getHealthEventForVisit();
                    final ProductivityEventContainer pce = sectionsPagerAdapter.getProductivityEventForVisit();
                    final DynamicEventContainer dce = sectionsPagerAdapter.getDynamicEventForVisit();

                    try {

                        String comments = "";

                        final HerdVisitCommentsDialog commentsDialog = new HerdVisitCommentsDialog(a);
                        commentsDialog.show();
                        commentsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                ConfrimHerdVisitInsertionDialog confrimHerdVisitInsertionDialog = new ConfrimHerdVisitInsertionDialog(a, a, herdID, hce, pce, dce, commentsDialog.getComments());
                                confrimHerdVisitInsertionDialog.show();

                            }
                        });

                    } catch (Exception e) {
                        //   ErrorDialog dialog = new ErrorDialog(getApplicationContext(), e.getMessage());
                        //  dialog.show();
                        Log.e("ILRI", e.getMessage());
                    }
                }
            }
        });
    }
}