package com.ilri.herdmanager.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.HerdVisit;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

public class HerdVisitInfoDialog extends Dialog
{
    HerdVisit mVisit;
    Context mContext;
    public HerdVisitInfoDialog(@NonNull Context context, HerdVisit visit) {
        super(context);
        mVisit = visit;
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_herd_visit_info);

        TextView visitDateTV = findViewById(R.id.dialog_herd_visit_info_visitDate_TV);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String selectedDate = sdf.format(mVisit.HerdVisitDate);
        visitDateTV.setText("Herd Visit of the "+ selectedDate);

        TextView babyTV = findViewById(R.id.dialog_herd_visit_info_baby_tv);
        babyTV.setText(String.valueOf(mVisit.babiesAtVisit));
        TextView youngTV = findViewById(R.id.dialog_herd_visit_info_young_tv);
        youngTV.setText(String.valueOf(mVisit.youngAtVisit));
        TextView oldTV = findViewById(R.id.dialog_herd_visit_info_old_tv);
        oldTV.setText(String.valueOf(mVisit.oldAtVisit));

        TextView commentsTV = findViewById(R.id.dialog_herd_visit_info_comments_TV);
        commentsTV.setText(mVisit.comments);



        Button closeDialogButton = findViewById(R.id.dialog_herd_visit_info_close_button);
        closeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
