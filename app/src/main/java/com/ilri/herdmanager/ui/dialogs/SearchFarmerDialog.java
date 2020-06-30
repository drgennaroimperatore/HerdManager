package com.ilri.herdmanager.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.adapters.HerdsAndVisitsListExpandableListAdapter;

public class SearchFarmerDialog extends DialogFragment {

    private Context mContext;
    private  HerdsAndVisitsListExpandableListAdapter mHerdsAndVisitsAdapter;
    public SearchFarmerDialog (Context context, HerdsAndVisitsListExpandableListAdapter adapter)
    {
        mContext = context;
        mHerdsAndVisitsAdapter = adapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.dialog_filter_farmers,null);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText searchFarmerEditText = view.findViewById(R.id.dialog_search_farmer_search_editText);
        Button searchFarmerButton = view.findViewById(R.id.dialog_search_farmer_search_button);

        searchFarmerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHerdsAndVisitsAdapter.filterFarmer(searchFarmerEditText.getText().toString());
                dismiss();
            }
        });



    }




}
