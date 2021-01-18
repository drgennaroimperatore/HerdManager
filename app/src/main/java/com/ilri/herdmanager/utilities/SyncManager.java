package com.ilri.herdmanager.utilities;

import android.content.Context;
import android.util.Log;

import com.ilri.herdmanager.classes.HealthEventContainer;
import com.ilri.herdmanager.database.entities.AnimalMovementsForDynamicEvent;
import com.ilri.herdmanager.database.entities.BirthsForProductivityEvent;
import com.ilri.herdmanager.database.entities.BodyConditionForHealthEvent;
import com.ilri.herdmanager.database.entities.DeathsForDynamicEvent;
import com.ilri.herdmanager.database.entities.DiseasesForHealthEvent;
import com.ilri.herdmanager.database.entities.DynamicEvent;
import com.ilri.herdmanager.database.entities.Farmer;
import com.ilri.herdmanager.database.entities.HealthEvent;
import com.ilri.herdmanager.database.entities.Herd;
import com.ilri.herdmanager.database.entities.HerdVisit;
import com.ilri.herdmanager.database.entities.MilkProductionForProductivityEvent;
import com.ilri.herdmanager.database.entities.ProductivityEvent;
import com.ilri.herdmanager.database.entities.SignsForHealthEvent;
import com.ilri.herdmanager.database.entities.SyncStatus;
import com.ilri.herdmanager.ui.dialogs.ErrorDialog;
import com.ilri.herdmanager.ui.dialogs.ErrorDialogFragment;
import com.ilri.herdmanager.ui.notifications.NotificationsFragment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

class SyncManager {
    private static final SyncManager ourInstance = new SyncManager();
    private static NotificationsFragment m_NotificationFragment;

    static SyncManager getInstance(NotificationsFragment nf) {
        m_NotificationFragment =nf;
        return ourInstance;
    }

    private SyncManager() {
    }

    private String sendPost(String functionName, Map<String, Object> params)
    {
        URL url = null;
        try {
          //  url = new URL("http://10.0.2.2:61330/Home/"+functionName); // debug string
           url = new URL("http://herdmanager.d3f.world/Home/"+functionName);
        } catch (Exception e)
        {

        }
        /*Map<String,Object> params = new LinkedHashMap<>();
        params.put("farmerID", "1");
        params.put("speciesID", "2");*/
        // params.put("reply_to_thread", 10394);
        //params.put("message", "Shark attacks in Botany Bay have gotten out of control. We need more defensive dolphins to protect the schools here, but Mayor Porpoise is too busy stuffing his snout with lobsters. He's so shellfish.");

        String response = new String();
        HttpURLConnection conn = null;
        InputStream result = null;

        try {
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            result =conn.getInputStream();
            Reader in = new BufferedReader(new InputStreamReader(result, "UTF-8"));

            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) >= 0;)
                sb.append((char)c);
            response = sb.toString();
            Log.i("response",response);
            in.close();
            int responseCode = conn.getResponseCode();
            Log.i("responsecode",String.valueOf(responseCode));
            conn.disconnect();


        } catch (Exception e)
        {
            Log.e("syncError",e.getMessage());

            m_NotificationFragment.showErrorMessage(e.getMessage());
            result = conn.getErrorStream();
            conn.disconnect();

        }

        finally
        {
            if (result != null) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        result));
                StringBuilder data = new StringBuilder();
                String line;
                try {
                    while ((line = rd.readLine()) != null) {
                        data.append(line);
                        data.append('\n');
                    }
                    Log.i("syncErrorlog",data.toString());
                    rd.close();
                } catch (Exception e)
                {
                    Log.e("SyncError2", e.getMessage());
                }
            }

        }
        return response;
    }

    public String insertHerd(Herd h, String newFarmerID)
    {

        Map<String, Object> herdParams = new LinkedHashMap<>();
        if(!h.syncStatus.equals(SyncStatus.NOT_SYNCHRONISED.toString()))
            herdParams.put("ID",h.serverID);
        herdParams.put("speciesID",h.speciesID);
        herdParams.put("farmerID",newFarmerID);
        herdParams.put("nBabies",h.nBabies);
        herdParams.put("nYoung", h.nYoung);
        herdParams.put("nOld",h.nOld);
      return sendPost("InsertHerd",herdParams);
    }

    public String insertFarmer(Farmer f, int UUID)
    {
        Map<String, Object> farmerParams = new LinkedHashMap<>();
        String stat = SyncStatus.NOT_SYNCHRONISED.toString();
        if(!f.syncStatus.equals(SyncStatus.NOT_SYNCHRONISED.toString()))
            farmerParams.put("ID",f.serverID);
        farmerParams.put("firstName",f.firstName);
        farmerParams.put("secondName", f.secondName);
        farmerParams.put("region",f.region);
        farmerParams.put("district",f.district);
        farmerParams.put("kebele",f.kebele);
        farmerParams.put("UserID",UUID);

        return sendPost("InsertFarmer",farmerParams);

    }

    public String insertUser(String userName, String UUID)
    {
        Map<String, Object> userParams = new LinkedHashMap<>();
        userParams.put("Name",userName);
        userParams.put("UUID",UUID);

        return sendPost("InsertUser",userParams);
    }

    public String insertHerdVisit(HerdVisit hv, int newHerdID)
    {
        Map<String, Object> herdVisitParams = new LinkedHashMap<>();
        if(!hv.syncStatus.equals( SyncStatus.NOT_SYNCHRONISED.toString()))
            herdVisitParams.put("ID",hv.serverID);
        herdVisitParams.put("HerdID", newHerdID);
        herdVisitParams.put("babiesAtVisit",hv.babiesAtVisit);
        herdVisitParams.put("youngAtVisit",hv.youngAtVisit);
        herdVisitParams.put("oldAtVisit",hv.oldAtVisit);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String selectedDate = sdf.format(hv.HerdVisitDate);
        herdVisitParams.put("HerdVisitDate",selectedDate);
        herdVisitParams.put("comments",hv.comments);

        return sendPost("InsertHerdVisit",herdVisitParams);
    }

    public String insertHealthEvent(HealthEvent healthEvent, int newHerdVisitID)
    {
       Map<String, Object> healthEventParams = new LinkedHashMap<>();
       if(!healthEvent.syncStatus.equals( SyncStatus.NOT_SYNCHRONISED.toString()))
        healthEventParams.put("ID",healthEvent.serverID);
       healthEventParams.put("herdVisitID", newHerdVisitID);

        return sendPost("InsertHealthEvent",healthEventParams);
    }

    public String insertDiseaseForHealthEvent(DiseasesForHealthEvent dhe, int newHealthEventID)
    {
        Map<String, Object> dheParams= new LinkedHashMap<>();
        if(!dhe.syncStatus.equals( SyncStatus.NOT_SYNCHRONISED.toString()))
            dheParams.put("ID",dhe.serverID);
        dheParams.put("diseaseID",dhe.diseaseID);
        dheParams.put("healthEventID",newHealthEventID);
        dheParams.put("numberOfAffectedBabies",dhe.numberOfAffectedBabies);
        dheParams.put("numberOfAffectedYoung", dhe.numberOfAffectedYoung);
        dheParams.put("numberOfAffectedOld", dhe.numberOfAffectedOld);

        return sendPost("InsertDiseaseForHealthEvent", dheParams);
    }

    public String insertSignForHealthEvent(SignsForHealthEvent she, int newHealthEventID)
    {
        Map<String, Object> sheParams= new LinkedHashMap<>();
        if(!she.syncStatus.equals( SyncStatus.NOT_SYNCHRONISED.toString()))
            sheParams.put("ID",she.serverID);
        sheParams.put("signID",she.signID);
        sheParams.put("healthEventID",newHealthEventID);
        sheParams.put("numberOfAffectedBabies",she.numberOfAffectedBabies);
        sheParams.put("numberOfAffectedYoung", she.numberOfAffectedYoung);
        sheParams.put("numberOfAffectedOld", she.numberOfAffectedOld);

        return sendPost("InsertSignForHealthEvent", sheParams);
    }

    public String insertBodyConditionForHealthEvent(BodyConditionForHealthEvent bche, int healthEventID)
    {
        Map<String, Object> bcheParams = new LinkedHashMap<>();
        if(!bche.syncStatus.equals(SyncStatus.NOT_SYNCHRONISED.toString()))
            bcheParams.put("ID",bche.serverID);
        bcheParams.put("bodyConditionID",bche.bodyConditionID);
        bcheParams.put("healthEventID",healthEventID);
        bcheParams.put("numberOfAffectedBabies",bche.nAffectedAdult);
        bcheParams.put("numberOfAffectedYoung",bche.nAffectedYoung);
        bcheParams.put("numberOfAffectedBabies",bche.nAffectedBabies);

        return sendPost("InsertBodyConditionForHealthEvent", bcheParams);

    }

    public String insertProductivityEvent(ProductivityEvent productivityEvent, int newHerdVisitID)
    {
        Map<String ,Object> productivityParams = new LinkedHashMap<>();
        if(!productivityEvent.syncStatus.equals( SyncStatus.NOT_SYNCHRONISED.toString()))
            productivityParams.put("ID",productivityEvent.serverID);
        productivityParams.put("herdVisitID",newHerdVisitID);

        return sendPost("InsertProductivityEvent",productivityParams);

    }

    public String insertMilkForProducitivityEvent(MilkProductionForProductivityEvent mpe, int newProdEventID)
    {
        Map<String, Object> milkParams = new LinkedHashMap<>();
        if(!mpe.syncStatus.equals( SyncStatus.NOT_SYNCHRONISED.toString()))
            milkParams.put("ID",mpe.serverID);
        milkParams.put("litresOfMilkPerDay", mpe.litresOfMilkPerDay);
        milkParams.put("numberOfLactatingAnimals", mpe.numberOfLactatingAnimals);
        milkParams.put("productivityEventID", newProdEventID);

        return sendPost("InsertMilkForProductivityEvent",milkParams);

    }

    public String insertBirthsForProductivityEvent (BirthsForProductivityEvent bpe, int newProdEventID)
    {
        Map<String, Object> birthsParams = new LinkedHashMap<>();
        if(!bpe.syncStatus.equals( SyncStatus.NOT_SYNCHRONISED.toString()))
            birthsParams.put("ID",bpe.serverID);
        birthsParams.put("nOfBirths",bpe.nOfBirths);
        birthsParams.put("nOfGestatingAnimals",bpe.nOfGestatingAnimals);
        birthsParams.put("productivityEventID",newProdEventID);

        return sendPost("InsertBirthsForProductivityEvent", birthsParams);
    }

    public String insertDynamicEvent(DynamicEvent dynamicEvent, int newHerdVisitID)
    {
        Map<String, Object> dynamicEventParams = new LinkedHashMap<>();
        if(!dynamicEvent.syncStatus.equals( SyncStatus.NOT_SYNCHRONISED.toString()))
            dynamicEventParams.put("ID",dynamicEvent.serverID);
        dynamicEventParams.put("herdVisitID",newHerdVisitID);

        return sendPost("InsertDynamicEvent",dynamicEventParams);
    }

    public String insertAnimalMovementsForDynamicEvent(AnimalMovementsForDynamicEvent amde, int newDynamicEventID)
    {
        Map <String, Object> animalMovementParams = new LinkedHashMap<>();
        if(!amde.syncStatus.equals( SyncStatus.NOT_SYNCHRONISED.toString()))
            animalMovementParams.put("ID",amde.serverID);

        animalMovementParams.put("boughtBabies",amde.boughtBabies);
        animalMovementParams.put("boughtYoung",amde.boughtYoung);
        animalMovementParams.put("boughtOld",amde.boughtOld);

        animalMovementParams.put("lostBabies",amde.lostBabies);
        animalMovementParams.put("lostYoung",amde.lostYoung);
        animalMovementParams.put("lostOld",amde.lostOld);

        animalMovementParams.put("soldBabies",amde.soldBabies);
        animalMovementParams.put("soldYoung",amde.soldYoung);
        animalMovementParams.put("soldOld",amde.soldOld);

        animalMovementParams.put("dynamicEventID",newDynamicEventID);

        return sendPost("InsertAnimalMovementForDynamicEvent", animalMovementParams);
    }

    public String insertDeathForDynamicEvent(DeathsForDynamicEvent dde, int newDynamicEventID)
    {
        Map<String, Object> deathParams = new LinkedHashMap<>();
        if(!dde.syncStatus.equals( SyncStatus.NOT_SYNCHRONISED.toString()))
            deathParams.put("ID",dde.serverID);
        deathParams.put("causeOfDeath",dde.causeOfDeath);
        deathParams.put("deadBabies",dde.deadBabies);
        deathParams.put("deadYoung",dde.deadYoung);
        deathParams.put("deadOld",dde.deadOld);
        deathParams.put("dynamicEventID",newDynamicEventID);

        return sendPost("InsertDeathForDynamicEvent",deathParams);
    }
}
