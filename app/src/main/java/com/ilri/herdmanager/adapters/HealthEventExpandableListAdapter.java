package com.ilri.herdmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.HealthEvent;

import java.util.ArrayList;

public class HealthEventExpandableListAdapter extends BaseExpandableListAdapter {
   Context mContext;
   ArrayList<HealthEvent> mSignsList = new ArrayList<>();
   ArrayList<HealthEvent> mDiseaseList = new ArrayList<>();
   ArrayList<String> mGroupHeaders;

    @Override
    public int getGroupCount() {
        return mGroupHeaders.size();
    }

    public HealthEventExpandableListAdapter(Context context, ArrayList<HealthEvent> healthEvents)
    {
        mContext = context;

        mSignsList.add(0,new HealthEvent());

        mDiseaseList.add(0,new HealthEvent());


        mGroupHeaders = new ArrayList<>();
        mGroupHeaders.add("Diseases");
        mGroupHeaders.add("Signs");


    }

    @Override
    public int getChildrenCount(int groupPosition) {

        int childSize =0;

        if(groupPosition==0)
            childSize = mDiseaseList.size();
        if(groupPosition==1)
            childSize = mSignsList.size();



        return childSize;
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
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        View view = convertView;

        if(view== null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.herd_event_list_header,null);

        }

        TextView header = view.findViewById(R.id.health_event_header_textView);
        header.setText(mGroupHeaders.get(groupPosition));

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);






                convertView = inflater.inflate(R.layout.herd_health_event_row, null);










        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
