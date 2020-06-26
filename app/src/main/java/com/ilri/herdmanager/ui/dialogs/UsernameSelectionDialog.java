package com.ilri.herdmanager.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.utilities.Info;

public class UsernameSelectionDialog extends Dialog {
    private SharedPreferences mSharedPreferences;
    public UsernameSelectionDialog(@NonNull Context context, SharedPreferences sp)
    {
        super(context);
        setContentView(R.layout.dialog_username_selection);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        mSharedPreferences = sp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final EditText nameET = findViewById(R.id.dialog_info_FirstName_editText);
        final EditText secondNameET = findViewById(R.id.dialog_info_SecondName_editText);
        final EditText additionalInfoET = findViewById(R.id.dialog_info_Additional_editText);
        final Button submitButton = findViewById(R.id.dialog_info_submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nameET.getText().toString().isEmpty() || secondNameET.getText().toString().isEmpty() || additionalInfoET.getText().toString().isEmpty() )
                {
                    submitButton.setText("Please Fill all Fields");
                }
                else {
                    SharedPreferences.Editor spEditor = mSharedPreferences.edit();
                    spEditor.putBoolean(Info.SHARED_PREFERENCES_KEY_ISFIRSTACCESS, true);
                    String username = nameET.getText().toString() + "," + secondNameET.getText().toString() + "," + additionalInfoET.getText().toString();
                    spEditor.putString(Info.SHARED_PREFERENCES_KEY_USERNAME, username);
                    spEditor.commit();
                    dismiss();
                }
            }
        });
    }
}
