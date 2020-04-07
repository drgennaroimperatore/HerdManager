package com.ilri.herdmanager.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.ilri.herdmanager.R;

public class FarmerNotAssignedWarningDialog extends Dialog {
    private Button mButtonDismissWarning;

    public FarmerNotAssignedWarningDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_warning_no_farmer_assigned);

        mButtonDismissWarning = findViewById(R.id.button_dismiss_farmer_warning);
        mButtonDismissWarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
