package com.ilri.herdmanager.ui.notifications;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.ilri.herdmanager.R;
import com.ilri.herdmanager.ui.dialogs.ErrorDialogFragment;
import com.ilri.herdmanager.utilities.SyncTask;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);



            }
        });



        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        final ImageButton syncButton = view.findViewById(R.id.button_sync_data);
        final TextView syncStatusTV = view.findViewById(R.id.sync_button_status_textView);
        final NotificationsFragment nf = this;

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SyncTask st = new SyncTask(nf,getContext(),syncButton,syncStatusTV);
                String[] P= {};
                st.execute(P);

            }

        });


    }

    public void showErrorMessage(String error)
    {
        ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment(getContext(),error);
        errorDialogFragment.show(getFragmentManager(),"dialog");
    }

    public void showInfoToast(String message)
    {
        new AlertDialog.Builder(getContext())
            .setTitle("Delete entry")
            .setMessage("Are you sure you want to delete this entry?")

            // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Continue with delete operation
                }
            })

            // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }

}