package com.ilri.herdmanager.ui.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.BodyCondition;
import com.ilri.herdmanager.database.entities.HerdDao;
import com.ilri.herdmanager.database.entities.HerdDatabase;

import java.util.ArrayList;

public class BodyConditionInformationDialog extends DialogFragment {

    Context mContext;
    String mSpecies;

    public BodyConditionInformationDialog(Context context, String species)
    {
        mContext = context;
        mSpecies = species;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.Theme_AppCompat);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(mContext).inflate(R.layout.dialog_body_condition_information,null);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HerdDao dao = HerdDatabase.getInstance(mContext).getHerdDao();
        ArrayList<BodyCondition> bodyConditions =(ArrayList<BodyCondition>) dao.getBodyConditionBySpecies(mSpecies);
        TableLayout layout= view.findViewById(R.id.dialog_body_condition_information_tableLayout);

        for (int i =0; i<bodyConditions.size(); i++)
        {
            BodyCondition bodyCondition = bodyConditions.get(i);
            layout.addView (generateDescriptionRow("L."+bodyCondition.stage, bodyCondition.description));
        }
    }

    private TableRow generateDescriptionRow(String level, String description)
    {
        TableRow.LayoutParams params= new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams paramsTV = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15,0,25,0);

        TableRow row = new TableRow(mContext);
        row.setLayoutParams(params);


        TextView levelTV = new TextView(mContext);
        levelTV.setLayoutParams(paramsTV);
        levelTV.setTextColor(Color.BLACK);
        levelTV.setText(level);
        row.addView(levelTV);

        TextView desc = new TextView(mContext);
        desc.setLayoutParams(paramsTV);
        desc.setTextColor(Color.BLACK);
        desc.setText(description);
        desc.setGravity(Gravity.CENTER);
        desc.setPadding(25,0,55,0);
        row.addView(desc);


        return row;
    }
}
