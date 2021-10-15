package com.ilri.herdmanager.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ilri.herdmanager.ui.MainActivity;
import com.ilri.herdmanager.ui.NewCaseActivity;
import com.ilri.herdmanager.R;
import com.ilri.herdmanager.ui.dialogs.HowToUseDialog;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ImageButton mAddCaseButton, mHowToUseButton;
    private MainActivity mMainActivityReference;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
               // textView.setText("Welcome To The Herd Manager");
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAddCaseButton = (ImageButton) view.findViewById(R.id.add_case_Button);
        mAddCaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToNewCaseActivityIntent = new Intent(getActivity(), NewCaseActivity.class);
                startActivity(goToNewCaseActivityIntent);
            }
        });
        mHowToUseButton = (ImageButton) view.findViewById(R.id.how_to_use_button);
        mHowToUseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               HowToUseDialog dialog = new HowToUseDialog(getContext());
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                dialog.show(ft, "dialog");

            }
        });
    }
}