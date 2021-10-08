package com.ilri.herdmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.ADDB;
import com.ilri.herdmanager.database.entities.ADDBDAO;
import com.ilri.herdmanager.database.entities.HerdDao;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.database.entities.SignsForDiseasesForHealthEvent;

import java.util.ArrayList;

public class DiseaseForSingleAnimalSignsDialogAdapter extends ArrayAdapter<SignsForDiseasesForHealthEvent> {
    Context mContext;
    ADDBDAO mADDBDao;
    TextView mSignNameTV, mSignPresenceTV;
    public DiseaseForSingleAnimalSignsDialogAdapter(@NonNull Context context, int resource, ArrayList<SignsForDiseasesForHealthEvent> signsForDiseasesForHealthEvents)
    {
        super(context, resource,signsForDiseasesForHealthEvents);
        mContext=context;
        mADDBDao = ADDB.getInstance(context).getADDBDAO();
    }




    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(R.layout.dialog_disease_signs_list_row,null);
        mSignNameTV = convertView.findViewById(R.id.dialog_disease_signs_list_row_sign_name);
        String signName = mADDBDao.getSignNameFromIDOLD(getItem(position).signID).get(0);
        mSignNameTV.setText(signName);
        mSignPresenceTV = convertView.findViewById(R.id.dialog_disease_signs_list_row_sign_presence);
        mSignPresenceTV.setText(getItem(position).presence);
        return convertView;
    }
}
