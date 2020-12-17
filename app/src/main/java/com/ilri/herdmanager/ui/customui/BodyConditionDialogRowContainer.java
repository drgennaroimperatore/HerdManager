package com.ilri.herdmanager.ui.customui;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;


import com.google.android.material.tabs.TabLayout;
import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.BodyConditionForHealthEvent;
import com.ilri.herdmanager.database.entities.HerdDao;

import java.util.ArrayList;
import java.util.List;

public class BodyConditionDialogRowContainer {

    private Context m_context;
    private EditText m_nAffectedBabiesET, m_nAffectedYoungET, m_nAffectedOldET;



    public BodyConditionDialogRowContainer(Context ctx)
    {
        m_context = ctx;

    }

    private BodyConditionDialogRowContainer()
    {

    }

    private void setM_nAffectedBabiesET(EditText m_nAffectedBabiesET) {
        this.m_nAffectedBabiesET = m_nAffectedBabiesET;
    }

    private void setM_nAffectedOldET(EditText m_nAffectedOldET) {
        this.m_nAffectedOldET = m_nAffectedOldET;
    }

    private void setM_nAffectedYoungET(EditText m_nAffectedYoungET) {
        this.m_nAffectedYoungET = m_nAffectedYoungET;
    }

    public TableRow generateDialogRow(int level)
    {
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams TVparams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams etParams = new TableRow.LayoutParams(convertdpstoPixels(30), TableRow.LayoutParams.WRAP_CONTENT);
        etParams.setMargins(0,0,15,15);

        TableRow row = new TableRow(m_context);
        row.setLayoutParams(params);

        TextView levelTV = new TextView(m_context);
        levelTV.setLayoutParams(TVparams);
        levelTV.setText("Level "+ level);
        levelTV.setTextColor(Color.BLACK);
        row.addView(levelTV);

        m_nAffectedBabiesET = new EditText(m_context);
        m_nAffectedBabiesET.setLayoutParams(etParams);
        m_nAffectedBabiesET.setWidth(30);
        m_nAffectedBabiesET.setBackgroundColor(Color.BLACK);
        m_nAffectedBabiesET.setText("0");
        m_nAffectedBabiesET.setInputType(InputType.TYPE_CLASS_NUMBER);
        row.addView(m_nAffectedBabiesET);

        m_nAffectedYoungET = new EditText(m_context);
        m_nAffectedYoungET.setLayoutParams(etParams);
        m_nAffectedYoungET.setBackgroundColor(Color.BLACK);
        m_nAffectedYoungET.setInputType(InputType.TYPE_CLASS_NUMBER);
        m_nAffectedYoungET.setText("0");
        row.addView(m_nAffectedYoungET);

        m_nAffectedOldET = new EditText(m_context);
        m_nAffectedOldET.setLayoutParams(etParams);
        m_nAffectedOldET.setBackgroundColor(Color.BLACK);
        m_nAffectedOldET.setText("0");
        m_nAffectedOldET.setInputType(InputType.TYPE_CLASS_NUMBER);
        row.addView(m_nAffectedOldET);

        return row;
    }

    public TableRow generateDialogRow(int level, int nAffBabies, int nAffYoung, int nAffOld)
    {
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams TVparams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams etParams = new TableRow.LayoutParams(convertdpstoPixels(30), TableRow.LayoutParams.WRAP_CONTENT);
        etParams.setMargins(0,0,15,15);

        TableRow row = new TableRow(m_context);
        row.setLayoutParams(params);

        TextView levelTV = new TextView(m_context);
        levelTV.setLayoutParams(TVparams);
        levelTV.setText("Level "+ level);
        levelTV.setTextColor(Color.BLACK);
        row.addView(levelTV);

        m_nAffectedBabiesET = new EditText(m_context);
        m_nAffectedBabiesET.setLayoutParams(etParams);
        m_nAffectedBabiesET.setWidth(30);
        m_nAffectedBabiesET.setBackgroundColor(Color.BLACK);
        m_nAffectedBabiesET.setText(String.valueOf(nAffBabies));
        m_nAffectedBabiesET.setInputType(InputType.TYPE_CLASS_NUMBER);
        row.addView(m_nAffectedBabiesET);

        m_nAffectedYoungET = new EditText(m_context);
        m_nAffectedYoungET.setLayoutParams(etParams);
        m_nAffectedYoungET.setBackgroundColor(Color.BLACK);
        m_nAffectedYoungET.setInputType(InputType.TYPE_CLASS_NUMBER);
        m_nAffectedYoungET.setText(String.valueOf(nAffYoung));
        row.addView(m_nAffectedYoungET);

        m_nAffectedOldET = new EditText(m_context);
        m_nAffectedOldET.setLayoutParams(etParams);
        m_nAffectedOldET.setBackgroundColor(Color.BLACK);
        m_nAffectedOldET.setText(String.valueOf(nAffOld));
        m_nAffectedOldET.setInputType(InputType.TYPE_CLASS_NUMBER);
        row.addView(m_nAffectedOldET);

        return row;
    }

    private int convertdpstoPixels(int dps)
    {
        final float scale = m_context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);

        return pixels;
    }

    public static BodyConditionDialogRowContainer generateBodyConditionContainerFromRow(TableRow row)
    {
        BodyConditionDialogRowContainer container = new BodyConditionDialogRowContainer();
        // get affected babies box
        EditText affBabiesET = (EditText) row.getChildAt(1);
        container.setM_nAffectedBabiesET(affBabiesET);
        //do the same
        EditText affYoungET = (EditText) row.getChildAt(2);
        container.setM_nAffectedYoungET(affYoungET);
        //and container is populated and can be returned
        EditText affOldET = (EditText) row.getChildAt(3);
        container.setM_nAffectedOldET(affOldET);
        return  container;
    }

    public static BodyConditionForHealthEvent generateBodyConditionFromHealthEventFromRow(TableRow row, HerdDao dao, String species)
    {
        BodyConditionForHealthEvent bche = new BodyConditionForHealthEvent();


       TextView levelTV = (TextView) row.getChildAt(0);
       String levelStr = levelTV.getText().toString();
      int level= Integer.valueOf(levelStr.split(" ")[1]);

        bche.bodyConditionID= dao.getBodyConditionIDFromStageAndSpecies(level,species);
        // get affected babies box
        EditText affBabiesET = (EditText) row.getChildAt(1);
        String str =affBabiesET.getText().toString();
        bche.nAffectedBabies = Integer.valueOf(str);

        //do the same
        EditText affYoungET = (EditText) row.getChildAt(2);
        str = affYoungET.getText().toString();
        bche.nAffectedYoung= Integer.valueOf(str);

        //and container is populated and can be returned
        EditText affOldET = (EditText) row.getChildAt(3);
        str = affOldET.getText().toString();
        bche.nAffectedAdult= Integer.valueOf(str);
        return  bche;
    }


}
