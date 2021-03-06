package com.ilri.herdmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.ADDB;
import com.ilri.herdmanager.database.entities.ADDBDAO;
import com.ilri.herdmanager.database.entities.DiseasesForHealthEvent;
import com.ilri.herdmanager.database.entities.HealthEvent;
import com.ilri.herdmanager.database.entities.SignsForHealthEvent;
import com.ilri.herdmanager.database.entities.SyncStatus;

import java.util.ArrayList;

public class HealthEventExpandableListAdapter extends BaseExpandableListAdapter {
   Context mContext;
   ArrayList<SignsForHealthEvent> mSignsList = new ArrayList<>();
   ArrayList<DiseasesForHealthEvent> mDiseaseList = new ArrayList<>();
   ArrayList<String> mGroupHeaders;
   ADDBDAO addbdao = null;
   boolean isReadOnly = false;

    @Override
    public int getGroupCount() {
        return mGroupHeaders.size();
    }

    public HealthEventExpandableListAdapter(Context context, ArrayList<HealthEvent> healthEvents)
    {
        mContext = context;
        addbdao = ADDB.getInstance(context).getADDBDAO();

       // mSignsList.add(0,new HealthEvent());

       // mDiseaseList.add(0,new HealthEvent());

        mGroupHeaders = new ArrayList<>();
        //mGroupHeaders.add("Diseases");
        mGroupHeaders.add("Signs");
        isReadOnly= false;
    }

    public void setReadOnlyData(ArrayList<DiseasesForHealthEvent> dhe, ArrayList<SignsForHealthEvent> she)
    {
        mDiseaseList = dhe;
        mSignsList = she;
        notifyDataSetChanged();
        isReadOnly= true;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        int childSize =0;


        if(groupPosition==1)
            childSize = mDiseaseList.size();
        if(groupPosition==0)
            childSize = mSignsList.size();



        return childSize;
    }

    @Override
    public Object getGroup(int groupPosition)  {
        groupPosition=1;
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

//

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


      //  if(convertView == null)
      //  {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(groupPosition==1)
                convertView = inflater.inflate(R.layout.herd_health_event_disease_row, null);
            if(groupPosition==0)
                convertView = inflater.inflate(R.layout.herd_health_event_signs_row, null);

        //}

        TextView name = null;
        TextView numberOfAffectedBabies = null;
        TextView numberOfAffecedYoung = null;
        TextView numberOfAffectedOld = null;

        if(groupPosition==1) // disease
        {
            DiseasesForHealthEvent dhe = mDiseaseList.get(childPosition);

            name = convertView.findViewById(R.id.health_event_disease_row_diseaseName);
            name.setText(addbdao.getDiseaseNameFromId(dhe.diseaseID).get(0));

          /*  if (isReadOnly)
            {
                ImageView syncStatusImgView = convertView.findViewById(R.id.health_event_disease_row_syncStatus_imgView);
                syncStatusImgView.setVisibility(View.VISIBLE);

                if(dhe.syncStatus.equals(SyncStatus.NOT_SYNCHRONISED.toString()))
                    syncStatusImgView.setImageResource(R.drawable.drawable_sync_status_not_synced);
                if(dhe.syncStatus.equals(SyncStatus.PARTIALLY_SYNCHRONISED.toString()))
                    syncStatusImgView.setImageResource(R.drawable.drawable_sync_status_partially_synced);
                if(dhe.syncStatus.equals(SyncStatus.SYNCHRNOISED.toString()))
                    syncStatusImgView.setImageResource(R.drawable.drawable_sync_status_synced);
            }*/

            numberOfAffectedBabies = convertView.findViewById(R.id.health_event_disease_row_number_of_affected_babies_text_view);
            numberOfAffecedYoung = convertView.findViewById(R.id.health_event_disease_row_number_of_affected_young_text_view);
            numberOfAffectedOld = convertView.findViewById(R.id.health_event_disease_row_number_of_affected_old_text_view);

            numberOfAffectedBabies.setText(String.valueOf(dhe.numberOfAffectedBabies));
            numberOfAffecedYoung.setText(String.valueOf(dhe.numberOfAffectedYoung));
            numberOfAffectedOld.setText(String.valueOf(dhe.numberOfAffectedOld));

        }

        if(groupPosition==0) // signs
        {
            SignsForHealthEvent she = mSignsList.get(childPosition);

            name = convertView.findViewById(R.id.health_event_sign_row_signName);
            name.setText(addbdao.getSignNameFromID(she.signID).get(0));

           /* if (isReadOnly)
            {
                ImageView syncStatusImgView = convertView.findViewById(R.id.health_event_sign_row_syncStatus_imgView);
                syncStatusImgView.setVisibility(View.VISIBLE);

                if(she.syncStatus.equals(SyncStatus.NOT_SYNCHRONISED.toString()))
                    syncStatusImgView.setImageResource(R.drawable.drawable_sync_status_not_synced);
                if(she.syncStatus.equals(SyncStatus.PARTIALLY_SYNCHRONISED.toString()))
                    syncStatusImgView.setImageResource(R.drawable.drawable_sync_status_partially_synced);
                if(she.syncStatus.equals(SyncStatus.SYNCHRNOISED.toString()))
                    syncStatusImgView.setImageResource(R.drawable.drawable_sync_status_synced);
            }*/

            numberOfAffectedBabies = convertView.findViewById(R.id.health_event_sign_row_number_of_affected_babies_text_view);
            numberOfAffecedYoung = convertView.findViewById(R.id.health_event_sign_row_number_of_affected_young_text_view);
            numberOfAffectedOld = convertView.findViewById(R.id.health_event_sign_row_number_of_affected_old_text_view);

            numberOfAffectedBabies.setText(String.valueOf(she.numberOfAffectedBabies));
            numberOfAffecedYoung.setText(String.valueOf(she.numberOfAffectedYoung));
            numberOfAffectedOld.setText(String.valueOf(she.numberOfAffectedOld));

        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean addNewSign (SignsForHealthEvent she)
    {

        for(SignsForHealthEvent s: mSignsList)
        {
            if(she.equals(s))
            {
                return true;
            }
        }

        mSignsList.add(she);
        notifyDataSetChanged();


        return false;
    }
    public boolean addNewDisease(DiseasesForHealthEvent dhe)
    {


        for(DiseasesForHealthEvent d:mDiseaseList)
            if(dhe.equals(d))
            {
                return true;
            }

        mDiseaseList.add(dhe);
        notifyDataSetChanged();

        return false;

    }

    public void editDisease( int pos, int b, int y, int o)
    {
       DiseasesForHealthEvent dhe = mDiseaseList.get(pos);
       dhe.numberOfAffectedOld = o;
       dhe.numberOfAffectedYoung = y;
       dhe.numberOfAffectedBabies = b;
       notifyDataSetChanged();
    }

    public void editSign(int pos, int b, int y, int o)
    {
        SignsForHealthEvent she = mSignsList.get(pos);
        she.numberOfAffectedBabies =b;
        she.numberOfAffectedYoung=y;
        she.numberOfAffectedOld=o;
        notifyDataSetChanged();
    }

    public void deleteDisease (int pos)
    {
        mDiseaseList.remove(pos);
        notifyDataSetChanged();
    }

    public void deleteSign(int pos)
    {
        mSignsList.remove(pos);
        notifyDataSetChanged();
    }



    public ArrayList<DiseasesForHealthEvent> getDiseasesForHealthEvent() {return mDiseaseList;}
    public ArrayList<SignsForHealthEvent> getSignsForHealthEvent() {return mSignsList;}

    public DiseasesForHealthEvent getDiseaseForHealthEvent(int pos) {return mDiseaseList.get(pos);}
    public SignsForHealthEvent getSignsForHealthEvent(int pos) {return mSignsList.get(pos);}
}
