package com.ilri.herdmanager.utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ilri.herdmanager.database.entities.Farmer;
import com.ilri.herdmanager.database.entities.Herd;
import com.ilri.herdmanager.database.entities.HerdDao;
import com.ilri.herdmanager.database.entities.HerdDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class SyncTask extends AsyncTask {
    Context m_context;
    SyncManager manager = SyncManager.getInstance();
    HerdDao herdDao =  HerdDatabase.getInstance(m_context).getHerdDao();

    public SyncTask(Context context) {
        super();
        m_context = context;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }

    @Override
    protected Object doInBackground(Object[] objects)
    {

        ArrayList<Farmer> farmers =(ArrayList<Farmer>) herdDao.getAllFarmers();
        for(Farmer f: farmers)
        {
            int newFarmerID = syncFarmer(f);
            ArrayList<Herd> herdsOwnedByFarmer = (ArrayList) herdDao.getHerdsByFarmerID(f.ID);
            for(Herd h: herdsOwnedByFarmer)
            {
               int newHerdID = syncHerd(h,newFarmerID);
            }

        }

        return null;
    }

    private int syncFarmer(Farmer f)
    {
        String farmerInsertionResponse = manager.insertFarmer(f.firstName,f.secondName,f.region,f.district,f.kebele);

        JSONObject farmerJson = null;
        int farmerNewID = -200;


        try {
            farmerJson = new JSONObject(farmerInsertionResponse);
            farmerNewID =farmerJson.getInt("ID");
            String outcome = farmerJson.getString("outcome");
            Log.i("Sync-NewFarmerID",String.valueOf(farmerNewID));
            Log.i("Sync-outcomeFarmerIns",outcome);

        } catch (JSONException e)
        {
            //the formation of the json string is handled by asp so should be well formed
        }

        return farmerNewID;

    }

    private int syncHerd(Herd herd, int newFarmerID )
    {

        String herdInsertionResponse = manager.insertHerd(String.valueOf(herd.speciesID), String.valueOf(newFarmerID));

        JSONObject herdJson = null;
        int herdNewID = -200;
        try{

            herdJson = new JSONObject(herdInsertionResponse);
            herdNewID = herdJson.getInt("ID");
            String outcome = herdJson.getString("outcome");
            Log.i("Sync-NHerdID", String.valueOf(herdNewID));
            Log.i("Sync-NHOutcome", outcome);

        }catch (JSONException e)
        {

        }

        return herdNewID;
    }

    private int syncHerdVisit(int oldHerdID, int newHerdID)
    {
        return 0;
    }

    private int syncHealthEvent(int oldHerVisitID, int newHerdVisitID)
    {
        return 0;
    }

}
