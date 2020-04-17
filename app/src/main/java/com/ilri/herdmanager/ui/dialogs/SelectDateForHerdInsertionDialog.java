package com.ilri.herdmanager.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.ilri.herdmanager.R;
import com.ilri.herdmanager.ui.NewCaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SelectDateForHerdInsertionDialog extends Dialog {
    NewCaseActivity mActivity;


    public SelectDateForHerdInsertionDialog (NewCaseActivity context)
    {
       super(context);
       mActivity = context;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_date_for_herd_creation);
        CalendarView calendarView = findViewById(R.id.calendarView_dialog_select_herd_insertion_date);
        calendarView.setMaxDate(System.currentTimeMillis());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Calendar c = Calendar.getInstance();
                        c.set(year,month,dayOfMonth);
                String selectedDate = sdf.format(new Date(c.getTimeInMillis()));
                mActivity.setHerdInsertionDateEditText(selectedDate);
                dismiss();
            }
        });
    }
}
