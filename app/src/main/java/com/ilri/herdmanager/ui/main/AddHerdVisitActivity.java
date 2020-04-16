package com.ilri.herdmanager.ui.main;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.classes.HealthEventContainer;
import com.ilri.herdmanager.database.entities.HerdVisit;
import com.ilri.herdmanager.managers.HerdVisitManager;
import com.ilri.herdmanager.ui.main.SectionsPagerAdapter;

import java.util.Date;

public class AddHerdVisitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int herdID = getIntent().getIntExtra("herdID",-155);

        setContentView(R.layout.activity_add_herd_visit);
        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), herdID);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

               HealthEventContainer hce = sectionsPagerAdapter.getHealthEventForVisit();

                HerdVisitManager.getInstance().addVisitToHerd(getApplicationContext(), herdID, new Date(),hce.mDhes,hce.mShes);
            }
        });
    }
}