package com.ilri.herdmanager.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;
import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.Farmer;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.kmz.GeoData;
import com.ilri.herdmanager.ui.NewCaseActivity;

import java.util.ArrayList;
import java.util.LinkedList;

public class NewFarmerDialog extends Dialog {


    private EditText mEditTextFarmerFirstName, mEditTextFarmerSecondName;
    private Spinner mChosenRegionSpinner, mChosenDistrictSpinner, mChosenWoredaSpinner;
    private Button mAddNewFarmerButton;
    private NewCaseActivity mNewCaseActivity;

    public NewFarmerDialog(@NonNull Context context, NewCaseActivity a) {

        super(context);
        mNewCaseActivity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_new_farmer);

        mEditTextFarmerFirstName = findViewById(R.id.editText_new_farmer_firstName);
        mEditTextFarmerSecondName = findViewById(R.id.editText_new_farmer_secondName);

        mChosenRegionSpinner = findViewById(R.id.spinnerFarmerRegion);
        mChosenDistrictSpinner = findViewById(R.id.spinnerFarmerDistrict);
        mChosenWoredaSpinner = findViewById(R.id.spinnerFarmerKebele);

        final LinkedList<String> regionsOfEthi = GeoData.getInstance().getRegions();

        final ArrayAdapter<String> regionSpinnerAdapter = new ArrayAdapter(getContext(),R.layout.chosen_diagnosis_spinner_item,regionsOfEthi );
        mChosenRegionSpinner.setAdapter(regionSpinnerAdapter);

       // final ArrayList<String> districsFirstRegion = GeoData.getInstance().getDistricsForRegion(regionsOfEthi.get(0));

        final ArrayAdapter<String> districtsSpinnerAdapter = new ArrayAdapter(getContext(),R.layout.chosen_diagnosis_spinner_item);
        mChosenDistrictSpinner.setAdapter(districtsSpinnerAdapter);

        final ArrayAdapter<String> woredaSpinnerAdapter = new ArrayAdapter(getContext(),R.layout.chosen_diagnosis_spinner_item);
        mChosenWoredaSpinner.setAdapter(woredaSpinnerAdapter);


        mChosenRegionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                districtsSpinnerAdapter.clear();

                try {

                    ArrayList<String> districtsForRegion = GeoData.getInstance().getDistricsForRegion(regionsOfEthi.get(position));


                    districtsSpinnerAdapter.addAll(districtsForRegion);
                    districtsSpinnerAdapter.notifyDataSetChanged();
                    woredaSpinnerAdapter.clear();

                    ArrayList<String> woredasForDistrict = GeoData.getInstance().getWoredasForDistrics
                            ((String) GeoData.getInstance().getDistricsForRegion(regionsOfEthi.get(position)).get(0));


                    woredaSpinnerAdapter.addAll(woredasForDistrict);
                }
                catch (Exception e)
                {

                    int i =regionSpinnerAdapter.getCount();
                    e.getMessage();
                   // regionSpinnerAdapter.addAll(regionsOfEthi.get(0));
                    //regionSpinnerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }

        });


        mChosenDistrictSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                woredaSpinnerAdapter.clear();
                woredaSpinnerAdapter.addAll(GeoData.getInstance().getWoredasForDistrics((String) mChosenDistrictSpinner.getSelectedItem()));
                woredaSpinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mAddNewFarmerButton = findViewById(R.id.button_add_new_farmer);
        mAddNewFarmerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mEditTextFarmerFirstName.getText().toString().isEmpty() || mEditTextFarmerSecondName.getText().toString().isEmpty())
                {
                    //LayoutInflater inflater = (LayoutInflater) mNewCaseActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                  //  LinearLayout view = (LinearLayout) inflater.inflate(R.layout.snackbar_error_layout,null);
                    Snackbar mySnackbar = Snackbar.make(findViewById(R.id.new_farmer_dialog_parent), "Please Complete All Fields", Snackbar.LENGTH_LONG);
                    mySnackbar.getView().setBackgroundColor(R.color.black);
                    mySnackbar.show();

                }
                else
            {

                Farmer farmer = new Farmer();
                farmer.firstName = mEditTextFarmerFirstName.getText().toString();
                farmer.secondName = mEditTextFarmerSecondName.getText().toString();
                farmer.region = mChosenRegionSpinner.getSelectedItem().toString();
                farmer.district = mChosenDistrictSpinner.getSelectedItem().toString();
                farmer.kebele = mChosenWoredaSpinner.getSelectedItem().toString();

                farmer.ID = (int) HerdDatabase.getInstance(mNewCaseActivity).getHerdDao().InsertFarmer(farmer);

                mNewCaseActivity.assignFarmer(farmer);
                dismiss();
            }

            }
        });




    }


}
