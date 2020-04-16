package com.ilri.herdmanager.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.ADDB;
import com.ilri.herdmanager.database.entities.ADDBDAO;
import com.ilri.herdmanager.database.entities.Signs;
import com.ilri.herdmanager.database.entities.SignsForHealthEvent;
import com.ilri.herdmanager.ui.main.AddHeardHealthEventFragment;

import java.util.List;


public class NewSignEventDialog extends DialogFragment {

    Context mContext;
    List<String> mSigns = null;
    EditText mEditTextAffectedBabies, mEditTextAffectedYoung, mEditTextAffectedOld;
    ImageButton mButtonAddSignToHealthEvent;
    AddHeardHealthEventFragment mFragment;
    ADDBDAO addbdao = null;


    public NewSignEventDialog(Context context, List<String> signs, AddHeardHealthEventFragment f)
    {
        mContext=context;
        mSigns = signs;
        addbdao = ADDB.getInstance(context).getADDBDAO();
        mFragment = f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.dialog_new_health_event_sign, null);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Spinner signSpinner = view.findViewById(R.id.health_event_sign_spinner);
        mEditTextAffectedBabies = view.findViewById(R.id.editText_sign_health_event_baby);
        mEditTextAffectedBabies.setHint("0");
        mEditTextAffectedYoung = view.findViewById(R.id.editText_sign_health_event_young);
        mEditTextAffectedYoung.setHint("0");
        mEditTextAffectedOld = view.findViewById(R.id.editText_sign_health_event_old);
        mEditTextAffectedOld.setHint("0");


        mButtonAddSignToHealthEvent = view.findViewById(R.id.button_sign_health_add_sign);

        mButtonAddSignToHealthEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SignsForHealthEvent she = new SignsForHealthEvent();

                String signName = signSpinner.getSelectedItem().toString();
                int signID= addbdao.getSignIDFromName(signName).get(0);
                int nAffectedBabies = Integer.valueOf( mEditTextAffectedBabies.getText().toString());
                int nAffectedYoung =Integer.valueOf( mEditTextAffectedYoung.getText().toString());
                int nAffectedOld = Integer.valueOf(Integer.valueOf( mEditTextAffectedOld.getText().toString()));

                she.signID = signID;
                she.numberOfAffectedBabies  = nAffectedBabies;
                she.numberOfAffectedYoung = nAffectedYoung;
                she.numberOfAffectedOld = nAffectedOld;

                mFragment.addSignToList(she);


                dismiss();

            }
        });



        // String[] dummySigns = {"Sign 1", "Sign 2", "Sign 3"};
        ArrayAdapter<String> signSpinnerAdapter = new ArrayAdapter(mContext,R.layout.health_event_spinner_item, mSigns);
        signSpinner.setAdapter(signSpinnerAdapter);
    }
}
