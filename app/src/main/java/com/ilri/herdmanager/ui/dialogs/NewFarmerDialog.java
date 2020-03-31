package com.ilri.herdmanager.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.kmz.GeoData;

import java.util.ArrayList;
import java.util.LinkedList;

public class NewFarmerDialog extends Dialog {


    private Spinner mChosenRegionSpinner, mChosenDistrictSpinner, mChosenWoredaSpinner;
    public NewFarmerDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_new_farmer);

        mChosenRegionSpinner = findViewById(R.id.spinnerFarmerRegoion);
        mChosenDistrictSpinner = findViewById(R.id.spinnerFarmerDistrict);
        mChosenWoredaSpinner = findViewById(R.id.spinnerFarmerWoreda);

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




    }


}
