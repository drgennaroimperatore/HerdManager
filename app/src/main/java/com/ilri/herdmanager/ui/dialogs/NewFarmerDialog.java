package com.ilri.herdmanager.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.Farmer;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.kmz.GeoData;
import com.ilri.herdmanager.kmz.LocationData;
import com.ilri.herdmanager.ui.NewCaseActivity;

import java.util.ArrayList;
import java.util.LinkedList;

public class NewFarmerDialog extends Dialog {


    private EditText mEditTextFarmerFirstName, mEditTextFarmerSecondName;
    private Spinner mChosenRegionSpinner, mChosenZoneSpinner, mChosenWoredaSpinner;
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
        mChosenZoneSpinner = findViewById(R.id.spinnerFarmerDistrict);
        mChosenWoredaSpinner = findViewById(R.id.spinnerFarmerKebele);

        final ArrayList<String> regionsOfEthi = LocationData.getInstance().getRegions();
        final LinkedList<String> abbrRegions= new LinkedList<>();

       abbrRegions.addAll(regionsOfEthi); // we want to make sure this is a copy and not a reference

        for(String r: regionsOfEthi)
        {
            if (r.contains("Southern Nations"))
            {
                int index = abbrRegions.indexOf(r);
                abbrRegions.remove(r);
                abbrRegions.add(index, "SNNP");
            }
        }



        final ArrayAdapter<String> regionSpinnerAdapter = new ArrayAdapter(getContext(),R.layout.chosen_region_spinner_item,abbrRegions );
        mChosenRegionSpinner.setAdapter(regionSpinnerAdapter);

       // final ArrayList<String> districsFirstRegion = LocationData.getInstance().getDistricsForRegion(regionsOfEthi.get(0));

        final ArrayAdapter<String> zonesSpinnerAdapter = new ArrayAdapter(getContext(),R.layout.chosen_region_spinner_item);
        mChosenZoneSpinner.setAdapter(zonesSpinnerAdapter);

        final ArrayAdapter<String> woredaSpinnerAdapter = new ArrayAdapter(getContext(),R.layout.chosen_region_spinner_item);
        mChosenWoredaSpinner.setAdapter(woredaSpinnerAdapter);


        mChosenRegionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                zonesSpinnerAdapter.clear();

                try {

                    ArrayList<String> districtsForRegion = LocationData.getInstance().getZonesForRegion(regionsOfEthi.get(position));


                    zonesSpinnerAdapter.addAll(districtsForRegion);
                    zonesSpinnerAdapter.notifyDataSetChanged();
                    woredaSpinnerAdapter.clear();

                    ArrayList<String> woredasForZones = LocationData.getInstance().getWoredasForZone(
                            (String) LocationData.getInstance().getZonesForRegion(regionsOfEthi.get(position)).get(0));


                    woredaSpinnerAdapter.addAll(woredasForZones);
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


        mChosenZoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                woredaSpinnerAdapter.clear();
                woredaSpinnerAdapter.addAll(LocationData.getInstance().getWoredasForZone((String) mChosenZoneSpinner.getSelectedItem()));
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
                farmer.district = mChosenZoneSpinner.getSelectedItem().toString();
                farmer.kebele = mChosenWoredaSpinner.getSelectedItem().toString();

                farmer.ID = (int) HerdDatabase.getInstance(mNewCaseActivity).getHerdDao().InsertFarmer(farmer);

                mNewCaseActivity.assignFarmer(farmer);
                dismiss();
            }

            }
        });




    }


}
