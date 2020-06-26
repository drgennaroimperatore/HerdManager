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
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;
import com.ilri.herdmanager.R;
import com.ilri.herdmanager.classes.DynamicEventContainer;
import com.ilri.herdmanager.classes.HealthEventContainer;
import com.ilri.herdmanager.classes.ProductivityEventContainer;
import com.ilri.herdmanager.database.entities.AnimalMovementsForDynamicEvent;
import com.ilri.herdmanager.database.entities.BirthsForProductivityEvent;
import com.ilri.herdmanager.database.entities.DeathsForDynamicEvent;
import com.ilri.herdmanager.database.entities.DynamicEvent;
import com.ilri.herdmanager.database.entities.Herd;
import com.ilri.herdmanager.database.entities.HerdDao;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.database.entities.HerdVisit;
import com.ilri.herdmanager.database.entities.ProductivityEvent;
import com.ilri.herdmanager.managers.HerdVisitManager;
import com.ilri.herdmanager.ui.MainActivity;
import com.ilri.herdmanager.ui.main.AddHerdVisitActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ConfrimHerdVisitInsertionDialog extends Dialog {
    Context mContext;
    HealthEventContainer mHce;
    DynamicEventContainer mDce;
    ProductivityEventContainer mPce;
    AddHerdVisitActivity mA;
    int mHerdID = -155;
    Date mVisitDate = new Date();
    private TextView currentBabiesTV, currentYoungTV, currentOldTV;
    private TextView newBabiesTV, newYoungTV, newOldTV;
    private String mComments;

    public ConfrimHerdVisitInsertionDialog(Context context, AddHerdVisitActivity a, int herdID,
                                           HealthEventContainer hce, ProductivityEventContainer pce, DynamicEventContainer dce, String comments ) {
        super(context);
        mA = a;
        mHce = hce;
        mPce= pce;
        mDce = dce;
        mHerdID = herdID;
        mComments = comments;



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
       final int[] changes = calculateHerdChanges();
       final int[] currentSize = getCurrentHerdSize();

        currentBabiesTV = findViewById(R.id.dialog_herd_changes_current_babies_textView);
        currentYoungTV = findViewById(R.id.dialog_herd_changes_current_young_textView);
        currentOldTV = findViewById(R.id.dialog_herd_changes_current_old_textView);
        currentBabiesTV.setText(String.valueOf(currentSize[0]));
        currentYoungTV.setText(String.valueOf(currentSize[1]));
        currentOldTV.setText(String.valueOf(currentSize[2]));

        newBabiesTV = findViewById(R.id.dialog_herd_changes_new_babies_textView);
        newYoungTV =findViewById(R.id.dialog_herd_changes_new_young_textView);
        newOldTV = findViewById(R.id.dialog_herd_changes_new_old_textView);
        newOldTV.setText(String.valueOf(changes[2]));
        newYoungTV.setText(String.valueOf(changes[1]));
        newBabiesTV.setText(String.valueOf(changes[0]));

        Button cancelButton = findViewById(R.id.button_dialog_insert_visit_confirmation_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Button confirmButton = findViewById(R.id.button_dialog_insert_visit_confirmation_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  if(mHce.mDhes.isEmpty() && mHce.mShes.isEmpty())
                       Snackbar.make(v, "This visit is empty!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                else {*/

              int nBabies = changes[0];
              int nYoung = changes[1];
              int nOld = changes[2];

              if(isVisitDateValid()) {

                  HerdVisitManager.getInstance().addVisitToHerd(
                          mContext,
                          mHerdID, mVisitDate,
                          nBabies, nYoung, nOld,
                          mHce.mDhes, mHce.mShes,
                          mPce.milkProductionForProductivityEvent, mPce.birthsForProductivityEvent,
                          mDce.mMovements, mDce.mDeaths, mComments);
                  dismiss();
                  Intent intent = new Intent(mA, MainActivity.class);
                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                  mA.startActivity(intent);
              }
              else
              {
                  ErrorDialog errorDialog = new ErrorDialog(getContext(),"Date of visit can't be before or the same as date of last visit");
                  errorDialog.show();
              }
                    // }

            }
        });

    }

    public int[] calculateHerdChanges()
    {
        int[] herdChanges= new int[3];

        int nBabies=0; int nYoung=0; int nOld=0;

        HerdDao dao = HerdDatabase.getInstance(mContext).getHerdDao();
      List<HerdVisit> visitList=  dao.getAllHerdVisitsByHerdID(mHerdID);
        Herd herd = dao.getHerdByID(mHerdID).get(0);
      if(visitList.size()>0) {

          HerdVisit currentVisit = visitList.get(0);

          nBabies = currentVisit.babiesAtVisit;
          nYoung = currentVisit.youngAtVisit;
          nOld = currentVisit.oldAtVisit;
      }
      else
      {
          nBabies = herd.nBabies;
          nYoung = herd.nYoung;
          nOld = herd.nOld;
      }

        for(DeathsForDynamicEvent d:mDce.mDeaths)
        {
            nBabies = nBabies-d.deadBabies;
            nYoung = nYoung -d.deadYoung;
            nOld = nOld -d.deadOld;
        }

      AnimalMovementsForDynamicEvent ame = mDce.mMovements;

        nBabies = nBabies-ame.soldBabies;
        nBabies = nBabies-ame.lostBabies;
        nBabies = nBabies+ame.boughtBabies;

        nYoung = nYoung-ame.soldYoung;
        nYoung = nYoung-ame.lostYoung;
        nYoung = nYoung+ame.boughtYoung;

        nOld = nOld -ame.soldOld;
        nOld = nOld - ame.lostOld;
        nOld = nOld + ame.boughtOld;

        BirthsForProductivityEvent bpe = mPce.birthsForProductivityEvent;
        nBabies+= bpe.nOfBirths;

        if(nBabies<0)
            nBabies=0;
        if(nYoung<0)
            nYoung=0;
        if(nOld<0)
            nOld=0;

        herdChanges[0] =nBabies;
        herdChanges[1] =nYoung;
        herdChanges[2] =nOld;

        return herdChanges;

    }

    public int[] getCurrentHerdSize()
    {
        int[] herdSize= new int[3];



        HerdDao dao = HerdDatabase.getInstance(mContext).getHerdDao();
        Herd herd = dao.getHerdByID(mHerdID).get(0);
        List<HerdVisit> herdVisits = dao.getAllHerdVisitsByHerdID(mHerdID);

        if(herdVisits.size()>0)
        {
            HerdVisit hv = herdVisits.get(0);
            herdSize[0] = hv.babiesAtVisit;
            herdSize[1] = hv.youngAtVisit;
            herdSize[2] = hv.oldAtVisit;
        }
        else
        {
            herdSize[0] = herd.nBabies;
            herdSize[1] = herd.nYoung;
            herdSize[2] = herd.nOld;
        }

        return herdSize;
    }

    public boolean isVisitDateValid()
    {
        HerdDao dao = HerdDatabase.getInstance(mContext).getHerdDao();
       List<HerdVisit> visits = dao.getAllHerdVisitsByHerdID(mHerdID);

       if(visits.size()==0)
           return true;
       HerdVisit lastVisitInserted= visits.get(0);

        Date lastVisitDate = lastVisitInserted.HerdVisitDate;

        Calendar lastVisitcalendar = Calendar.getInstance();
        lastVisitcalendar.setTime( lastVisitDate );
        lastVisitcalendar.set(Calendar.HOUR_OF_DAY, 0);
        lastVisitcalendar.set(Calendar.MINUTE, 0);
        lastVisitcalendar.set(Calendar.SECOND, 0);
        lastVisitcalendar.set(Calendar.MILLISECOND, 0);

       lastVisitDate= lastVisitcalendar.getTime();
       lastVisitcalendar.setTime(mVisitDate);
       lastVisitcalendar.set(Calendar.HOUR_OF_DAY, 0);
       lastVisitcalendar.set(Calendar.MINUTE, 0);
       lastVisitcalendar.set(Calendar.SECOND, 0);
       lastVisitcalendar.set(Calendar.MILLISECOND, 0);
       mVisitDate = lastVisitcalendar.getTime();

       if(mVisitDate.equals(lastVisitDate))
           return false;

       return (lastVisitDate.before(mVisitDate));
    }
}
