package com.ilri.herdmanager.managers;

import android.content.Context;

import com.ilri.herdmanager.database.entities.Farmer;
import com.ilri.herdmanager.database.entities.Herd;
import com.ilri.herdmanager.database.entities.HerdDatabase;

import java.util.List;

public class HerdManager {
    private static final HerdManager ourInstance = new HerdManager();

    public static HerdManager getInstance() {
        return ourInstance;
    }

    private HerdManager() {
    }

    public List<Herd> getAllHerdsForFarmer (Context context , int farmerID)
    {
       return HerdDatabase.getInstance(context).getHerdDao().getHerdsByFarmerID(farmerID);
    }

    public List<Farmer> getAllFarmers(Context context)
    {
        return HerdDatabase.getInstance(context).getHerdDao().getAllFarmers();
    }
}
