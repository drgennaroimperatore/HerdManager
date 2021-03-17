package com.ilri.herdmanager.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.ADDB;
import com.ilri.herdmanager.database.entities.ADDBDAO;
import com.ilri.herdmanager.database.entities.Diseases;
import com.ilri.herdmanager.database.entities.Signs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DiagnoseSingleAnimalActivity extends AppCompatActivity {

    List<RadioGroup> mSignRadioGroups = new ArrayList<RadioGroup>();
    List<Signs> mSignsForAnimal = new ArrayList<>();
    // AnimalAgeSeekBar mAnimalAgeSeekBar;
    Button diagnoseButton;
    int mCurrentAnimalID;
    ADDB mADDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnose_single_animal);
        mADDB= ADDB.getInstance(this);
      final  ADDBDAO dao = mADDB.getADDBDAO();


        //Add signs
        LayoutInflater inflater = LayoutInflater.from(this);


        int signCounter=0;


        LinearLayout signsContainer = (LinearLayout)findViewById(R.id.signs_container);


        mCurrentAnimalID =91;
        mSignsForAnimal =dao.getAllSignsForAnimal(mCurrentAnimalID);
        populateSignsContainer(signsContainer, mSignsForAnimal);


        ////////////////REFERENCE CODE FOR CREATING SYMPTOMS LIST
     /*   <!--<net.crosp.customradiobtton.PresetRadioGroup
        android:id="@+id/preset_time_radio_group_1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentTop="false"
        android:layout_margin="3dp"
        android:layout_marginBottom="13dp"
        android:layout_below="@id/textView_symptom_heading"
        android:orientation="horizontal"
        android:weightSum="3"
        app:presetRadioCheckedId="@+id/preset_time_value_button_6">


        <net.crosp.customradiobtton.PresetValueButton
        android:id="@+id/preset_time_value_button_3"
        style="@style/PresetLayoutButton"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        app:presetButtonUnitText="Present"
        app:presetButtonValueText="Fever" />*/

        ////////////// END OF REFERENCE CODE


                List<Signs> signs =dao.getAllSignsForAnimal(mCurrentAnimalID);
                populateSignsContainer(signsContainer, signs);


        String t = dao.getAnimalNameFromID(91).get(0);


        diagnoseButton =(Button)findViewById(R.id.diagnose_button);
        diagnoseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                getSelectedSigns();

                GoToResults(diagnoseAnimal(dao,getSelectedSigns(),mCurrentAnimalID,dao.getAllDiseases()));


            }
        });




    }

    public List<String> getNamesFromSigns(List<Signs> signs)
    {
        List<String> names = new ArrayList<>();
        for(Signs sign: signs)
        {
            names.add(sign.Name);
        }
        return names;
    }

    public HashMap<String, Float> diagnoseAnimal(ADDBDAO dao, HashMap<Signs, String> selectedSigns, int animalID, List<Diseases> diseases)
    {
        HashMap<String, Float> diagnosis = new HashMap<>();

        for(Diseases d:diseases)

        {

            if (!checkIfDiseasesAffectsAnimal(dao, animalID, d.Id))
                continue;

            float chainProbability = 1.0f;
            for (Map.Entry selectedSign: selectedSigns.entrySet())
            {
                String signPresence = (String)selectedSign.getValue();
                int signID = ((Signs)selectedSign.getKey()).Id;

                float likelihoodValue = 1.0f; // sign is not observed

                if(signPresence.equals("Present"))
                {

                    try
                    {

                        String stringl =  dao.getLikelihoodValue(animalID, signID, d.Id).get(0).Value;
                        Float floatl =Float.parseFloat( dao.getLikelihoodValue(animalID, signID, d.Id).get(0).Value);
                        likelihoodValue = Float.parseFloat( dao.getLikelihoodValue(animalID, signID, d.Id).get(0).Value)/100.0f;


                    } catch (IndexOutOfBoundsException iob)
                    {
                        Log.d("Diagnosis error ", "SignID "+signID+"DiseaseID "+d.Id+ "AnimalID "+animalID );
                    }
                }

                if (signPresence.equals("Not Present"))
                {
                    try
                    {

                        String stringl =  dao.getLikelihoodValue(animalID, signID, d.Id).get(0).Value;
                        Float floatl =Float.parseFloat( dao.getLikelihoodValue(animalID, signID, d.Id).get(0).Value);
                        likelihoodValue = 1.0f - Float.parseFloat( dao.getLikelihoodValue(animalID, signID, d.Id).get(0).Value)/100.0f;


                    } catch (IndexOutOfBoundsException iob)
                    {
                        Log.d("Diagnosis error ", "SignID "+signID+"DiseaseID "+d.Id+ "AnimalID "+animalID );
                    }
                }
                chainProbability*=likelihoodValue;


            }


            float prior = Float.parseFloat(dao.getPriorForDisease(animalID, d.Id).get(0).Probability);
            float posterior = chainProbability * prior;
            diagnosis.put(d.Name, posterior*100.0f);
        }

        HashMap<String, Float> n = normaliseDiagnoses(diagnosis);
        HashMap<String, Float> s= sortDiagnoses(n);

        return s;
    }

    private boolean checkIfDiseasesAffectsAnimal(ADDBDAO dao, int animalID, int id) {
        return !dao.getPriorForDisease(animalID,id).isEmpty();
    }

    public  HashMap<String, Float> normaliseDiagnoses(HashMap <String, Float> originalList)
    {
        Float sum =0.0f;
        for(Map.Entry e : originalList.entrySet())
        {
            sum+=(Float) e.getValue();
        }

        HashMap<String, Float> normalised = new HashMap<>();

        for(Map.Entry e : originalList.entrySet())
        {
            Float norm = (Float)e.getValue()/sum;
            norm*=100.0f;
            BigDecimal bd = new BigDecimal(Float.toString(norm));
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            norm= bd.floatValue();

            normalised.put((String)e.getKey(), norm );
        }

        return normalised;
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
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }


    public HashMap<Signs, String> getSelectedSigns()
    {
        HashMap <Signs,String> selectedSigns = new HashMap<>();

        int index=0;
        for(RadioGroup group: mSignRadioGroups)
        {
            RadioButton selectedRadioButton = findViewById( group.getCheckedRadioButtonId());

            selectedSigns.put(mSignsForAnimal.get(index), (String)selectedRadioButton.getText());
            index++;


        }
        return selectedSigns;
    }

    public HashMap<String, String> getSelectedSignsStrings()
    {
        HashMap <String,String> selectedSigns = new HashMap<>();

        int index=0;
        for(RadioGroup group: mSignRadioGroups)
        {
            RadioButton selectedRadioButton = findViewById( group.getCheckedRadioButtonId());

            selectedSigns.put(mSignsForAnimal.get(index).Name, (String)selectedRadioButton.getText());
            index++;

        }
        return selectedSigns;
    }

    public void populateSignsContainer(LinearLayout signsContainer, List<Signs> signs)
    {
        int signCounter=0;

        mSignRadioGroups = new ArrayList<>();


        signsContainer.removeAllViews();
        for(Signs sign :signs)
        {
            TextView label = new TextView(this);
            label.setText(sign.Name);
            RadioGroup group = new RadioGroup(this);
            group.setOrientation(RadioGroup.HORIZONTAL);

            RadioButton presentRadioButton = new RadioButton(this);
            presentRadioButton.setText("Present");
            group.addView(presentRadioButton);

            RadioButton notPresentRadioButton = new RadioButton(this);
            group.addView(notPresentRadioButton);
            notPresentRadioButton.setText("Not Present");

            RadioButton notObservedRadioButton = new RadioButton(this);
            group.addView(notObservedRadioButton);
            notObservedRadioButton.setText("Not Observed");

            notObservedRadioButton.setChecked(true);

            signsContainer.addView(label);

            signsContainer.addView(group);

            mSignRadioGroups.add(group);

            signCounter++;
        }

        mSignsForAnimal = signs;
    }

    public void GoToResults(HashMap<String, Float> d)
    {
        Intent myIntent = new Intent(this, DiagnoseSingleAnimalResultsActivity.class);
        myIntent.putExtra("species", mADDB.getADDBDAO().getAnimalNameFromID(mCurrentAnimalID).get(0));
        myIntent.putExtra("signs",getSelectedSignsStrings());
        myIntent.putExtra("diagnoses",d );

        startActivity(myIntent);
    }
}