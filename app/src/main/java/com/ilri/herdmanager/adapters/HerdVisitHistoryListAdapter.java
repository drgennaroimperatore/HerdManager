package com.ilri.herdmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.HerdVisit;
import com.ilri.herdmanager.ui.main.AddHerdVisitActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HerdVisitHistoryListAdapter extends ArrayAdapter<HerdVisit> {
    private Context mContext;
    private  List<HerdVisit> mData;
    //private int mHerdID =-155;


    public HerdVisitHistoryListAdapter(@NonNull Context context, int resource, List<HerdVisit> data) {
        super(context, resource, data);
        mContext = context;
        mData =data;
       // mHerdID = herdID;

    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.herd_visit_history_list_row,null);



        final HerdVisit hv = mData.get(position);
        TextView herdVisiInfoTV = row.findViewById(R.id.herdvisit_history_list_row_textview_date);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String selectedDate = sdf.format(hv.HerdVisitDate);
        herdVisiInfoTV.setText("Herd visit of the "+ selectedDate);

        Button detailsButton = row.findViewById(R.id.herdvisit_history_list_row_button_view_visit_details);
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToAddHerdVisitActivity = new Intent(mContext, AddHerdVisitActivity.class);

                goToAddHerdVisitActivity.putExtra("isReadOnly",true);
                goToAddHerdVisitActivity.putExtra("herdVisitID",hv.ID );
                goToAddHerdVisitActivity.putExtra("herdVisitDate",hv.HerdVisitDate);

                //goToAddHerdVisitActivity.putExtra("herdID",herd.ID);
                mContext.startActivity(goToAddHerdVisitActivity);
            }
        });

        return row;
    }

    @Override
    public int getPosition(@Nullable HerdVisit item) {
        return super.getPosition(item);
    }

}
