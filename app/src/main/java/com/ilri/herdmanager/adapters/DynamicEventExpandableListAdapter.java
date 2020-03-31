package com.ilri.herdmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.DynamicEvent;

import java.util.ArrayList;

public class DynamicEventExpandableListAdapter extends BaseExpandableListAdapter {

    Context mContext;
    ArrayList<String> mHeaders = new ArrayList<>();
    ArrayList<DynamicEvent> mEvents = new ArrayList<>();

    public DynamicEventExpandableListAdapter(Context context)
    {
        mContext = context;

        mHeaders.add("Animal Movements");
        mEvents.add(new DynamicEvent());
        mHeaders.add("Deaths");
    }

    @Override
    public int getGroupCount() {
        return mHeaders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mEvents.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        DynamicEvent child = new DynamicEvent();

        if(groupPosition==0)
        {
            child = mEvents.get(childPosition);
        }

        if(groupPosition==1)
        {
            child = mEvents.get(childPosition);
        }

        return child;
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
        return true;
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
        headerTitle.setText(mHeaders.get(groupPosition));

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        View view = convertView;

        if(view== null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(groupPosition==0)
                view = inflater.inflate(R.layout.dynamic_event_movement_list_row,null);
            if(groupPosition==1)
                view=inflater.inflate(R.layout.dynamic_event_deaths_list_row,null);

        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
