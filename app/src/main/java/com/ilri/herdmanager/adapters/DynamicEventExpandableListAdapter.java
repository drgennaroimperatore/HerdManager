package com.ilri.herdmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.AnimalMovementsForDynamicEvent;
import com.ilri.herdmanager.database.entities.DeathsForDynamicEvent;
import com.ilri.herdmanager.database.entities.DynamicEvent;

import java.util.ArrayList;
import java.util.List;

public class DynamicEventExpandableListAdapter extends BaseExpandableListAdapter {

    Context mContext;
    ArrayList<String> mHeaders = new ArrayList<>();
   AnimalMovementsForDynamicEvent mAnimalMovements = new AnimalMovementsForDynamicEvent();
   List<DeathsForDynamicEvent> mDeathsForDynamicEvent = new ArrayList<DeathsForDynamicEvent>();

    public DynamicEventExpandableListAdapter(Context context)
    {
        mContext = context;

        mHeaders.add("Animal Movements");
       // mAnimalMovements.add(new DynamicEvent());
        mHeaders.add("Deaths");
    }

    @Override
    public int getGroupCount() {
        return mHeaders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        int childSize =0;

        if(groupPosition==0)
            return 1;
        if(groupPosition ==1)
            return mDeathsForDynamicEvent.size();

        return childSize;

    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) { return null; }

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

         LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(groupPosition==0) {

                view = inflater.inflate(R.layout.dynamic_event_movement_list_row, null);

                TextView animalsBoughtBabiesTV = view.findViewById(R.id.animal_movement_row_textView_animals_bought_babies);
                TextView animalsBoughtYoungTV = view.findViewById(R.id.animal_movement_row_textView_animals_bought_young);
                TextView animalsBoughtOldTV = view.findViewById(R.id.animal_movement_row_textView_animals_bought_old);

                TextView animalsSoldBabiesTV = view.findViewById(R.id.animal_movement_row_textView_animals_sold_babies);
                TextView animalsSoldYoungTV = view.findViewById(R.id.animal_movement_row_textView_animals_sold_young);
                TextView animalsSoldOldTV = view.findViewById(R.id.animal_movement_row_textView_animals_sold_old);

                TextView animalsLostBabiesTV = view.findViewById(R.id.animal_movement_row_textView_animals_lost_babies);
                TextView animalsLostYoungTV = view.findViewById(R.id.animal_movement_row_textView_animals_lost_young);
                TextView animalsLostOldTV = view.findViewById(R.id.animal_movement_row_textView_animals_lost_old);

                animalsBoughtBabiesTV.setText(String.valueOf(mAnimalMovements.boughtBabies));
                animalsBoughtYoungTV.setText(String.valueOf(mAnimalMovements.boughtYoung));
                animalsBoughtOldTV.setText(String.valueOf(mAnimalMovements.boughtOld));

                animalsSoldBabiesTV.setText(String.valueOf(mAnimalMovements.soldBabies));
                animalsSoldYoungTV.setText(String.valueOf(mAnimalMovements.soldYoung));
                animalsSoldOldTV.setText(String.valueOf(mAnimalMovements.soldOld));

                animalsLostBabiesTV.setText(String.valueOf(mAnimalMovements.lostBabies));
                animalsLostYoungTV.setText(String.valueOf(mAnimalMovements.lostYoung));
                animalsLostOldTV.setText(String.valueOf(mAnimalMovements.lostOld));







            }
            if(groupPosition==1)
                view=inflater.inflate(R.layout.dynamic_event_deaths_list_row,null);



        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void editAnimalMovements(AnimalMovementsForDynamicEvent amde)
    {
        mAnimalMovements = amde;
        notifyDataSetChanged();
    }
}
