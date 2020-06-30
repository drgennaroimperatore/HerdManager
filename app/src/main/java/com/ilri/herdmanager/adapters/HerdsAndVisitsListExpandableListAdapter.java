package com.ilri.herdmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.ADDB;
import com.ilri.herdmanager.database.entities.Farmer;
import com.ilri.herdmanager.database.entities.Herd;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.database.entities.HerdVisit;
import com.ilri.herdmanager.database.entities.SyncStatus;
import com.ilri.herdmanager.managers.HerdManager;
import com.ilri.herdmanager.ui.dialogs.HerdVisitHistoryDialog;
import com.ilri.herdmanager.ui.main.AddHerdVisitActivity;

import java.util.List;

public class HerdsAndVisitsListExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<Farmer> mGroupHeaders = null;
    private HerdManager mHerdManager = HerdManager.getInstance();
    private FragmentManager mFragmentManager;



    public HerdsAndVisitsListExpandableListAdapter() {
        super();
    }

    public HerdsAndVisitsListExpandableListAdapter(Context context, FragmentManager fm) {
        super();
        mContext = context;
        mGroupHeaders = HerdManager.getInstance().getAllFarmers(context);
        mFragmentManager = fm;
    }


    @Override
    public int getGroupCount() {
        return mGroupHeaders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        int farmerID = mGroupHeaders.get(groupPosition).ID;
        return mHerdManager.getAllHerdsForFarmer(mContext,farmerID).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupHeaders.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        int farmerID = mGroupHeaders.get(groupPosition).ID;
        return mHerdManager.getAllHerdsForFarmer(mContext,farmerID).get(childPosition);
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
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

       Farmer f = mGroupHeaders.get(groupPosition);

        View view = convertView;

        if(view== null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.manage_herds_visits_list_header,null);

        }

        TextView headingFarmerName = view.findViewById(R.id.herd_visits_list_header_farmer_name_textview);
        headingFarmerName.setText(f.firstName + " " + f.secondName );

        ImageView syncStatusImageView = view.findViewById(R.id.manage_herd_visits_list_header_syncStatus_imageView);
        String syncStatusString = f.syncStatus.toString();

        if(syncStatusString.equals( SyncStatus.NOT_SYNCHRONISED.toString()))
            syncStatusImageView.setImageResource(R.drawable.drawable_sync_status_not_synced);
        if(syncStatusString.equals( SyncStatus.PARTIALLY_SYNCHRONISED.toString()))
            syncStatusImageView.setImageResource(R.drawable.drawable_sync_status_partially_synced);
        if(syncStatusString.equals( SyncStatus.SYNCHRNOISED.toString()))
            syncStatusImageView.setImageResource(R.drawable.drawable_sync_status_synced);


        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        View view = convertView;

        int farmerID = mGroupHeaders.get(groupPosition).ID;
       final Herd herd = mHerdManager.getAllHerdsForFarmer(mContext,farmerID).get(childPosition);

        if(view== null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.manage_herds_visits_list_secondary_row,null);

        }

        int speciesID = herd.speciesID;
        String herdSpeciesInfo = ADDB.getInstance(mContext).getADDBDAO().getAnimalNameFromID(speciesID).get(0);
        TextView herdInfoHeaderTV = view.findViewById(R.id.manage_herd_visits_list_secondary_row_herd_info_textview);
        herdInfoHeaderTV.setText("Herd of "+ herdSpeciesInfo);

        ImageView syncStatusImgView= view.findViewById(R.id.manage_herd_visits_list_secondary_row_herd_info_syncStatus_imgView);
        String syncStatus = herd.syncStatus;

        if(syncStatus.equals(SyncStatus.SYNCHRNOISED.toString()))
            syncStatusImgView.setImageResource(R.drawable.drawable_sync_status_synced);
        if(syncStatus.equals(SyncStatus.PARTIALLY_SYNCHRONISED.toString()))
            syncStatusImgView.setImageResource(R.drawable.drawable_sync_status_partially_synced);
        if(syncStatus.equals(SyncStatus.NOT_SYNCHRONISED.toString()))
            syncStatusImgView.setImageResource(R.drawable.drawable_sync_status_not_synced);


        Button addVisitToHerdButton, consultHerdVisitInformationButton;

        addVisitToHerdButton = view.findViewById(R.id.herd_vist_list_secondary_row_add_visit_button);

        addVisitToHerdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToAddHerdVisitActivity = new Intent(mContext, AddHerdVisitActivity.class);
                goToAddHerdVisitActivity.putExtra("herdID",herd.ID);
                mContext.startActivity(goToAddHerdVisitActivity);

            }
        });
        consultHerdVisitInformationButton = view.findViewById(R.id.herd_vist_list_secondary_row_consult_visits_button);

        consultHerdVisitInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HerdVisitHistoryDialog dialog = new HerdVisitHistoryDialog(herd.ID);
                dialog.show(mFragmentManager,"dialog");

            }
        });






        return view;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

public void filterFarmer(String searchQuery)
{

    mGroupHeaders = HerdDatabase.getInstance(mContext).getHerdDao().getFarmerbyName(searchQuery);
    notifyDataSetChanged();
}

public void resetFarmerList()
{
    mGroupHeaders = HerdDatabase.getInstance(mContext).getHerdDao().getAllFarmers();
    notifyDataSetChanged();
}
}



