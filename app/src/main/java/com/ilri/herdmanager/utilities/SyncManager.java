package com.ilri.herdmanager.utilities;

import android.util.Log;

import com.ilri.herdmanager.classes.HealthEventContainer;
import com.ilri.herdmanager.database.entities.BirthsForProductivityEvent;
import com.ilri.herdmanager.database.entities.DiseasesForHealthEvent;
import com.ilri.herdmanager.database.entities.HealthEvent;
import com.ilri.herdmanager.database.entities.HerdVisit;
import com.ilri.herdmanager.database.entities.MilkProductionForProductivityEvent;
import com.ilri.herdmanager.database.entities.ProductivityEvent;
import com.ilri.herdmanager.database.entities.SignsForHealthEvent;

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

    static SyncManager getInstance() {
        return ourInstance;
    }

    private SyncManager() {
    }

    private String sendPost(String functionName, Map<String, Object> params)
    {
        URL url = null;
        try {
            url = new URL("http://10.0.2.2:61330/Home/"+functionName);
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
            conn.disconnect();


        } catch (Exception e)
        {
            Log.e("syncError",e.getMessage());
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

    public String insertHerd(String speciesID, String newFarmerID)
    {
        Map<String, Object> herdParams = new LinkedHashMap<>();
        herdParams.put("speciesID",speciesID);
        herdParams.put("farmerID",newFarmerID);
      return sendPost("InsertHerd",herdParams);
    }

    public String insertFarmer(String firstName, String secondName, String region, String district, String kebele)
    {
        Map<String, Object> farmerParams = new LinkedHashMap<>();
        farmerParams.put("firstName",firstName);
        farmerParams.put("secondName", secondName);
        farmerParams.put("region",region);
        farmerParams.put("district",district);
        farmerParams.put("kebele",kebele);

        return sendPost("InsertFarmer",farmerParams);

    }

    public String insertHerdVisit(HerdVisit hv, int newHerdID)
    {
        Map<String, Object> herdVisitParams = new LinkedHashMap<>();
        herdVisitParams.put("HerdID", newHerdID);
        herdVisitParams.put("babiesAtVisit",hv.babiesAtVisit);
        herdVisitParams.put("youngAtVisit",hv.youngAtVisit);
        herdVisitParams.put("oldAtVisit",hv.oldAtVisit);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String selectedDate = sdf.format(hv.HerdVisitDate);
        herdVisitParams.put("HerdVisitDate",selectedDate);

        return sendPost("InsertHerdVisit",herdVisitParams);
    }

    public String insertHealthEvent(HealthEvent healthEvent, int newHerdVisitID)
    {
       Map<String, Object> healthEventParams = new LinkedHashMap<>();
       healthEventParams.put("herdVisitID", newHerdVisitID);

        return sendPost("InsertHealthEvent",healthEventParams);
    }

    public String insertDiseaseForHealthEvent(DiseasesForHealthEvent dhe, int newHealthEventID)
    {
        Map<String, Object> dheParams= new LinkedHashMap<>();
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
        sheParams.put("signID",she.signID);
        sheParams.put("healthEventID",newHealthEventID);
        sheParams.put("numberOfAffectedBabies",she.numberOfAffectedBabies);
        sheParams.put("numberOfAffectedYoung", she.numberOfAffectedYoung);
        sheParams.put("numberOfAffectedOld", she.numberOfAffectedOld);

        return sendPost("InsertSignForHealthEvent", sheParams);
    }

    public String insertProductivityEvent(ProductivityEvent productivityEvent, int newHerdVisitID)
    {
        Map<String ,Object> productivityParams = new LinkedHashMap<>();
        productivityParams.put("herdVisitID",newHerdVisitID);

        return sendPost("InsertProductivityEvent",productivityParams);

    }

    public String insertMilkForProducitivityEvent(MilkProductionForProductivityEvent mpe, int newProdEventID)
    {
        Map<String, Object> milkParams = new LinkedHashMap<>();
        milkParams.put("litresOfMilkPerDay", mpe.litresOfMilkPerDay);
        milkParams.put("numberOfLactatingAnimals", mpe.numberOfLactatingAnimals);
        milkParams.put("productivityEventID", newProdEventID);

        return sendPost("InsertMilkForProductivityEvent",milkParams);

    }

    public String insertBirthsForProductivityEvent (BirthsForProductivityEvent bpe, int newProdEventID)
    {
        Map<String, Object> birthsParams = new LinkedHashMap<>();
        birthsParams.put("nOfBirths",bpe.nOfBirths);
        birthsParams.put("nOfGestatingAnimals",bpe.nOfGestatingAnimals);
        birthsParams.put("productivityEventID",bpe.productivityEventID);

        return sendPost("InsertBirthsForProductivityEvent", birthsParams);
    }
}
