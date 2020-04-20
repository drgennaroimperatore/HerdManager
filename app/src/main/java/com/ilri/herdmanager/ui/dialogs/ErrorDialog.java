package com.ilri.herdmanager.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ilri.herdmanager.R;

public class ErrorDialog extends Dialog {
    private String mError = null;
    public ErrorDialog(@NonNull Context context, String error) {
        super(context);
        mError = error;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_warning_error);
        TextView errorMessageTV = findViewById(R.id.textView_dialog_error_message);
        errorMessageTV.setText(mError);
    }
}
