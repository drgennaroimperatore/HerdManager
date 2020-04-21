package com.ilri.herdmanager.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;
import com.ilri.herdmanager.R;
import com.ilri.herdmanager.classes.HealthEventContainer;
import com.ilri.herdmanager.managers.HerdVisitManager;
import com.ilri.herdmanager.ui.MainActivity;
import com.ilri.herdmanager.ui.main.AddHerdVisitActivity;

import java.util.Calendar;
import java.util.Date;

public class ConfrimHerdVisitInsertionDialog extends Dialog {
    Context mContext;
    HealthEventContainer mHce;
    AddHerdVisitActivity mA;
    int mHerdID = -155;
    Date mVisitDate = new Date();
    public ConfrimHerdVisitInsertionDialog(Context context, AddHerdVisitActivity a, int herdID, HealthEventContainer hce) {
        super(context);
        mA = a;
        mHce = hce;
        mHerdID = herdID;



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm_herd_visit);


        final CalendarView calendarView = findViewById(R.id.calendarView_dialog_insert_visit_confirmation_date);
        calendarView.setMaxDate(System.currentTimeMillis());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year,month,dayOfMonth);
                mVisitDate =  new Date(c.getTimeInMillis());
            }
        });

        Button confirmButton = findViewById(R.id.button_dialog_insert_visit_confirmation_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mHce.mDhes.isEmpty() && mHce.mShes.isEmpty())
                       Snackbar.make(v, "This visit is empty!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                else {

                    HerdVisitManager.getInstance().addVisitToHerd(mContext, mHerdID,mVisitDate, mHce.mDhes, mHce.mShes);
                    Intent intent = new Intent(mA, MainActivity.class);
                    mA.startActivity(intent);
                }
            }
        });

    }
}
