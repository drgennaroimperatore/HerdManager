package com.ilri.herdmanager.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.ADDB;
import com.ilri.herdmanager.database.entities.Farmer;
import com.ilri.herdmanager.database.entities.Herd;
import com.ilri.herdmanager.database.entities.HerdDao;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.ui.NewCaseActivity;
import com.ilri.herdmanager.ui.main.AddHerdVisitActivity;

import java.util.Date;

public class NewCaseConfirmationDialog extends Dialog {

    String mSpecies;
    int mHerdSize;
    Date mDate;
    String mFname, mSname;

    TextView mSpeciesTextView, mHerdSizeTextView, mDateTextView, mFnameTextView, mSnameTextView;
    Button mAddHerdButton, mCancelHerdButton;
    NewCaseActivity mActivity;
    int mFarmerID;

    public NewCaseConfirmationDialog(@NonNull Context context) {
        super(context);
    }

    public NewCaseConfirmationDialog(@NonNull Context context, NewCaseActivity activity, String species, int herdSize, Date date, int farmerID)
    {
        super(context);
        mSpecies = species;
        mHerdSize = herdSize;
        mDate = date;
        mFarmerID = farmerID;
        mActivity = activity;



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirmation_new_herd);

        mSpeciesTextView = findViewById(R.id.textview_new_herd_confirmation_species);
        mSpeciesTextView.setText(mSpecies);
        mHerdSizeTextView = findViewById(R.id.textview_new_herd_confirmation_herd_size);
        mHerdSizeTextView.setText(String.valueOf(mHerdSize));
        mDateTextView = findViewById(R.id.textview_new_herd_confirmation_date_of_insertion);
        mFnameTextView = findViewById(R.id.textview_new_herd_confirmation_farmer_firstname);
        mSnameTextView = findViewById(R.id.textview_new_herd_confirmation_farmer_secondname);

       Farmer farmer = HerdDatabase.getInstance(mActivity).getHerdDao().getFarmerByID(mFarmerID).get(0);

        mFnameTextView.setText(farmer.firstName);
        mSnameTextView.setText(farmer.secondName);

        mSpeciesTextView = findViewById(R.id.textview_new_herd_confirmation_farmer_secondname);

        mAddHerdButton = findViewById(R.id.button_confirm_new_herd);

        mAddHerdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HerdDao dao = HerdDatabase.getInstance(mActivity).getHerdDao();

                Herd herd = new Herd();

                herd.speciesID = ADDB.getInstance(mActivity).getADDBDAO().getAnimalIDFromName(" "+mSpecies.toUpperCase() ).get(0);
                herd.farmerID = mFarmerID;


                dao.InsertHerd(herd);

              mActivity.goToEvents();

            }
        });

        mCancelHerdButton = findViewById(R.id.button_cancel_new_herd);

        mCancelHerdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }


}
