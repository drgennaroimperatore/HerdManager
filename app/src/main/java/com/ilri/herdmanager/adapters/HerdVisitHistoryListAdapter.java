package com.ilri.herdmanager.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.HerdVisit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HerdVisitHistoryListAdapter extends ArrayAdapter<HerdVisit> {
    private Context mContext;
    private  List<HerdVisit> mData;


    public HerdVisitHistoryListAdapter(@NonNull Context context, int resource, List<HerdVisit> data) {
        super(context, resource, data);
        mContext = context;
        mData =data;

    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.herd_visit_history_list_row,null);



        HerdVisit hv = mData.get(position);
        TextView herdVisiInfoTV = row.findViewById(R.id.herdvisit_history_list_row_textview_date);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String selectedDate = sdf.format(hv.HerdVisitDate);
        herdVisiInfoTV.setText("Herd visit of the "+ selectedDate);

        return row;
    }

    @Override
    public int getPosition(@Nullable HerdVisit item) {
        return super.getPosition(item);
    }

}
