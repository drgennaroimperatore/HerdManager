package com.ilri.herdmanager.ui.main;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.ilri.herdmanager.adapters.HealthEventExpandableListAdapter;
import com.ilri.herdmanager.classes.HealthEventContainer;
import com.ilri.herdmanager.database.entities.ADDB;
import com.ilri.herdmanager.database.entities.AdditionalSigns;
import com.ilri.herdmanager.database.entities.BodyConditionForHealthEvent;
import com.ilri.herdmanager.database.entities.Diseases;
import com.ilri.herdmanager.database.entities.DiseasesForHealthEvent;
import com.ilri.herdmanager.database.entities.HealthEvent;
import com.ilri.herdmanager.database.entities.HealthIntervention;
import com.ilri.herdmanager.database.entities.HealthInterventionForHealthEvent;
import com.ilri.herdmanager.database.entities.Herd;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.database.entities.Signs;
import com.ilri.herdmanager.database.entities.SignsForHealthEvent;
import com.ilri.herdmanager.managers.HerdManager;
import com.ilri.herdmanager.managers.HerdVisitManager;
import com.ilri.herdmanager.ui.dialogs.BodyConditionDialog;
import com.ilri.herdmanager.ui.dialogs.HealthInterventionDialog;
import com.ilri.herdmanager.ui.dialogs.NewDiseaseEventDialog;
import com.ilri.herdmanager.ui.dialogs.NewSignEventDialog;
import com.ilri.herdmanager.R;

import java.util.ArrayList;
import java.util.List;

public class AddHeardHealthEventFragment extends Fragment {

    private AddHeardHealthViewModel mViewModel;
    private ExpandableListView mHealthEventExpandableListView;
    private Button mShowAddSignButton, mShowAddDiseaseButton, mShowEditBodyConditionButton, mShowAddInterventionButton;
    private int mHerdID = -155;
    private HealthEventExpandableListAdapter mAdapter;
    private boolean mEditableInReadOnly = false;

    public static AddHeardHealthEventFragment newInstance() {
        return new AddHeardHealthEventFragment();
    }

    public AddHeardHealthEventFragment() {}

    public AddHeardHealthEventFragment(int herdID)
    {
        mHerdID= herdID;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.add_heard_health_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddHeardHealthViewModel.class);
        // TODO: Use the ViewModel
        setRetainInstance(true);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //FragmentManager fragmentManager = getFragmentManager();
        Bundle args = getArguments();
        boolean isReadOnly= false;
        int herdVisitID = -145;
        if(args!=null) {
            mHerdID = args.getInt("herdID");
            herdVisitID = args.getInt("herdVisitID", -145);
            boolean isRO = args.getBoolean("isReadOnly", false);
            isReadOnly = ((isRO) && (herdVisitID!=-145));
        }


//        mShowAddDiseaseButton = view.findViewById(R.id.health_event_show_disease_dialog);
        //REMOVE THIS LINE IF WE WANT DISEASES BACK

//        mShowAddDiseaseButton.setVisibility(View.INVISIBLE);

        mShowAddSignButton = view.findViewById(R.id.health_event_show_sign_dialog);

        mShowEditBodyConditionButton = view.findViewById(R.id.health_event_show_body_condition_dialog);

        mShowAddInterventionButton = view.findViewById(R.id.health_event_show_health_intervention_dialog);

        mHealthEventExpandableListView = view.findViewById(R.id.health_event_exapandableListView);
        ArrayList<HealthEvent> healthEvents = new ArrayList<>();
        //healthEvents.add( new HealthEvent());

        HealthEventExpandableListAdapter adapter = new HealthEventExpandableListAdapter(getContext(), healthEvents, mHerdID);
        mHealthEventExpandableListView.setAdapter(adapter);
        mAdapter = adapter;
        mHealthEventExpandableListView.expandGroup(0);
        mHealthEventExpandableListView.expandGroup(1);
        mHealthEventExpandableListView.expandGroup(2);


      final  AddHeardHealthEventFragment f = this;
        final int hvID = herdVisitID;

      /*  mShowAddDiseaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Herd h = HerdDatabase.getInstance(getContext()).getHerdDao().getHerdByID(mHerdID).get(0);
                List<Diseases> diseases = ADDB.getInstance(getContext()).getADDBDAO().getAllDiseasesForAninal(h.speciesID);
                List<String> diseaseNames = new ArrayList<>();

                for(Diseases d: diseases)
                    diseaseNames.add(d.Name);

                DialogFragment dialogFragment = new NewDiseaseEventDialog(getContext(),diseaseNames,f );
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                dialogFragment.show(ft, "dialog");

            }
        });*/


        mShowAddInterventionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new HealthInterventionDialog(getContext(),mHerdID, f,hvID,mEditableInReadOnly);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                dialogFragment.show(ft, "dialog");

            }
        });

        if(!isReadOnly)
        mHealthEventExpandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);

                /*    if(groupPosition==1)//disease
                    {
                        Herd h = HerdDatabase.getInstance(getContext()).getHerdDao().getHerdByID(mHerdID).get(0);
                        List<Diseases> diseases = ADDB.getInstance(getContext()).getADDBDAO().getAllDiseasesForAninal(h.speciesID);
                        List<String> diseaseNames = new ArrayList<>();
                        DiseasesForHealthEvent dhe = mAdapter.getDiseaseForHealthEvent(childPosition);

                        for(Diseases d: diseases)
                            diseaseNames.add(d.Name);

                        DialogFragment dialogFragment = new NewDiseaseEventDialog(getContext(), diseaseNames, childPosition,
                                dhe.numberOfAffectedBabies,dhe.numberOfAffectedYoung,dhe.numberOfAffectedOld,f );
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        ft.addToBackStack(null);
                        dialogFragment.show(ft, "dialog");

                    }*/
                    if(groupPosition==0)//signs
                    {
                        Herd h = HerdDatabase.getInstance(getContext()).getHerdDao().getHerdByID(mHerdID).get(0);
                        List<AdditionalSigns> signs =ADDB.getInstance(getContext()).getADDBDAO().getAdditionalSigns();
                       // List<Signs> signs=  ADDB.getInstance(getContext()).getADDBDAO().getAllSignsForAnimal(h.speciesID);
                        List<String> sNames = new ArrayList<>();
                        SignsForHealthEvent she = mAdapter.getSignsForHealthEvent(childPosition);

                        for(AdditionalSigns s: signs)
                            sNames.add(s.Name);

                        DialogFragment dialogFragment = new NewSignEventDialog(getContext(), sNames,childPosition,
                                she.numberOfAffectedBabies,she.numberOfAffectedYoung,she.numberOfAffectedOld,f, mEditableInReadOnly);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        ft.addToBackStack(null);

                        dialogFragment.show(ft, "dialog");
                    }

                    if(groupPosition==1) // health interventions
                    {
                        Herd h = HerdDatabase.getInstance(getContext()).getHerdDao().getHerdByID(mHerdID).get(0);
                        HealthInterventionForHealthEvent healthIntervention = mAdapter.getHealthInterventionForHealthEvent(childPosition);
                        HealthInterventionDialog healthInterventionDialog = new HealthInterventionDialog(getContext(), h.ID,f,hvID,mEditableInReadOnly);

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        ft.addToBackStack(null);

                        healthInterventionDialog.show(ft, "dialog");
                    }
                }

                    return true;
                }

        });



        mShowAddSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Herd h = HerdDatabase.getInstance(getContext()).getHerdDao().getHerdByID(mHerdID).get(0);
                List<AdditionalSigns> signs = ADDB.getInstance(getContext()).getADDBDAO().getAdditionalSigns();
                //List<Signs> signs=  ADDB.getInstance(getContext()).getADDBDAO().getAllSignsForAnimal(h.speciesID);
                List<String> sNames = new ArrayList<>();

                for(AdditionalSigns s: signs)
                    sNames.add(s.Name);
                DialogFragment dialogFragment = new NewSignEventDialog(getContext(), sNames,f,mEditableInReadOnly, hvID);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                dialogFragment.show(ft, "dialog");

            }
        });

        mShowEditBodyConditionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dialogFragment = new BodyConditionDialog(getContext(),mHerdID,f,hvID,mEditableInReadOnly);
                if(mAdapter.getBodyConditionForHealthEvent().size()>0)
                    dialogFragment = new BodyConditionDialog(getContext(),mHerdID,f,hvID,mEditableInReadOnly);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                dialogFragment.show(ft, "dialog");

            }
        });

        //Read only stuff

        if(isReadOnly)
        {
//            mShowAddDiseaseButton.setVisibility(View.GONE);
            mShowAddSignButton.setVisibility(View.GONE);
            mShowEditBodyConditionButton.setVisibility(View.GONE);
            mShowAddInterventionButton.setVisibility(View.GONE);

            HealthEvent hv= HerdDatabase.getInstance(getContext()).getHerdDao().getHealthEventForVisit(herdVisitID).get(0);
            List<SignsForHealthEvent> she = HerdDatabase.getInstance(getContext()).getHerdDao().getSignsForHealthEvent(hv.ID);
            List<DiseasesForHealthEvent> dhe = HerdDatabase.getInstance(getContext()).getHerdDao().getDiseasesForHealthEvent(hv.ID);
            List<BodyConditionForHealthEvent> bche= HerdDatabase.getInstance(getContext()).getHerdDao().getBodyConditionForHealthEvent(hv.ID);
            List<HealthInterventionForHealthEvent> hihe = HerdDatabase.getInstance(getContext()).getHerdDao().getHealthInterventionsForHealthEvent(hv.ID);
          //  List<BodyConditionForHealthEvent> test =HerdDatabase.getInstance(getContext()).getHerdDao().testBodyConditionForHealthEvent();
            mAdapter.setReadOnlyData(mHerdID,(ArrayList<DiseasesForHealthEvent>) dhe,(ArrayList<SignsForHealthEvent>) she, (ArrayList<BodyConditionForHealthEvent>) bche, (ArrayList<HealthInterventionForHealthEvent>)hihe);
        }


    }

    public boolean addDiseaseToList(DiseasesForHealthEvent dhe)
    {
       return mAdapter.addNewDisease(dhe);
    }

    public boolean addSignToList(SignsForHealthEvent she)
    {
        return mAdapter.addNewSign(she);
    }

    public void editDisease(int pos, int b, int y, int o)
    {
        mAdapter.editDisease(pos,b,y,o);
    }


    public String getDiseaseName(int pos)
    {
       return ADDB.getInstance(getContext()).getADDBDAO().getDiseaseNameFromId(mAdapter.getDiseaseForHealthEvent(pos).diseaseID).get(0);

    }

    public String getSignName(int pos)
    {
        return ADDB.getInstance(getContext()).getADDBDAO().getSignNameFromID(mAdapter.getSignsForHealthEvent(pos).signID).get(0);
    }

    public void deleteDisease(int pos)
    {
        mAdapter.deleteDisease(pos);
    }

    public void deleteSign(int pos)
    {
        mAdapter.deleteSign(pos);
    }

    public SignsForHealthEvent editSign(int pos, int b, int y, int o)
    {
       return mAdapter.editSign(pos, b,y,o);

    }

    public HealthInterventionForHealthEvent editHealthInterventionForHealthEvent(int pos, int b, int y, int o)
    {
        return mAdapter.editHealthInterventionForHealthEvent(pos,b,y,o);
    }


    public void expandList(int g)
    {
        mHealthEventExpandableListView.expandGroup(g);
    }



    public HealthEventContainer getHealthEventContainer()
    {
       HealthEventContainer hce = new HealthEventContainer();
       hce.mDhes= mAdapter.getDiseasesForHealthEvent();
       hce.mShes = mAdapter.getSignsForHealthEvent();
       hce.mBChes = mAdapter.getBodyConditionForHealthEvent();
       hce.mHIhes = mAdapter.getHealthInterventionsForHealthEvent();
       return hce;
    }

    public void editBodyConditionList(ArrayList<BodyConditionForHealthEvent> valuesFromDialog)
    {
        mAdapter.editBodyConditionList(valuesFromDialog);
    }

    public boolean addHealthIntervention(HealthInterventionForHealthEvent healthIntervention) { return mAdapter.addHealthIntervention (healthIntervention);}
    public HealthInterventionForHealthEvent deleteHealthIntervention (int pos) {return mAdapter.deleteHealthIntervention( pos);}

    public void setEditableInReadOnly(boolean editable) {
        mEditableInReadOnly = editable;
       final AddHeardHealthEventFragment f = this;

        if (!mEditableInReadOnly)
            mHealthEventExpandableListView.setOnLongClickListener(null);
        else
            mHealthEventExpandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);

                /*    if(groupPosition==1)//disease
                    {
                        Herd h = HerdDatabase.getInstance(getContext()).getHerdDao().getHerdByID(mHerdID).get(0);
                        List<Diseases> diseases = ADDB.getInstance(getContext()).getADDBDAO().getAllDiseasesForAninal(h.speciesID);
                        List<String> diseaseNames = new ArrayList<>();
                        DiseasesForHealthEvent dhe = mAdapter.getDiseaseForHealthEvent(childPosition);

                        for(Diseases d: diseases)
                            diseaseNames.add(d.Name);

                        DialogFragment dialogFragment = new NewDiseaseEventDialog(getContext(), diseaseNames, childPosition,
                                dhe.numberOfAffectedBabies,dhe.numberOfAffectedYoung,dhe.numberOfAffectedOld,f );
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        ft.addToBackStack(null);
                        dialogFragment.show(ft, "dialog");

                    }*/
                    if(groupPosition==0)//signs
                    {
                        Herd h = HerdDatabase.getInstance(getContext()).getHerdDao().getHerdByID(mHerdID).get(0);
                        List<AdditionalSigns> signs =ADDB.getInstance(getContext()).getADDBDAO().getAdditionalSigns();
                        // List<Signs> signs=  ADDB.getInstance(getContext()).getADDBDAO().getAllSignsForAnimal(h.speciesID);
                        List<String> sNames = new ArrayList<>();
                        SignsForHealthEvent she = mAdapter.getSignsForHealthEvent(childPosition);

                        for(AdditionalSigns s: signs)
                            sNames.add(s.Name);

                        DialogFragment dialogFragment = new NewSignEventDialog(getContext(), sNames,childPosition,
                                she.numberOfAffectedBabies,she.numberOfAffectedYoung,she.numberOfAffectedOld,f, mEditableInReadOnly);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        ft.addToBackStack(null);

                        dialogFragment.show(ft, "dialog");
                    }

                    if(groupPosition==1) // health intervention
                    {
                        Herd h = HerdDatabase.getInstance(getContext()).getHerdDao().getHerdByID(mHerdID).get(0);
                        HealthInterventionForHealthEvent healthIntervention = mAdapter.getHealthInterventionForHealthEvent(childPosition);
                        HealthInterventionDialog healthInterventionDialog = new HealthInterventionDialog(getContext(), h.ID,f,healthIntervention,childPosition,mEditableInReadOnly);

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        ft.addToBackStack(null);

                        healthInterventionDialog.show(ft, "dialog");
                    }

                    return true;
                }

                return false;
            }
        });


        mAdapter.setEditableInReadOnly (editable);
        if(editable)
        {
            mShowAddSignButton.setVisibility(View.VISIBLE);
            mShowAddInterventionButton.setVisibility(View.VISIBLE);
            mShowEditBodyConditionButton.setVisibility(View.VISIBLE);
        }
        else {
            mShowAddSignButton.setVisibility(View.GONE);
            mShowAddInterventionButton.setVisibility(View.GONE);
            mShowEditBodyConditionButton.setVisibility(View.GONE);
        }
    }
}
