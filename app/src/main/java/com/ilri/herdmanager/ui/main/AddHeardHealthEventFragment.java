package com.ilri.herdmanager.ui.main;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.ilri.herdmanager.adapters.HealthEventExpandableListAdapter;
import com.ilri.herdmanager.classes.HealthEventContainer;
import com.ilri.herdmanager.database.entities.ADDB;
import com.ilri.herdmanager.database.entities.ADDBDAO;
import com.ilri.herdmanager.database.entities.AdditionalSigns;
import com.ilri.herdmanager.database.entities.BodyConditionForHealthEvent;
import com.ilri.herdmanager.database.entities.DiseasesForHealthEvent;
import com.ilri.herdmanager.database.entities.HealthEvent;
import com.ilri.herdmanager.database.entities.HealthInterventionForHealthEvent;
import com.ilri.herdmanager.database.entities.Herd;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.database.entities.SignsForDiseasesForHealthEvent;
import com.ilri.herdmanager.database.entities.SignsForHealthEvent;
import com.ilri.herdmanager.ui.dialogs.BodyConditionDialog;
import com.ilri.herdmanager.ui.dialogs.DiseaseForSingleAnimalDialog;
import com.ilri.herdmanager.ui.dialogs.HealthInterventionDialog;
import com.ilri.herdmanager.ui.dialogs.NewSignEventDialog;
import com.ilri.herdmanager.R;

import java.util.ArrayList;
import java.util.List;

public class AddHeardHealthEventFragment extends Fragment implements LifecycleObserver {

    private AddHeardHealthViewModel mViewModel;
    private ExpandableListView mHealthEventExpandableListView;
    private Button mShowAddSignButton, mShowDiagnoseAnimalButton, mShowEditBodyConditionButton, mShowAddInterventionButton;
    private int mHerdID = -155;
    private HealthEventExpandableListAdapter mAdapter;
    private boolean mEditableInReadOnly = false;

    ActivityResultLauncher<Intent> mDiagnoserLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData().hasExtra("chosenDiagnosis")) {
                        String chosenDiag = result.getData().getStringExtra("chosenDiagnosis");
                        String animalAge = result.getData().getStringExtra("animalAge");
                        ArrayList<SignsForDiseasesForHealthEvent> signsForDisease = (ArrayList<SignsForDiseasesForHealthEvent>) result.getData().getSerializableExtra("signs");
                        int babies = 0;
                        int young = 0;
                        int adult = 0;

                        if (animalAge.equals("P.Wnd"))
                            babies = 1;
                        else if (animalAge.equals("Young"))
                            young = 1;
                        else if (animalAge.equals("Adult"))
                            adult = 1;
                        mAdapter.addNewDisease(generateDiseaseForHealthEventFromDiseaseName(chosenDiag, babies, young, adult), signsForDisease);


                    }
                }
            });

    public static AddHeardHealthEventFragment newInstance() {
        return new AddHeardHealthEventFragment();
    }

    public AddHeardHealthEventFragment() {
    }

    public AddHeardHealthEventFragment(int herdID) {
        mHerdID = herdID;
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
        boolean isReadOnly = false;
        int herdVisitID = -145;
        if (args != null) {
            mHerdID = args.getInt("herdID");
            herdVisitID = args.getInt("herdVisitID", -145);
            boolean isRO = args.getBoolean("isReadOnly", false);
            isReadOnly = ((isRO) && (herdVisitID != -145));
        }

        mShowAddSignButton = view.findViewById(R.id.health_event_show_sign_dialog);

        mShowEditBodyConditionButton = view.findViewById(R.id.health_event_show_body_condition_dialog);

        mShowAddInterventionButton = view.findViewById(R.id.health_event_show_health_intervention_dialog);

        mShowDiagnoseAnimalButton = view.findViewById(R.id.health_event_show_diagnose_single_animal_dialog);

        mHealthEventExpandableListView = view.findViewById(R.id.health_event_exapandableListView);
        ArrayList<HealthEvent> healthEvents = new ArrayList<>();

        HealthEventExpandableListAdapter adapter = new HealthEventExpandableListAdapter(getContext(), healthEvents, mHerdID);
        mHealthEventExpandableListView.setAdapter(adapter);
        mAdapter = adapter;
        mHealthEventExpandableListView.expandGroup(0);
        mHealthEventExpandableListView.expandGroup(1);
        mHealthEventExpandableListView.expandGroup(2);


        final AddHeardHealthEventFragment f = this;
        final int hvID = herdVisitID;

        mShowAddInterventionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new HealthInterventionDialog(getContext(), mHerdID, f, hvID, mEditableInReadOnly);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                dialogFragment.show(ft, "dialog");

            }
        });

        if (!isReadOnly)
            mHealthEventExpandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                        int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                        int childPosition = ExpandableListView.getPackedPositionChild(id);

                        if (groupPosition == 3)//disease
                        {
                            List<SignsForDiseasesForHealthEvent> signsForDiseasesForHealthEvents = mAdapter.getSignsForSingleDiagnoses(childPosition);
                            DialogFragment dialogFragment = new DiseaseForSingleAnimalDialog(getContext(), signsForDiseasesForHealthEvents, f);
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                            if (prev != null) {
                                ft.remove(prev);
                            }
                            ft.addToBackStack(null);
                            dialogFragment.show(ft, "dialog");

                        }
                        if (groupPosition == 0)//signs
                        {
                            Herd h = HerdDatabase.getInstance(getContext()).getHerdDao().getHerdByID(mHerdID).get(0);
                            List<AdditionalSigns> signs = ADDB.getInstance(getContext()).getADDBDAO().getAdditionalSigns();
                            // List<Signs> signs=  ADDB.getInstance(getContext()).getADDBDAO().getAllSignsForAnimal(h.speciesID);
                            List<String> sNames = new ArrayList<>();
                            SignsForHealthEvent she = mAdapter.getSignsForHealthEvent(childPosition);

                            for (AdditionalSigns s : signs)
                                sNames.add(s.Name);

                            DialogFragment dialogFragment = new NewSignEventDialog(getContext(), sNames, childPosition,
                                    she.numberOfAffectedBabies, she.numberOfAffectedYoung, she.numberOfAffectedOld, f, mEditableInReadOnly);
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                            if (prev != null) {
                                ft.remove(prev);
                            }
                            ft.addToBackStack(null);

                            dialogFragment.show(ft, "dialog");
                        }

                        if (groupPosition == 1) // health interventions
                        {
                            Herd h = HerdDatabase.getInstance(getContext()).getHerdDao().getHerdByID(mHerdID).get(0);
                            HealthInterventionForHealthEvent healthIntervention = mAdapter.getHealthInterventionForHealthEvent(childPosition);
                            HealthInterventionDialog healthInterventionDialog = new HealthInterventionDialog(getContext(), h.ID, f, hvID, mEditableInReadOnly);

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
                List<AdditionalSigns> signs = ADDB.getInstance(getContext()).getADDBDAO().getAdditionalSigns();
                List<String> sNames = new ArrayList<>();

                for (AdditionalSigns s : signs)
                    sNames.add(s.Name);
                DialogFragment dialogFragment = new NewSignEventDialog(getContext(), sNames, f, mEditableInReadOnly, hvID);

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

                DialogFragment dialogFragment = new BodyConditionDialog(getContext(), mHerdID, f, hvID, mEditableInReadOnly);
                if (mAdapter.getBodyConditionForHealthEvent().size() > 0)
                    dialogFragment = new BodyConditionDialog(getContext(), mHerdID, f, hvID, mEditableInReadOnly);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                dialogFragment.show(ft, "dialog");

            }
        });

        mShowDiagnoseAnimalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               launchDiagnosisActivity(null);
            }
        });

        //Read only stuff

        if (isReadOnly) {
            mShowAddSignButton.setVisibility(View.GONE);
            mShowEditBodyConditionButton.setVisibility(View.GONE);
            mShowAddInterventionButton.setVisibility(View.GONE);
            mShowDiagnoseAnimalButton.setVisibility(View.GONE);

            HealthEvent hv = HerdDatabase.getInstance(getContext()).getHerdDao().getHealthEventForVisit(herdVisitID).get(0);
            List<SignsForHealthEvent> she = HerdDatabase.getInstance(getContext()).getHerdDao().getSignsForHealthEvent(hv.ID);
            List<DiseasesForHealthEvent> dhe = HerdDatabase.getInstance(getContext()).getHerdDao().getDiseasesForHealthEvent(hv.ID);
            ArrayList<ArrayList<SignsForDiseasesForHealthEvent>> sfdhe = new ArrayList<>();

            for (DiseasesForHealthEvent d : dhe) {
                ArrayList<SignsForDiseasesForHealthEvent> signs =
                        (ArrayList<SignsForDiseasesForHealthEvent>) HerdDatabase.getInstance(getContext()).getHerdDao().getSignsForDiseaseForHealthEvent(d.ID);
                sfdhe.add(signs);
            }

            List<BodyConditionForHealthEvent> bche = HerdDatabase.getInstance(getContext()).getHerdDao().getBodyConditionForHealthEvent(hv.ID);
            List<HealthInterventionForHealthEvent> hihe = HerdDatabase.getInstance(getContext()).getHerdDao().getHealthInterventionsForHealthEvent(hv.ID);

            mAdapter.setReadOnlyData(mHerdID, (ArrayList<DiseasesForHealthEvent>) dhe, (ArrayList<SignsForHealthEvent>) she, (ArrayList<BodyConditionForHealthEvent>) bche, (ArrayList<HealthInterventionForHealthEvent>) hihe, sfdhe);

            mHealthEventExpandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                        int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                        int childPosition = ExpandableListView.getPackedPositionChild(id);

                        if (groupPosition == 3)//disease
                        {
                            if (!mEditableInReadOnly) {
                                List<SignsForDiseasesForHealthEvent> signsForDiseasesForHealthEvents = mAdapter.getSignsForSingleDiagnoses(childPosition);
                                DialogFragment dialogFragment = new DiseaseForSingleAnimalDialog(getContext(), signsForDiseasesForHealthEvents, f);
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                                if (prev != null) {
                                    ft.remove(prev);
                                }
                                ft.addToBackStack(null);
                                dialogFragment.show(ft, "dialog");

                            }
                            else
                            {
                                launchDiagnosisActivity(mAdapter.getSignsForSingleDiagnoses(position));
                            }
                        }
                    }
                    return true;
                }
            });
        }
    }

    private void launchDiagnosisActivity(ArrayList<SignsForDiseasesForHealthEvent> sfdfhe) {
        Intent intent = new Intent(getActivity(), DiagnoseSingleAnimalActivity.class);
        Herd herd = HerdDatabase.getInstance(getContext()).getHerdDao().getHerdByID(mHerdID).get(0);
        intent.putExtra("speciesID", herd.speciesID);
        if(sfdfhe!=null)
            intent.putExtra("readOnly_signs",sfdfhe);
        mDiagnoserLauncher.launch(intent);
    }


    public boolean addDiseaseToList(DiseasesForHealthEvent dhe,
                                    ArrayList<SignsForDiseasesForHealthEvent> signsForDiseasesForHealthEvents) {
        return mAdapter.addNewDisease(dhe, signsForDiseasesForHealthEvents);
    }

    public boolean addSignToList(SignsForHealthEvent she) {
        return mAdapter.addNewSign(she);
    }

    public void editDisease(int pos, int b, int y, int o) {
        mAdapter.editDisease(pos, b, y, o);
    }


    public String getDiseaseName(int pos) {
        return ADDB.getInstance(getContext()).getADDBDAO().getDiseaseNameFromId(mAdapter.getDiseaseForHealthEvent(pos).diseaseID).get(0);

    }

    public String getSignName(int pos) {
        return ADDB.getInstance(getContext()).getADDBDAO().getSignNameFromID(mAdapter.getSignsForHealthEvent(pos).signID).get(0);
    }

    public void deleteDisease(int pos) {
        mAdapter.deleteDisease(pos);
    }

    public void deleteSign(int pos) {
        mAdapter.deleteSign(pos);
    }

    public SignsForHealthEvent editSign(int pos, int b, int y, int o) {
        return mAdapter.editSign(pos, b, y, o);

    }

    public HealthInterventionForHealthEvent editHealthInterventionForHealthEvent(int pos, int b, int y, int o) {
        return mAdapter.editHealthInterventionForHealthEvent(pos, b, y, o);
    }


    public void expandList(int g) {
        mHealthEventExpandableListView.expandGroup(g);
    }


    public HealthEventContainer getHealthEventContainer() {
        HealthEventContainer hce = new HealthEventContainer();
        hce.mDhes = mAdapter.getDiseasesForHealthEvent();
        hce.mShes = mAdapter.getSignsForHealthEvent();
        hce.mBChes = mAdapter.getBodyConditionForHealthEvent();
        hce.mHIhes = mAdapter.getHealthInterventionsForHealthEvent();
        hce.mSFDFGE = mAdapter.getAllSignsForSingleDiagnoses();
        return hce;
    }

    public void editBodyConditionList(ArrayList<BodyConditionForHealthEvent> valuesFromDialog) {
        mAdapter.editBodyConditionList(valuesFromDialog);
    }

    public boolean addHealthIntervention(HealthInterventionForHealthEvent healthIntervention) {
        return mAdapter.addHealthIntervention(healthIntervention);
    }

    public HealthInterventionForHealthEvent deleteHealthIntervention(int pos) {
        return mAdapter.deleteHealthIntervention(pos);
    }

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

                        if (groupPosition == 0)//signs
                        {
                            Herd h = HerdDatabase.getInstance(getContext()).getHerdDao().getHerdByID(mHerdID).get(0);
                            List<AdditionalSigns> signs = ADDB.getInstance(getContext()).getADDBDAO().getAdditionalSigns();
                            // List<Signs> signs=  ADDB.getInstance(getContext()).getADDBDAO().getAllSignsForAnimal(h.speciesID);
                            List<String> sNames = new ArrayList<>();
                            SignsForHealthEvent she = mAdapter.getSignsForHealthEvent(childPosition);

                            for (AdditionalSigns s : signs)
                                sNames.add(s.Name);

                            DialogFragment dialogFragment = new NewSignEventDialog(getContext(), sNames, childPosition,
                                    she.numberOfAffectedBabies, she.numberOfAffectedYoung, she.numberOfAffectedOld, f, mEditableInReadOnly);
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                            if (prev != null) {
                                ft.remove(prev);
                            }
                            ft.addToBackStack(null);

                            dialogFragment.show(ft, "dialog");
                        }

                        if (groupPosition == 1) // health intervention
                        {
                            Herd h = HerdDatabase.getInstance(getContext()).getHerdDao().getHerdByID(mHerdID).get(0);
                            HealthInterventionForHealthEvent healthIntervention = mAdapter.getHealthInterventionForHealthEvent(childPosition);
                            HealthInterventionDialog healthInterventionDialog = new HealthInterventionDialog(getContext(), h.ID, f, healthIntervention, childPosition, mEditableInReadOnly);

                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                            if (prev != null) {
                                ft.remove(prev);
                            }
                            ft.addToBackStack(null);

                            healthInterventionDialog.show(ft, "dialog");
                        }

                        if (groupPosition==3)
                        {
                            launchDiagnosisActivity(mAdapter.getSignsForSingleDiagnoses(childPosition));
                        }

                        return true;
                    }

                    return false;
                }
            });


        mAdapter.setEditableInReadOnly(editable);
        if (editable) {
            mShowAddSignButton.setVisibility(View.VISIBLE);
            mShowAddInterventionButton.setVisibility(View.VISIBLE);
            mShowEditBodyConditionButton.setVisibility(View.VISIBLE);
            mShowDiagnoseAnimalButton.setVisibility(View.VISIBLE);
        } else {
            mShowAddSignButton.setVisibility(View.GONE);
            mShowAddInterventionButton.setVisibility(View.GONE);
            mShowEditBodyConditionButton.setVisibility(View.GONE);
            mShowDiagnoseAnimalButton.setVisibility(View.GONE);
        }
    }

    private DiseasesForHealthEvent generateDiseaseForHealthEventFromDiseaseName(String diseaseName,
                                                                                int babies,
                                                                                int young,
                                                                                int adult) {
        diseaseName = diseaseName.split(":")[0].toUpperCase().trim();
        ADDBDAO addbdao = ADDB.getInstance(getContext()).getADDBDAO();
        DiseasesForHealthEvent dhe = new DiseasesForHealthEvent();
        int diseaseID = addbdao.getDiseaseIDFromName(diseaseName).get(0);
        dhe.diseaseID = diseaseID;
        dhe.numberOfAffectedBabies = babies;
        dhe.numberOfAffectedYoung = young;
        dhe.numberOfAffectedOld = adult;
        return dhe;
    }
}
