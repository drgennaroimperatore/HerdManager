package com.ilri.herdmanager.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ilri.herdmanager.R;
import com.ilri.herdmanager.kmz.KMZParser;
import com.ilri.herdmanager.kmz.LocationsLoader;
import com.ilri.herdmanager.ui.dialogs.UsernameSelectionDialog;
import com.ilri.herdmanager.utilities.Info;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
       // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        String UUIDstr = UUID.randomUUID().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("userPrefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        if(!sharedPreferences.contains(Info.SHARED_PREFERENCES_KEY_UUID)) {
            spEditor.putString(Info.SHARED_PREFERENCES_KEY_UUID, UUIDstr);
            spEditor.commit();
        }

        if(!sharedPreferences.contains(Info.SHARED_PREFERENCES_KEY_ISFIRSTACCESS)
                && !sharedPreferences.contains(Info.SHARED_PREFERENCES_KEY_USERNAME))
        {
            new UsernameSelectionDialog(this,sharedPreferences).show();
        }

        try {
            new LocationsLoader().execute(getAssets().open("locations.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

}
