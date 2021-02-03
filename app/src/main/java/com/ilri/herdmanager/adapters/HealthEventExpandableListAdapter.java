package com.ilri.herdmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.ADDB;
import com.ilri.herdmanager.database.entities.ADDBDAO;
import com.ilri.herdmanager.database.entities.BodyCondition;
import com.ilri.herdmanager.database.entities.BodyConditionForHealthEvent;
import com.ilri.herdmanager.database.entities.DiseasesForHealthEvent;
import com.ilri.herdmanager.database.entities.HealthEvent;
import com.ilri.herdmanager.database.entities.HealthIntervention;
import com.ilri.herdmanager.database.entities.HealthInterventionForHealthEvent;
import com.ilri.herdmanager.database.entities.Herd;
import com.ilri.herdmanager.database.entities.HerdDao;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.database.entities.SignsForHealthEvent;
import com.ilri.herdmanager.database.entities.SyncStatus;
import com.ilri.herdmanager.ui.customui.BodyConditionRowContainer;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HealthEventExpandableListAdapter extends BaseExpandableListAdapter {
   Context mContext;
   ArrayList<SignsForHealthEvent> mSignsList = new ArrayList<>();
   ArrayList<DiseasesForHealthEvent> mDiseaseList = new ArrayList<>();
   ArrayList<BodyConditionForHealthEvent> mBodyCondtion = new ArrayList<>();
   ArrayList<HealthInterventionForHealthEvent> mHealthIntervention = new ArrayList<>();
   ArrayList<String> mGroupHeaders;
   ADDBDAO addbdao = null;
   HerdDao herdDao = null;
   boolean isReadOnly = false;
   String mSpecies ="";
    TableLayout mBodyConditionTableLayout= null;
    int mHerdID=-155;
    private boolean mEditableInReadOnly;

    @Override
    public int getGroupCount() {
        return mGroupHeaders.size();
    }

    public HealthEventExpandableListAdapter(Context context, ArrayList<HealthEvent> healthEvents, int herdID)
    {
        mContext = context;
        addbdao = ADDB.getInstance(context).getADDBDAO();
        herdDao= HerdDatabase.getInstance(context).getHerdDao();

        List<BodyCondition> bodyConditionList = herdDao.testBodyConditionTable();

        //Identify the herd name and pass to the initialiser
        isReadOnly= false;
        mHerdID=herdID;


       // mSignsList.add(0,new HealthEvent());

       // mDiseaseList.add(0,new HealthEvent());

        mGroupHeaders = new ArrayList<>();
        //mGroupHeaders.add("Diseases");
        mGroupHeaders.add("Signs");
        mGroupHeaders.add("Health Interventions");
        mGroupHeaders.add("Body Condition");

    }

    public void setReadOnlyData(int herdID,
                                ArrayList<DiseasesForHealthEvent> dhe,
                                ArrayList<SignsForHealthEvent> she,
                                ArrayList<BodyConditionForHealthEvent> bche,
                                ArrayList<HealthInterventionForHealthEvent> hihe) {
        mDiseaseList = dhe;
        mSignsList = she;
        mBodyCondtion = bche;
        mHealthIntervention = hihe;
        notifyDataSetChanged();
        isReadOnly = true;
        mHerdID=herdID;

    }

    @Override
    public int getChildrenCount(int groupPosition) {

        int childSize =0;


       /* if(groupPosition==1)
            childSize = mDiseaseList.size();*/
        if(groupPosition==0)
            childSize = mSignsList.size();
        if(groupPosition==2)
            childSize=1; // body condition
        if(groupPosition==1)
            childSize= mHealthIntervention.size();

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

            if(groupPosition==2)
               // convertView = inflater.inflate(R.layout.herd_health_event_disease_row, null);
                convertView = inflater.inflate(R.layout.health_event_body_condition_row,null);
            if(groupPosition==0)
                convertView = inflater.inflate(R.layout.herd_health_event_signs_row, null);
            if(groupPosition==1)
                convertView = inflater.inflate(R.layout.health_event_health_intervention_row, null);

        //}

        TextView name = null;
        TextView numberOfAffectedBabies = null;
        TextView numberOfAffecedYoung = null;
        TextView numberOfAffectedOld = null;

       /* if(groupPosition==1) // disease
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
        /*    numberOfAffectedBabies = convertView.findViewById(R.id.health_event_disease_row_number_of_affected_babies_text_view);
            numberOfAffecedYoung = convertView.findViewById(R.id.health_event_disease_row_number_of_affected_young_text_view);
            numberOfAffectedOld = convertView.findViewById(R.id.health_event_disease_row_number_of_affected_old_text_view);

            numberOfAffectedBabies.setText(String.valueOf(dhe.numberOfAffectedBabies));
            numberOfAffecedYoung.setText(String.valueOf(dhe.numberOfAffectedYoung));
            numberOfAffectedOld.setText(String.valueOf(dhe.numberOfAffectedOld));
        }*/

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

        if(groupPosition==2)//body condition
        {

            mBodyConditionTableLayout = convertView.findViewById(R.id.health_event_show_body_condition_row_tablelayou);
            initialiseBodyConditionList();
        }

        if(groupPosition==1) // health intervention
        {
            HealthInterventionForHealthEvent healthInterventionForHealthEvent = mHealthIntervention.get(childPosition);
            TextView intervertionNameTV = convertView.findViewById(R.id.health_intervention_row_name_textView);
            String healthInterventionName =  herdDao.getHealthInterventionNameFromID(healthInterventionForHealthEvent.healthInterventionID);
            if(healthInterventionName.contains("-"))
            {
               healthInterventionName= healthInterventionName.split("-")[0].trim();

               if(healthInterventionName.equals("Other"))
                   healthInterventionName =healthInterventionForHealthEvent.comments;
               if(healthInterventionName.equals("Vaccination"))
                   healthInterventionName+=": "+ healthInterventionForHealthEvent.vaccinationName;
            }

            intervertionNameTV.setText(healthInterventionName);

            TextView nAffectedBabiesTV = convertView.findViewById(R.id.health_intervention_row_nAffectedBabies_textView);
            nAffectedBabiesTV.setText(String.valueOf(healthInterventionForHealthEvent.nBabies));

            TextView nAffectedYoungTV = convertView.findViewById(R.id.health_intervention_row_nAffectedYoung_textView);
            nAffectedYoungTV.setText(String.valueOf(healthInterventionForHealthEvent.nYoung));

            TextView nAffectedOldTV = convertView.findViewById(R.id.health_intervention_row_nAffectedOld_textView);
            nAffectedOldTV.setText(String.valueOf(healthInterventionForHealthEvent.nOld));

           /* TextView vaccinationNameTV =convertView.findViewById(R.id.health_intervention_row_vaccination_textView);
            vaccinationNameTV.setText(healthInterventionForHealthEvent.vaccinationName);*/

            TextView commentsTV = convertView.findViewById(R.id.health_intervention_row_comment_textView);
            commentsTV.setVisibility(View.GONE);
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

    public SignsForHealthEvent editSign(int pos, int b, int y, int o)
    {
        SignsForHealthEvent she = mSignsList.get(pos);
        she.numberOfAffectedBabies =b;
        she.numberOfAffectedYoung=y;
        she.numberOfAffectedOld=o;
        notifyDataSetChanged();

        return she;
    }

    public HealthInterventionForHealthEvent
    editHealthInterventionForHealthEvent(int pos, int b, int y, int o)
    {
        HealthInterventionForHealthEvent hihe = mHealthIntervention.get(pos);
        hihe.nBabies =b; hihe.nYoung = y; hihe.nOld =o;
        notifyDataSetChanged();
        return hihe;
    }

    public void deleteHealthInterventionForHealthEvent(int pos)
    {
        mHealthIntervention.remove(pos);
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

    private void initialiseBodyConditionList()
    {
        int val=0;
            Herd herd = herdDao.getHerdByID(mHerdID).get(0);
            switch (addbdao.getAnimalNameFromID(herd.speciesID).get(0)) {
                case " CATTLE":
                    mSpecies = "CATTLE";
                    break;
                case " CAMEL":
                    mSpecies = "CAMEL";
                    break;
                case " SHEEP":
                case " GOAT":
                    mSpecies = "SHEEP_GOAT";
                    break;
            }


        List<BodyCondition> bodyConditionList= herdDao.getBodyConditionBySpecies(mSpecies);

      if(mBodyCondtion.size()==0) {
          for (int i = 0; i < bodyConditionList.size(); i++) {
              BodyCondition bodyCondition = bodyConditionList.get(i);
              BodyConditionRowContainer container = new BodyConditionRowContainer(mContext);
              int level = bodyCondition.stage;
              TableRow row = container.generateTableRow(level, 0, 0, 0);
              mBodyConditionTableLayout.addView(row);
          }
      }
          else
          {
              int c=mBodyConditionTableLayout.getChildCount();
              for (int i=0; i<mBodyCondtion.size(); i++)
              {
                  BodyConditionForHealthEvent bche= mBodyCondtion.get(i);
                  BodyConditionRowContainer container = new BodyConditionRowContainer(mContext);
                  int level = bodyConditionList.get(i).stage;
                  TableRow row = container.generateTableRow(level, bche.nAffectedBabies, bche.nAffectedYoung, bche.nAffectedAdult);
                  mBodyConditionTableLayout.addView(row);
              }
          }
      }


    public void editBodyConditionList(ArrayList<BodyConditionForHealthEvent> conditionForHealthEvents)
    {
      mBodyCondtion= conditionForHealthEvents;
      notifyDataSetChanged();
    }



    public ArrayList<DiseasesForHealthEvent> getDiseasesForHealthEvent() {return mDiseaseList;}
    public ArrayList<SignsForHealthEvent> getSignsForHealthEvent() {return mSignsList;}
    public ArrayList<BodyConditionForHealthEvent>getBodyConditionForHealthEvent() {return mBodyCondtion;}

    public DiseasesForHealthEvent getDiseaseForHealthEvent(int pos) {return mDiseaseList.get(pos);}
    public SignsForHealthEvent getSignsForHealthEvent(int pos) {return mSignsList.get(pos);}
    public BodyConditionForHealthEvent getBodyConditionForHealthEvent (int pos) {return mBodyCondtion.get(pos);}
    public HealthInterventionForHealthEvent getHealthInterventionForHealthEvent(int pos) {return mHealthIntervention.get(pos);}

    public boolean addHealthIntervention(HealthInterventionForHealthEvent healthIntervention)
    {
        mHealthIntervention.add(healthIntervention);
        notifyDataSetChanged();
        return true; // this boolean value is needed for checks
    }

    public void deleteHealthIntervention(HealthInterventionForHealthEvent healthIntervention)
    {
        mHealthIntervention.remove(healthIntervention);
        notifyDataSetChanged();
    }

    public List<HealthInterventionForHealthEvent> getHealthInterventionsForHealthEvent()
    {
        return mHealthIntervention;
    }

    public void setEditableInReadOnly(boolean editable) {
        mEditableInReadOnly = editable;
    }
}
