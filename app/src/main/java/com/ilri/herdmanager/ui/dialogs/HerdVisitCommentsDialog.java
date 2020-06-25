package com.ilri.herdmanager.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ilri.herdmanager.R;

public class HerdVisitCommentsDialog extends Dialog {
    private TextView mCharacterLimitTV;
    private String mComments;
    EditText commentsEt;
    public HerdVisitCommentsDialog(@NonNull Context context)
    {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comments_herdvisit);
        mCharacterLimitTV = findViewById(R.id.dialog_comments_char_limit_textView);
       commentsEt = findViewById(R.id.dialog_comments_herdvisit_editBox);
        Button insertCommentButton = findViewById(R.id.dialog_comments_submit_button);
        insertCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mComments = commentsEt.getText().toString();
                dismiss();
            }
        });
        commentsEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mCharacterLimitTV.setText(String.valueOf(s.length())+"/"+"500");

            }
        });


    }
    public String getComments() {return mComments;}
}
