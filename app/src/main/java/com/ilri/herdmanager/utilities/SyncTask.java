package com.ilri.herdmanager.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Button;

import com.ilri.herdmanager.database.entities.AnimalMovementsForDynamicEvent;
import com.ilri.herdmanager.database.entities.BirthsForProductivityEvent;
import com.ilri.herdmanager.database.entities.DeathsForDynamicEvent;
import com.ilri.herdmanager.database.entities.DiseasesForHealthEvent;
import com.ilri.herdmanager.database.entities.DynamicEvent;
import com.ilri.herdmanager.database.entities.Farmer;
import com.ilri.herdmanager.database.entities.HealthEvent;
import com.ilri.herdmanager.database.entities.Herd;
import com.ilri.herdmanager.database.entities.HerdDao;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.database.entities.HerdVisit;
import com.ilri.herdmanager.database.entities.MilkProductionForProductivityEvent;
import com.ilri.herdmanager.database.entities.ProductivityEvent;
import com.ilri.herdmanager.database.entities.SignsForHealthEvent;
import com.ilri.herdmanager.database.entities.SyncStatus;

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
    Button mButton;
    String mOriginalButtonText ="";

    public SyncTask(Context context, Button button) {
        super();
        m_context = context;
        herdDao =  HerdDatabase.getInstance(m_context).getHerdDao();
        mButton = button;
    }



    @Override
    protected void onPostExecute(Object o) {

        super.onPostExecute(o);
        mButton.setEnabled(true);
        mButton.setText(mOriginalButtonText);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mButton.setEnabled(false);
        mOriginalButtonText = mButton.getText().toString();
        mButton.setText("Sync in Progress...");

    }

    @Override
    protected Object doInBackground(Object[] objects)
    {

        ArrayList<Farmer> farmers =(ArrayList<Farmer>) herdDao.getAllFarmers();
        farmerloop:
        for(Farmer f: farmers)
        {
            if(f.syncStatus.equals( SyncStatus.SYNCHRNOISED.toString()))
                continue ;

            int newFarmerID = syncFarmer(f);
            ArrayList<Herd> herdsOwnedByFarmer = (ArrayList) herdDao.getHerdsByFarmerID(f.ID);

            if(newFarmerID==-200)
                break farmerloop;

            f.syncStatus = SyncStatus.PARTIALLY_SYNCHRONISED.toString();
            f.serverID= newFarmerID;
            herdDao.UpdateFarmer(f);

            for(Herd h: herdsOwnedByFarmer)
            {
                if(h.syncStatus.equals( SyncStatus.SYNCHRNOISED.toString()))
                    continue;


                int newHerdID = syncHerd(h,newFarmerID);
                if(newHerdID==-200)
                    break farmerloop;


                h.syncStatus = SyncStatus.PARTIALLY_SYNCHRONISED.toString();

                h.serverID = newHerdID;
                herdDao.UpdateHerd(h);

               ArrayList<HerdVisit> visitsForHerd = (ArrayList) herdDao.getAllHerdVisitsByHerdID(h.ID);

               for (HerdVisit hv: visitsForHerd )
               {
                   if(hv.syncStatus.equals( SyncStatus.SYNCHRNOISED.toString()))
                       continue;

                   int newVisitID = syncHerdVisit(hv, newHerdID);
                   if(newVisitID==-200)
                   {
                       break farmerloop;
                   }


                   hv.syncStatus= SyncStatus.PARTIALLY_SYNCHRONISED.toString();
                   hv.serverID= newVisitID;
                   herdDao.UpdateHerdVisit(hv);

                   HealthEvent healthEventForVisit = herdDao.getHealthEventForVisit(hv.ID).get(0);
                   int newHealthEventID = syncHealthEvent(healthEventForVisit, newVisitID);

                   if(newHealthEventID==-200)
                       break farmerloop;


                   healthEventForVisit.syncStatus = SyncStatus.PARTIALLY_SYNCHRONISED.toString();
                   healthEventForVisit.serverID = newHealthEventID;
                   herdDao.UpdateHealthEvent(healthEventForVisit);




                   ArrayList<DiseasesForHealthEvent> diseasesForHealthEvent = (ArrayList<DiseasesForHealthEvent>) herdDao.getDiseasesForHealthEvent(healthEventForVisit.ID);
                   ArrayList<SignsForHealthEvent> signsForHealthEvent = (ArrayList) herdDao.getSignsForHealthEvent(healthEventForVisit.ID);

                   for(DiseasesForHealthEvent dhe:diseasesForHealthEvent)
                   {
                       if(dhe.syncStatus.equals( SyncStatus.SYNCHRNOISED.toString()))
                           continue;

                      int dheID = syncDiseaseForHealthEvent(dhe,newHealthEventID);
                      if(dheID==-200)
                          break farmerloop;
                      dhe.syncStatus = SyncStatus.SYNCHRNOISED.toString();
                      dhe.serverID = dheID;
                      herdDao.UpdateDiseaseForHealthEvent(dhe);

                   }
                   for(SignsForHealthEvent she: signsForHealthEvent)
                   {
                       if(she.syncStatus.equals( SyncStatus.SYNCHRNOISED.toString()))
                           continue;

                     int sheID= syncSignForHealthEvent(she, newHealthEventID);
                     if(sheID==-200)
                         break farmerloop;

                     she.syncStatus = SyncStatus.SYNCHRNOISED.toString();
                     she.serverID = sheID;
                     herdDao.UpdateSignForHealthEvent(she);

                   }
                   healthEventForVisit.syncStatus = SyncStatus.SYNCHRNOISED.toString();
                   herdDao.UpdateHealthEvent(healthEventForVisit);

                   ProductivityEvent productivityEventForVisit= herdDao.getProductivityEventForVisit(hv.ID).get(0);
                   int newProdEventID = syncProductivityEvent(productivityEventForVisit,newVisitID);

                   if(newProdEventID ==-200)
                       break farmerloop;

                   productivityEventForVisit.syncStatus = SyncStatus.PARTIALLY_SYNCHRONISED.toString();
                   productivityEventForVisit.serverID= newProdEventID;
                   herdDao.UpdateProductivityEvent(productivityEventForVisit);



                   MilkProductionForProductivityEvent mpe = herdDao.getMilkProductionForProductivityEvent(productivityEventForVisit.ID).get(0);
                   if(!mpe.syncStatus.equals(SyncStatus.SYNCHRNOISED)) {


                       int mpeID = syncMilkProductionForProdEvent(mpe, newProdEventID);

                       if (mpeID == -200)
                           break farmerloop;
                       mpe.serverID = mpeID;
                       mpe.syncStatus = SyncStatus.SYNCHRNOISED.toString();
                       herdDao.UpdateMilkForProductivityEvent(mpe);
                   }

                   BirthsForProductivityEvent bpe = herdDao.getBirthsForProductivityEvent(productivityEventForVisit.ID).get(0);
                   if(!bpe.syncStatus.equals(SyncStatus.SYNCHRNOISED)) {
                       int bpeID = syncBirthsForProductivityEvent(bpe, newProdEventID);

                       if (bpeID == -200)
                           break farmerloop;
                       bpe.serverID = bpeID;
                       bpe.syncStatus = SyncStatus.SYNCHRNOISED.toString();
                       herdDao.UpdateBirthsForProductivityEvent(bpe);
                   }

                   productivityEventForVisit.syncStatus = SyncStatus.SYNCHRNOISED.toString();

                   herdDao.UpdateProductivityEvent(productivityEventForVisit);

                   DynamicEvent dynamicEventForVisit = herdDao.getDynamicEventForVisit(hv.ID).get(0);
                   int newDynamicEventID =syncDynamicEvent(dynamicEventForVisit,newVisitID);

                   if(newDynamicEventID==-200)
                       break farmerloop;

                   dynamicEventForVisit.syncStatus = SyncStatus.PARTIALLY_SYNCHRONISED.toString();
                   dynamicEventForVisit.serverID = newDynamicEventID;
                   herdDao.UpdateDynamicEvent(dynamicEventForVisit);

                   AnimalMovementsForDynamicEvent amde = herdDao.getAnimalMovementsForDynamicEvent(dynamicEventForVisit.ID).get(0);
                   int mdeID = syncAnimalMovementForDynamicEvent(amde, newDynamicEventID);
                   if(!amde.syncStatus.equals(SyncStatus.SYNCHRNOISED)) {

                       if (mdeID == -200)
                           break farmerloop;

                       amde.syncStatus = SyncStatus.SYNCHRNOISED.toString();
                       amde.serverID = mdeID;
                       herdDao.UpdateAnimalMovementsForDynamicEvent(amde);
                   }

                   ArrayList<DeathsForDynamicEvent> deathsForDynamicEvents = (ArrayList<DeathsForDynamicEvent>) herdDao.getDeathsForDynamicEvent(dynamicEventForVisit.ID);

                   for(DeathsForDynamicEvent dde:deathsForDynamicEvents)
                   {
                      int ddeID= syncDeathForAnimalMovementEvent(dde,newDynamicEventID);
                      if(dde.syncStatus.equals(SyncStatus.SYNCHRNOISED))
                          continue;
                      if(ddeID==-200)
                          break farmerloop;

                      dde.syncStatus = SyncStatus.SYNCHRNOISED.toString();
                      dde.serverID=ddeID;
                      herdDao.UpdateDeathsForDynamicEvent(dde);
                   }

                   dynamicEventForVisit.syncStatus = SyncStatus.SYNCHRNOISED.toString();
                   herdDao.UpdateDynamicEvent(dynamicEventForVisit);


                   hv.syncStatus = SyncStatus.SYNCHRNOISED.toString();
                   herdDao.UpdateHerdVisit(hv);
               } // for herd visit
                h.syncStatus = SyncStatus.SYNCHRNOISED.toString();
               herdDao.UpdateHerd(h);
            } // for herd

            f.syncStatus = SyncStatus.SYNCHRNOISED.toString();
            herdDao.UpdateFarmer(f);
        }// for farmer

        return null;
    }

    private int syncFarmer(Farmer f)
    {


        String UUID = m_context.getSharedPreferences("userPrefs",Context.MODE_PRIVATE).getString(Info.SHARED_PREFERENCES_KEY_UUID,"");
        String userInsertionResponse =manager.insertUser(UUID);

        JSONObject userJson = null;
        int userID = -200;


        try {
            userJson = new JSONObject(userInsertionResponse);
            userID =userJson.getInt("ID");
            String outcome = userJson.getString("outcome");
            Log.i("Sync-UserID",String.valueOf(userID));
            Log.i("Sync-outcomeUserIns",outcome);

        } catch (JSONException e)
        {
            //the formation of the json string is handled by asp so should be well formed
        }




        int fid = -1;
        if(!(f.syncStatus .equals( SyncStatus.SYNCHRNOISED.toString()) && f.serverID>0))
            fid = f.serverID;

        String farmerInsertionResponse = manager.insertFarmer(f,userID);

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

        String herdInsertionResponse =
                manager.insertHerd(herd,
                String.valueOf(newFarmerID));

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

    private int syncDynamicEvent(DynamicEvent dynamicEvent, int newHerdVisitID)
    {
        String dynamicEventInsertionResponse = manager.insertDynamicEvent(dynamicEvent, newHerdVisitID);

        JSONObject dynamicEventJson = null;
        int dynamicEventNewID = -200;

        try
        {
            dynamicEventJson = new JSONObject(dynamicEventInsertionResponse);
            dynamicEventNewID = dynamicEventJson.getInt("ID");
            String outcome = dynamicEventJson.getString("outcome");
            Log.i("Sync-DynamicID",String.valueOf(dynamicEventNewID));
            Log.i("Sync-DynamicOutcome",outcome);

        }
        catch (JSONException e)
        {

        }

        return dynamicEventNewID;
    }

    private int syncAnimalMovementForDynamicEvent(AnimalMovementsForDynamicEvent amde, int newDynamicEventID)
    {
        String animalMovementInsertionResponse = manager.insertAnimalMovementsForDynamicEvent(amde, newDynamicEventID);

        JSONObject animalMovementJson = null;
        int animalMovementNewID = -200;

        try
        {
            animalMovementJson = new JSONObject(animalMovementInsertionResponse);
            animalMovementNewID = animalMovementJson.getInt("ID");
            String outcome = animalMovementJson.getString("outcome");
            Log.i("Sync-AnimalMovID",String.valueOf(animalMovementNewID));
            Log.i("Sync-AniMovOutcome",outcome);

        }
        catch (JSONException e)
        {

        }

        return animalMovementNewID;
    }

    private int syncDeathForAnimalMovementEvent(DeathsForDynamicEvent dde, int newDynamicEventID)
    {
        String deathInsertionResponse = manager.insertDeathForDynamicEvent(dde, newDynamicEventID);

        JSONObject deathJson = null;
        int deathNewID = -200;

        try
        {
            deathJson = new JSONObject(deathInsertionResponse);
            deathNewID = deathJson.getInt("ID");
            String outcome = deathJson.getString("outcome");
            Log.i("Sync-DynamicID",String.valueOf(deathNewID));
            Log.i("Sync-DynamicOutcome",outcome);

        }
        catch (JSONException e)
        {

        }

        return deathNewID;
    }

}
