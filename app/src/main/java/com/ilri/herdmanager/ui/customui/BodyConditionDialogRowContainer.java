package com.ilri.herdmanager.ui.customui;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.view.ContextThemeWrapper;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;


import com.ilri.herdmanager.R;

public class BodyConditionDialogRowContainer {

    private final Context m_context;
    private EditText m_nAffectedBabiesET, m_nAffectedYoungET, m_nAffectedOldET;


    public BodyConditionDialogRowContainer(Context ctx)
    {
        m_context = ctx;

    }

    public TableRow generateDialogRow(int level)
    {
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams TVparams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams etParams = new TableRow.LayoutParams(convertdpstoPixels(40), TableRow.LayoutParams.WRAP_CONTENT);

        TableRow row = new TableRow(m_context);
        row.setLayoutParams(params);

        TextView levelTV = new TextView(m_context);
        levelTV.setLayoutParams(TVparams);
        levelTV.setText("Level "+ level);
        levelTV.setTextColor(Color.BLACK);
        row.addView(levelTV);

        m_nAffectedBabiesET = new EditText(m_context);
        m_nAffectedBabiesET.setLayoutParams(etParams);
        m_nAffectedBabiesET.setBackgroundColor(R.color.black);
        row.addView(m_nAffectedBabiesET);

        m_nAffectedYoungET = new EditText(m_context);
        m_nAffectedYoungET.setLayoutParams(etParams);
        m_nAffectedYoungET.setBackgroundColor(R.color.black);
        row.addView(m_nAffectedYoungET);

        m_nAffectedOldET = new EditText(m_context);
        m_nAffectedOldET.setLayoutParams(etParams);
        m_nAffectedOldET.setBackgroundColor(R.color.black);
        row.addView(m_nAffectedOldET);

        return row;
    }

    private int convertdpstoPixels(int dps)
    {
        final float scale = m_context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);

        return pixels;
    }
}
