package com.ilri.herdmanager.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;
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
    Button mButtonAddSignToHealthEvent;
    AddHeardHealthEventFragment mFragment;
    ADDBDAO addbdao = null;
   Integer nAffectedBabies, nAffectedYoung, nAffectedOld;
    Integer mPositionToEdit;
    boolean isEditing = false;


    public NewSignEventDialog(Context context, List<String> signs, AddHeardHealthEventFragment f)
    {
        mContext=context;
        mSigns = signs;
        addbdao = ADDB.getInstance(context).getADDBDAO();
        mFragment = f;
    }

    public NewSignEventDialog(Context context, List<String> signs,
                              int pos,
                              int affectedBabies,
                              int affectedYoung,
                              int affectedOld,
                              AddHeardHealthEventFragment fragment)
    {
        mContext=context;
        mSigns = signs;
        addbdao = ADDB.getInstance(context).getADDBDAO();
        mFragment = fragment;
        nAffectedBabies = affectedBabies;
        nAffectedYoung= affectedYoung;
        nAffectedOld = affectedOld;
        isEditing = true;
        mPositionToEdit = pos;
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

        final CoordinatorLayout cl = view.findViewById(R.id.dialog_sign_parent);

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

                if(mEditTextAffectedOld.getText().toString().isEmpty())
                    mEditTextAffectedOld.setText("0");
                if(mEditTextAffectedYoung.getText().toString().isEmpty())
                    mEditTextAffectedYoung.setText("0");
                if(mEditTextAffectedBabies.getText().toString().isEmpty())
                    mEditTextAffectedBabies.setText("0");

                int nAffectedBabies = Integer.valueOf( mEditTextAffectedBabies.getText().toString());
                int nAffectedYoung =Integer.valueOf( mEditTextAffectedYoung.getText().toString());
                int nAffectedOld = Integer.valueOf(Integer.valueOf( mEditTextAffectedOld.getText().toString()));



                if(nAffectedBabies==0 && nAffectedYoung==0 && nAffectedOld==0)
                {
                    Snackbar mySnackbar = Snackbar.make(cl, "No Animal Was Affected", Snackbar.LENGTH_LONG);
                    mySnackbar.getView().setBackgroundColor(R.color.black);
                    mySnackbar.show();
                }
                else {

                    she.signID = signID;
                    she.numberOfAffectedBabies = nAffectedBabies;
                    she.numberOfAffectedYoung = nAffectedYoung;
                    she.numberOfAffectedOld = nAffectedOld;

                   if(mFragment.addSignToList(she))
                   {
                       Snackbar mySnackbar = Snackbar.make(cl, "This sign was already inserted", Snackbar.LENGTH_LONG);
                       mySnackbar.getView().setBackgroundColor(R.color.black);
                       mySnackbar.show();}
                   else {
                       mFragment.expandList(1);
                       dismiss();
                   }
                }

            }
        });



        // String[] dummySigns = {"Sign 1", "Sign 2", "Sign 3"};
        ArrayAdapter<String> signSpinnerAdapter = new ArrayAdapter(mContext,R.layout.health_event_spinner_item, mSigns);
        signSpinner.setAdapter(signSpinnerAdapter);
    }
}
