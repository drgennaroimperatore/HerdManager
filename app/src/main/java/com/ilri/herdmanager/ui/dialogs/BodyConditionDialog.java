package com.ilri.herdmanager.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.adapters.HealthEventExpandableListAdapter;
import com.ilri.herdmanager.database.entities.ADDB;
import com.ilri.herdmanager.database.entities.ADDBDAO;
import com.ilri.herdmanager.database.entities.BodyCondition;
import com.ilri.herdmanager.database.entities.BodyConditionForHealthEvent;
import com.ilri.herdmanager.database.entities.Herd;
import com.ilri.herdmanager.database.entities.HerdDao;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.ui.customui.BodyConditionDialogRowContainer;
import com.ilri.herdmanager.ui.main.AddHeardHealthEventFragment;

import java.util.ArrayList;
import java.util.List;

public class BodyConditionDialog extends DialogFragment {

    public String mSpecies ="";
    private Context mContext;
    private ADDBDAO addbdao;
    private HerdDao herdDao;
    private int herdID;
    private List<String []> mRowValues = new ArrayList<>();
    AddHeardHealthEventFragment fragment;



    private TableLayout mTableLayout;


    public BodyConditionDialog(@NonNull Context context, int herdID, AddHeardHealthEventFragment adapter)
    {

        mContext = context;
        this.herdID = herdID;
       fragment = adapter;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Theme_AppCompat);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       inflater = LayoutInflater.from(mContext);
        return inflater.inflate(R.layout.dialog_body_condition,null);
    }

    @Override
    public void onResume() {
        super.onResume();

        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        addbdao = ADDB.getInstance(mContext).getADDBDAO();
        herdDao= HerdDatabase.getInstance(mContext).getHerdDao();

        mTableLayout = view.findViewById(R.id.dialog_body_condition_tableLayout);

        //Identify the herd name and pass to the initialiser
        Herd herd = herdDao.getHerdByID(herdID).get(0);
        switch(addbdao.getAnimalNameFromID(herd.speciesID).get(0))
        {
            case " CATTLE":
                mSpecies= "CATTLE";
                break;
            case " CAMEL":
                mSpecies="CAMEL";
                break;
            case " SHEEP":
            case " GOAT":
                mSpecies="SHEEP_GOAT";
                break;
        }


        initialiseDialog();

        Button confirmButton = view.findViewById(R.id.dialog_body_condition_confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            fragment.editBodyConditionList((ArrayList<BodyConditionForHealthEvent>) getValuesFromDialog());
                dismiss();
            }
        });
    }

    private void initialiseDialog()
    {
        List<BodyCondition> bodyConditionList = herdDao.getBodyConditionBySpecies(mSpecies);

        for(int i=0; i<bodyConditionList.size(); i++)
        {
            BodyCondition bodyCondition = bodyConditionList.get(i);
            BodyConditionDialogRowContainer container = new BodyConditionDialogRowContainer(mContext,getFragmentManager(),mSpecies);

            TableRow row=null;
            if(fragment.getHealthEventContainer()!=null) {
                List<BodyConditionForHealthEvent> bche = fragment.getHealthEventContainer().mBChes;
                if (bche != null)
                    if (bche.size() >0) {
                        BodyConditionForHealthEvent bodyConditionForHealthEvent = bche.get(i);
                        row = container.generateDialogRow(bodyCondition.stage,
                                bodyConditionForHealthEvent.nAffectedBabies,
                                bodyConditionForHealthEvent.nAffectedYoung,
                                bodyConditionForHealthEvent.nAffectedAdult);
                    }
                else
                    {
                        row  = container.generateDialogRow(bodyCondition.stage);
                    }
            }
            else
            {
                row  = container.generateDialogRow(bodyCondition.stage);
            }
                mTableLayout.addView(row);
                String[] rowVals = new String[3];
                mRowValues.add(rowVals);


        }

    }



    public List<BodyConditionForHealthEvent> getValuesFromDialog()
    {
        List<BodyConditionForHealthEvent> vals = new ArrayList<>();
        for(int i=1; i<mTableLayout.getChildCount(); i++)
        {
            TableRow row = (TableRow) mTableLayout.getChildAt(i);
            BodyConditionForHealthEvent bche= BodyConditionDialogRowContainer.generateBodyConditionFromHealthEventFromRow(row, herdDao,mSpecies);
            vals.add(bche);
        }
       return vals;
    }


}
