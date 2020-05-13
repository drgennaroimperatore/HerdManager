package com.ilri.herdmanager.utilities;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class SyncTask extends AsyncTask {
    public SyncTask() {
        super();
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
    protected Object doInBackground(Object[] objects) {

        URL url = null;
        try {
           url = new URL("http://10.0.2.2:61330/Home/InsertHerd");
        } catch (Exception e)
        {

        }
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("farmerID", "1");
        params.put("speciesID", "2");
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


        return null;
    }
}
