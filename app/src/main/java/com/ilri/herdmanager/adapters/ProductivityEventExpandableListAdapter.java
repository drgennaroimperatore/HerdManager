package com.ilri.herdmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.BirthsForProductivityEvent;
import com.ilri.herdmanager.database.entities.MilkProductionForProductivityEvent;
import com.ilri.herdmanager.database.entities.SyncStatus;

import java.util.ArrayList;
import java.util.List;

public class ProductivityEventExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private MilkProductionForProductivityEvent mMilkProduction;
    private BirthsForProductivityEvent mBirths;
    private List<String> mGroupHeaders;
    private boolean isReadOnly=false;

    public ProductivityEventExpandableListAdapter(Context context)
    {
        mMilkProduction = new MilkProductionForProductivityEvent();
        mBirths = new BirthsForProductivityEvent();
        mGroupHeaders = new ArrayList<>();
        mGroupHeaders.add("Milk Production");
        mGroupHeaders.add("Births");
        mContext = context;
        isReadOnly = false;
    }

    public void setReadOnly (MilkProductionForProductivityEvent mpe, BirthsForProductivityEvent bpe)
    {
        mBirths = bpe;
        mMilkProduction = mpe;
        notifyDataSetChanged();
        isReadOnly=true;
    }

    @Override
    public int getGroupCount() {
        return mGroupHeaders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupHeaders.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = convertView;

        if(view== null)
        {
            view = inflater.inflate(R.layout.herd_event_list_header,null);

        }
        TextView headerTitle = view.findViewById(R.id.health_event_header_textView);
        headerTitle.setText(mGroupHeaders.get(groupPosition));

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(groupPosition==0) {
            convertView = inflater.inflate(R.layout.productivity_event_milk_production_row, null);
            TextView litresPerDayET = convertView.findViewById(R.id.textview_productivity_event_milk_row_litres_day);
            litresPerDayET.setText(String.valueOf(mMilkProduction.litresOfMilkPerDay));
            TextView lactatingAnimalsET = convertView.findViewById(R.id.textview_productivity_event_milk_row_lactating_animals);
            lactatingAnimalsET.setText(String.valueOf(mMilkProduction.numberOfLactatingAnimals));

            if(isReadOnly)
            {
              /*  ImageView syncImgView = convertView.findViewById(R.id.productivity_event_milk_production_row_sync_imgview);
                syncImgView.setVisibility(View.VISIBLE);

                if(mMilkProduction.syncStatus.equals(SyncStatus.NOT_SYNCHRONISED.toString()))
                    syncImgView.setImageResource(R.drawable.drawable_sync_status_not_synced);
                if(mMilkProduction.syncStatus.equals(SyncStatus.PARTIALLY_SYNCHRONISED.toString()))
                    syncImgView.setImageResource(R.drawable.drawable_sync_status_partially_synced);
                if(mMilkProduction.syncStatus.equals(SyncStatus.SYNCHRNOISED.toString()))
                    syncImgView.setImageResource(R.drawable.drawable_sync_status_synced);*/
            }
        }

        if(groupPosition==1) {
            convertView = inflater.inflate(R.layout.productivity_event_births_row, null);
            TextView gestatingAnimalsET = convertView.findViewById(R.id.textview_productivity_event_births_row_gestating_animals);
            gestatingAnimalsET.setText(String.valueOf(mBirths.nOfGestatingAnimals));
            TextView birthsET = convertView.findViewById(R.id.textview_productivity_event_births_row_births_since_last_visit);
            birthsET.setText(String.valueOf(mBirths.nOfBirths));

            if(isReadOnly)
            {
               /* ImageView syncImgView = convertView.findViewById(R.id.productivity_event_animal_births_row_sync_imgview);
                syncImgView.setVisibility(View.VISIBLE);

                if(mBirths.syncStatus.equals(SyncStatus.NOT_SYNCHRONISED.toString()))
                    syncImgView.setImageResource(R.drawable.drawable_sync_status_not_synced);
                if(mBirths.syncStatus.equals(SyncStatus.PARTIALLY_SYNCHRONISED.toString()))
                    syncImgView.setImageResource(R.drawable.drawable_sync_status_partially_synced);
                if(mBirths.syncStatus.equals(SyncStatus.SYNCHRNOISED.toString()))
                    syncImgView.setImageResource(R.drawable.drawable_sync_status_synced);*/
            }
        }



        return convertView;



    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void setBirths (BirthsForProductivityEvent bpe)
    {
        mBirths = bpe;
        notifyDataSetChanged();
    }

    public void setMilkProduction(MilkProductionForProductivityEvent mpe)
    {
        mMilkProduction = mpe;
        notifyDataSetChanged();
    }

    public BirthsForProductivityEvent getBirths() {
        return mBirths;
    }

    public MilkProductionForProductivityEvent getMilkProduction()
    {
        return mMilkProduction;
    }
}
