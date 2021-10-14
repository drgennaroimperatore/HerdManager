package com.ilri.herdmanager.ui.customui;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.widget.TableRow;
import android.widget.TextView;

import com.ilri.herdmanager.R;

public class BodyConditionRowContainer
{
    private TextView m_LevelTv, m_nAffBabiesTV, m_nAffYoungTV, m_nAffOldTV;
    private Context m_context;

    public BodyConditionRowContainer(Context ctx)
    {
        m_context = ctx;
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

       // ContextThemeWrapper ctr = new ContextThemeWrapper(ctx, R.style.RowContentTextStyle);
        m_LevelTv = new TextView(ctx);
        m_LevelTv.setLayoutParams(params);
        m_LevelTv.setGravity(Gravity.CENTER_HORIZONTAL);
        m_LevelTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
        m_nAffBabiesTV = new TextView(ctx);
        m_nAffBabiesTV.setLayoutParams(params);
        m_nAffBabiesTV.setGravity(Gravity.CENTER_HORIZONTAL);
        m_nAffBabiesTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
        m_nAffYoungTV= new TextView(ctx);
        m_nAffYoungTV.setLayoutParams(params);
        m_nAffYoungTV.setGravity(Gravity.CENTER_HORIZONTAL);
        m_nAffYoungTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
        m_nAffOldTV = new TextView(ctx);
        m_nAffOldTV.setLayoutParams(params);
        m_nAffOldTV.setGravity(Gravity.CENTER_HORIZONTAL);
        m_nAffOldTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
    }
    public TableRow generateTableRow(int level, int nBabies, int nYoung, int nOld)
    {
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

        TableRow row = new TableRow(m_context);
        row.setLayoutParams(params);

        m_LevelTv.setText(String.valueOf(level));
        row.addView(m_LevelTv);

        m_nAffBabiesTV.setText(String.valueOf(nBabies));
        row.addView(m_nAffBabiesTV);

        m_nAffYoungTV.setText(String.valueOf(nYoung));
        row.addView(m_nAffYoungTV);

        m_nAffOldTV.setText(String.valueOf(nOld));
        row.addView(m_nAffOldTV);
        return row;
    }


}
