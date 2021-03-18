package com.ilri.herdmanager.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ilri.herdmanager.R;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DiagnoseSingleAnimalResultsActivity extends AppCompatActivity {

    private HashMap<String, Float> mDiagnoses = new HashMap<>();
    private Button mChooseDiagnosisButton;
    private String mChosenDiagnosis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnose_single_animal_results);
        try {
            mDiagnoses = sortDiagnoses((HashMap<String, Float>) getIntent().getSerializableExtra("diagnoses"));
            LinearLayout diagnosesContainter = findViewById(R.id.activity_diagnose_results_results_container);
            populateDiagnosesContainer(diagnosesContainter);

            mChooseDiagnosisButton = findViewById(R.id.activity_diagnose_results_choose_diagnosis_button);
            mChooseDiagnosisButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backToHealthFragment();
                }
            });
        } catch (Exception e)
        {

        }


    }

    public void populateDiagnosesContainer(LinearLayout container)
    {
        RadioGroup group = new RadioGroup(this);

        group.setOrientation(RadioGroup.VERTICAL);
        mChosenDiagnosis = mDiagnoses.entrySet().toArray()[0].toString();
        int index =0;
        for (Map.Entry<String, Float> diag : mDiagnoses.entrySet()) {

            RadioButton presentRadioButton = new RadioButton(this);

            presentRadioButton.setText(formatDiagnosis(diag.getKey()) + " " + diag.getValue()+"%");
            group.addView(presentRadioButton);

        }
        group.check(0);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               RadioButton radioButton=findViewById(checkedId);
              mChosenDiagnosis = radioButton.getText().toString();
            }
        });

        container.addView(group);


    }

    private String formatDiagnosis(String d)
    {
        String [] parts = d.split(" ");

        if(parts.length>1)
        {
            String result ="";
            for (int p=0; p<parts.length; p++)
            {
                parts[p] = parts[p].charAt(0)+(parts[p].substring(1).toLowerCase()+" ");
              result =  result.concat(parts[p]);
            }

            return result;


        }
        else
            return d.charAt(0)+(d.substring(1).toLowerCase());

    }

    public HashMap<String, Float> sortDiagnoses(HashMap <String, Float> originalList )
    {
        List<Map.Entry<String, Float> > list =
                new LinkedList<Map.Entry<String, Float> >(originalList.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Float> >() {
            public int compare(Map.Entry<String, Float> o1,
                               Map.Entry<String, Float> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Float> temp = new LinkedHashMap<String, Float>();
        for (Map.Entry<String, Float> aa : list) {


            BigDecimal bd = new BigDecimal(Float.toString(aa.getValue()));
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);

            temp.put(aa.getKey(), bd.floatValue());
        }
        return temp;
    }

    public void backToHealthFragment ()
    {
        Intent  chosenDiagnosisIntent = new Intent();
        chosenDiagnosisIntent.putExtra("chosenDiagnosis",mChosenDiagnosis);
        setResult(RESULT_OK,chosenDiagnosisIntent);
        finish();
    }


}