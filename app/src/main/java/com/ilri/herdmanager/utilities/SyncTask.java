package com.ilri.herdmanager.utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.ilri.herdmanager.database.entities.BirthsForProductivityEvent;
import com.ilri.herdmanager.database.entities.DiseasesForHealthEvent;
import com.ilri.herdmanager.database.entities.Farmer;
import com.ilri.herdmanager.database.entities.HealthEvent;
import com.ilri.herdmanager.database.entities.Herd;
import com.ilri.herdmanager.database.entities.HerdDao;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.database.entities.HerdVisit;
import com.ilri.herdmanager.database.entities.MilkProductionForProductivityEvent;
import com.ilri.herdmanager.database.entities.ProductivityEvent;
import com.ilri.herdmanager.database.entities.SignsForHealthEvent;

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
    HerdDao herdDao;

    public SyncTask(Context context) {
        super();
        m_context = context;
        herdDao =  HerdDatabase.getInstance(m_context).getHerdDao();
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
               ArrayList<HerdVisit> visitsForHerd = (ArrayList) herdDao.getAllHerdVisitsByHerdID(h.ID);

               for (HerdVisit hv: visitsForHerd )
               {
                   int newVisitID = syncHerdVisit(hv, newHerdID);

                   HealthEvent healthEventForVisit = herdDao.getHealthEventForVisit(hv.ID).get(0);
                   int newHealthEventID = syncHealthEvent(healthEventForVisit, newVisitID);

                   ArrayList<DiseasesForHealthEvent> diseasesForHealthEvent = (ArrayList<DiseasesForHealthEvent>) herdDao.getDiseasesForHealthEvent(healthEventForVisit.ID);
                   ArrayList<SignsForHealthEvent> signsForHealthEvent = (ArrayList) herdDao.getSignsForHealthEvent(healthEventForVisit.ID);

                   for(DiseasesForHealthEvent dhe:diseasesForHealthEvent)
                   {
                       syncDiseaseForHealthEvent(dhe,newHealthEventID);

                   }
                   for(SignsForHealthEvent she: signsForHealthEvent)
                   {
                       syncSignForHealthEvent(she, newHealthEventID);

                   }

                   ProductivityEvent productivityEventForVisit= herdDao.getProductivityEventForVisit(hv.ID).get(0);
                   int newProdEventID = syncProductivityEvent(productivityEventForVisit,newVisitID);

                   MilkProductionForProductivityEvent mpe = herdDao.getMilkProductionForProductivityEvent(productivityEventForVisit.ID).get(0);
                   syncMilkProductionForProdEvent(mpe,newProdEventID);

                   BirthsForProductivityEvent bpe = herdDao.getBirthsForProductivityEvent(productivityEventForVisit.ID).get(0);
                   syncBirthsForProductivityEvent(bpe, newProdEventID);

               } // for herd visit
            } // for herd

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

    private int syncHerdVisit(HerdVisit visit, int newHerdID)
    {
        String visitInsertionResponse = manager.insertHerdVisit(visit,newHerdID);

        JSONObject visitJson = null;
        int vistNewID = -200;

        try {
            visitJson = new JSONObject(visitInsertionResponse);
            vistNewID = visitJson.getInt("ID");
            String outcome = visitJson.getString("outcome");
            Log.i("Sync-NVisitID", String.valueOf(vistNewID));
            Log.i("Sync-NHVOutcome", outcome);

        }
        catch (JSONException e)
        {

        }

        return vistNewID;
    }

    private int syncHealthEvent(HealthEvent healthEvent, int newHerdVisitID)
    {
        String healthEventInsertionResponse = manager.insertHealthEvent(healthEvent,newHerdVisitID);

        JSONObject healthEventJson = null;
        int healthEventNewID = -200;

        try {
            healthEventJson = new JSONObject(healthEventInsertionResponse);
            healthEventNewID = healthEventJson.getInt("ID");
            String outcome = healthEventJson.getString("outcome");
            Log.i("Sync-NHealthEventID", String.valueOf(healthEventNewID));
            Log.i("Sync-NHEEVOutcome", outcome);

        }
        catch (JSONException e)
        {

        }


        return healthEventNewID;
    }

    private int syncDiseaseForHealthEvent(DiseasesForHealthEvent dhe, int newHealthEventID)
    {
        String dheInsertionResponse = manager.insertDiseaseForHealthEvent(dhe, newHealthEventID);

        JSONObject dheJson = null;
        int dheNewID = -200;

        try
        {
            dheJson = new JSONObject(dheInsertionResponse);
            dheNewID = dheJson.getInt("ID");
            String outcome =dheJson.getString("outcome");
            Log.i("Sync-NDheID",String.valueOf(dheNewID));
            Log.i("Sync-NDheOutcome",outcome);
        }
        catch (JSONException e)
        {

        }

        return dheNewID;
    }

    private int syncSignForHealthEvent(SignsForHealthEvent she, int newHealthEventID)
    {
        String sheInsertionResponse = manager.insertSignForHealthEvent(she, newHealthEventID);

        JSONObject sheJson = null;
        int sheNewID = -200;

        try
        {
            sheJson = new JSONObject(sheInsertionResponse);
            sheNewID = sheJson.getInt("ID");
            String outcome =sheJson.getString("outcome");
            Log.i("Sync-NSheID",String.valueOf(sheNewID));
            Log.i("Sync-SheOutcome",outcome);
        }
        catch (JSONException e)
        {

        }

        return sheNewID;

    }

    private int syncProductivityEvent(ProductivityEvent productivityEvent, int newHerdVisitID)
    {
        String productivityInsertionResponse = manager.insertProductivityEvent(productivityEvent, newHerdVisitID);

        JSONObject prodJson = null;
        int prodNewID = -200;

        try
        {
            prodJson = new JSONObject(productivityInsertionResponse);
            prodNewID = prodJson.getInt("ID");
            String outcome =prodJson.getString("outcome");
            Log.i("Sync-NprodID",String.valueOf(prodNewID));
            Log.i("Sync-prodOutcome",outcome);
        }
        catch (JSONException e)
        {

        }


        return prodNewID;
    }

    private int syncBirthsForProductivityEvent(BirthsForProductivityEvent bpe, int newProdEventID)
    {
        String birthInsertionResponse = manager.insertBirthsForProductivityEvent(bpe, newProdEventID);

        JSONObject birthsJson = null;
        int birthsNewID = -200;

        try
        {
            birthsJson = new JSONObject(birthInsertionResponse);
            birthsNewID = birthsJson.getInt("ID");
            String outcome =birthsJson.getString("outcome");
            Log.i("Sync-NBirthID",String.valueOf(birthsNewID));
            Log.i("Sync-BirthOutcome",outcome);
        }
        catch (JSONException e)
        {

        }

        return birthsNewID;
    }

    private int syncMilkProductionForProdEvent(MilkProductionForProductivityEvent mpe, int newProdEventID)
    {
        String milkInsertionResponse = manager.insertMilkForProducitivityEvent(mpe, newProdEventID);

        JSONObject milkJson = null;
        int milkNewID = -200;

        try
        {
            milkJson = new JSONObject(milkInsertionResponse);
            milkNewID = milkJson.getInt("ID");
            String outcome =milkJson.getString("outcome");
            Log.i("Sync-NMilkID",String.valueOf(milkNewID));
            Log.i("Sync-MilkOutcome",outcome);
        }
        catch (JSONException e)
        {

        }

        return milkNewID;
    }

}
