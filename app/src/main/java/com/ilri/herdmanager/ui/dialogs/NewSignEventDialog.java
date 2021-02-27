package com.ilri.herdmanager.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ilri.herdmanager.managers.HerdVisitManager;
import com.ilri.herdmanager.ui.customui.SignAutoCompleteTextView;
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
    boolean mEditingInReadOnly;
    int mHerdVisitID =-155;


    public NewSignEventDialog(Context context, List<String> signs,
                              AddHeardHealthEventFragment f,
                              boolean editableInReadOnly,
                              int herdVisitID)
    {
        mContext=context;
        mSigns = signs;
        addbdao = ADDB.getInstance(context).getADDBDAO();
        mFragment = f;
        mEditingInReadOnly = editableInReadOnly;
        mHerdVisitID = herdVisitID;

    }





    public NewSignEventDialog(Context context, List<String> signs,
                              int pos,
                              int affectedBabies,
                              int affectedYoung,
                              int affectedOld,
                              AddHeardHealthEventFragment fragment,
                              boolean isEditingInReadOnly)
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
        mEditingInReadOnly = isEditingInReadOnly;
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

       final SignAutoCompleteTextView signAutoComplete = view.findViewById(R.id.dialog_health_signs_autcomptv);



       // final Spinner signSpinner = view.findViewById(R.id.health_event_sign_spinner);
        if(isEditing)
        {
            //signSpinner.setVisibility(View.GONE);
            signAutoComplete.setVisibility(View.GONE);
            TextView header = view.findViewById(R.id.dialog_new_health_event_sign_dialog_title_textview);
            String signName = mFragment.getSignName(mPositionToEdit);
            if(signName.length()>=11)
                signName = signName.substring(0,10);
            header.setText("Edit "+ signName);
        }
        mEditTextAffectedBabies = view.findViewById(R.id.editText_sign_health_event_baby);
        mEditTextAffectedBabies.setHint("0");
        mEditTextAffectedYoung = view.findViewById(R.id.editText_sign_health_event_young);
        mEditTextAffectedYoung.setHint("0");
        mEditTextAffectedOld = view.findViewById(R.id.editText_sign_health_event_old);
        mEditTextAffectedOld.setHint("0");

        if(nAffectedBabies!=null && nAffectedYoung!=null && nAffectedOld!=null)
        {
            mEditTextAffectedBabies.setText(String.valueOf(nAffectedBabies));
            mEditTextAffectedYoung.setText(String.valueOf(nAffectedYoung));
            mEditTextAffectedOld.setText(String.valueOf(nAffectedOld));
        }



        mButtonAddSignToHealthEvent = view.findViewById(R.id.button_sign_health_add_sign);

        if(isEditing)
        {
            mButtonAddSignToHealthEvent.setText("Edit Sign");
            Button deleteSignButton = view.findViewById(R.id.button_sign_health_delete_sign);
            deleteSignButton.setVisibility(View.VISIBLE);

            deleteSignButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragment.deleteSign(mPositionToEdit);
                    dismiss();
                }
            });
        }


        mButtonAddSignToHealthEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SignsForHealthEvent she = new SignsForHealthEvent();

                String signName = signAutoComplete.getText().toString();
                if(isEditing)
                {
                    signName = mFragment.getSignName(mPositionToEdit);
                }
                if (addbdao.getSignIDFromName(signName).size() == 0)
                {
                    Toast.makeText(mContext,"Invalid Sign!", Toast.LENGTH_LONG).show();

                } else {
                    int signID = addbdao.getSignIDFromName(signName).get(0);

                    if (mEditTextAffectedOld.getText().toString().isEmpty())
                        mEditTextAffectedOld.setText("0");
                    if (mEditTextAffectedYoung.getText().toString().isEmpty())
                        mEditTextAffectedYoung.setText("0");
                    if (mEditTextAffectedBabies.getText().toString().isEmpty())
                        mEditTextAffectedBabies.setText("0");


                    int nAffectedBabies = Integer.valueOf(mEditTextAffectedBabies.getText().toString());
                    int nAffectedYoung = Integer.valueOf(mEditTextAffectedYoung.getText().toString());
                    int nAffectedOld = Integer.valueOf(Integer.valueOf(mEditTextAffectedOld.getText().toString()));


                    if (nAffectedBabies == 0 && nAffectedYoung == 0 && nAffectedOld == 0) {
                        Toast.makeText(mContext,"Please Fill a Field", Toast.LENGTH_LONG).show();

                    } else {
                        she.signID = signID;
                        she.numberOfAffectedBabies = nAffectedBabies;
                        she.numberOfAffectedYoung = nAffectedYoung;
                        she.numberOfAffectedOld = nAffectedOld;

                        if (isEditing) {
                           SignsForHealthEvent editedSign = mFragment.editSign(mPositionToEdit, nAffectedBabies, nAffectedYoung, nAffectedOld);
                            if(mEditingInReadOnly)
                                HerdVisitManager.getInstance().editSignForHealthEvent(mContext,editedSign);
                            dismiss();
                        } else {
                            if (mFragment.addSignToList(she)) {
                                Toast.makeText(mContext,"Please Fill a Field", Toast.LENGTH_LONG).show();
                            } else {
                           //     mFragment.expandList(1);
                                if(mEditingInReadOnly)
                                    HerdVisitManager.getInstance().addSignToExistingVisit(mContext,she,mHerdVisitID);
                                dismiss();
                            }
                        }
                    }
                }
            }
        });

        // String[] dummySigns = {"Sign 1", "Sign 2", "Sign 3"};
        ArrayAdapter<String> signSpinnerAdapter = new ArrayAdapter(mContext,R.layout.health_event_spinner_item, mSigns);

        //signSpinner.setAdapter(signSpinnerAdapter);

        signAutoComplete.setThreshold(0);
        //signAutoComplete.showDropDown();
        signAutoComplete.setAdapter(signSpinnerAdapter);
    }
}
