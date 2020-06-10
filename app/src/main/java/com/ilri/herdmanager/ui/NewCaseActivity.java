package com.ilri.herdmanager.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.Farmer;
import com.ilri.herdmanager.database.entities.Herd;
import com.ilri.herdmanager.database.entities.HerdDao;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.ui.dialogs.ErrorDialog;
import com.ilri.herdmanager.ui.dialogs.ExisitingFarmerDialog;
import com.ilri.herdmanager.ui.dialogs.FarmerNotAssignedWarningDialog;
import com.ilri.herdmanager.ui.dialogs.NewCaseConfirmationDialog;
import com.ilri.herdmanager.ui.dialogs.NewFarmerDialog;
import com.ilri.herdmanager.ui.dialogs.SelectDateForHerdInsertionDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewCaseActivity extends AppCompatActivity {

    Button mUseNewFarmer, mUseExistingFarmer;
    RadioGroup mSpeciesRadioGroup;
    TextView  mHerdSizeDateTextView, mAssignedFarmerStatusTextView;
    EditText mHerdInsertionDateEditText;
    boolean mIsFarmerAssigned = false;
    Farmer mAssignedFarmer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_case);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final NewCaseActivity a = this;

        HerdDao hd = HerdDatabase.getInstance(this).getHerdDao();
       List<Herd> test = hd.getAllHerds();
       List<Farmer> testF = hd.getAllFarmers();

        mUseNewFarmer = findViewById(R.id.add_new_farmer_button);
        mSpeciesRadioGroup = findViewById(R.id.species_radioGroup);

        mHerdInsertionDateEditText =findViewById(R.id.new_herd_activity_textview_date_of_insertion);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String selectedDate = sdf.format(new Date());
        mHerdInsertionDateEditText.setText(selectedDate);


        mHerdInsertionDateEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                SelectDateForHerdInsertionDialog d = new SelectDateForHerdInsertionDialog(a);
                d.show();
                }
                return true;

            }
        });



      /*  mHerdInsertionDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

       // mHerdSizeDateTextView = findViewById(R.id.new_herd_activity_textview_date_of_insertion_heading);
        mAssignedFarmerStatusTextView = findViewById(R.id.new_case_textview_farmer_assignment_status);



        final Context context = this;



        mUseNewFarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewFarmerDialog dialog = new NewFarmerDialog(NewCaseActivity.this,a );
                dialog.show();


        }
        });

        mUseExistingFarmer= findViewById(R.id.use_existing_farmer_button);

        mUseExistingFarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExisitingFarmerDialog dialog = new ExisitingFarmerDialog(NewCaseActivity.this, a);
                List<Farmer> farmers =HerdDatabase.getInstance(context).getHerdDao().getAllFarmers();
                if(farmers.size()>0)
                    dialog.show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/


                EditText nBabiesET = findViewById(R.id.editText_confirm_herd_visit_number_of_babies);
                EditText nYoungET = findViewById(R.id.editText_confirm_herd_visit_number_of_young);
                EditText nOldET = findViewById(R.id.editText_confirm_herd_visit_number_of_old);

                String nBabiesstr = nBabiesET.getText().toString();
                String nYoungstr = nYoungET.getText().toString();
                String nOldstr = nOldET.getText().toString();



                int  nBabies=0; //Integer.valueOf(nBabiesstr);
                int  nYoung =0; //Integer.valueOf(nYoungstr);
                int  nOld =0; //Integer.valueOf(nOldstr);

                if(nOldstr.isEmpty() || nYoungstr.isEmpty() || nBabiesstr.isEmpty())
                {

                }
                else
                {
                    nBabies=Integer.valueOf(nBabiesstr);
                    nYoung =Integer.valueOf(nYoungstr);
                    nOld =Integer.valueOf(nOldstr);
                }



               int selectedID = mSpeciesRadioGroup.getCheckedRadioButtonId();
                RadioButton selectedButton = findViewById(selectedID);
                String species = selectedButton.getText().toString();



                Date dateOfInsertion = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                try {
                    dateOfInsertion = sdf.parse(mHerdInsertionDateEditText.getText().toString());
                } catch (ParseException e)
                {
                    dateOfInsertion = new Date();
                }

                if(mIsFarmerAssigned) {

                }
                else
                {
                    FarmerNotAssignedWarningDialog warningDialog = new FarmerNotAssignedWarningDialog(context);
                    warningDialog.show();
                }

                if((nBabies+nYoung+nOld)==0)
                {
                    ErrorDialog errorDialog = new ErrorDialog(context, "There are no animals in the herd!");
                    errorDialog.show();
                }
                else
                {
                    if(mIsFarmerAssigned)
                    {
                        NewCaseConfirmationDialog confirmationDialog = new NewCaseConfirmationDialog
                                (context, a, species, nBabies,nYoung,nOld, dateOfInsertion, mAssignedFarmer.ID);
                        confirmationDialog.show();
                    }
                }



            }
        });
    }

    public void goToEvents(Intent goToAddHerdVisitActivity)
    {

         startActivity(goToAddHerdVisitActivity);

    }

    public void assignFarmer(Farmer farmer)
    {
        mAssignedFarmer = farmer;
        mIsFarmerAssigned = true;
        mAssignedFarmerStatusTextView.setTextColor(getResources().getColor(R.color.green));
        mAssignedFarmerStatusTextView.setText("Farmer Assigned");


    }

    public void setHerdInsertionDateEditText(String date)
    {
        mHerdInsertionDateEditText.setText(date);
    }


}
