package com.ilri.herdmanager.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ilri.herdmanager.R;

import org.w3c.dom.Text;

public class ErrorDialogFragment extends DialogFragment {
    Context mContext;
    String mErrorMessage;
    public ErrorDialogFragment(Context context, String message) {
        super();
        mContext =context;
        mErrorMessage = message;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.dialog_warning_error,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView errorMessageTV = view.findViewById(R.id.textView_dialog_error_message);
        errorMessageTV.setText(mErrorMessage);
    }
}
