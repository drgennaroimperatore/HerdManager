package com.ilri.herdmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.BirthsForProductivityEvent;
import com.ilri.herdmanager.database.entities.MilkProductionForProductivityEvent;

import java.util.ArrayList;
import java.util.List;

public class ProductivityEventExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private MilkProductionForProductivityEvent mMilkProduction;
    private BirthsForProductivityEvent mBirths;
    private List<String> mGroupHeaders;

    public ProductivityEventExpandableListAdapter(Context context)
    {
        mMilkProduction = new MilkProductionForProductivityEvent();
        mBirths = new BirthsForProductivityEvent();
        mGroupHeaders = new ArrayList<>();
        mGroupHeaders.add("Milk Production");
        mGroupHeaders.add("Births");
        mContext = context;
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
        }

        if(groupPosition==1) {
            convertView = inflater.inflate(R.layout.productivity_event_births_row, null);
        }



        return convertView;



    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
