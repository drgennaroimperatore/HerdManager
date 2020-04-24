package com.ilri.herdmanager.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.adapters.HerdVisitHistoryListAdapter;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.database.entities.HerdVisit;

import java.util.ArrayList;
import java.util.List;

public class HerdVisitHistoryDialog extends DialogFragment {
    ListView mHerdVisitsListView;
    int mHerdID = -155;
    public HerdVisitHistoryDialog(int herdID) {
        super();
        mHerdID = herdID;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.dialog_herd_visit_history, null);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       List<HerdVisit> visits= HerdDatabase.getInstance(getContext()).getHerdDao().getAllHerdVisitsByHerdID(mHerdID);

       if(visits.isEmpty())
       {
           TextView dialogTitleTV = view.findViewById(R.id.textView_herd_visit_history_title);
           dialogTitleTV.setText("There are no visits for this herd");
       }
       else
           {

           mHerdVisitsListView = view.findViewById(R.id.listview_herdvisithistory_dialog);
           HerdVisitHistoryListAdapter adapter = new HerdVisitHistoryListAdapter(getActivity(), 0, visits);
           mHerdVisitsListView.setAdapter(adapter);
       }

    }
}
