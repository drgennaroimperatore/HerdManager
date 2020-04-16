package com.ilri.herdmanager.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

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

        ArrayAdapter<String> regionSpinnerAdapter = new ArrayAdapter(getContext(),R.layout.chosen_diagnosis_spinner_item,regionsOfEthi );
        mChosenRegionSpinner.setAdapter(regionSpinnerAdapter);

        ArrayList<String> districsFirstRegion = GeoData.getInstance().getDistricsForRegion(regionsOfEthi.get(0));

        final ArrayAdapter<String> districtsSpinnerAdapter = new ArrayAdapter(getContext(),R.layout.chosen_diagnosis_spinner_item);
        mChosenDistrictSpinner.setAdapter(districtsSpinnerAdapter);

        final ArrayAdapter<String> woredaSpinnerAdapter = new ArrayAdapter(getContext(),R.layout.chosen_diagnosis_spinner_item);
        mChosenWoredaSpinner.setAdapter(woredaSpinnerAdapter);


        mChosenRegionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                districtsSpinnerAdapter.clear();
                districtsSpinnerAdapter.addAll(GeoData.getInstance().getDistricsForRegion(regionsOfEthi.get(position)));
                districtsSpinnerAdapter.notifyDataSetChanged();
                woredaSpinnerAdapter.clear();
                woredaSpinnerAdapter.addAll(GeoData.getInstance().getWoredasForDistrics
                        ((String) GeoData.getInstance().getDistricsForRegion(regionsOfEthi.get(position)).get(0)));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

                Farmer farmer = new Farmer();
                farmer.firstName= mEditTextFarmerFirstName.getText().toString();
                farmer.secondName = mEditTextFarmerSecondName.getText().toString();
                farmer.region = mChosenRegionSpinner.getSelectedItem().toString();
                farmer.district= mChosenDistrictSpinner.getSelectedItem().toString();
                farmer.kebele = mChosenWoredaSpinner.getSelectedItem().toString();

                farmer.ID = (int)  HerdDatabase.getInstance(mNewCaseActivity).getHerdDao().InsertFarmer(farmer);

                mNewCaseActivity.assignFarmer(farmer);
                dismiss();
            }
        });




    }


}
